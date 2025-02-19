package com.honghung.chatapp.service.chat;

import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.honghung.chatapp.constant.ConversationType;
import com.honghung.chatapp.dto.request.chat.DeleteChatMessageRequest;
import com.honghung.chatapp.dto.response.chat.ChatMessageResponse; 
import com.honghung.chatapp.model.ChatMessageRequest;
import com.honghung.chatapp.model.UserPrincipal;
import com.honghung.chatapp.utils.JSONUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Primary
public class ChatTypeServiceImpl implements ChatTypeService {
    private final SimpMessagingTemplate messagingTemplate; 
    private final ChatService chatService;

    // private void setDestinationPrefix(String prefix) {
    //     messagingTemplate.setUserDestinationPrefix(prefix);
    // }

    @Override
    public void handleSelfChatMessage(ChatMessageRequest chatMessageRequest) {
        ChatMessageResponse userChatMessage = chatService.addMessage(chatMessageRequest, ConversationType.SELF);
        messagingTemplate.convertAndSend("/topic/self/" + chatMessageRequest.getSender().toString(), userChatMessage);
    }

    @Override
    public void handleDirectChatMessage(ChatMessageRequest chatMessage) {
        ChatMessageResponse userChatMessage = chatService.addMessage(chatMessage, ConversationType.DIRECT);
        messagingTemplate.convertAndSend(
                "/topic/direct/" + userChatMessage.getConversationId(),
                userChatMessage);
    }

    @Override
    public void handleGroupChatMessage(UUID chatId, ChatMessageRequest chatMessage) {
        System.out.println("Sending group message from: " + chatMessage.getSender() + " to group: " + chatId);
        // return chatMessage;
        messagingTemplate.convertAndSend("/topic/group/" + chatId.toString(), chatMessage);
    }

    @Override
    public void handleDeleteChatMessage(DeleteChatMessageRequest chatMessage) { 
        long deletedMessageId = chatService.deleteMessage(chatMessage.conversationId(), chatMessage.messageId(), chatMessage.fileId());
        messagingTemplate.convertAndSend("/topic/deleted-message/" + chatMessage.conversationId(), deletedMessageId);
    }
}
