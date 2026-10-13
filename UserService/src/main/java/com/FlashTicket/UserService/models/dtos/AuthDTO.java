package com.FlashTicket.UserService.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AuthDTO(
    @Email(message = "Please, type correct email")
    String email,

    @Min(value = 8, message = "Minimal size of password is - 8")
    @NotBlank(message = "Password is required")
    String password
) {
}
