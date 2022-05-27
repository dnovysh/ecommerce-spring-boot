package ru.shopocon.ecommerce.common.model;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import ru.shopocon.ecommerce.common.model.types.ApiNestedError;
import ru.shopocon.ecommerce.common.model.types.RequiredActionType;

public final class ApiErrorBuilder {

    private ApiError apiError;

    private ApiErrorBuilder() {
    }

    public ApiErrorBuilder(@NonNull HttpStatus httpStatus) {
        apiError = new ApiError(httpStatus);
    }

    public static ApiErrorBuilder builder(@NonNull HttpStatus httpStatus) {
        return new ApiErrorBuilder(httpStatus);
    }

    public ApiErrorBuilder setMessage(String message) {
        apiError.setMessage(message);
        return this;
    }

    public ApiErrorBuilder setDebugMessage(String debugMessage) {
        apiError.setDebugMessage(debugMessage);
        return this;
    }

    public ApiErrorBuilder setRequiredAction(RequiredActionType requiredAction) {
        apiError.setRequiredAction(requiredAction);
        return this;
    }

    public ApiErrorBuilder setDebugMessage(Throwable ex) {
        apiError.setDebugMessage(ex.getLocalizedMessage());
        return this;
    }

    public ApiErrorBuilder addApiNestedError(@NonNull ApiNestedError nestedError) {
        apiError.addApiNestedError(nestedError);
        return this;
    }

    public ApiError build() {
        return apiError;
    }
}
