package ru.shopocon.ecommerce.identity.model;

import ru.shopocon.ecommerce.common.model.ApiError;

public record SignInResponse(UserResponseDto user,
                             ApiError error) {

    public SignInResponse(UserResponseDto user) {
        this(user, null);
    }

    public SignInResponse(ApiError error) {
        this(null, error);
    }
}
