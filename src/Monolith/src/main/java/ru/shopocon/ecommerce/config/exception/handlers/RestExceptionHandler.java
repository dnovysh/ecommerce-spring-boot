package ru.shopocon.ecommerce.config.exception.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.shopocon.ecommerce.common.model.ApiErrorBuilder;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

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

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        String supportedMethods = ex.getSupportedHttpMethods() != null
            ? ex.getSupportedHttpMethods().stream().map(HttpMethod::toString).collect(Collectors.joining(", "))
            : "no supported methods";
        String message = "Http method %s is not supported. Supported methods are %s"
            .formatted(ex.getMethod(), supportedMethods);
        log.warn("Http method %s is not supported".formatted(ex.getMethod()), ex);
        return ApiErrorBuilder.builder(METHOD_NOT_ALLOWED, message, ex).buildResponseEntity();
    }
}
