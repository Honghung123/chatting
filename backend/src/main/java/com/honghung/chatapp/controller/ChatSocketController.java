package com.honghung.chatapp.controller;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.*; 
import org.springframework.stereotype.Controller;

import com.honghung.chatapp.dto.request.chat.DeleteChatMessageRequest;
import com.honghung.chatapp.model.ChatMessageRequest;
import com.honghung.chatapp.service.chat.ChatService;
import com.honghung.chatapp.service.chat.ChatTypeService;
import com.honghung.chatapp.utils.JSONUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatSocketController { 
    private final ChatService chatService;
    private final RabbitTemplate rabbitTemplate;
    private final ChatTypeService chatTypeService;

    @MessageMapping("/online/{userId}")
    public void handleUserOnlineStatus(@DestinationVariable UUID userId) {
        System.out.println("===================> User with id: " + userId + " is online.");
        chatService.handleUpdateUserOnlineStatus(userId);
    }  

    /**
     * Sends a chat message to all connected users.
     * 
     * param chatMessage The chat message to be sent.
     * return The sent chat message.
     */
    @MessageMapping("/send")
    @SendTo("/topic/public")
    public ChatMessageRequest sendMessage(@Payload ChatMessageRequest chatMessage) {
        return chatMessage;
    }

    // 🔵 Self Chat
    @MessageMapping("/self")
    public void sendSelfMessage(@Payload ChatMessageRequest chatMessage) {
        chatTypeService.handleSelfChatMessage(chatMessage);
    }

    // 🟠 Direct Chat (chat 1-1)
    @MessageMapping("/direct")
    public void sendDirectMessage(@Payload ChatMessageRequest chatMessage) {
        System.out.println(JSONUtils.convertToJSON(chatMessage));
        chatTypeService.handleDirectChatMessage(chatMessage);
    }
   
    @MessageMapping("/message/delete")
    public void deleteMessage(@Payload DeleteChatMessageRequest chatMessage) {
        System.out.println(JSONUtils.convertToJSON(chatMessage));
        chatTypeService.handleDeleteChatMessage(chatMessage);
    }

    // 🔴 Group Chat
    @MessageMapping("/group/{chatId}")
    // @SendTo("/topic/group.{chatId}")
    public void sendGroupMessage(@Payload ChatMessageRequest chatMessage, @DestinationVariable UUID chatId) {
        chatTypeService.handleGroupChatMessage(chatId, chatMessage);
    }
}       
