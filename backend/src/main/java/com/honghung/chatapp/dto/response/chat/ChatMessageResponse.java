package com.honghung.chatapp.dto.response.chat;

import java.time.LocalDateTime;
import java.util.UUID;

import com.honghung.chatapp.constant.MessageContentType;
import com.honghung.chatapp.model.UserQuickInfo;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponse {
    private Long messageId;
    private UUID conversationId;
    private UserQuickInfo sender;
    // private UserQuickInfo recipient;
    private String content;
    private MessageContentType messageContentType;
    private AttachmentResponse attachment;
    private LocalDateTime createdAt;
}
