package ru.shopocon.ecommerce.config.exception.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.shopocon.ecommerce.common.exception.exceptions.AuthClassCastException;
import ru.shopocon.ecommerce.common.model.ApiError;
import ru.shopocon.ecommerce.common.model.ApiErrorBuilder;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class AuthenticationExceptionHandler {

    private final static String SOMETHING_WENT_WRONG =
        "Something went wrong, try again later or contact support";

    @ExceptionHandler(value = {
        DisabledException.class,
        LockedException.class,
        UsernameNotFoundException.class,
        BadCredentialsException.class,
        AuthClassCastException.class,
        AuthenticationException.class
    })
    ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
        if (ex instanceof DisabledException disabledException) {
            return buildResponse(BAD_REQUEST, "The account is disabled", disabledException);
        } else if (ex instanceof LockedException lockedException) {
            return buildResponse(BAD_REQUEST, "The account is locked", lockedException);
        } else if (ex instanceof UsernameNotFoundException || ex instanceof BadCredentialsException) {
            log.warn("The username or password is incorrect", ex);
            return buildResponse(BAD_REQUEST, "The username or password is incorrect", ex);
        } else if (ex instanceof AuthClassCastException authClassCastException) {
            log.error("Invalid principal type", authClassCastException);
            return buildResponse(INTERNAL_SERVER_ERROR, SOMETHING_WENT_WRONG, authClassCastException);
        }
        log.error("Unknown authentication error", ex);
        return buildResponse(INTERNAL_SERVER_ERROR, SOMETHING_WENT_WRONG, ex);
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus httpStatus,
                                                   String message,
                                                   AuthenticationException ex) {
        return ApiErrorBuilder.builder(httpStatus, message)
            .setDebugMessage(ex.getMessage())
            .buildTypedResponseEntity();
    }
}
