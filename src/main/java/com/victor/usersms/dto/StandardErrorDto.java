package com.victor.usersms.dto;

import java.time.LocalDateTime;

public record StandardErrorDto(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message
) {}