package com.honghung.chatapp.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest (
    @NotBlank(message = "Name cannot be blank")
    String name,
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
    String email,
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 7, message = "Password must be at least 7 characters long")
    String password
    ){}
