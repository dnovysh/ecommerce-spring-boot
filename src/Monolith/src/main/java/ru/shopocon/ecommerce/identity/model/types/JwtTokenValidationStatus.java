package ru.shopocon.ecommerce.identity.model.types;

public enum JwtTokenValidationStatus {
    SUCCESS,
    EMPTY_USER_DETAILS,
    SIGNATURE_EXCEPTION,
    MALFORMED_JWT_EXCEPTION,
    EXPIRED_JWT_EXCEPTION,
    UNSUPPORTED_JWT_EXCEPTION,
    ILLEGAL_ARGUMENT_EXCEPTION,
    CLASS_CAST_EXCEPTION
}
