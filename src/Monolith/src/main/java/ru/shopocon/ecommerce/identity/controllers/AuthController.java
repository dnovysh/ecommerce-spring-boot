package ru.shopocon.ecommerce.identity.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.shopocon.ecommerce.identity.model.RefreshResponse;
import ru.shopocon.ecommerce.identity.model.SignInRequest;
import ru.shopocon.ecommerce.identity.model.SignInResponse;
import ru.shopocon.ecommerce.identity.model.SignOutResponse;
import ru.shopocon.ecommerce.identity.model.types.TokenType;
import ru.shopocon.ecommerce.identity.services.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/signin",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignInResponse> signIn(
        @CookieValue(name = TokenType.Constants.ACCESS_TOKEN_NAME, required = false)
            String existingEncryptedAccessToken,
        @CookieValue(name = TokenType.Constants.REFRESH_TOKEN_NAME, required = false)
            String existingEncryptedRefreshToken,
        @Valid @RequestBody SignInRequest signInRequest,
        HttpServletResponse response,
        Authentication existingAuthentication,
        Principal existingPrincipal
    ) {
        if (existingAuthentication.isAuthenticated() && existingPrincipal != null) {
            return authService.createAlreadySignedInResponse(existingPrincipal);
        }
        return authService.signIn(signInRequest, response,
            existingEncryptedAccessToken, existingEncryptedRefreshToken);
    }

    @PostMapping(value = "/signout")
    public ResponseEntity<SignOutResponse> signOut(HttpServletRequest request,
                                                   HttpServletResponse response) {
        return authService.signOut(request, response);
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<RefreshResponse> refresh(
        @CookieValue(name = TokenType.Constants.REFRESH_TOKEN_NAME, required = false)
            String encryptedRefreshToken,
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication,
        Principal principal
    ) {
        return authService.refresh(encryptedRefreshToken, request, response, authentication, principal);
    }
}
