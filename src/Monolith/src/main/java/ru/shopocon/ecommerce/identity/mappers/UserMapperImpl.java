package ru.shopocon.ecommerce.identity.mappers;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ru.shopocon.ecommerce.identity.domain.security.Role;
import ru.shopocon.ecommerce.identity.domain.security.User;
import ru.shopocon.ecommerce.identity.model.DealerDto;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwt;
import ru.shopocon.ecommerce.identity.model.UserResponseDto;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    private final DealerMapper dealerMapper;

    public UserMapperImpl(DealerMapper dealerMapper) {
        this.dealerMapper = dealerMapper;
    }

    @Override
    public UserDetailsJwt mapToUserDetailsJwt(@NonNull User user) {
        return new UserDetailsJwt(
            user.getId(),
            user.getUsername(),
            user.isAccountNonExpired(),
            user.isAccountNonLocked(),
            user.isCredentialsNonExpired(),
            user.isEnabled(),
            user.isDealerRepresentative(),
            createDealerDto(user),
            user.getUserAlias(),
            extractRolesAsStringSet(user),
            extractAuthorities(user)
        );
    }

    @Override
    public UserResponseDto mapToUserResponseDto(@NonNull User user) {
        return new UserResponseDto(
            user.getId(),
            user.getUsername(),
            user.isAccountNonExpired(),
            user.isAccountNonLocked(),
            user.isCredentialsNonExpired(),
            user.isEnabled(),
            user.isDealerRepresentative(),
            createDealerDto(user),
            user.getUserAlias(),
            extractRolesAsStringSet(user),
            extractAuthoritiesAsStringSet(user)
        );
    }

    private DealerDto createDealerDto(@NonNull User user) {
        if (user.getDealer() == null) {
            return null;
        }
        return dealerMapper.mapToDealerDto(user.getDealer());
    }

    private Set<String> extractRolesAsStringSet(@NonNull User user) {
        return user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());
    }

    private Set<GrantedAuthority> extractAuthorities(@NonNull User user) {
        return Set.copyOf(user.getAuthorities());
    }

    private Set<String> extractAuthoritiesAsStringSet(@NonNull User user) {
        return user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
    }
}
