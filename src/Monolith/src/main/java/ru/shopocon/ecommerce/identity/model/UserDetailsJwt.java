package ru.shopocon.ecommerce.identity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
public class UserDetailsJwt implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 46390446L;

    private static final String PASSWORD = "User password not provided by token";

    private final Long id;
    private final String username;
    private final String password;
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

    @JsonCreator
    public UserDetailsJwt(@JsonProperty("id") Long id,
                          @JsonProperty("username") String username,
                          @JsonProperty("password") String password,
                          @JsonProperty("accountNonExpired") boolean accountNonExpired,
                          @JsonProperty("accountNonLocked") boolean accountNonLocked,
                          @JsonProperty("credentialsNonExpired") boolean credentialsNonExpired,
                          @JsonProperty("enabled") boolean enabled,
                          @JsonProperty("dealerRepresentative") boolean dealerRepresentative,
                          @JsonProperty("dealer") DealerDto dealer,
                          @JsonProperty("userAlias") String userAlias,
                          @JsonProperty("roles") Set<String> roles,
                          @JsonProperty("authorities") Set<GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = PASSWORD;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.dealerRepresentative = dealerRepresentative;
        this.dealer = dealer;
        this.userAlias = userAlias;
        this.roles = (roles != null) ? roles : new HashSet<>();
        this.authorities = (authorities != null) ? authorities : new HashSet<>();
    }

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
        this(id, username, PASSWORD, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled,
            dealerRepresentative, dealer, userAlias, roles, authorities);
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.copyOf(authorities);
    }
}
