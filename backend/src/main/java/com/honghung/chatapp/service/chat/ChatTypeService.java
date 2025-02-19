package com.honghung.chatapp.service.chat;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.honghung.chatapp.dto.request.chat.DeleteChatMessageRequest;
import com.honghung.chatapp.model.ChatMessageRequest;

@Service
public interface ChatTypeService {
    void handleSelfChatMessage(ChatMessageRequest chatMessage);
    void handleDirectChatMessage(ChatMessageRequest chatMessage);
    void handleGroupChatMessage(UUID chatId, ChatMessageRequest chatMessage);
    void handleDeleteChatMessage(DeleteChatMessageRequest chatMessage);
}
