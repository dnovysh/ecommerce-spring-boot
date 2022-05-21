package ru.shopocon.ecommerce.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import org.springframework.http.HttpStatus;
import ru.shopocon.ecommerce.common.model.types.ApiNestedError;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
public class ApiError {
    private int status;
    private String error;
    private String isoOffsetDateTime;
    private long timestamp;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private String debugMessage;
    @Getter
    @Singular
    private List<ApiNestedError> nestedErrors;

    private ApiError() {
        final var now = OffsetDateTime.now();
        this.isoOffsetDateTime = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.timestamp = now.toInstant().toEpochMilli();
        this.nestedErrors = new ArrayList<>();
    }

    ApiError(HttpStatus httpStatus) {
        this();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
    }

    ApiError(HttpStatus httpStatus, String message, Throwable ex) {
        this(httpStatus);
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(HttpStatus httpStatus, Throwable ex) {
        this(httpStatus, "Unexpected error", ex);
    }
}
