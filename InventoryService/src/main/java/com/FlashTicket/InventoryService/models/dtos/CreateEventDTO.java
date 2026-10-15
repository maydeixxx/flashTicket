package com.FlashTicket.InventoryService.models.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateEventDTO(
        @NotBlank(message = "Event name cant be blank")
        String eventName,
        @DecimalMin(value = "0.00", message = "Price cant be negative")
        BigDecimal price,
        @Min(value = 0, message = "Capacity cant negative")
        Integer capacity,

        @NotNull(message = "Start time cant be null")
        LocalDateTime startsAt,
        @NotNull(message = "End time cant be null")
        LocalDateTime endsAt
) {
}
