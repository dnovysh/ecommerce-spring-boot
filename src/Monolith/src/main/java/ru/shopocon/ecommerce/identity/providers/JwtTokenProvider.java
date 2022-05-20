package ru.shopocon.ecommerce.identity.providers;

import org.springframework.security.core.Authentication;
import ru.shopocon.ecommerce.identity.model.Token;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwtDto;

import javax.servlet.http.HttpServletRequest;

public interface JwtTokenProvider {

    String getUsername(String token);

    Authentication getAuthentication(String token);

    String resolveToken(HttpServletRequest request);

    boolean validateToken(String token);

    Token createAccessToken(UserDetailsJwtDto user);

    Token createRefreshToken(UserDetailsJwtDto user);
}
