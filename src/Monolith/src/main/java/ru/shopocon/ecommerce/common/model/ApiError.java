package ru.shopocon.ecommerce.common.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import ru.shopocon.ecommerce.common.model.types.ApiNestedError;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ApiError {
    @Getter
    private int status;
    @Getter
    private String error;
    @Getter
    private final String isoOffsetDateTime;
    @Getter
    private final long timestamp;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private String debugMessage;
    @Getter
    private final List<ApiNestedError> nestedErrors;

    private ApiError() {
        final var now = OffsetDateTime.now();
        this.isoOffsetDateTime = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.timestamp = now.toInstant().toEpochMilli();
        this.nestedErrors = new ArrayList<>();
    }

    public ApiError(@NonNull HttpStatus httpStatus) {
        this();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = "";
        this.debugMessage = "";
    }

    public ApiError(@NonNull HttpStatus httpStatus, @NonNull String message) {
        this(httpStatus);
        this.message = message;
    }

    public ApiError(@NonNull HttpStatus httpStatus, @NonNull String message, @NonNull Throwable ex) {
        this(httpStatus, message);
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(@NonNull HttpStatus httpStatus, @NonNull Throwable ex) {
        this(httpStatus, "Unexpected error", ex);
    }

    public ApiError addApiNestedError(@NonNull ApiNestedError nestedError) {
        nestedErrors.add(nestedError);
        return this;
    }
}
