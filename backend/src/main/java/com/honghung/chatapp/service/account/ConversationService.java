package com.honghung.chatapp.service.account;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.honghung.chatapp.dto.response.PaginationData;
import com.honghung.chatapp.dto.response.chat.ConversationResponse;
import com.honghung.chatapp.dto.response.user.UserInfoResponse;

@Service
public interface ConversationService {
    public List<ConversationResponse> getUserConversations(UUID userId);
    public PaginationData<UserInfoResponse> getSuggestFriends(UUID id, int page, int size);
}
