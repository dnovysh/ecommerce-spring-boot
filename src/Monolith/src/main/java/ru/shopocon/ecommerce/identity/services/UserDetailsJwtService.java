package ru.shopocon.ecommerce.identity.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwtDto;

public interface UserDetailsJwtService {
    UserDetailsJwtDto loadUserJwtDtoByUsername(String username) throws UsernameNotFoundException;

    UserDetailsJwtDto loadUserJwtDtoByUserId(Long id) throws UsernameNotFoundException;
}
