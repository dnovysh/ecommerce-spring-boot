package ru.shopocon.ecommerce.identity.mappers;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwt;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwtPlain;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserDetailsJwtMapperImpl implements UserDetailsJwtMapper {

    @Override
    public UserDetailsJwtPlain mapToUserDetailsJwtPlain(UserDetailsJwt userDetailsJwt) {
        if (userDetailsJwt == null) {
            return null;
        }
        return new UserDetailsJwtPlain(
            userDetailsJwt.getId(),
            userDetailsJwt.getUsername(),
            userDetailsJwt.isAccountNonExpired(),
            userDetailsJwt.isAccountNonLocked(),
            userDetailsJwt.isCredentialsNonExpired(),
            userDetailsJwt.isEnabled(),
            userDetailsJwt.isDealerRepresentative(),
            userDetailsJwt.getDealer(),
            userDetailsJwt.getUserAlias(),
            rolesToArray(userDetailsJwt.getRoles()),
            authoritiesToArray(userDetailsJwt.getAuthorities())
        );
    }

    @Override
    public UserDetailsJwt mapFromUserDetailsJwtPlain(UserDetailsJwtPlain userDetailsJwtPlain) {
        if (userDetailsJwtPlain == null) {
            return null;
        }
        return new UserDetailsJwt(
          userDetailsJwtPlain.getId(),
            userDetailsJwtPlain.getUsername(),
            userDetailsJwtPlain.isAccountNonExpired(),
            userDetailsJwtPlain.isAccountNonLocked(),
            userDetailsJwtPlain.isCredentialsNonExpired(),
            userDetailsJwtPlain.isEnabled(),
            userDetailsJwtPlain.isDealerRepresentative(),
            userDetailsJwtPlain.getDealer(),
            userDetailsJwtPlain.getUserAlias(),
            rolesToSet(userDetailsJwtPlain.getRoles()),
            authoritiesToSet(userDetailsJwtPlain.getAuthorities())
        );
    }

    private String[] rolesToArray(Set<String> roles) {
        if (roles == null || roles.size() == 0) {
            return new String[0];
        }
        return roles.toArray(new String[0]);
    }

    private Set<String> rolesToSet(String[] roles) {
        if (roles == null || roles.length == 0) {
            return new HashSet<>();
        }
        return Arrays.stream(roles).collect(Collectors.toSet());
    }

    private String[] authoritiesToArray(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null || authorities.size() == 0) {
            return new String[0];
        }
        return authorities.stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }

    private Set<GrantedAuthority> authoritiesToSet(String[] authorities) {
        if (authorities == null || authorities.length == 0) {
            return new HashSet<>();
        }
        return Arrays.stream(authorities).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}
