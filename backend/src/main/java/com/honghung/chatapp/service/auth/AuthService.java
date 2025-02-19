package com.honghung.chatapp.service.auth;

import org.springframework.stereotype.Service;

import com.honghung.chatapp.dto.request.user.RenewTokenRequest;
import com.honghung.chatapp.dto.request.user.UserLoginRequest;
import com.honghung.chatapp.dto.request.user.UserLogoutRequest;
import com.honghung.chatapp.dto.request.user.UserRegisterRequest; 
import com.honghung.chatapp.model.AuthenticatedToken;

@Service
public interface AuthService {
    AuthenticatedToken handleLoginRequest(UserLoginRequest request);
    void handleRegisterRequest(UserRegisterRequest request); 
    void logoutAccount(UserLogoutRequest request);
    AuthenticatedToken activateAccount(String token);
    AuthenticatedToken handleRenewTokens(RenewTokenRequest request);

}