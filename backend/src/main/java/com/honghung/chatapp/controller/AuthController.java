package com.honghung.chatapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honghung.chatapp.constant.AppProperties;
import com.honghung.chatapp.dto.request.user.RenewTokenRequest;
import com.honghung.chatapp.dto.request.user.UserLoginRequest;
import com.honghung.chatapp.dto.request.user.UserLogoutRequest;
import com.honghung.chatapp.dto.request.user.UserRegisterRequest;
import com.honghung.chatapp.dto.request.user.VerificationCodeRequest;
import com.honghung.chatapp.dto.response.SuccessResponseEntity;
import com.honghung.chatapp.dto.response.user.UserLoginResponse;
import com.honghung.chatapp.entity.User;
import com.honghung.chatapp.model.AuthenticatedToken;
import com.honghung.chatapp.service.auth.AuthService;
import com.honghung.chatapp.service.user.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid; 
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus; 
import org.springframework.web.bind.annotation.*;
 

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController { 
    private final AuthService authService;
    // private final AppProperties appPropertiesConfig;

    @PostMapping("/register")
    public SuccessResponseEntity<Object> handleRegister(@Valid @RequestBody UserRegisterRequest userDto) {
        authService.handleRegisterRequest(userDto);
        return SuccessResponseEntity.from(HttpStatus.CREATED, "Register successfully. Please check your email to activate account", userDto.email());
    }

    @PostMapping("/login")
    public SuccessResponseEntity<AuthenticatedToken> handleLogin(@Valid @RequestBody UserLoginRequest userDto) {
        AuthenticatedToken loginInfo = authService.handleLoginRequest(userDto);
        return SuccessResponseEntity.from(HttpStatus.OK, "Login successfully", loginInfo);
    }

    // @PostMapping("/refresh-token")
    // public SuccessResponseEntity<AuthenticatedToken> handleGenerateNewAccessToken(@Valid @RequestBody TokenRequest tokenRequest) {
    //     AuthenticatedToken token = authService.handleGeneratingNewTokenRequest(tokenRequest.refreshToken());
    //     return SuccessResponseEntity.from(HttpStatus.OK, "Generate new access token successfully", token);
    // }

    @GetMapping("/activate")
    public SuccessResponseEntity<AuthenticatedToken> handleActivateAccount(@NotBlank @RequestParam String token) {
        AuthenticatedToken tokenInfo = authService.activateAccount(token);
        return SuccessResponseEntity.from(HttpStatus.OK, "Activate account successfully", tokenInfo);
    }
   
    @PostMapping("/logout")
    public SuccessResponseEntity<Void> handleLogout(@Valid @RequestBody UserLogoutRequest userLogoutRequest) {
        authService.logoutAccount(userLogoutRequest);
        return SuccessResponseEntity.from(HttpStatus.NO_CONTENT, "Logout account successfully");
    }
   
    @PostMapping("/renew-token")
    public SuccessResponseEntity<Void> handleRenewToken(@Valid @RequestBody RenewTokenRequest renewTokenRequest) {
        authService.handleRenewTokens(renewTokenRequest);
        return SuccessResponseEntity.from(HttpStatus.NO_CONTENT, "Renew token successfully");
    }

    // @GetMapping("/check-email")
    // public ResponseEntity handleCheckEmail(
    //         @NotBlank(message = "email must be not blank")
    //         @Email(message = "invalid email format") @RequestParam String email)
    // {
    //     if(userService.existByEmail(email)) {
    //         return SuccessResponseEntity.from(HttpStatus.OK, "Email existed", email);
    //     }
    //     ErrorResponseEntity.Payload payload = new ErrorResponseEntity.Payload();
    //     payload.setStatus(404);
    //     payload.setPath("/auth/check-email");
    //     payload.setMessage("Email is not existed");
    //     payload.setErrorCode("email_not_exists");
    //     payload.setTimestamp(new Date());
    //     return new ErrorResponseEntity(payload);
    // }

    // @PostMapping("/forgot-password")
    // public SuccessResponseEntity<String> handleForgotPassword(@Valid @RequestBody UserResetPasswordRequest userResetPasswordRequest) {
    //     userService.handleForgotPassword(userResetPasswordRequest);
    //     return SuccessResponseEntity.from(HttpStatus.OK, "Verification Code was send to your email", userResetPasswordRequest.getEmail());
    // }

    @PostMapping("/verify-otp")
    public SuccessResponseEntity<String> handleVerifyCode(@Valid @RequestBody VerificationCodeRequest verificationCodeRequest) {
        authService.verifyCode(verificationCodeRequest);
        return SuccessResponseEntity.from(HttpStatus.NO_CONTENT, "Verify email successfully");
    }
}
