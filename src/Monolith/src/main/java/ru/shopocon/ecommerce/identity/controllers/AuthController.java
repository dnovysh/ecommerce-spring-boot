package ru.shopocon.ecommerce.identity.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.shopocon.ecommerce.identity.model.AuthResponse;
import ru.shopocon.ecommerce.identity.model.SignInRequest;
import ru.shopocon.ecommerce.identity.model.SignOutResponse;
import ru.shopocon.ecommerce.identity.model.SignUpRequest;
import ru.shopocon.ecommerce.identity.services.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;

import static ru.shopocon.ecommerce.identity.providers.JwtTokenProvider.DEFAULT_ACCESS_COOKIE_NAME;
import static ru.shopocon.ecommerce.identity.providers.JwtTokenProvider.DEFAULT_REFRESH_COOKIE_NAME;


@RestController
//@BasePathAwareController
@RequestMapping("${spring.data.rest.base-path}/auth")
public class AuthController {
    private final static String ACCESS_COOKIE_NAME =
        "${shopocon.security.jwt.access-cookie-name:" + DEFAULT_ACCESS_COOKIE_NAME + "}";
    private final static String REFRESH_COOKIE_NAME =
        "${shopocon.security.jwt.refresh-cookie-name:" + DEFAULT_REFRESH_COOKIE_NAME + "}";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/signin",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> signIn(
        @CookieValue(name = ACCESS_COOKIE_NAME, required = false) String existingEncryptedAccessToken,
        @CookieValue(name = REFRESH_COOKIE_NAME, required = false) String existingEncryptedRefreshToken,
        @Valid @RequestBody SignInRequest signInRequest,
        HttpServletResponse response, Authentication existingAuthentication, Principal existingPrincipal
    ) {
        return authService.signIn(signInRequest, response,
            existingEncryptedAccessToken, existingEncryptedRefreshToken,
            existingAuthentication, existingPrincipal);
    }

    @PostMapping(value = "/signup",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> signUp(
        @CookieValue(name = ACCESS_COOKIE_NAME, required = false) String existingEncryptedAccessToken,
        @CookieValue(name = REFRESH_COOKIE_NAME, required = false) String existingEncryptedRefreshToken,
        @Valid @RequestBody SignUpRequest signUpRequest,
        HttpServletResponse response, Authentication existingAuthentication, Principal existingPrincipal
    ) {
        return authService.signUp(signUpRequest, response,
            existingEncryptedAccessToken, existingEncryptedRefreshToken,
            existingAuthentication, existingPrincipal);
    }

    @PostMapping(value = "/signout")
    public ResponseEntity<SignOutResponse> signOut(HttpServletRequest request,
                                                   HttpServletResponse response) {
        return authService.signOut(request, response);
    }

    @GetMapping(value = "/refresh")
    public ResponseEntity<AuthResponse> refresh(
        @CookieValue(name = REFRESH_COOKIE_NAME, required = false) String encryptedRefreshToken,
        HttpServletRequest request, HttpServletResponse response,
        Authentication authentication, Principal principal
    ) {
        return authService.refresh(encryptedRefreshToken, request, response, authentication, principal);
    }
}
