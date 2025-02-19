package com.honghung.chatapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honghung.chatapp.dto.request.chat.ConversationRequest;
import com.honghung.chatapp.dto.response.SuccessResponseEntity;
import com.honghung.chatapp.dto.response.chat.ChatMessageResponse;
import com.honghung.chatapp.service.chat.ChatService;
import com.nimbusds.oauth2.sdk.SuccessResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/new-conversation")
    public SuccessResponseEntity<String> createNewConversation(@RequestBody ConversationRequest entity) {
        UUID conversationId = chatService.createNewConversation(entity);
        return SuccessResponseEntity.from(HttpStatus.OK, "Get conversation id successfully", conversationId.toString());
    }
    

    @GetMapping("/messages/{conversationId}")
    public SuccessResponseEntity<List<ChatMessageResponse>> getConversationMessages(@PathVariable UUID conversationId) {
        List<ChatMessageResponse> messages = chatService.getConversationMessages(conversationId);
        return SuccessResponseEntity.from(HttpStatus.OK, "Get conversation messages successfully", messages);
    } 
}
