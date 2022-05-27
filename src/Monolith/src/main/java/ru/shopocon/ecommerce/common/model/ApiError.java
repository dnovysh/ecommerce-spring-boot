package ru.shopocon.ecommerce.common.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import ru.shopocon.ecommerce.common.model.types.ApiNestedError;
import ru.shopocon.ecommerce.common.model.types.RequiredActionType;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ApiError {
    @Getter
    private final String isoOffsetDateTime;

    @Getter
    private final long timestamp;

    @Getter
    private final List<ApiNestedError> nestedErrors;

    @Getter
    private int status;

    @Getter
    private String error;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String debugMessage;

    @Getter
    @Setter
    private RequiredActionType requiredAction;

    private ApiError() {
        final var now = OffsetDateTime.now();
        this.isoOffsetDateTime = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.timestamp = now.toInstant().toEpochMilli();
        this.message = "";
        this.debugMessage = "";
        this.requiredAction = RequiredActionType.NO_ACTION_AVAILABLE;
        this.nestedErrors = new ArrayList<>();
    }

    public ApiError(@NonNull HttpStatus httpStatus) {
        this();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
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

    public void addApiNestedError(@NonNull ApiNestedError nestedError) {
        nestedErrors.add(nestedError);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(status);
    }
}
