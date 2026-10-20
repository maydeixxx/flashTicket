package com.FlashTicket.InventoryService.models.domain.valueObjects;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.Objects;
import java.util.UUID;

public record EventId (UUID id) {
    public EventId {
        Objects.requireNonNull(id, "Id cant be null");
    }

    public static EventId generateId() {
        return new EventId(UuidCreator.getTimeOrderedEpoch());
    }
}
