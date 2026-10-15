package com.FlashTicket.InventoryService.models.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventDTO(
        UUID eventId,
        String eventName,
        BigDecimal price,
        Integer capacity,
        LocalDateTime startsAt,
        LocalDateTime endsAt
) {
}
