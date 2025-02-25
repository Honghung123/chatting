package com.honghung.chatapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.honghung.chatapp.constant.RedisKey;
import com.honghung.chatapp.dto.response.PaginationData;
import com.honghung.chatapp.dto.response.SuccessResponseEntity;
import com.honghung.chatapp.dto.response.chat.ConversationResponse;
import com.honghung.chatapp.dto.response.user.UserInfoResponse;
import com.honghung.chatapp.model.UserPrincipal;
import com.honghung.chatapp.service.account.ConversationService;
import com.honghung.chatapp.service.redis.RedisService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final ConversationService conversationService; 

    @GetMapping("/conversations")
    public SuccessResponseEntity<List<ConversationResponse>> getUserConversations() {
        var user = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var friendList = conversationService.getUserConversations(user.getId());
        return SuccessResponseEntity.from(HttpStatus.OK, "Get user conversations successfully", friendList);
    }
   
    @GetMapping("/suggest-friends")
    public SuccessResponseEntity<PaginationData<UserInfoResponse>> getSuggestFriends(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false, defaultValue = "newest") String sortBy
    ) {
        var user = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var friendList = conversationService.getSuggestFriends(user.getId(), page - 1, size, sortBy);
        return SuccessResponseEntity.from(HttpStatus.OK, "Get suggest friends successfully", friendList);
    }
}
