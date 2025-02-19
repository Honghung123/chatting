package com.honghung.chatapp.config;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.honghung.chatapp.model.ChatMessageRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListenerConfig {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        if (userId != null) {
            log.info("user disconnected: {}", userId);
            // var chatMessage = ChatMessageRequest.builder()
            //         .type(ChatMessageRequest.MessageType.LEAVE)
            //         .sender(UUID.fromString(userId))
            //         .build();
            // messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }

}