package ru.shopocon.ecommerce.identity.model;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@Getter
public class UserDetailsJwtDto implements UserDetails, Serializable {
    private final Long id;
    private final String password = "User password not provided by token";
    private final String username;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    private final boolean dealerRepresentative;
    private final DealerDto dealer;
    private final String userAlias;
    private final Set<GrantedAuthority> authorities;
    private final Set<String> roles;

    public UserDetailsJwtDto(Long id,
                             String username,
                             boolean accountNonExpired,
                             boolean accountNonLocked,
                             boolean credentialsNonExpired,
                             boolean enabled,
                             boolean dealerRepresentative,
                             DealerDto dealer,
                             String userAlias,
                             @NonNull Set<GrantedAuthority> authorities,
                             @NonNull Set<String> roles) {
        this.id = id;
        this.username = username;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.dealerRepresentative = dealerRepresentative;
        this.dealer = dealer;
        this.userAlias = userAlias;
        this.authorities = authorities;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.copyOf(authorities);
    }
}
