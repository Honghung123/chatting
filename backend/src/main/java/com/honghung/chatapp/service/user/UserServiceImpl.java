package com.honghung.chatapp.service.user;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.honghung.chatapp.component.exception.BusinessException;
import com.honghung.chatapp.component.exception.types.UserException;
import com.honghung.chatapp.constant.KafkaTopic;
import com.honghung.chatapp.constant.RedisKey;
import com.honghung.chatapp.dto.request.user.UserRegisterRequest;
import com.honghung.chatapp.dto.request.user.UserUpdateRequest;
import com.honghung.chatapp.entity.User;
import com.honghung.chatapp.repository.UserRepository;
import com.honghung.chatapp.service.redis.RedisService;
import com.honghung.chatapp.utils.EmailUtils;
import com.honghung.chatapp.utils.JSONUtils;
import com.honghung.chatapp.utils.PasswordUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Primary
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RedisService redisService; 

    @Override
    public List<User> getUserList() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserList'");
    }

    @Override
    @Cacheable(value = "user-information", key = "#id")
    public User getUserById(UUID id) {
        // Object userOnRedis = redisService.getHashWithKeyAndHashKey(RedisKey.USER_INFO, id.toString());
        // if(userOnRedis != null) {
        //     return JSONUtils.convertToObject(String.valueOf(userOnRedis), User.class);
        // }
        User user = userRepository.findById(id).orElseThrow(
            () -> BusinessException.from(UserException.USER_NOT_FOUND, "User not found")
        );
        String userJSON = JSONUtils.convertToJSON(user);
        redisService.appendHash(RedisKey.USER_INFO, id.toString(), userJSON);
        return user;
    }

    @Override
    public User getUserByIdNoCached(UUID id) {
        return userRepository.findById(id).orElseThrow(
            () -> BusinessException.from(UserException.USER_NOT_FOUND, "User not found")
        ); 
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
            () -> BusinessException.from(UserException.USER_NOT_FOUND, "User not found"
        ));
    }

    @Override
    @Cacheable(value = "user-info-data", key = "#email")
    public User getUserByEmail(String email) {
        System.out.println("------------> Call getUserByEmail with email: " + email + " from database");
        return userRepository.findByEmail(email).orElseThrow(
            () -> BusinessException.from(UserException.USER_NOT_FOUND, "User not found")
        );
    }

    /**
     * Check if user exists by email
     * @param email email of user
     * @return true if user exists, false otherwise
     */
    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User insertUser(UserRegisterRequest request) {
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .username(EmailUtils.extractUsernameFromEmail(request.email()))
                .password(PasswordUtils.encode(request.password()))
                .status(true)
                .isActivated(false)
                .role("USER")
                .build();
        User savedUser = userRepository.save(user);
        String userJSON = JSONUtils.convertToJSON(savedUser); 
        redisService.appendHash(RedisKey.USER_INFO, savedUser.getId().toString(), userJSON);
        return savedUser;
    }

    @Override
    @CachePut(value = "user-info-data", key = "#user.id")
    public User updateUser(UserUpdateRequest user) {
        Object userOnRedis = redisService.getHashWithKeyAndHashKey(RedisKey.USER_INFO, user.id().toString());
        User targetUser = null;
        if(userOnRedis != null) {
            targetUser = JSONUtils.convertToObject(String.valueOf(userOnRedis), User.class);
        }
        targetUser = targetUser != null ? targetUser : getUserById(user.id());
        User updatedUser = userRepository.save(targetUser);
        String userJSON = JSONUtils.convertToJSON(updatedUser);
        redisService.appendHash(RedisKey.USER_INFO, updatedUser.getId().toString(), userJSON);
        return updatedUser;
    }

    @Override
    public void updateUserAccountActivation(UUID id, boolean isActivated) {
        User user = getUserById(id);
        user.setIsActivated(isActivated);
        userRepository.save(user);
    }

    @Override
    public List<User> getFriendList(UUID userId) {
        return userRepository.getFriendList(userId);
    }

    @KafkaListener(topics = { KafkaTopic.UPDATE_USER_ONLINE_STATUS_ON_CACHE }, groupId = "send_message_group")
    public void updateUserOnlineStatusOnCache(String message) {
        System.out.println("------------> Received message from Kafka: " + message);
        Map<String, Object> map = JSONUtils.readJSONString(message);
        String userId = (String) map.get("userId");
        String userOnlineStatusKey = RedisKey.USER_ONLINE_STATUS + ":" + userId.toString(); 
        redisService.setValue(userOnlineStatusKey, true);
        redisService.setTimeToLive(userOnlineStatusKey, 5, TimeUnit.MINUTES);
    }

    @Override
    public void verifyUserEmail(String email, boolean verified) {
        User user = getUserByEmail(email);
        user.setIsActivated(verified);
        userRepository.save(user);
    }
}
