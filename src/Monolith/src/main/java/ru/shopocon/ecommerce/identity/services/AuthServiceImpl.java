package ru.shopocon.ecommerce.identity.services;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.shopocon.ecommerce.common.model.ApiError;
import ru.shopocon.ecommerce.identity.domain.security.User;
import ru.shopocon.ecommerce.identity.mappers.UserMapper;
import ru.shopocon.ecommerce.identity.model.SignInResponse;
import ru.shopocon.ecommerce.identity.model.UserResponseDto;
import ru.shopocon.ecommerce.identity.repositories.UserJpaRepository;

import java.security.Principal;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserJpaRepository userRepository;
    private final UserDetailsJwtService userDetailsJwtService;
    private final UserMapper userMapper;

    public AuthServiceImpl(UserJpaRepository userRepository,
                           UserDetailsJwtService userDetailsJwtService,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userDetailsJwtService = userDetailsJwtService;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<SignInResponse> createAlreadySignedInResponse(@NonNull Principal principal) {
        final User user = obtainUserFromPrincipal(principal);
        if (user == null) {
            log.error("Already signed in with the wrong principal");
            val error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong, try to sign out or contact support");
            val signInResponse = new SignInResponse(null, error);
            return ResponseEntity.internalServerError().body(signInResponse);
        }
        final UserResponseDto userResponse = userMapper.mapToUserResponseDto(user);
        val error = new ApiError(HttpStatus.BAD_REQUEST,
            "The user is already signed in, you need to sign out first");
        val signInResponse = new SignInResponse(userResponse, error);
        return ResponseEntity.badRequest().body(signInResponse);
    }

    @Override
    public User obtainUserFromPrincipal(@NonNull Principal principal) {
        if (principal instanceof UserDetails userDetails) {
            val optionalUser = userRepository.findByUsername(userDetails.getUsername());
            if (optionalUser.isEmpty()) {
                log.error("UserDetails does not have a valid username");
                return null;
            }
            return optionalUser.orElseThrow();
        } else {
            log.error("Principal object is not an instance of UserDetails");
            val username = principal.getName();
            val optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isEmpty()) {
                log.error("Principal object does not have a valid username");
                return null;
            }
            return optionalUser.orElseThrow();
        }
    }
}
