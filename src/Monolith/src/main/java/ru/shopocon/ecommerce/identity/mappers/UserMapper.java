package ru.shopocon.ecommerce.identity.mappers;

import org.springframework.lang.NonNull;
import ru.shopocon.ecommerce.identity.domain.security.User;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwt;
import ru.shopocon.ecommerce.identity.model.UserResponseDto;

public interface UserMapper {
    UserDetailsJwt mapToUserDetailsJwt(@NonNull User user);

    UserResponseDto mapToUserResponseDto(@NonNull User user);
}
