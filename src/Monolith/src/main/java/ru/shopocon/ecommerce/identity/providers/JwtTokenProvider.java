package ru.shopocon.ecommerce.identity.providers;

import org.springframework.security.core.Authentication;
import ru.shopocon.ecommerce.identity.model.JwtGetBodyDto;
import ru.shopocon.ecommerce.identity.model.Token;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public interface JwtTokenProvider {

    String getUsername(String token);

    Authentication getAuthentication(String token);

    String resolveBearerToken(HttpServletRequest request);

    boolean validateEncryptedToken(String encryptedToken);

    boolean validateToken(String token);

    JwtGetBodyDto getBodyFromEncryptedToken(String encryptedToken);

    JwtGetBodyDto getBody(String token);

    Token createAccessToken(UserDetailsJwt user);

    Token createRefreshToken(UserDetailsJwt user);

    Cookie createCookie(Token token, boolean rememberMe);

    Cookie createAccessCookie(UserDetailsJwt userDetailsJwt, boolean rememberMe);

    Cookie createRefreshCookie(UserDetailsJwt userDetailsJwt, boolean rememberMe);
}
