package com.honghung.chatapp.dto.response.user;

import com.honghung.chatapp.model.AuthenticatedToken;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {
    private UUID id;
    private String email;
    private AuthenticatedToken token;
}
