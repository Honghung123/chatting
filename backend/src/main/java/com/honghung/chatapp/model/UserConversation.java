package com.honghung.chatapp.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.honghung.chatapp.constant.MessageContentType;
import com.honghung.chatapp.entity.Conversation;
import com.honghung.chatapp.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserConversation {
    private Conversation conversation;
    private User user; 
}
