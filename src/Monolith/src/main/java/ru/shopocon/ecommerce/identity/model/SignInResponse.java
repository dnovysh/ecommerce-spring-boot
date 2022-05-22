package ru.shopocon.ecommerce.identity.model;

import ru.shopocon.ecommerce.common.model.ApiError;

public record SignInResponse(UserResponseDto user,
                             ApiError error) {
}
