package com.honghung.chatapp.dto.request.user;

public record RenewTokenRequest(
        String refreshToken,
        String accessToken
) {
    
}
