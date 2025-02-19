package com.honghung.chatapp.mapper;

import com.honghung.chatapp.dto.response.chat.ConversationResponse;
import com.honghung.chatapp.entity.Conversation;
import com.honghung.chatapp.model.UserConversation;

public final class ConversationMapper {
    public static final ConversationResponse convertToConversationResponse(Conversation conversation) {
        if(conversation.getLatestMessage() == null) return ConversationResponse.builder()
        .conversationId(conversation.getId()) 
        .createdAt(conversation.getCreatedAt()) 
        .conversationType(conversation.getConversationType())
        .build();
        return ConversationResponse.builder()
            .conversationId(conversation.getId())
            .sender(UserMapper.convertToUserQuickInfo(conversation.getLatestMessage().getSender()))
            .latestMessage(conversation.getLatestMessage().getContent())
            .messageContentType(conversation.getLatestMessage().getMessageContentType())
            .conversationType(conversation.getConversationType())
            .createdAt(conversation.getCreatedAt())
            .build();
    }
    public static final ConversationResponse convertToConversationResponse(UserConversation userConversation, boolean isOnline) {
        Conversation conversation = userConversation.getConversation();
        if(conversation == null) return ConversationResponse.builder()
            .user(UserMapper.convertToUserQuickInfo(userConversation.getUser()))
            .build();
        if(conversation.getLatestMessage() == null) return ConversationResponse.builder()
        .conversationId(conversation.getId()) 
        .createdAt(conversation.getCreatedAt()) 
        .conversationType(conversation.getConversationType())
        .user(UserMapper.convertToUserQuickInfo(userConversation.getUser()))
        .build();
        return ConversationResponse.builder()
            .conversationId(conversation.getId())
            .sender(UserMapper.convertToUserQuickInfo(conversation.getLatestMessage().getSender()))
            .user(UserMapper.convertToUserQuickInfo(userConversation.getUser()))
            .latestMessage(conversation.getLatestMessage().getContent())
            .messageContentType(conversation.getLatestMessage().getMessageContentType())
            .conversationType(conversation.getConversationType())
            .isOnline(isOnline)
            .createdAt(conversation.getCreatedAt())
            .build();
    }
}
