package ru.shopocon.ecommerce.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
    private final String timestamp;

    @Getter
    private final long timestampMs;

    private HttpStatus httpStatus;

    @Getter
    private int status;

    @Getter
    private String error;

    @JsonInclude(Include.NON_NULL)
    @Getter
    @Setter
    private List<ApiNestedError> errors;

    @Getter
    @Setter
    private String message;

    @JsonInclude(Include.NON_NULL)
    @Getter
    @Setter
    private String debugMessage;

    @JsonInclude(Include.NON_NULL)
    @Getter
    @Setter
    private String path;

    @Getter
    @Setter
    private RequiredActionType requiredAction;

    private ApiError() {
        final var now = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        this.timestamp = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.timestampMs = now.toInstant().toEpochMilli();
        this.message = "No message available";
        this.requiredAction = RequiredActionType.NO_ACTION_AVAILABLE;
    }

    public ApiError(@NonNull HttpStatus httpStatus) {
        this();
        this.httpStatus = httpStatus;
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
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(nestedError);
    }

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
