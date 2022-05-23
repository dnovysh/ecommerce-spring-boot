package ru.shopocon.ecommerce.identity.model;

import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@Getter
public class UserDetailsJwt implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 46390446L;

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
    @NonNull
    private final Set<String> roles;
    @NonNull
    private final Set<GrantedAuthority> authorities;

    public UserDetailsJwt(Long id,
                          String username,
                          boolean accountNonExpired,
                          boolean accountNonLocked,
                          boolean credentialsNonExpired,
                          boolean enabled,
                          boolean dealerRepresentative,
                          DealerDto dealer,
                          String userAlias,
                          @NonNull Set<String> roles,
                          @NonNull Set<GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.dealerRepresentative = dealerRepresentative;
        this.dealer = dealer;
        this.userAlias = userAlias;
        this.roles = roles;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.copyOf(authorities);
    }
}
