package com.honghung.chatapp.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest (
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email is not valid")
        String email,
        @NotBlank(message = "Password cannot be blank")
        String password
){
}
