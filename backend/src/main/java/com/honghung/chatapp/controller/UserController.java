package com.honghung.chatapp.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honghung.chatapp.constant.RedisKey;
import com.honghung.chatapp.dto.request.user.UserUpdateRequest;
import com.honghung.chatapp.dto.response.SuccessResponseEntity;
import com.honghung.chatapp.dto.response.user.UserInfoResponse;
import com.honghung.chatapp.entity.User;
import com.honghung.chatapp.mapper.UserMapper;
import com.honghung.chatapp.model.UserPrincipal;
import com.honghung.chatapp.service.redis.RedisService;
import com.honghung.chatapp.service.user.UserService; 

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final RedisService redisService; 
    
    @GetMapping("/info")
    public SuccessResponseEntity<UserInfoResponse> getUserInfo() {
        var userPricipal = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userPricipal.getEmail());
        if(user != null) {
            redisService.setTimeToLive(RedisKey.USER_ONLINE_STATUS + ":" + user.getId().toString(), 5, TimeUnit.MINUTES);
        }
        return SuccessResponseEntity.from(HttpStatus.OK, "Get user info successfully", UserMapper.convertToUserInfoResponse(user)); 
    }

    @PutMapping("update/{id}")
    @Caching(
        evict = {
            @CacheEvict(value = "user-info", allEntries = true)
        },
        put = {
            @CachePut(cacheNames = "user-info", key = "#id")
        }
    
    )
    public SuccessResponseEntity<UserInfoResponse> updateUserInfo(@PathVariable UUID id, @RequestBody UserUpdateRequest request) {
        User updatedUser = null;
        return SuccessResponseEntity.from(HttpStatus.OK, "Update user info successfully", UserMapper.convertToUserInfoResponse(updatedUser));
    }

    @DeleteMapping("/delete/{id}")
    @CacheEvict(cacheNames = "user-info", key = "#id", beforeInvocation = true)
    public SuccessResponseEntity<Void> deleteUserInfo(@PathVariable UUID id) {
        // userService.deleteUser(id);
        return SuccessResponseEntity.from(HttpStatus.NO_CONTENT, "Delete user info successfully");  
    }
}
