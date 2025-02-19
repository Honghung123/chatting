package com.honghung.chatapp.dto.response.chat;

import java.time.LocalDateTime;
import java.util.UUID;

import com.honghung.chatapp.constant.ConversationType;
import com.honghung.chatapp.constant.MessageContentType;
import com.honghung.chatapp.model.UserQuickInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationResponse {
    private UUID conversationId;
    private ConversationType conversationType;
    private UserQuickInfo user;
    private UserQuickInfo sender;
    private String latestMessage;
    private MessageContentType messageContentType;
    private boolean isOnline;
    private LocalDateTime createdAt;
}
