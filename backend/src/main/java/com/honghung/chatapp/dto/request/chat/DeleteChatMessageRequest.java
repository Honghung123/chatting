package com.honghung.chatapp.dto.request.chat;

import java.util.UUID;

public record DeleteChatMessageRequest(UUID conversationId, Long messageId, UUID senderId, String fileId) {
    
}
