package ru.shopocon.ecommerce.identity.model;

import ru.shopocon.ecommerce.common.model.ApiError;

public record SignInResponse(UserDetailsResponseDto userDetails,
                             ApiError apiError) {
}
