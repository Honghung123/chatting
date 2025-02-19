package com.honghung.chatapp.service.account;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.honghung.chatapp.constant.RedisKey;
import com.honghung.chatapp.dto.response.PaginationData;
import com.honghung.chatapp.dto.response.chat.ConversationResponse;
import com.honghung.chatapp.dto.response.user.UserInfoResponse;
import com.honghung.chatapp.entity.User;
import com.honghung.chatapp.mapper.ConversationMapper;
import com.honghung.chatapp.mapper.UserMapper;
import com.honghung.chatapp.model.UserConversation;
import com.honghung.chatapp.entity.Conversation;
import com.honghung.chatapp.repository.ConversationRepository;
import com.honghung.chatapp.repository.UserRepository;
import com.honghung.chatapp.service.redis.RedisService;
import com.honghung.chatapp.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;

    @Override
    public List<ConversationResponse> getUserConversations(UUID userId) {
        List<UserConversation> conversations = conversationRepository.getUserConversations(userId);
        List<UserConversation> otherContacts = conversationRepository.getUserNotInConversations(userId);
        conversations.addAll(otherContacts);
        List<ConversationResponse> conversationResponses = conversations.stream()
                .map(conversation -> ConversationMapper.convertToConversationResponse(conversation, redisService.getValue(RedisKey.USER_ONLINE_STATUS + ":" + conversation.getUser().getId().toString()) != null)).toList();
        return conversationResponses;
    }

    @Override
    public PaginationData<UserInfoResponse> getSuggestFriends(UUID id, int page, int size) {
        System.out.println(id);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> friends = userRepository.getSuggestUserList(id, pageable);
        List<UserInfoResponse> suggestUserInfo = friends.getContent().stream().map(UserMapper::convertToUserInfoResponse).toList();    
        return PaginationData.<UserInfoResponse>builder()
            .totalElements(friends.getTotalElements())
            .totalPages(friends.getTotalPages())
            .page(page)
            .pageSize(size)
            .data(suggestUserInfo)
            .build();
    }
}
