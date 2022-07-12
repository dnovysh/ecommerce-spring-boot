package ru.shopocon.ecommerce.config.exception.handlers;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.shopocon.ecommerce.common.model.ApiError;
import ru.shopocon.ecommerce.common.model.ApiErrorBuilder;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class DataExceptionHandler {

    private final static String SOMETHING_WENT_WRONG =
        "Something went wrong, try again later or contact support";

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException", ex);
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            return getConstraintViolationErrorResponse(constraintViolationException);
        }
        return ApiErrorBuilder.builder(INTERNAL_SERVER_ERROR, SOMETHING_WENT_WRONG)
            .setDebugMessage(ex.getMessage())
            .buildTypedResponseEntity();
    }

    private ResponseEntity<ApiError> getConstraintViolationErrorResponse(
        ConstraintViolationException constraintViolationException
    ) {
        val cause = constraintViolationException.getCause();
        if (cause != null) {
            if (cause.getMessage().toLowerCase().contains("un_user_username")) {
                return buildUnUserUsernameResponse();
            } else if (cause.getMessage().toLowerCase().contains("un_product_category_name")) {
                return buildUnProductCategoryNameResponse();
            }
        }
        return ApiErrorBuilder.builder(CONFLICT, "Constraint violation")
            .setDebugMessage(constraintViolationException.getMessage())
            .buildTypedResponseEntity();
    }

    private ResponseEntity<ApiError> buildUnUserUsernameResponse() {
        return ApiErrorBuilder.builder(CONFLICT, "This username is already in use")
            .addApiValidationError(new FieldError(
                "user", "username", "This email is already in use"))
            .buildTypedResponseEntity();
    }

    private ResponseEntity<ApiError> buildUnProductCategoryNameResponse() {
        return ApiErrorBuilder.builder(CONFLICT, "This category name already exists")
            .addApiValidationError(new FieldError(
                "category", "name", "This category name already exists"))
            .buildTypedResponseEntity();
    }
}
