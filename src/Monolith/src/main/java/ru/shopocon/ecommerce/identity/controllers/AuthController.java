package ru.shopocon.ecommerce.identity.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.shopocon.ecommerce.identity.model.SignInRequest;
import ru.shopocon.ecommerce.identity.model.SignInResponse;
import ru.shopocon.ecommerce.identity.model.types.TokenType;
import ru.shopocon.ecommerce.identity.services.AuthService;

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
            String existingAccessToken,
        @CookieValue(name = TokenType.Constants.REFRESH_TOKEN_NAME, required = false)
            String existingRefreshToken,
        @Valid @RequestBody SignInRequest signInRequest,
        Authentication existingAuthentication,
        Principal existingPrincipal
    ) {
        if (existingAuthentication.isAuthenticated() && existingPrincipal != null) {
            return authService.createAlreadySignedInResponse(existingPrincipal);
        }
        return authService.signIn(signInRequest, existingAccessToken, existingRefreshToken);

        // ToDo remember me
    }

}
