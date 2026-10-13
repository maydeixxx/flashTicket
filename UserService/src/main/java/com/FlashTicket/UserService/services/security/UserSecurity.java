package com.FlashTicket.UserService.services.security;

import com.FlashTicket.UserService.models.domain.enums.Role;

public record UserSecurity(
        String email,
        Role role
) {
}
