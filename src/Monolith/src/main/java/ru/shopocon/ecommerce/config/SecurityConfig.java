package ru.shopocon.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    @Value("${shopocon.security.profile}")
    private String webSecurityProfile;

    @Value("${shopocon.security.origins}")
    private String[] allowedOrigins;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    public void configure(WebSecurity web) {
        if (isDev()) {
            web.ignoring()
                .antMatchers("/v2/api-docs")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/configuration/**")
                .antMatchers("/webjars/**")
                .antMatchers("/static/**")
                .antMatchers("/public/**")
                .antMatchers("/h2-console/**");
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final String[] commonPermitAllPaths = {
            "/", "auth/login", "auth/register", "auth/logout",
            "auth/signin", "auth/signup", "auth/signout",
            "/resources/static/**", "/resources/templates/**", "/webjars/**",
        };
        final String[] catalogPermitAllPaths = {
            "%s/catalog-categories/**".formatted(basePath),
            "%s/catalog-products/**".formatted(basePath),
            "%s/catalog-reviews/**".formatted(basePath),
        };

        http.cors().and()
            .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        http.authorizeRequests(authorize -> authorize
                .antMatchers(commonPermitAllPaths).permitAll()
                .antMatchers(catalogPermitAllPaths).permitAll()
            )
            .authorizeRequests().anyRequest().authenticated().and()
            .formLogin().disable()
            .httpBasic().disable();

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(3)
            .maxSessionsPreventsLogin(true)
            .sessionRegistry(sessionRegistry()).and()
            .sessionFixation().migrateSession();

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        if (isDev()) {
            http.headers().frameOptions().sameOrigin();
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("%s/**".formatted(basePath), configuration);
        return source;
    }

    private boolean isDev() {
        return "dev".equals(webSecurityProfile);
    }
}
