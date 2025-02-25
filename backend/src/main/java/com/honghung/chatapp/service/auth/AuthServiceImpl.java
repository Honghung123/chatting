package com.honghung.chatapp.service.auth;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Service;

import com.honghung.chatapp.component.auth.JwtService;
import com.honghung.chatapp.component.exception.BusinessException;
import com.honghung.chatapp.component.exception.types.AuthException;
import com.honghung.chatapp.component.exception.types.TokenException;
import com.honghung.chatapp.component.exception.types.UserException;
import com.honghung.chatapp.constant.AppProperties;
import com.honghung.chatapp.constant.KafkaTopic;
import com.honghung.chatapp.constant.RedisKey;
import com.honghung.chatapp.dto.request.user.RenewTokenRequest;
import com.honghung.chatapp.dto.request.user.UserLoginRequest;
import com.honghung.chatapp.dto.request.user.UserLogoutRequest;
import com.honghung.chatapp.dto.request.user.UserRegisterRequest;
import com.honghung.chatapp.dto.request.user.VerificationCodeRequest;
import com.honghung.chatapp.entity.User;
import com.honghung.chatapp.mapper.UserMapper;
import com.honghung.chatapp.model.AuthenticatedToken;
import com.honghung.chatapp.model.UserPrincipal;
import com.honghung.chatapp.service.email.EmailService;
import com.honghung.chatapp.service.redis.RedisService;
import com.honghung.chatapp.service.user.UserService;
import com.honghung.chatapp.utils.DateUtils;
import com.honghung.chatapp.utils.JSONUtils;
import com.honghung.chatapp.utils.PasswordUtils;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    // private final EmailService emailService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AppProperties appProperties;
    private final RedisService redisService;

    @Override
    public AuthenticatedToken handleLoginRequest(UserLoginRequest request) {
        // Get user by email
        User user = userService.getUserByEmail(request.email());
        // Verify password
        if (!PasswordUtils.matches(request.password(), user.getPassword())) {
            throw BusinessException.from(UserException.PASSWORD_NOT_MATCH, "Email or password is incorrect");
        }
        // verify active user
        if (!user.getStatus()) {
            throw BusinessException.from(UserException.USER_NOT_ACTIVATED, "This account is not activated");
        }
        UserPrincipal userPrincipal = UserMapper.convUserPrincipalFromUser(user);
        // Generate token
        String accessToken = jwtService.generateAccessToken(userPrincipal);
        String refreshToken = jwtService.generateRefreshToken(userPrincipal);
        AuthenticatedToken token = AuthenticatedToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return token;
    }

    @Override
    public void handleRegisterRequest(UserRegisterRequest request) {
        // Verify email exists
        if (userService.existByEmail(request.email())) {
            throw BusinessException.from(UserException.EMAIL_ALREADY_EXISTS, "Email already exists");
        }
        // Create user
        User user = userService.insertUser(request);
        // Send email with token expires in 1 hour to activate account
        Date expirationDate = DateUtils.addTime(new Date(System.currentTimeMillis()), 1, ChronoUnit.HOURS);
        Map<String, Object> claims = Map.of("tokenType", OAuth2ParameterNames.TOKEN, "action", "activate");
        String activateToken = jwtService.generateToken(user.getEmail(), claims, expirationDate);
        String activateLink = appProperties.getCors().getFrontendActivateUrl() + "?token=" + activateToken;
        
        // Send email
        String content = "Click here to activate your account: " + activateLink;
        Map<String, Object> map = Map.of("receiver", user.getEmail(), "subject", "Activate account", "content", content);
        String message = JSONUtils.convertToJSON(map);
        kafkaTemplate.send(KafkaTopic.SEND_CONFIRM_ACCOUNT_LINK_VIA_EMAIL, message);
        // emailService.sendEmail(user.getEmail(), "Activate account",
        //         "Click here to activate your account: " + activateLink);
    }

    @Override
    public void logoutAccount(UserLogoutRequest request) {
        // Verify token
        if (!jwtService.isTheSpecificToken(request.accessToken(), OAuth2ParameterNames.ACCESS_TOKEN)
                || !jwtService.isTheSpecificToken(request.refreshToken(), OAuth2ParameterNames.REFRESH_TOKEN)) {
            throw BusinessException.from(TokenException.INVALID_TOKEN, "Invalid token type");
        }
        Date atExpDate = jwtService.getExpiration(request.accessToken());
        Date rfExpDate = jwtService.getExpiration(request.refreshToken());
        // Store tokens to blacklist on Redis
        String keySeperator = ":";
        redisService.setValue(RedisKey.BLACKLIST_ACCESS_TOKENS + keySeperator + request.accessToken(), atExpDate);
        redisService.setTimeToLive(RedisKey.BLACKLIST_ACCESS_TOKENS + keySeperator + request.accessToken(), atExpDate);
        redisService.setValue(RedisKey.BLACKLIST_REFRESH_TOKENS + keySeperator + request.refreshToken(), rfExpDate);
        redisService.setTimeToLive(RedisKey.BLACKLIST_REFRESH_TOKENS + keySeperator + request.refreshToken(), rfExpDate);
    }

    @Override
    public AuthenticatedToken activateAccount(String token) {
        // Verify token
        if (!jwtService.isAValidToken(token)) {
            throw BusinessException.from(TokenException.INVALID_TOKEN, "Invalid token");
        }
        if (!jwtService.isTheSpecificToken(token, OAuth2ParameterNames.TOKEN)) {
            throw BusinessException.from(TokenException.INVALID_TOKEN, "Invalid token type");
        }
        // Get user by email
        String userEmail = jwtService.getSubject(token);
        User user = userService.getUserByEmail(userEmail);
        // Activate user 
        userService.updateUserAccountActivation(user.getId(), true);
        UserPrincipal userPrincipal = UserMapper.convUserPrincipalFromUser(user);
        String accessToken = jwtService.generateAccessToken(userPrincipal);
        String refreshToken = jwtService.generateRefreshToken(userPrincipal);
        return AuthenticatedToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticatedToken handleRenewTokens(RenewTokenRequest request) {
        // Verify token
        if (!jwtService.isTheSpecificToken(request.accessToken(), OAuth2ParameterNames.ACCESS_TOKEN)) {
            throw BusinessException.from(AuthException.INVALID_TOKEN, "Invalid Token");
        }
        if (!jwtService.isTheSpecificToken(request.refreshToken(), OAuth2ParameterNames.REFRESH_TOKEN)) {
            throw BusinessException.from(AuthException.INVALID_TOKEN, "Invalid Token");
        }
        // Get user by email
        String userEmail = jwtService.getSubject(request.accessToken());
        User user = userService.getUserByEmail(userEmail);
        UserPrincipal userPrincipal = UserMapper.convUserPrincipalFromUser(user);
        // Generate token
        String newAccessToken = jwtService.generateAccessToken(userPrincipal);
        String newRefreshToken = jwtService.generateRefreshToken(userPrincipal);
        // Store old tokens to blacklist on Redis
        String keySeperator = ":";
        redisService.setValue(RedisKey.BLACKLIST_ACCESS_TOKENS + keySeperator + request.accessToken(),
                jwtService.getExpiration(request.accessToken()));
        redisService.setTimeToLive(RedisKey.BLACKLIST_ACCESS_TOKENS + keySeperator + request.accessToken(),
                jwtService.getExpiration(request.accessToken()));
        redisService.setValue(RedisKey.BLACKLIST_REFRESH_TOKENS + keySeperator + request.refreshToken(),
                jwtService.getExpiration(request.refreshToken()));
        redisService.setTimeToLive(RedisKey.BLACKLIST_REFRESH_TOKENS + keySeperator + request.refreshToken(),
                jwtService.getExpiration(request.refreshToken()));
        return AuthenticatedToken.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public void verifyCode(VerificationCodeRequest verificationCodeRequest) {
        String storedKey = RedisKey.VERIFICATION_OTP + ":" + verificationCodeRequest.email();
        String storedCode = (String)redisService.getValue(storedKey);
        if (storedCode == null) {
            throw BusinessException.from(AuthException.INVALID_VERIFICATION_OTP, "Invalid verification code");
        }
        if (!storedCode.equals(verificationCodeRequest.otpCode())) {
            throw BusinessException.from(AuthException.INVALID_VERIFICATION_OTP, "Invalid verification code");
        } 
        userService.verifyUserEmail(verificationCodeRequest.email(), true);
        redisService.deleteKey(storedKey);
    }
}
