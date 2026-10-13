package com.FlashTicket.UserService.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateDTO(
        @Email(message = "Provide correct email")
        @NotBlank(message = "Email is required")
        String email,
        String newEmail,
        String newPassword
) {
}
