package ru.shopocon.ecommerce.common.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import ru.shopocon.ecommerce.common.model.types.ApiNestedError;
import ru.shopocon.ecommerce.common.model.types.RequiredActionType;

import java.util.List;

public final class ApiErrorBuilder {

    private ApiError apiError;

    private ApiErrorBuilder() {
    }

    public ApiErrorBuilder(@NonNull HttpStatus httpStatus) {
        apiError = new ApiError(httpStatus);
    }

    public ApiErrorBuilder(@NonNull HttpStatus httpStatus, @NonNull String message) {
        apiError = new ApiError(httpStatus, message);
    }

    public ApiErrorBuilder(@NonNull HttpStatus httpStatus, @NonNull String message, @NonNull Throwable ex) {
        apiError = new ApiError(httpStatus, message, ex);
    }

    public static ApiErrorBuilder builder(@NonNull HttpStatus httpStatus) {
        return new ApiErrorBuilder(httpStatus);
    }

    public static ApiErrorBuilder builder(@NonNull HttpStatus httpStatus, @NonNull String message) {
        return new ApiErrorBuilder(httpStatus, message);
    }

    public static ApiErrorBuilder builder(@NonNull HttpStatus httpStatus, @NonNull String message, @NonNull Throwable ex) {
        return new ApiErrorBuilder(httpStatus, message, ex);
    }

    public ApiErrorBuilder setMessage(String message) {
        apiError.setMessage(message);
        return this;
    }

    public ApiErrorBuilder setDebugMessage(String debugMessage) {
        apiError.setDebugMessage(debugMessage);
        return this;
    }

    public ApiErrorBuilder setDebugMessage(Throwable ex) {
        apiError.setDebugMessage(ex.getLocalizedMessage());
        return this;
    }

    public ApiErrorBuilder setPath(String path) {
        apiError.setPath(path);
        return this;
    }

    public ApiErrorBuilder setRequiredAction(RequiredActionType requiredAction) {
        apiError.setRequiredAction(requiredAction);
        return this;
    }

    public ApiErrorBuilder addApiNestedError(@NonNull ApiNestedError nestedError) {
        apiError.addApiNestedError(nestedError);
        return this;
    }

    public ApiErrorBuilder addApiValidationError(@NonNull String object,
                                                 String field,
                                                 Object rejectedValue,
                                                 String message) {
        return addApiNestedError(new ApiValidationError(object, field, rejectedValue, message));
    }

    public ApiErrorBuilder addApiValidationError(@NonNull String object,
                                                 String message) {
        return addApiNestedError(new ApiValidationError(object, message));
    }

    public ApiErrorBuilder addApiValidationError(FieldError fieldError) {
        return addApiValidationError(
            fieldError.getObjectName(),
            fieldError.getField(),
            fieldError.getRejectedValue(),
            fieldError.getDefaultMessage());
    }

    public ApiErrorBuilder addApiValidationError(ObjectError objectError) {
        return addApiValidationError(
            objectError.getObjectName(),
            objectError.getDefaultMessage());
    }

    public ApiErrorBuilder addApiValidationFieldErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addApiValidationError);
        return this;
    }

    public ApiErrorBuilder addApiValidationObjectErrors(List<ObjectError> objectErrors) {
        objectErrors.forEach(this::addApiValidationError);
        return this;
    }

    public ApiError build() {
        return apiError;
    }

    public ResponseEntity<Object> buildResponseEntity() {
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    public ResponseEntity<ApiError> buildTypedResponseEntity() {
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }
}
