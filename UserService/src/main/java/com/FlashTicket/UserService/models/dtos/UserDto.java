package com.FlashTicket.UserService.models.dtos;

import com.FlashTicket.UserService.models.domain.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDto(
        UUID id,
        String email,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
