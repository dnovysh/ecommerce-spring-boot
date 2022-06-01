package ru.shopocon.ecommerce.common.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidPrincipal extends AuthenticationException {
    public InvalidPrincipal(String msg) {
        super(msg);
    }

    public InvalidPrincipal(String msg, Throwable cause) {
        super(msg, cause);
    }
}
