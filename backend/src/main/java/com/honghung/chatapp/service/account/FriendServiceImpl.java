package com.honghung.chatapp.service.account;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.honghung.chatapp.entity.User;
import com.honghung.chatapp.repository.FriendRepository;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    @Override
    public List<User> getUserConversations(UUID userId) {
        return friendRepository.getUserFriendListByUserId(userId);
    }
}
