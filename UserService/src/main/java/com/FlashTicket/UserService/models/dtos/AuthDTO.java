package com.FlashTicket.UserService.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthDTO(
    @Email(message = "Please, type correct email")
    @NotBlank(message = "Email is required")
    String email,

    @Size(min = 8, message = "Minimal size of password is - 8")
    @NotBlank(message = "Password is required")
    String password
) {
}
