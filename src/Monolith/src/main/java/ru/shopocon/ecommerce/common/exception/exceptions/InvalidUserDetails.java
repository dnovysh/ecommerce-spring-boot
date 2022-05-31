package ru.shopocon.ecommerce.common.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidUserDetails extends AuthenticationException {
    public InvalidUserDetails(String msg) {
        super(msg);
    }

    public InvalidUserDetails(String msg, Throwable cause) {
        super(msg, cause);
    }
}
