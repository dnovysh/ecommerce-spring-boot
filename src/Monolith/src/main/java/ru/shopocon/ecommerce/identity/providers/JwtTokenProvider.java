package ru.shopocon.ecommerce.identity.providers;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwtDto;
import ru.shopocon.ecommerce.identity.services.UserDetailsServiceJpaImpl;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class JwtTokenProvider {
    private final String jwtSecret;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    private final UserDetailsServiceJpaImpl userDetailsService;

    public JwtTokenProvider(
        @Value("${shopocon.security.jwt.secret}: defaultJwtSecretKey") String jwtSecret,
        @Value("${shopocon.security.jwt.access-token-expiration-min}: 10") Long accessTokenExpirationMin,
        @Value("${shopocon.security.jwt.refresh-token-expiration-min}: 360") Long refreshTokenExpirationMin,
        UserDetailsServiceJpaImpl userDetailsService) {
        this.jwtSecret = jwtSecret;
        this.accessTokenExpiration = accessTokenExpirationMin * 60 * 1000;
        this.refreshTokenExpiration = refreshTokenExpirationMin  * 60 * 1000;
        this.userDetailsService = userDetailsService;
    }

    public Authentication getAuthentication(String token) {
        UserDetailsJwtDto userDetails = userDetailsService.loadUserJwtDtoByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

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


    public Token generateToken(User user) {


        Claims claims = Jwts.claims().setSubject(user.getUsername());

        claims.put("auth", user.getRoles().stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));

        Date now = new Date();
        Long duration = now.getTime() + jwtExpirationInMs;
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY, 8);

        String token = Jwts.builder().setClaims(claims).setSubject((user.getUsername())).setIssuedAt(new Date())
            .setExpiration(expiryDate).signWith(SignatureAlgorithm.HS256, jwtSecret).compact();

        return new Token(Token.TokenType.ACCESS, token, duration, LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));


    }


    public Token generateRefreshToken(User user) {


        Claims claims = Jwts.claims().setSubject(user.getUsername());


        claims.put("auth", user.getRoles().stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));
        Date now = new Date();
        Long duration = now.getTime() + refreshTokenExpirationMsec;
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMsec);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY, 8);
        String token = Jwts.builder().setClaims(claims).setSubject((user.getUsername())).setIssuedAt(new Date())
            .setExpiration(expiryDate).signWith(SignatureAlgorithm.HS256, jwtSecret).compact();

        return new Token(Token.TokenType.REFRESH, token, duration, LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));


    }
}
