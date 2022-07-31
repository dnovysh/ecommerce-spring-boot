package ru.shopocon.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.shopocon.ecommerce.common.exception.entrypoints.AuthenticationEntryPointImpl;
import ru.shopocon.ecommerce.config.filters.JwtRequestFilter;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    @Value("${shopocon.security.profile}")
    private String webSecurityProfile;

    @Value("${shopocon.security.origins}")
    private List<String> allowedOrigins;

    @Value("${server.servlet.session.cookie.name}")
    private String sessionIdCookieName;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

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
            "/", "/error",
            "/resources/static/**", "/resources/templates/**", "/webjars/**"
        };
        final String[] identityPermitAllPaths = {
            concatBasePath("/auth/signin"),
            concatBasePath("/auth/signup"),
            concatBasePath("/auth/signout"),
            concatBasePath("/auth/refresh")
        };
        final String[] catalogPermitAllPaths = {
            concatBasePath("/catalog-products/**"),
            concatBasePath("/catalog-reviews/**")
        };

        final String categoriesUrl = concatBasePath("/catalog-categories/**");
        final String productManagementUrl =
            concatBasePath("/management/products/**");

        http.cors().and()
            .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        http.exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint())
            .accessDeniedHandler(accessDeniedHandler);

        http.authorizeRequests(authorize -> authorize
                    .antMatchers(commonPermitAllPaths).permitAll()
                    .antMatchers(identityPermitAllPaths).permitAll()
                    .antMatchers(catalogPermitAllPaths).permitAll()
                    .antMatchers(concatBasePath("/identity-roles/**")).hasRole("ADMIN")
                    .antMatchers(concatBasePath("/identity-authorities/**")).hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, concatBasePath("/identity-dealers/**")).permitAll()
                    .antMatchers(concatBasePath("/identity-dealers/**")).denyAll()
                    .antMatchers(HttpMethod.GET, categoriesUrl).permitAll()
                    .antMatchers(HttpMethod.POST, categoriesUrl).hasAuthority("category.create")
                    .antMatchers(HttpMethod.PUT, categoriesUrl).hasAuthority("category.update")
                    .antMatchers(HttpMethod.PATCH, categoriesUrl).hasAuthority("category.update")
                    .antMatchers(HttpMethod.DELETE, categoriesUrl).hasAuthority("category.delete")
                    .antMatchers(HttpMethod.GET, productManagementUrl).permitAll()
//                .hasAnyAuthority(
//                    "product.read", "dealer.product.read")
            )
            .authorizeRequests().anyRequest().authenticated().and()
            .formLogin().disable()
            .httpBasic().disable();

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(5)
            .maxSessionsPreventsLogin(true)
            .sessionRegistry(sessionRegistry()).and()
            .sessionFixation().migrateSession();

        http.logout()
            .logoutUrl("auth/signout")
            .clearAuthentication(true)
            .invalidateHttpSession(true)
            .deleteCookies(sessionIdCookieName);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        if (isDev()) {
            http.headers().frameOptions().sameOrigin();
        }
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPointImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
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
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(concatBasePath("/**"), configuration);
        return source;
    }

    private boolean isDev() {
        return "dev".equals(webSecurityProfile);
    }

    private String concatBasePath(String url) {
        return "%s%s".formatted(basePath, url);
    }
}
