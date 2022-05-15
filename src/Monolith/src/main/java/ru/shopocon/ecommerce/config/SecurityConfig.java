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
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    @Value("${shopocon.web-security.profile}")
    private String webSecurityProfile;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final String[] commonPermitAllPaths = {
            "/", "auth/login", "auth/register", "auth/logout",
            "auth/signin", "auth/signup", "auth/signout",
            "/resources/static/**", "/resources/templates/**", "/webjars/**",
        };
        final String[] catalogPermitAllPaths = {
            this.basePath + "/catalog-categories/**",
            this.basePath + "/catalog-products/**",
            this.basePath + "/catalog-reviews/**",
        };
        http.authorizeRequests(authorize -> authorize
                .antMatchers(commonPermitAllPaths).permitAll()
                .antMatchers(catalogPermitAllPaths).permitAll()
            )
            .authorizeRequests()
            .anyRequest().authenticated().and()
            .formLogin().disable()
            .httpBasic().disable();

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(3)
            .maxSessionsPreventsLogin(true)
            .sessionRegistry(sessionRegistry()).and()
            .sessionFixation().migrateSession();

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        if ("dev".equals(webSecurityProfile)) {
            web.ignoring()
                .antMatchers("/v2/api-docs")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/configuration/**")
                .antMatchers("/webjars/**")//
                .antMatchers("/static/**")
                .antMatchers("/public/**")
                .antMatchers("/h2-console/**/**");
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


}
