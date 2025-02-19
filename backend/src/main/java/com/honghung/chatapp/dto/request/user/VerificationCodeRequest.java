package com.honghung.chatapp.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerificationCodeRequest(
     @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
    String email,

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 6, message = "Invalid verification code")
    String code
) {
    
}
