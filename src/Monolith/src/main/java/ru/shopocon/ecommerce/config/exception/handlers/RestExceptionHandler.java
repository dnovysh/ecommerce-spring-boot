package ru.shopocon.ecommerce.config.exception.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.shopocon.ecommerce.common.model.ApiErrorBuilder;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
        HttpMediaTypeNotSupportedException ex, HttpHeaders headers,
        HttpStatus status, WebRequest request
    ) {
        String supportedMediaTypes = ex.getSupportedMediaTypes()
            .stream().map(MimeType::toString).collect(Collectors.joining(", "));
        String message = "%s media type is not supported. Supported media types are %s"
            .formatted(ex.getContentType(), supportedMediaTypes);
        return ApiErrorBuilder.builder(UNSUPPORTED_MEDIA_TYPE, message, ex).buildResponseEntity();
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException ex, HttpHeaders headers,
        HttpStatus status, WebRequest request
    ) {
        String message = "%s parameter is missing".formatted(ex.getParameterName());
        return ApiErrorBuilder.builder(BAD_REQUEST, message, ex).buildResponseEntity();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers,
        HttpStatus status, WebRequest request
    ) {
        return ApiErrorBuilder.builder(BAD_REQUEST, "Validation error")
            .addApiValidationFieldErrors(ex.getBindingResult().getFieldErrors())
            .addApiValidationObjectErrors(ex.getBindingResult().getGlobalErrors())
            .buildResponseEntity();
    }
}
