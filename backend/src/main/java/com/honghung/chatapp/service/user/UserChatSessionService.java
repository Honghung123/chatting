package com.honghung.chatapp.service.user;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.honghung.chatapp.model.UserQuickInfo;

@Service
public class UserChatSessionService {
    private final Map<UUID, UserQuickInfo> userSessions = new ConcurrentHashMap<>();

    public void addUserSession(UUID userId, UserQuickInfo userQuickInfo) {
        userSessions.put(userId, userQuickInfo);
    }

    public Optional<UserQuickInfo> getUserSession(UUID userId) {
        return Optional.ofNullable(userSessions.get(userId));
    }
}
