package com.honghung.chatapp.dto.response.user;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private UUID id;
    private String name;
    private String username;
    private String email;
    private String role;
    private Boolean isActivated;
    private Boolean status;
    private String avatarUrl;
    private String avatarId;
    private String coverUrl;
    private String coverId;
    private String bio;
}
