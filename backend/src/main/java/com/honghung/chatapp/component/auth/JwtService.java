package com.honghung.chatapp.component.auth;

import java.util.Date;
import java.util.Map;

import com.honghung.chatapp.model.UserPrincipal;

public interface JwtService {
    String getSubject(String token);

    Date getExpiration(String token);

    boolean isTokenExpired(String token);

    String generateToken(String subject, Map<String, Object> claims, Date expiryDate);

    String generateRefreshToken(UserPrincipal user);

    String generateAccessToken(UserPrincipal user);

    <T> T getClaimsProperty(String property, String token);

    boolean validateToken(String token, UserPrincipal user);

    boolean isAValidToken(String token);

    boolean isTheSpecificToken(String token, String type);
}