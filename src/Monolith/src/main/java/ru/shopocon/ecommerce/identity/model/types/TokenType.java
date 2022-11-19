package ru.shopocon.ecommerce.identity.model.types;

public enum TokenType {
    ACCESS(Constants.ACCESS_TOKEN_NAME),
    REFRESH(Constants.REFRESH_TOKEN_NAME);

    private static final TokenType[] VALUES = values();
    private final String value;

    TokenType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public String getTokenName() {
        return this.value;
    }

    public static final class Constants {
        public static final String ACCESS_TOKEN_NAME = "accessToken";
        public static final String REFRESH_TOKEN_NAME = "refreshToken";
    }
}
