package ru.shopocon.ecommerce.identity.controllers;

import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shopocon.ecommerce.identity.model.SignInRequest;
import ru.shopocon.ecommerce.identity.model.SignInResponse;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping(value = "/signin",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignInResponse> signIn(
        @CookieValue(name = "accessToken", required = false) String accessToken,
        @CookieValue(name = "refreshToken", required = false) String refreshToken,
        @Valid @RequestBody SignInRequest signInRequest,
        HttpServletResponse response
    ) {
        val username = signInRequest.getUsername();
        val password = signInRequest.getPassword();

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        if (isAlreadyLoggedIn(signInRequest.getUsername())) {
            signInResponse signInResponse = new signInResponse();
            signInResponse.setError("User Already logged in");
            return ResponseEntity.ok(signInResponse);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
        return userService.signIn(signInRequest, decryptedAccessToken, decryptedRefreshToken);
        // ToDo remember me
    }

}
