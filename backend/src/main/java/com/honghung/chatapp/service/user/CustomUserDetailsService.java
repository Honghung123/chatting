package com.honghung.chatapp.service.user; 
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.honghung.chatapp.component.exception.BusinessException;
import com.honghung.chatapp.mapper.UserMapper;
import com.honghung.chatapp.model.UserPrincipal;
import com.honghung.chatapp.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if(username.contains("@")) {
            return loadUserByEmail(username);
        }
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("user_not_found", "User not found"));
        return UserMapper.convUserPrincipalFromUser(user);
    }


    public UserPrincipal loadUserByEmail(String userEmail) {
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException("user_not_found", "User not found"));
        return UserMapper.convUserPrincipalFromUser(user);
    }
} 
