package com.honghung.chatapp.model; 

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticatedToken {
    private String accessToken;
    private String refreshToken;
}
