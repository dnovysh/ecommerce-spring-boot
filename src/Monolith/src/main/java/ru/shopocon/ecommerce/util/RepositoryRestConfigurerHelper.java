package ru.shopocon.ecommerce.util;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.http.HttpMethod;

public final class RepositoryRestConfigurerHelper {
    private RepositoryRestConfigurerHelper() {
    }

    public static void disableHttpMethodsForDomainTypes(
            RepositoryRestConfiguration config,
            Class<?>[] domainTypes,
            HttpMethod[] unsupportedHttpMethods) {
        for (Class<?> domainType : domainTypes) {
            config.getExposureConfiguration().forDomainType(domainType)
                    .withItemExposure(
                            (metdata, httpMethods) -> httpMethods.disable(unsupportedHttpMethods))
                    .withCollectionExposure(
                            (metdata, httpMethods) -> httpMethods.disable(unsupportedHttpMethods));
        }
    }
}
