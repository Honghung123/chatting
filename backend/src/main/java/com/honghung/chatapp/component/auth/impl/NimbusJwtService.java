package com.honghung.chatapp.component.auth.impl;

import com.honghung.chatapp.component.auth.JwtService;
import com.honghung.chatapp.component.exception.BusinessException;
import com.honghung.chatapp.component.exception.types.TokenException;
import com.honghung.chatapp.constant.AppProperties;
import com.honghung.chatapp.model.UserPrincipal;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class NimbusJwtService implements JwtService {
    private final AppProperties authConfigProperties;

    private String generateToken(UserPrincipal user, Date expiryDate, String tokenType) {
        var jwsClaims = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .jwtID(user.getId().toString())
                .expirationTime(expiryDate)
                .issueTime(new Date())
                .issuer("honghung.com")
                // .claim("scope", buildUserRoles(user.getAuthorities()))
                .claim("tokenType", tokenType)
                .build();
        try {
            return this.buildToken(jwsClaims);
        } catch (JOSEException e) {
            throw BusinessException.from(TokenException.ERROR_GENERATING_TOKEN,
                    "An error occurred while generating token");
        }
    }

    private String buildToken(JWTClaimsSet jwsClaims) throws JOSEException {
        var jwsHeader = new JWSHeader(JWSAlgorithm.parse(authConfigProperties.getJwt().getMacAlgorithm()));
        var payload = new Payload(jwsClaims.toJSONObject());
        var jwsObject = new JWSObject(jwsHeader, payload);
        jwsObject.sign(new MACSigner(authConfigProperties.getJwt().getSecret().getBytes()));
        return jwsObject.serialize();
    }

    public JWTClaimsSet getJWTClaimsSet(String token) throws ParseException, JOSEException {
        var jwsVerifier = new MACVerifier(authConfigProperties.getJwt().getSecret().getBytes());
        var signedJwt = SignedJWT.parse(token);
        boolean verified = signedJwt.verify(jwsVerifier);
        if (!verified) {
            throw BusinessException.from(TokenException.INVALID_TOKEN, "Invalid token");
        }
        return signedJwt.getJWTClaimsSet();
    }

    @Override
    public String getSubject(String token) {
        try {
            return this.getJWTClaimsSet(token).getSubject();
        } catch (ParseException e) {
            throw BusinessException.from(TokenException.INVALID_TOKEN, "Invalid token");
        } catch (JOSEException e) {
            throw BusinessException.from(TokenException.INVALID_TOKEN, "Invalid token");
        }
    }

    @Override
    public Date getExpiration(String token) {
        try {
            return this.getJWTClaimsSet(token).getExpirationTime();
        } catch (ParseException e) {
            throw BusinessException.from(TokenException.INVALID_TOKEN, "Invalid token");
        } catch (JOSEException e) {
            throw BusinessException.from(TokenException.INVALID_TOKEN, "Invalid token");
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        return this.getExpiration(token).before(new Date());
    }

    @Override
    public String generateToken(String subject, Map<String, Object> claims, Date expiryDate) {
        var jwsClaimsBuilder = new JWTClaimsSet.Builder()
                .subject(subject)
                .jwtID(subject)
                .expirationTime(expiryDate)
                .issueTime(new Date())
                .issuer("honghung.com");
        // Add all claims from the map
        claims.forEach(jwsClaimsBuilder::claim);
        var jwsClaims = jwsClaimsBuilder.build();
        try {
            return this.buildToken(jwsClaims);
        } catch (JOSEException e) {
            throw BusinessException.from(TokenException.ERROR_GENERATING_TOKEN,
                    "An error occurred while generating token");
        }
    }

    @Override
    public String generateRefreshToken(UserPrincipal user) {
        var expiryDate = authConfigProperties.getJwt().getRefreshTokenExpirationDate();
        return this.generateToken(user, expiryDate, OAuth2ParameterNames.REFRESH_TOKEN);
    }

    @Override
    public String generateAccessToken(UserPrincipal user) {
        var accessTokenExpiryDate = authConfigProperties.getJwt().getAccessTokenExpirationDate();
        return this.generateToken(user, accessTokenExpiryDate, OAuth2ParameterNames.ACCESS_TOKEN);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getClaimsProperty(String property, String token) {
        try {
            return (T) this.getJWTClaimsSet(token).getClaim(property);
        } catch (Exception e) {
            throw BusinessException.from(TokenException.INVALID_TOKEN, "Failed to cast to the specific type");
        }
    }

    @Override
    public boolean validateToken(String token, UserPrincipal user) {
        try {
            if (this.getSubject(token).equals(user.getEmail())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Failed to verify token. Error message: {}", e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public boolean isAValidToken(String token) {
        return this.getSubject(token) != null && !this.isTokenExpired(token);
    }

    @Override
    public boolean isTheSpecificToken(String token, String tokenType) {
        try {
            return this.getClaimsProperty("tokenType", token).equals(tokenType);
        } catch (Exception e) {
            log.error("Failed to check the type of the token. Error message: {}: {}", e.getLocalizedMessage(), e, token);
            return false;
        }
    }
}
