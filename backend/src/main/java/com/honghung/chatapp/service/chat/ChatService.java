package com.honghung.chatapp.service.chat;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.honghung.chatapp.constant.ConversationType;
import com.honghung.chatapp.dto.request.chat.ConversationRequest; 
import com.honghung.chatapp.dto.response.chat.ChatMessageResponse;
import com.honghung.chatapp.entity.Attachment; 
import com.honghung.chatapp.model.ChatMessageRequest;

@Service
public interface ChatService {
    void getAllMessages();
    ChatMessageResponse addMessage(ChatMessageRequest chatMessage, ConversationType conversationType);
    ChatMessageResponse addMessage(ChatMessageRequest chatMessage, ConversationType conversationType, Attachment attachment);
    Long deleteMessage(UUID conversationId, Long messageId, String fileId);
    void handleUpdateUserOnlineStatus(UUID userId);
    // UUID getConversationId(UUID userId, UUID otherUserId);
    UUID createNewConversation(ConversationRequest request);
    List<ChatMessageResponse> getConversationMessages(UUID conversationId);
}
