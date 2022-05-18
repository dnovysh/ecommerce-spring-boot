package ru.shopocon.ecommerce.identity.mappers;

import lombok.val;
import org.springframework.stereotype.Component;
import ru.shopocon.ecommerce.identity.domain.security.Role;
import ru.shopocon.ecommerce.identity.domain.security.User;
import ru.shopocon.ecommerce.identity.model.DealerDto;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwtDto;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDetailsJwtDto mapToUserDetailsJwtDto(User user) {
        val dealerDto = new DealerDto(
            user.getDealer().getId(),
            user.getDealer().getName()
        );

        val roles = user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());

        return new UserDetailsJwtDto(
            user.getId(),
            user.getUsername(),
            user.isAccountNonExpired(),
            user.isAccountNonLocked(),
            user.isCredentialsNonExpired(),
            user.isEnabled(),
            user.isDealerRepresentative(),
            dealerDto,
            user.getUserAlias(),
            Set.copyOf(user.getAuthorities()),
            roles
        );
    }
}
