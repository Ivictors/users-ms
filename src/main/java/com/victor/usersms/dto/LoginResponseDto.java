package com.victor.usersms.dto;

import jakarta.validation.constraints.NotNull;

public record LoginResponseDto(@NotNull String acessToken,
                               @NotNull Long expiresIn){
}
