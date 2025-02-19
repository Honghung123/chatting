package com.honghung.chatapp.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor; 

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQuickInfo {
    private UUID id;
    private String name;
    private String username;
    private String email;
    private String avatarUrl;
}
