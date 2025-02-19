package com.honghung.chatapp.mapper;

import com.honghung.chatapp.dto.response.chat.ChatMessageResponse;
import com.honghung.chatapp.entity.ChatMessage;

public final class ChatMessageMapper {
    public static final ChatMessageResponse convertToChatMessageResponse(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .messageId(chatMessage.getMessageId())
                .conversationId(chatMessage.getConversationId())
                .sender(UserMapper.convertToUserQuickInfo(chatMessage.getSender()))
                .content(chatMessage.getContent())
                .messageContentType(chatMessage.getMessageContentType())
                .attachment(chatMessage.getAttachment() == null ? null : AttachmentMapper.convertToAttachmentResponse(chatMessage.getAttachment()))
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}
