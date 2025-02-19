package com.honghung.chatapp.component.auth.oauth2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.honghung.chatapp.component.auth.JwtService;
import com.honghung.chatapp.constant.AppProperties;
import com.honghung.chatapp.model.UserPrincipal;

import java.io.IOException;

// handle when login with Google successfully
// return token for client

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final AppProperties appPropertiesConfig;

    @Override
    @SneakyThrows
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        this.clearAuthenticationAttributes(request);
        final UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userPrincipal);
        String refreshToken = jwtService.generateRefreshToken(userPrincipal);
        System.out.println("Login google access token: " + accessToken);
        System.out.println("Login google refresh token: " + refreshToken);

        this.getRedirectStrategy().sendRedirect(request, response, appPropertiesConfig.getCors().getFrontendUrl() + "/login-success?accessToken=" + accessToken
                + "&refreshToken=" + refreshToken
                + "&id=" + userPrincipal.getId()
                + "&email=" + userPrincipal.getEmail());
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                        Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, chain, authentication);
    }

}