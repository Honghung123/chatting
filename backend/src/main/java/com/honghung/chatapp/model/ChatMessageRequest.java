package com.honghung.chatapp.model;

import java.util.UUID;

import com.honghung.chatapp.constant.MessageContentType;
import com.honghung.chatapp.dto.request.chat.AttachmentRequest;

import lombok.*; 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageRequest {
    private UUID conversationId;
    private String content;
    private UUID sender;
    private UUID recipient; 
    private MessageContentType messageContentType; 
    private AttachmentRequest attachment;
}
