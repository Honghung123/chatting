package com.honghung.chatapp.model.oauth2;

import java.util.Map;

import com.honghung.chatapp.constant.OAuth2AuthenticationProvider;

public final class OAuth2UserInfoFactory {
    public static OAuth2UserInfo createOAuth2UserInfo(String registerId, Map<String, Object> attributes){
        var provider = OAuth2AuthenticationProvider.of(registerId); 
        return switch (provider) {
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
            default -> null;
        };
    }
}   
