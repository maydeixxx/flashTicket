package com.FlashTicket.InventoryService.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateEventDTO(
        @NotNull(message = "Provide correct id")
        UUID eventId,
        @NotBlank(message = "Provide type of update")
        String typeOfUpdate,

        Integer capacity,
        LocalDateTime startsAt,
        LocalDateTime endsAt
) {
}