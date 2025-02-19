package com.honghung.chatapp.dto.request.chat;

import java.util.UUID;

public record ConversationRequest(UUID userId, UUID otherUserId, String conversationType) {
    
}
