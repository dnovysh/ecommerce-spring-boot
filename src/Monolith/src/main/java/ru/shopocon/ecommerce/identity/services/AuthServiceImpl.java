package ru.shopocon.ecommerce.identity.services;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.shopocon.ecommerce.common.model.ApiError;
import ru.shopocon.ecommerce.identity.domain.security.User;
import ru.shopocon.ecommerce.identity.mappers.UserMapper;
import ru.shopocon.ecommerce.identity.model.*;
import ru.shopocon.ecommerce.identity.model.types.JwtTokenValidationStatus;
import ru.shopocon.ecommerce.identity.providers.JwtTokenProvider;
import ru.shopocon.ecommerce.identity.repositories.UserJpaRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserJpaRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(UserJpaRepository userRepository,
                           UserDetailsJwtService userDetailsJwtService,
                           UserMapper userMapper,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public ResponseEntity<SignInResponse> signIn(SignInRequest signInRequest,
                                                 HttpServletResponse response,
                                                 String existingEncryptedAccessToken,
                                                 String existingEncryptedRefreshToken) {
        logExistingTokenWarnIfIncorrect("signIn",
            existingEncryptedAccessToken, existingEncryptedRefreshToken);
        User user;
        try {
            Authentication authentication = setContextAuthentication(
                signInRequest.getUsername(),
                signInRequest.getPassword()
            );
            user = (User) authentication.getPrincipal();
        } catch (DisabledException ex) {
            val error = new ApiError(HttpStatus.BAD_REQUEST, "The account is disabled");
            return ResponseEntity.badRequest().body(new SignInResponse(error));
        } catch (LockedException ex) {
            val error = new ApiError(HttpStatus.BAD_REQUEST, "The account is locked");
            return ResponseEntity.badRequest().body(new SignInResponse(error));
        } catch (AuthenticationException ex) {
            val error = new ApiError(HttpStatus.BAD_REQUEST, "The username or password is incorrect");
            return ResponseEntity.badRequest().body(new SignInResponse(error));
        } catch (ClassCastException ex) {
            log.error(ex.getLocalizedMessage());
            val error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong, try again later or contact support");
            return ResponseEntity.internalServerError().body(new SignInResponse(error));
        }
        final UserDetailsJwt userDetailsJwt = userMapper.mapToUserDetailsJwt(user);
        final UserResponseDto userResponse = userMapper.mapToUserResponseDto(user);
        final Cookie accessCookie = tokenProvider
            .createAccessCookie(userDetailsJwt, signInRequest.isRememberMe());
        final Cookie refreshCookie = tokenProvider
            .createRefreshCookie(userDetailsJwt, signInRequest.isRememberMe());
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        return ResponseEntity.ok(new SignInResponse(userResponse));
    }

    @Override
    public ResponseEntity<SignInResponse> signOut(HttpServletRequest request,
                                                  HttpServletResponse response) {




    }


    @Override
    public Authentication setContextAuthentication(String username, String password) {
        final Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Override
    public ResponseEntity<SignInResponse> createAlreadySignedInResponse(@NonNull Principal principal) {
        final User user = obtainUserFromPrincipal(principal);
        if (user == null) {
            log.error("Already signed in with the wrong principal");
            val error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong, try to sign out or contact support");
            val signInResponse = new SignInResponse(error);
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

    private void logExistingTokenWarnIfIncorrect(String methodName,
                                                 String encryptedAccessToken,
                                                 String encryptedRefreshToken) {
        logTokenWarnIfIncorrect(methodName, encryptedAccessToken, "Access");
        logTokenWarnIfIncorrect(methodName, encryptedRefreshToken, "Refresh");
    }

    private void logTokenWarnIfIncorrect(String methodName, String encryptedToken, String tokenAlias) {
        if (encryptedToken != null) {
            final JwtGetBodyDto tokenBody = tokenProvider.getBodyFromEncryptedToken(encryptedToken);
            final JwtTokenValidationStatus status = tokenBody.status();
            if (status == JwtTokenValidationStatus.SUCCESS) {
                log.error("[%s] %s token is valid but user (id=%s) is not logged in"
                    .formatted(methodName, tokenAlias, tokenBody.userDetailsJwt().getId()));
            } else if (status != JwtTokenValidationStatus.EXPIRED_JWT_EXCEPTION) {
                log.error("[%s] %s token is invalid".formatted(methodName, tokenAlias));
            }
        }
    }
}
