package ru.shopocon.ecommerce.catalog.security.permissions;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('product.update') OR" +
    " (hasAuthority('dealer.product.update')" +
    " AND @dealerAuthenticationManager.isDealerRepresentative(authentication))")

public @interface ProductUpdatePermission {
}
