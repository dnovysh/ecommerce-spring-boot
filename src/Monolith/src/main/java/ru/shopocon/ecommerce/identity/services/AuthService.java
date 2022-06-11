package ru.shopocon.ecommerce.identity.services;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import ru.shopocon.ecommerce.identity.domain.security.User;
import ru.shopocon.ecommerce.identity.model.AuthResponse;
import ru.shopocon.ecommerce.identity.model.SignInRequest;
import ru.shopocon.ecommerce.identity.model.SignOutResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

public interface AuthService {

    ResponseEntity<AuthResponse> signIn(SignInRequest signInRequest,
                                        HttpServletResponse response,
                                        String existingEncryptedAccessToken,
                                        String existingEncryptedRefreshToken,
                                        Authentication existingAuthentication,
                                        Principal existingPrincipal);

    ResponseEntity<SignOutResponse> signOut(HttpServletRequest request,
                                            HttpServletResponse response);

    ResponseEntity<AuthResponse> refresh(String encryptedRefreshToken,
                                         HttpServletRequest request,
                                         HttpServletResponse response,
                                         Authentication authentication,
                                         Principal principal);

    Authentication setContextAuthentication(String username, String password);

    User obtainUserFromPrincipal(@NonNull Principal principal);
}
