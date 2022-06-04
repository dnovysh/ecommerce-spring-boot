package ru.shopocon.ecommerce.common.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AlreadySignedInException extends AuthenticationException {
    public AlreadySignedInException(String msg) {
        super(msg);
    }

    public AlreadySignedInException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
