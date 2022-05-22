package ru.shopocon.ecommerce.identity.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.shopocon.ecommerce.identity.mappers.UserMapper;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwtDto;
import ru.shopocon.ecommerce.identity.repositories.UserJpaRepository;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsServiceJpaImpl implements UserDetailsService, UserDetailsJwtService {

    private final UserJpaRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
            getUsernameNotFoundException("name", username)
        );
    }

    @Override
    @Transactional
    public UserDetailsJwtDto loadUserJwtDtoByUsername(String username) throws UsernameNotFoundException {
        val user = userRepository.findByUsername(username).orElseThrow(() ->
            getUsernameNotFoundException("name", username));
        return userMapper.mapToUserDetailsJwtDto(user);
    }

    @Override
    @Transactional
    public UserDetailsJwtDto loadUserJwtDtoByUserId(Long id) throws UsernameNotFoundException {
        val user = userRepository.findById(id).orElseThrow(() ->
            getUsernameNotFoundException("id", id.toString()));
        return userMapper.mapToUserDetailsJwtDto(user);
    }

    @Override
    public UsernameNotFoundException getUsernameNotFoundException(String uniqueFieldName,
                                                                   String value) {
        return new UsernameNotFoundException("User %s: %s not found".formatted(uniqueFieldName, value));
    }
}
