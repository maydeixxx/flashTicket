package com.FlashTicket.InventoryService.models.dtos;

import java.time.LocalDateTime;

public record ErrorResponseDTO (
        String error,
        String message,
        int code,
        String path,
        LocalDateTime timestamp
) {
}
