package ru.shopocon.ecommerce.identity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.shopocon.ecommerce.identity.model.types.TokenType;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class Token {
    private TokenType tokenType;
    private String value;
    private long duration;
    private OffsetDateTime expiration;
}
