package com.honghung.chatapp.service.user;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.honghung.chatapp.dto.request.user.UserRegisterRequest;
import com.honghung.chatapp.dto.request.user.UserUpdateRequest;
import com.honghung.chatapp.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Service
public interface UserService {
    List<User> getUserList();

    User getUserById(UUID id);
    User getUserByIdNoCached(UUID id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    boolean existByEmail(String email);

    User insertUser(UserRegisterRequest request);

    User updateUser(UserUpdateRequest user);

    void updateUserAccountActivation(UUID id, boolean isActivated);

    List<User> getFriendList(UUID userId);

    void verifyUserEmail(String email, boolean verified);
}
