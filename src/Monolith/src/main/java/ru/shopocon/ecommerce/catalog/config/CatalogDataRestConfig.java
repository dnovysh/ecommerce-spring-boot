package ru.shopocon.ecommerce.catalog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import ru.shopocon.ecommerce.catalog.domain.Category;
import ru.shopocon.ecommerce.catalog.domain.Product;
import ru.shopocon.ecommerce.catalog.domain.Review;
import ru.shopocon.ecommerce.catalog.projections.InlineCategory;
import ru.shopocon.ecommerce.util.RepositoryRestConfigurerHelper;

@Configuration
public class CatalogDataRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
                                                     CorsRegistry cors) {
        final HttpMethod[] unsupportedHttpMethods = {
            HttpMethod.POST,
            HttpMethod.PUT,
            HttpMethod.DELETE
        };
        final Class<?>[] domainTypes = {
            Category.class,
            Product.class,
            Review.class
        };
        RepositoryRestConfigurerHelper
            .disableHttpMethodsForDomainTypes(config, domainTypes, unsupportedHttpMethods);
        config.exposeIdsFor(domainTypes);
        config.getProjectionConfiguration().addProjection(InlineCategory.class);
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
    }
}
