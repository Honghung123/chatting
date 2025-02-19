package com.honghung.chatapp.service.account;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.honghung.chatapp.entity.User;

@Service
public interface FriendService {
    public List<User> getUserConversations(UUID userId);
}
