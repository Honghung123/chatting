package com.honghung.chatapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import com.honghung.chatapp.component.auth.JwtFilter;
import com.honghung.chatapp.component.auth.impl.CustomAuthenticationEntrypoint;
import com.honghung.chatapp.component.auth.oauth2.OAuth2AuthenticationFailureHandler;
import com.honghung.chatapp.component.auth.oauth2.OAuth2AuthenticationSuccessHandler;
import com.honghung.chatapp.constant.ShareData;
import com.honghung.chatapp.service.user.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity // Allow config security for each request
@EnableMethodSecurity // Allow authorize basa-role for each method
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtFilter jwtFilter;
    private final DaoAuthenticationProvider daoAauthenticationProvider;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final CustomAuthenticationEntrypoint customAuthenticationEntrypoint;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(daoAauthenticationProvider)
                .authorizeHttpRequests(
                        request -> {
                            request.requestMatchers(ShareData.BYPASS_URLS.toArray(String[]::new))
                                    .permitAll()
                                    .anyRequest()
                                    .authenticated();
                        })
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntrypoint))
                .sessionManagement(this.statelessSessionCustomizer())
                .oauth2Login(this.oauth2LoginCustomizer())
                .formLogin(login -> login.disable()) // Disable default form login
                .requestCache(cache -> cache.requestCache(this.requestCache())) // Custom request cache
                .build();
    }

    private Customizer<SessionManagementConfigurer<HttpSecurity>> statelessSessionCustomizer() {
        return session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private Customizer<OAuth2LoginConfigurer<HttpSecurity>> oauth2LoginCustomizer() {
        return oauth2 -> {
            oauth2.loginPage("/auth/login")
                    .userInfoEndpoint(endpoint -> endpoint.userService(customOAuth2UserService))
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler);
        };
    }

    private HttpSessionRequestCache requestCache() {
        var requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);
        return requestCache;
    }
}