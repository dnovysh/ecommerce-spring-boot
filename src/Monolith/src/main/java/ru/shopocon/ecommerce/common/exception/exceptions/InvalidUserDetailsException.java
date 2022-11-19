package ru.shopocon.ecommerce.common.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidUserDetailsException extends AuthenticationException {
    public InvalidUserDetailsException(String msg) {
        super(msg);
    }

    public InvalidUserDetailsException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
