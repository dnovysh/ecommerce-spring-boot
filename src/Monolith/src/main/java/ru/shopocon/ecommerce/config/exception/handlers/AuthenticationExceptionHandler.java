package ru.shopocon.ecommerce.config.exception.handlers;

import io.jsonwebtoken.security.WeakKeyException;
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
import ru.shopocon.ecommerce.common.exception.exceptions.AlreadySignedInException;
import ru.shopocon.ecommerce.common.exception.exceptions.AuthClassCastException;
import ru.shopocon.ecommerce.common.exception.exceptions.InvalidPrincipalException;
import ru.shopocon.ecommerce.common.exception.exceptions.InvalidUserDetailsException;
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

    @ExceptionHandler(value = AlreadySignedInException.class)
    ResponseEntity<ApiError> handleAlreadySignedIn(AlreadySignedInException alreadySignedInException) {
        return buildResponse(BAD_REQUEST,
            "You are already signed in, you need to sign out first", alreadySignedInException);
    }

    @ExceptionHandler(value = DisabledException.class)
    ResponseEntity<ApiError> handleDisabledException(DisabledException disabledException) {
        return buildResponse(BAD_REQUEST, "The account is disabled", disabledException);
    }

    @ExceptionHandler(value = LockedException.class)
    ResponseEntity<ApiError> handleLockedException(LockedException lockedException) {
        return buildResponse(BAD_REQUEST, "The account is locked", lockedException);
    }

    @ExceptionHandler(value = {
        UsernameNotFoundException.class,
        BadCredentialsException.class
    })
    ResponseEntity<ApiError> handleIncorrectUsernameOrPassword(AuthenticationException ex) {
        log.warn("The username or password is incorrect", ex);
        return buildResponse(BAD_REQUEST, "The login or password is incorrect", ex);
    }

    @ExceptionHandler(value = AuthClassCastException.class)
    ResponseEntity<ApiError> handleAuthClassCastException(AuthClassCastException ex) {
        log.error("Invalid principal type", ex);
        return buildResponse(INTERNAL_SERVER_ERROR, SOMETHING_WENT_WRONG, ex);
    }

    @ExceptionHandler(value = InvalidUserDetailsException.class)
    ResponseEntity<ApiError> handleInvalidUserDetailsException(InvalidUserDetailsException ex) {
        log.error(ex.getMessage(), ex);
        return buildResponse(INTERNAL_SERVER_ERROR, SOMETHING_WENT_WRONG, ex);
    }

    @ExceptionHandler(value = InvalidPrincipalException.class)
    ResponseEntity<ApiError> handleInvalidPrincipalException(InvalidPrincipalException ex) {
        log.error(ex.getMessage(), ex);
        return buildResponse(INTERNAL_SERVER_ERROR, SOMETHING_WENT_WRONG, ex);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
        log.error("Unknown authentication error", ex);
        return buildResponse(INTERNAL_SERVER_ERROR, SOMETHING_WENT_WRONG, ex);
    }

    @ExceptionHandler(value = WeakKeyException.class)
    ResponseEntity<ApiError> handleWeakKeyException(WeakKeyException ex) {
        log.error("Weak secret key", ex);
        return ApiErrorBuilder.builder(INTERNAL_SERVER_ERROR, SOMETHING_WENT_WRONG)
            .setDebugMessage("No token created")
            .buildTypedResponseEntity();
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus httpStatus,
                                                   String message,
                                                   AuthenticationException ex) {
        return ApiErrorBuilder.builder(httpStatus, message)
            .setDebugMessage(ex.getMessage())
            .buildTypedResponseEntity();
    }
}
