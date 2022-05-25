package ru.shopocon.ecommerce.identity.model;

import ru.shopocon.ecommerce.identity.model.types.JwtTokenValidationStatus;

import java.time.OffsetDateTime;

public record JwtGetBodyDto(JwtTokenValidationStatus status,
                            String username,
                            UserDetailsJwt userDetailsJwt,
                            OffsetDateTime issuedAt,
                            OffsetDateTime expiration) {
    public JwtGetBodyDto(JwtTokenValidationStatus status){
        this(status, null, null, null, null);
    }
}
