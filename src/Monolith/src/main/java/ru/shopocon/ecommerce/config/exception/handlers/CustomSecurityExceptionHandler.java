package ru.shopocon.ecommerce.config.exception.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.shopocon.ecommerce.common.exception.exceptions.DealerNotMatchException;
import ru.shopocon.ecommerce.common.model.ApiError;
import ru.shopocon.ecommerce.common.model.ApiErrorBuilder;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomSecurityExceptionHandler {

    @ExceptionHandler(DealerNotMatchException.class)
    public ResponseEntity<ApiError> handleDealerNotMatchException(DealerNotMatchException ex) {
        log.error("DealerNotMatchException", ex);
        return ApiErrorBuilder.builder(FORBIDDEN, "Access denied")
            .setDebugMessage(ex.getMessage())
            .buildTypedResponseEntity();
    }
}
