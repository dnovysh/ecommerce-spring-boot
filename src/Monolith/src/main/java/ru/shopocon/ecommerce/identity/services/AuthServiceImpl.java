package ru.shopocon.ecommerce.identity.services;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.shopocon.ecommerce.common.exception.exceptions.AlreadySignedIn;
import ru.shopocon.ecommerce.common.exception.exceptions.InvalidPrincipal;
import ru.shopocon.ecommerce.common.exception.exceptions.InvalidUserDetails;
import ru.shopocon.ecommerce.common.util.StringUtils;
import ru.shopocon.ecommerce.identity.domain.security.User;
import ru.shopocon.ecommerce.identity.mappers.UserMapper;
import ru.shopocon.ecommerce.identity.model.*;
import ru.shopocon.ecommerce.identity.model.types.JwtTokenValidationStatus;
import ru.shopocon.ecommerce.identity.providers.JwtTokenProvider;
import ru.shopocon.ecommerce.identity.repositories.UserJpaRepository;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;

import static ru.shopocon.ecommerce.common.util.StringUtils.isBlank;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserJpaRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(UserJpaRepository userRepository,
                           UserMapper userMapper,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public ResponseEntity<AuthResponse> signIn(SignInRequest signInRequest,
                                               HttpServletResponse response,
                                               String existingEncryptedAccessToken,
                                               String existingEncryptedRefreshToken) {
        logExistingTokenWarnIfIncorrect("signIn",
            existingEncryptedAccessToken, existingEncryptedRefreshToken);
        Authentication authentication = setContextAuthentication(
            signInRequest.getUsername(),
            signInRequest.getPassword()
        );
        val user = (User) authentication.getPrincipal();
        final UserDetailsJwt userDetailsJwt = userMapper.mapToUserDetailsJwt(user);
        final UserResponseDto userResponse = userMapper.mapToUserResponseDto(user);
        final Cookie accessCookie = tokenProvider
            .createAccessCookie(userDetailsJwt, signInRequest.isRememberMe());
        final Cookie refreshCookie = tokenProvider
            .createRefreshCookie(userDetailsJwt, signInRequest.isRememberMe());
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        return ResponseEntity.ok(new AuthResponse(userResponse));
    }

    @Override
    public ResponseEntity<SignOutResponse> signOut(HttpServletRequest request,
                                                   HttpServletResponse response) {
        final HttpSession session = request.getSession(false);
        if (session != null) {
            try {
                request.logout();
            } catch (ServletException ex) {
                log.error("Session logout error {}", ex.getMessage());
            }
        }
        tokenProvider.addAccessCleanCookie(response);
        tokenProvider.addRefreshCleanCookieIfRememberMeFalse(request, response);
        return ResponseEntity.ok(new SignOutResponse("You've been signed out"));
    }

    @Override
    public ResponseEntity<AuthResponse> refresh(String encryptedRefreshToken,
                                                HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication,
                                                Principal principal) {
        if (authentication != null && authentication.isAuthenticated() && principal != null) {
            final User user = obtainUserFromPrincipal(principal);
            final UserResponseDto userResponse = userMapper.mapToUserResponseDto(user);
            return ResponseEntity.ok(new AuthResponse(userResponse));
        }
        if (isBlank(encryptedRefreshToken)) {
            return createBadRequestRefreshResponse();
        }
        final JwtGetBodyDto tokenBody = tokenProvider.getBodyFromEncryptedToken(encryptedRefreshToken);
        if (tokenBody.status() != JwtTokenValidationStatus.SUCCESS) {
            return createBadRequestRefreshResponse();
        }
        val optionalUser = userRepository.findByUsername(tokenBody.username());
        if (optionalUser.isEmpty()) {
            return createBadRequestRefreshResponse();
        }
        val user = optionalUser.get();
        final UserDetailsJwt userDetailsJwt = userMapper.mapToUserDetailsJwt(user);
        final UserResponseDto userResponse = userMapper.mapToUserResponseDto(user);
        boolean rememberMe = tokenProvider.getRememberMeByRefreshCookieMaxAge(request);
        final Cookie accessCookie = tokenProvider.createAccessCookie(userDetailsJwt, rememberMe);
        response.addCookie(accessCookie);
        return ResponseEntity.ok(new AuthResponse(userResponse));
    }

    @Override
    public Authentication setContextAuthentication(String username, String password) {
        final Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Override
    public void checkAlreadySignedIn(Authentication authentication, Principal principal) {
        if (authentication == null || principal == null) {
            return;
        }
        if (!authentication.isAuthenticated()) {
            return;
        }
        final User user = obtainUserFromPrincipal(principal);
        log.warn("User id=%s already signed in".formatted(user.getId()));
        throw new AlreadySignedIn("The user already signed in");
    }

    @Override
    public User obtainUserFromPrincipal(@NonNull Principal principal) {
        if (principal instanceof UserDetails userDetails) {
            val optionalUser = userRepository.findByUsername(userDetails.getUsername());
            return optionalUser.orElseThrow(() ->
                new InvalidUserDetails("UserDetails does not have a valid username"));
        } else {
            log.error("Principal object is not an instance of UserDetails");
            val username = principal.getName();
            val optionalUser = userRepository.findByUsername(username);
            return optionalUser.orElseThrow(() ->
                new InvalidPrincipal("Principal object does not have a valid username"));
        }
    }

    private void logExistingTokenWarnIfIncorrect(String methodName,
                                                 String encryptedAccessToken,
                                                 String encryptedRefreshToken) {
        logTokenWarnIfIncorrect(methodName, encryptedAccessToken, "Access");
        logTokenWarnIfIncorrect(methodName, encryptedRefreshToken, "Refresh");
    }

    private void logTokenWarnIfIncorrect(String methodName, String encryptedToken, String tokenAlias) {
        if (StringUtils.isNotBlank(encryptedToken)) {
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

    private ResponseEntity<AuthResponse> createBadRequestRefreshResponse() {
        return ResponseEntity.badRequest().body(new AuthResponse(null));
    }
}
