package ru.shopocon.ecommerce.identity.providers;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.shopocon.ecommerce.common.util.EncryptionService;
import ru.shopocon.ecommerce.identity.model.Token;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwt;
import ru.shopocon.ecommerce.identity.model.types.TokenType;
import ru.shopocon.ecommerce.identity.services.UserDetailsServiceJpaImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {
    private final String issuer;
    private final String jwtSecret;
    private final long accessTokenExpirationMin;
    private final long refreshTokenExpirationMin;
    private final String accessCookieName;
    private final String refreshCookieName;
    private final boolean cookieSecure;

    private final UserDetailsServiceJpaImpl userDetailsService;
    private final EncryptionService encryptionService;

    public JwtTokenProviderImpl(
        @Value("${shopocon.security.jwt.issuer:shopoconIdentityService}") String issuer,
        @Value("${shopocon.security.jwt.secret:defaultJwtSecretKey}") String jwtSecret,
        @Value("${shopocon.security.jwt.access-token-expiration-min:10}") Long accessTokenExpirationMin,
        @Value("${shopocon.security.jwt.refresh-token-expiration-min:360}") Long refreshTokenExpirationMin,
        @Value("${shopocon.security.jwt.access-cookie-name:AuthJwtAccess}") String accessCookieName,
        @Value("${shopocon.security.jwt.refresh-cookie-name:AuthJwtRefresh}") String refreshCookieName,
        @Value("${shopocon.security.jwt.cookie-secure:true}") boolean cookieSecure,
        UserDetailsServiceJpaImpl userDetailsService,
        EncryptionService encryptionService) {
        this.issuer = issuer;
        this.jwtSecret = jwtSecret;
        this.accessTokenExpirationMin = accessTokenExpirationMin;
        this.refreshTokenExpirationMin = refreshTokenExpirationMin;
        this.accessCookieName = accessCookieName;
        this.refreshCookieName = refreshCookieName;
        this.cookieSecure = cookieSecure;
        this.userDetailsService = userDetailsService;
        this.encryptionService = encryptionService;
    }

    @Override
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public Authentication getAuthentication(String token) {
        UserDetailsJwt userDetails = userDetailsService.loadUserDetailsJwtByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.warn("Invalid JWT Signature");
        } catch (MalformedJwtException ex) {
            log.warn("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.warn("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.warn("Unsupported JWT exception");
        } catch (IllegalArgumentException ex) {
            log.warn("Jwt claims string is empty");
        }
        return false;
    }

    @Override
    public Token createAccessToken(UserDetailsJwt user) {
        return createToken(user, TokenType.ACCESS, OffsetDateTime.now());
    }

    @Override
    public Token createRefreshToken(UserDetailsJwt user) {
        return createToken(user, TokenType.REFRESH, OffsetDateTime.now());
    }

    @Override
    public Cookie createCookie(Token token, boolean rememberMe) {
        String cookieName = (token.getTokenType() == TokenType.ACCESS) ?
            accessCookieName : refreshCookieName;
        String encryptedToken = encryptionService.encrypt(token.getValue());
        final Cookie cookie = new Cookie(cookieName, encryptedToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        if (rememberMe) {
            int maxAge = Math.toIntExact(token.getDuration());
            cookie.setMaxAge(maxAge);
        }
        return cookie;
    }

    @Override
    public Cookie createAccessCookie(UserDetailsJwt userDetailsJwt, boolean rememberMe) {
        final Token token = createAccessToken(userDetailsJwt);
        return createCookie(token, rememberMe);
    }

    @Override
    public Cookie createRefreshCookie(UserDetailsJwt userDetailsJwt, boolean rememberMe) {
        final Token token = createRefreshToken(userDetailsJwt);
        return createCookie(token, rememberMe);
    }

    private Token createToken(UserDetailsJwt user,
                              TokenType tokenType,
                              OffsetDateTime issuedAt) {
        final long duration = 60 * (
            (tokenType == TokenType.REFRESH)
                ? refreshTokenExpirationMin : accessTokenExpirationMin
        );
        val expiration = issuedAt.plusSeconds(duration);
        final Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("userDetails", user);
        claims.put("issuedAt", issuedAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        claims.put("expiration", expiration.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        final String token = Jwts.builder()
            .setSubject(user.getUsername())
            .setClaims(claims)
            .setIssuer(issuer)
            .setIssuedAt(Date.from(issuedAt.toInstant()))
            .setExpiration(Date.from(expiration.toInstant()))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
        return new Token(tokenType, token, duration, expiration);
    }
}
