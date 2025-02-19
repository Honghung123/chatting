package com.honghung.chatapp.mapper;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.honghung.chatapp.dto.response.user.UserInfoResponse; 
import com.honghung.chatapp.entity.User; 
import com.honghung.chatapp.model.UserPrincipal;
import com.honghung.chatapp.model.UserQuickInfo;

public final class UserMapper {
    public static UserPrincipal convUserPrincipalFromUser(User user) {
        List<SimpleGrantedAuthority> userAuthorities = List.of(new SimpleGrantedAuthority(user.getRole()));
        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(userAuthorities)
                .build();
    }

    public static UserInfoResponse convertToUserInfoResponse(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .isActivated(user.getIsActivated())
                .status(user.getStatus())
                .avatarUrl(user.getAvatarUrl())
                .avatarId(user.getAvatarId())
                .coverUrl(user.getCoverUrl())
                .coverId(user.getCoverId())
                .build();
    }

    public static UserQuickInfo convertToUserQuickInfo(User user) {
        return UserQuickInfo.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .build();
    } 
}
