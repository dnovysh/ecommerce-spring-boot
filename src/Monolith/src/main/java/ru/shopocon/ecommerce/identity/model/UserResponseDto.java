package ru.shopocon.ecommerce.identity.model;

import java.util.Set;

public record UserResponseDto(Long id,
                              String username,
                              boolean accountNonExpired,
                              boolean accountNonLocked,
                              boolean credentialsNonExpired,
                              boolean enabled,
                              boolean dealerRepresentative,
                              DealerDto dealer,
                              String userAlias,
                              Set<String> roles,
                              Set<String> authorities) {
}
