package com.honghung.chatapp.dto.request.user;

public record UserLogoutRequest(
    String accessToken,
    String refreshToken
) {
    
}
