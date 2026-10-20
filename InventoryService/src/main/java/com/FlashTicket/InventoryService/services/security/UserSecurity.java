package com.FlashTicket.InventoryService.services.security;

import java.util.UUID;

public record UserSecurity(
        UUID userId,
        String email
) {
}
