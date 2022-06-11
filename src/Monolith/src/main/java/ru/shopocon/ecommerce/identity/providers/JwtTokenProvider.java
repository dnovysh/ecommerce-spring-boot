package ru.shopocon.ecommerce.identity.providers;

import ru.shopocon.ecommerce.identity.model.JwtGetBodyDto;
import ru.shopocon.ecommerce.identity.model.Token;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JwtTokenProvider {

    static final String DEFAULT_ACCESS_COOKIE_NAME = "AuthJwtAccess";
    static final String DEFAULT_REFRESH_COOKIE_NAME = "AuthJwtRefresh";

    String getBearerToken(HttpServletRequest request);

    String extractAccessTokenFromCookie(HttpServletRequest request);

    String extractRefreshTokenFromCookie(HttpServletRequest request);

    JwtGetBodyDto getBodyFromEncryptedToken(String encryptedToken);

    JwtGetBodyDto getBody(String token);

    Token createAccessToken(UserDetailsJwt user);

    Token createRefreshToken(UserDetailsJwt user);

    Cookie createCookie(Token token, boolean rememberMe);

    Cookie createAccessCookie(UserDetailsJwt userDetailsJwt, boolean rememberMe);

    Cookie createRefreshCookie(UserDetailsJwt userDetailsJwt, boolean rememberMe);

    void addAccessCleanCookie(HttpServletResponse response);

    void addRefreshCleanCookie(HttpServletResponse response);

    void addRefreshCleanCookieIfRememberMeFalse(HttpServletRequest request,
                                                HttpServletResponse response);

    boolean getRememberMeByRefreshCookieMaxAge(HttpServletRequest request);
}
