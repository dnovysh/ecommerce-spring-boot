package ru.shopocon.ecommerce.common.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidPrincipalException extends AuthenticationException {
    public InvalidPrincipalException(String msg) {
        super(msg);
    }

    public InvalidPrincipalException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
