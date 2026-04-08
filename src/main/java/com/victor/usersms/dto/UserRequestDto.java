package com.victor.usersms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDto (
    @NotBlank(message = "email can't be blank") 
    @Email(message = "email should be valid") 
    String email,
    
    @NotBlank(message = "username can't be blank") 
    @Size(min = 3, max = 50, message = "username must be between 3 and 50 characters") 
    String username,
    
    @NotBlank(message = "password can't be blank") 
    @Size(min = 8, max = 100, message = "password must be between 8 and 100 characters") 
    String password) {
}
