package com.victor.usersms.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(@NotNull(message = "username can't be null") @Size(min = 3, max = 50, message = "username must be between 3 and 50 characters")  String username,
                              @NotNull(message = "password can't be null") @Size(min = 8, max = 100, message = "password must be between 8 and 100 characters")  String password) {
}
