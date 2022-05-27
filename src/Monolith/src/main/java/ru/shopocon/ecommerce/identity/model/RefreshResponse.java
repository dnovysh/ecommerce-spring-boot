package ru.shopocon.ecommerce.identity.model;

import ru.shopocon.ecommerce.common.model.ApiError;

public record RefreshResponse(UserResponseDto user,
                              ApiError error) {

    public RefreshResponse(UserResponseDto user) {
        this(user, null);
    }

    public RefreshResponse(ApiError error) {
        this(null, error);
    }
}
