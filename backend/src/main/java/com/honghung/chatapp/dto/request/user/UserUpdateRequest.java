package com.honghung.chatapp.dto.request.user;

import java.util.UUID;

public record UserUpdateRequest(    UUID id,
    String name,
    String email,
    String avatarUrl,
    String coverUrl) {

}
