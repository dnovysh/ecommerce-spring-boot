package ru.shopocon.ecommerce.identity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import ru.shopocon.ecommerce.identity.domain.Dealer;
import ru.shopocon.ecommerce.identity.domain.security.Authority;
import ru.shopocon.ecommerce.identity.domain.security.Role;
import ru.shopocon.ecommerce.identity.domain.security.User;
import ru.shopocon.ecommerce.util.RepositoryRestConfigurerHelper;

@Configuration
public class IdentityDataRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
                                                     CorsRegistry cors) {
        final HttpMethod[] unsupportedHttpMethods = {
            HttpMethod.POST,
            HttpMethod.PUT,
            HttpMethod.DELETE,
            HttpMethod.PATCH
        };
        final Class<?>[] restrictedDomainTypes = {
            Authority.class,
            Role.class
        };
        final Class<?>[] domainTypes = {
            Authority.class,
            Role.class,
            User.class,
            Dealer.class
        };

        RepositoryRestConfigurerHelper
            .disableHttpMethodsForDomainTypes(config, restrictedDomainTypes, unsupportedHttpMethods);
        config.exposeIdsFor(domainTypes);
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
    }
}
