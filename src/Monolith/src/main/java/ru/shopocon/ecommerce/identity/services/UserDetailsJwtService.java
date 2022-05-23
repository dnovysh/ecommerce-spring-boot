package ru.shopocon.ecommerce.identity.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwt;

public interface UserDetailsJwtService {
    UserDetailsJwt loadUserDetailsJwtByUsername(String username) throws UsernameNotFoundException;

    UserDetailsJwt loadUserDetailsJwtByUserId(Long id) throws UsernameNotFoundException;

    UsernameNotFoundException getUsernameNotFoundException(String uniqueFieldName,
                                                           String value);
}
