package ru.shopocon.ecommerce.common.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AlreadySignedIn extends AuthenticationException {
    public AlreadySignedIn(String msg) {
        super(msg);
    }

    public AlreadySignedIn(String msg, Throwable cause) {
        super(msg, cause);
    }
}
