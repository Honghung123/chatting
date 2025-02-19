package com.honghung.chatapp.component.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.honghung.chatapp.component.exception.BusinessException;
import com.honghung.chatapp.component.exception.types.AuthException;
import com.honghung.chatapp.constant.RedisKey;
import com.honghung.chatapp.constant.ShareData;
import com.honghung.chatapp.model.UserPrincipal;
import com.honghung.chatapp.service.redis.RedisService;
import com.honghung.chatapp.service.user.CustomUserDetailsService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("Incoming request: " + request.getRequestURI() + " - " + request.getMethod());
        // Get token from header
        final String authorizationHeader = request.getHeader("Authorization");
        if (!ShareData.BYPASS_URLS.contains(request.getRequestURI()) && StringUtils.hasText(authorizationHeader)
                && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring(7);
            if(redisService.existsByKey(RedisKey.BLACKLIST_ACCESS_TOKENS + ":" + accessToken)) {
                throw BusinessException.from(AuthException.UNAUTHORIZED, "Access token is invalid");
            }
            if (jwtService.isTheSpecificToken(accessToken, OAuth2ParameterNames.ACCESS_TOKEN)) {
                try {
                    String userEmail = jwtService.getSubject(accessToken);
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        log.info("Current user session: " + userEmail);
                        UserPrincipal user = this.userDetailsService.loadUserByEmail(userEmail);
                        var authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        context.setAuthentication(authToken);
                        SecurityContextHolder.setContext(context);
                        log.info("Saved authentication to the security context");
                    } else {
                        log.error("Failed to set user authentication in the security context!");
                    }
                } catch (Exception e) {
                    log.error("Failed to set user authentication in the security context!");
                }
            } else {
                log.error("Invalid access token: " + accessToken);
            }
        } else {
            log.info("Not found access token in the header of the request");
        }
        filterChain.doFilter(request, response);
    }
}