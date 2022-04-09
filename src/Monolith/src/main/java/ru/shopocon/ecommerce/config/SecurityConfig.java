package ru.shopocon.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final String[] commonPermitAllPaths = {
            "/", "/webjars/**", "/login", "/resources/static/**",
            "/resources/templates/**"
        };
        final String[] catalogPermitAllPaths = {
            this.basePath + "/catalog-categories/**",
            this.basePath + "/catalog-products/**",
            this.basePath + "/catalog-reviews/**",
        };
        http
            .authorizeRequests(authorize ->
                authorize
                    .antMatchers(commonPermitAllPaths).permitAll()
                    .antMatchers(catalogPermitAllPaths).permitAll()
            )
            .authorizeRequests()
            .anyRequest().authenticated().and()
            .formLogin().and()
            .httpBasic();
    }
}
