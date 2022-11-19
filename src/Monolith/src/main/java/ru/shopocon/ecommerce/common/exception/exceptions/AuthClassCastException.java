package ru.shopocon.ecommerce.common.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthClassCastException extends AuthenticationException {
    public AuthClassCastException(String msg) {
        super(msg);
    }

    public AuthClassCastException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
