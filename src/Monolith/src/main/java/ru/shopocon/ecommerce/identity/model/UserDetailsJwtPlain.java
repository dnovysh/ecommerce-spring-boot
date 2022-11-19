package ru.shopocon.ecommerce.identity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDetailsJwtPlain implements Serializable {

    @Serial
    private static final long serialVersionUID = 46390446L;

    private Long id;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private boolean dealerRepresentative;
    private DealerDto dealer;
    private String userAlias;
    private String[] roles;
    private String[] authorities;
}
