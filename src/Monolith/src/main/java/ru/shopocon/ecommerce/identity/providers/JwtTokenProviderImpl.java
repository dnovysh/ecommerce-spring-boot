package ru.shopocon.ecommerce.identity.providers;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import ru.shopocon.ecommerce.common.util.EncryptionService;
import ru.shopocon.ecommerce.common.util.StringUtils;
import ru.shopocon.ecommerce.identity.model.JwtGetBodyDto;
import ru.shopocon.ecommerce.identity.model.Token;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwt;
import ru.shopocon.ecommerce.identity.model.types.JwtTokenValidationStatus;
import ru.shopocon.ecommerce.identity.model.types.TokenType;
//import ru.shopocon.ecommerce.identity.services.UserDetailsServiceJpaImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

// ToDo clear comments


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
    private final String cookiePath = "/";

    //    private final UserDetailsServiceJpaImpl userDetailsService;
    private final EncryptionService encryptionService;

    public JwtTokenProviderImpl(
        @Value("${shopocon.security.jwt.issuer:shopoconIdentityService}") String issuer,
        @Value("${shopocon.security.jwt.secret:defaultJwtSecretKey}") String jwtSecret,
        @Value("${shopocon.security.jwt.access-token-expiration-min:10}") Long accessTokenExpirationMin,
        @Value("${shopocon.security.jwt.refresh-token-expiration-min:360}") Long refreshTokenExpirationMin,
        @Value("${shopocon.security.jwt.access-cookie-name:AuthJwtAccess}") String accessCookieName,
        @Value("${shopocon.security.jwt.refresh-cookie-name:AuthJwtRefresh}") String refreshCookieName,
        @Value("${shopocon.security.jwt.cookie-secure:true}") boolean cookieSecure,
//        UserDetailsServiceJpaImpl userDetailsService,
        EncryptionService encryptionService) {
        this.issuer = issuer;
        this.jwtSecret = jwtSecret;
        this.accessTokenExpirationMin = accessTokenExpirationMin;
        this.refreshTokenExpirationMin = refreshTokenExpirationMin;
        this.accessCookieName = accessCookieName;
        this.refreshCookieName = refreshCookieName;
        this.cookieSecure = cookieSecure;
//        this.userDetailsService = userDetailsService;
        this.encryptionService = encryptionService;
    }

//    @Override
//    public String getUsername(String token) {
//        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
//    }
//
//    @Override
//    public Authentication getAuthentication(String token) {
//        UserDetailsJwt userDetails = userDetailsService.loadUserDetailsJwtByUsername(getUsername(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    @Override
    public String getBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            final String encryptedToken = bearerToken.substring(7);
            if (StringUtils.isNotBlank(encryptedToken)) {
                return encryptionService.decrypt(encryptedToken);
            }
        }
        return null;
    }

    @Override
    public String extractAccessTokenFromCookie(HttpServletRequest request) {
        return extractTokenFromCookie(request, accessCookieName);
    }

    @Override
    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        return extractTokenFromCookie(request, refreshCookieName);
    }

    @Override
    public boolean validateEncryptedToken(String encryptedToken) {
        final String token = encryptionService.decrypt(encryptedToken);
        return validateToken(token);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT Signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.warn("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT exception: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Jwt claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

    @Override
    public JwtGetBodyDto getBodyFromEncryptedToken(String encryptedToken) {
        final String token = encryptionService.decrypt(encryptedToken);
        return getBody(token);
    }

    @Override
    public JwtGetBodyDto getBody(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
            val userDetails = claims.get("userDetails", UserDetailsJwt.class);
            if (userDetails == null) {
                log.error("Empty user details");
                return new JwtGetBodyDto(JwtTokenValidationStatus.EMPTY_USER_DETAILS);
            }
            return new JwtGetBodyDto(
                JwtTokenValidationStatus.SUCCESS,
                claims.getSubject(),
                userDetails,
                OffsetDateTime.parse(claims.get("issuedAt", String.class),
                    DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                OffsetDateTime.parse(claims.get("expiration", String.class),
                    DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        } catch (SignatureException ex) {
            log.error("Invalid JWT Signature: {}", ex.getMessage());
            return new JwtGetBodyDto(JwtTokenValidationStatus.SIGNATURE_EXCEPTION);
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
            return new JwtGetBodyDto(JwtTokenValidationStatus.MALFORMED_JWT_EXCEPTION);
        } catch (ExpiredJwtException ex) {
            log.warn("Expired JWT token: {}", ex.getMessage());
            return new JwtGetBodyDto(JwtTokenValidationStatus.EXPIRED_JWT_EXCEPTION);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT exception: {}", ex.getMessage());
            return new JwtGetBodyDto(JwtTokenValidationStatus.UNSUPPORTED_JWT_EXCEPTION);
        } catch (IllegalArgumentException ex) {
            log.error("Jwt claims string is empty: {}", ex.getMessage());
            return new JwtGetBodyDto(JwtTokenValidationStatus.ILLEGAL_ARGUMENT_EXCEPTION);
        } catch (ClassCastException ex) {
            log.error("Jwt class cast exception: {}", ex.getMessage());
            return new JwtGetBodyDto(JwtTokenValidationStatus.CLASS_CAST_EXCEPTION);
        } catch (RuntimeException ex) {
            log.error("Unknown exception: {}", ex.getMessage());
            return new JwtGetBodyDto(JwtTokenValidationStatus.UNKNOWN_RUNTIME_EXCEPTION);
        }
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
        cookie.setPath(cookiePath);
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

    @Override
    public void addAccessCleanCookie(HttpServletResponse response) {
        response.addCookie(createAccessCleanCookie());
    }

    @Override
    public void addRefreshCleanCookieIfRememberMeFalse(HttpServletRequest request,
                                                       HttpServletResponse response) {
        Arrays.stream(request.getCookies())
            .filter((c) -> refreshCookieName.equals(c.getName()))
            .forEach((c) -> {
                if (c.getMaxAge() < 0) {
                    response.addCookie(createRefreshCleanCookie());
                }
            });
    }

    @Override
    public boolean getRememberMeByRefreshCookieMaxAge(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
            .filter((c) -> refreshCookieName.equals(c.getName()))
            .findFirst()
            .map(Cookie::getMaxAge)
            .map(maxAge -> maxAge > 0)
            .orElse(false);
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

    private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        final Cookie cookie = WebUtils.getCookie(request, cookieName);
        if (cookie == null) {
            return null;
        }
        final String encryptedToken = cookie.getValue();
        if (StringUtils.isBlank(encryptedToken)) {
            return null;
        }
        return encryptionService.decrypt(encryptedToken);
    }

    private Cookie createCleanCookie(String cookieName) {
        final Cookie cookie = new Cookie(cookieName, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath(cookiePath);
        cookie.setMaxAge(0);
        return cookie;
    }

    private Cookie createAccessCleanCookie() {
        return createCleanCookie(accessCookieName);
    }

    private Cookie createRefreshCleanCookie() {
        return createCleanCookie(refreshCookieName);
    }
}
