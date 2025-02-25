package com.honghung.chatapp.service.account;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.honghung.chatapp.constant.KafkaTopic;
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
import com.honghung.chatapp.utils.JSONUtils;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RedisService redisService;

    @Override
    public List<ConversationResponse> getUserConversations(UUID userId) {
        List<UserConversation> conversations = conversationRepository.getUserConversations(userId);
        List<UserConversation> otherContacts = conversationRepository.getUserNotInConversations(userId);
        conversations.addAll(otherContacts);
        List<ConversationResponse> conversationResponses = conversations.stream()
                .map(conversation -> ConversationMapper.convertToConversationResponse(conversation, redisService.getValue(RedisKey.USER_ONLINE_STATUS + ":" + conversation.getUser().getId().toString()) != null)).toList();
        Map<String, Object> map = Map.of("userId", userId.toString());
        String message = JSONUtils.convertToJSON(map);
        kafkaTemplate.send(KafkaTopic.UPDATE_USER_ONLINE_STATUS_ON_CACHE, message);
        return conversationResponses;
    }

    @Override
    public PaginationData<UserInfoResponse> getSuggestFriends(UUID id, int page, int size, String sortBy) {
        System.out.println(id);
        Sort sortByCreatedAt = sortBy.equals("newest") ? Sort.by("createdAt").descending() : Sort.unsorted();
        Pageable pageable = PageRequest.of(page, size, sortByCreatedAt);
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
