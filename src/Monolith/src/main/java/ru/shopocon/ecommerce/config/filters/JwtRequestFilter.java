package ru.shopocon.ecommerce.config.filters;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.shopocon.ecommerce.identity.domain.security.User;
import ru.shopocon.ecommerce.identity.mappers.UserMapper;
import ru.shopocon.ecommerce.identity.model.JwtGetBodyDto;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwt;
import ru.shopocon.ecommerce.identity.providers.JwtTokenProvider;
import ru.shopocon.ecommerce.identity.repositories.UserJpaRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.shopocon.ecommerce.common.util.StringUtils.isBlank;
import static ru.shopocon.ecommerce.identity.model.types.JwtTokenValidationStatus.SUCCESS;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserJpaRepository userRepository;
    private final UserMapper userMapper;

    public JwtRequestFilter(JwtTokenProvider jwtTokenProvider, UserJpaRepository userRepository, UserMapper userMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            boolean isAuthenticated = authenticateByAccessToken(request);
            if (!isAuthenticated) {
                authenticateByRefreshToken(request, response);
            }
        } catch (Exception ex) {
            log.error("Cannot set user authentication", ex);
        }
        filterChain.doFilter(request, response);
    }

    private boolean authenticateByAccessToken(@NonNull HttpServletRequest request) {
        String accessToken = jwtTokenProvider.extractAccessTokenFromCookie(request);
        if (isBlank(accessToken)) {
            return false;
        }
        JwtGetBodyDto bodyDto = jwtTokenProvider.getBody(accessToken);
        if (bodyDto.status() != SUCCESS) {
            return false;
        }
        final UserDetailsJwt userDetailsJwt = bodyDto.userDetailsJwt();
        setAuthentication(request, userDetailsJwt);
        return true;
    }

    private void authenticateByRefreshToken(@NonNull HttpServletRequest request,
                                            @NonNull HttpServletResponse response) {
        String refreshToken = jwtTokenProvider.extractRefreshTokenFromCookie(request);
        if (isBlank(refreshToken)) {
            return;
        }
        JwtGetBodyDto tokenBody = jwtTokenProvider.getBody(refreshToken);
        if (tokenBody.status() != SUCCESS) {
            return;
        }
        final String username = tokenBody.userDetailsJwt().getUsername();
        if (isBlank(username)) {
            return;
        }
        val optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return;
        }
        final User user = optionalUser.get();
        if (!user.isEnabled() ||
            !user.isAccountNonExpired() ||
            !user.isAccountNonLocked() ||
            !user.isCredentialsNonExpired()) {
            return;
        }
        final UserDetailsJwt userDetailsJwt = userMapper.mapToUserDetailsJwt(user);
        boolean rememberMe = jwtTokenProvider.getRememberMeByRefreshCookieMaxAge(request);
        final Cookie accessCookie = jwtTokenProvider.createAccessCookie(userDetailsJwt, rememberMe);
        response.addCookie(accessCookie);
        setAuthentication(request, userDetailsJwt);
    }

    private void setAuthentication(@NonNull HttpServletRequest request, UserDetailsJwt userDetailsJwt) {
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetailsJwt, null, userDetailsJwt.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
