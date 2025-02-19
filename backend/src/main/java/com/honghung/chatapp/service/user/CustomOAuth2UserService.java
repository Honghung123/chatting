package com.honghung.chatapp.service.user; 
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.honghung.chatapp.entity.User;
import com.honghung.chatapp.mapper.UserMapper; 
import com.honghung.chatapp.model.oauth2.OAuth2UserInfo;
import com.honghung.chatapp.model.oauth2.OAuth2UserInfoFactory;
import com.honghung.chatapp.repository.UserRepository;
import com.honghung.chatapp.utils.EmailUtils;
import com.honghung.chatapp.utils.PasswordUtils;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registerId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.createOAuth2UserInfo(registerId, oAuth2User.getAttributes());
        String userEmail = oAuth2UserInfo.getEmail();
        var userOptional = userRepository.findByEmail(userEmail);

        // if it is the first time user login with Google, insert new user, else update user
        User user = userOptional.isPresent() ?
                updateUserInfoIfSo(oAuth2UserInfo, userOptional.get(), registerId) :
                registerForNewUser(oAuth2UserInfo);
        return UserMapper.convUserPrincipalFromUser(user);
    }

    @Transactional
    User registerForNewUser(OAuth2UserInfo oAuth2UserInfo) {
        String username = EmailUtils.extractUsernameFromEmail(oAuth2UserInfo.getEmail());
        String hashedGeneratedPassword = PasswordUtils.encode(PasswordUtils.generateRandomPassword());
        var newUser = User.builder()
                .name(oAuth2UserInfo.getName())
                .avatarId(UUID.randomUUID().toString())
                .avatarUrl(oAuth2UserInfo.getAvatar())
                .username(username)
                .email(oAuth2UserInfo.getEmail())
                .password(hashedGeneratedPassword)
                .role("USER")
                .status(true)
                .build();
        return userRepository.save(newUser);
    }

    @Transactional
    User updateUserInfoIfSo(OAuth2UserInfo oAuth2UserInfo, User user, String registerId) {
        // Update avatar url
        user.setAvatarUrl(oAuth2UserInfo.getAvatar());
        user.setName(oAuth2UserInfo.getName());       
        return userRepository.save(user);
    }
}
