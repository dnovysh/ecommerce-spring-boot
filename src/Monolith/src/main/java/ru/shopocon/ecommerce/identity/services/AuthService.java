package ru.shopocon.ecommerce.identity.services;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import ru.shopocon.ecommerce.identity.domain.security.User;
import ru.shopocon.ecommerce.identity.model.SignInRequest;
import ru.shopocon.ecommerce.identity.model.SignInResponse;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

public interface AuthService {

    ResponseEntity<SignInResponse> signIn(SignInRequest signInRequest,
                                          HttpServletResponse response,
                                          String existingAccessToken,
                                          String existingRefreshToken);

    Authentication setContextAuthentication(String username, String password);

    ResponseEntity<SignInResponse> createAlreadySignedInResponse(@NonNull Principal principal);

    User obtainUserFromPrincipal(@NonNull Principal principal);
}
