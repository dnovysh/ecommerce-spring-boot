package ru.shopocon.ecommerce.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import ru.shopocon.ecommerce.common.model.types.ApiNestedError;
import ru.shopocon.ecommerce.common.model.types.RequiredActionType;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ApiError {

    @Getter
    private int status;

    @Getter
    private String error;

    @Getter
    private final List<ApiNestedError> errors;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String debugMessage;

    @Getter
    @Setter
    private String path;

    @Getter
    @Setter
    private RequiredActionType requiredAction;

    @Getter
    private final String timestamp;

    @Getter
    private final long timestampMs;

    private ApiError() {
        final var now = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        this.timestamp = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.timestampMs = now.toInstant().toEpochMilli();
        this.message = "No message available";
        this.debugMessage = "";
        this.path = "";
        this.requiredAction = RequiredActionType.NO_ACTION_AVAILABLE;
        this.errors = new ArrayList<>();
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
        errors.add(nestedError);
    }

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(status);
    }
}
