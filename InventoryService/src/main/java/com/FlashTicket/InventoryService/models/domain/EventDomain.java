package com.FlashTicket.InventoryService.models.domain;

import com.FlashTicket.InventoryService.models.domain.valueObjects.EventId;
import com.FlashTicket.InventoryService.models.domain.valueObjects.EventPrice;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class EventDomain {
    private final EventId id;

    private final String eventName;
    private final EventPrice price;
    private Integer capacity;

    private LocalDateTime startsAt;
    private LocalDateTime endsAt;

    private EventDomain(EventId id, String eventName, EventPrice price, Integer capacity, LocalDateTime startsAt, LocalDateTime endsAt) {
        if (eventName == null || eventName.isBlank()) throw new IllegalArgumentException("Event name is required");
        if (capacity == null || capacity <= 0) throw new IllegalArgumentException("Please provide correct capacity");
        if (startsAt == null || startsAt.isAfter(endsAt)) throw new IllegalArgumentException("Please provide correct start time");
        if (endsAt == null || endsAt.isBefore(startsAt)) throw new IllegalArgumentException("Please provide correct end time");

        this.id = id;
        this.eventName = eventName;
        this.price = price;
        this.capacity = capacity;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public static EventDomain createEvent(String eventName, BigDecimal rawPrice, Integer capacity, LocalDateTime startsAt, LocalDateTime endsAt) {
        EventId id = EventId.generateId();
        EventPrice price = new EventPrice(rawPrice);

        return new EventDomain(id, eventName, price, capacity, startsAt, endsAt);
    }

    public void updateCapacity(Integer newCapacity) {
        if (newCapacity == null || newCapacity <= 0) throw new IllegalArgumentException("Please provide correct new capacity");
        this.capacity = newCapacity;
    }

    public void updateStartsAt(LocalDateTime newStartsAt) {
        if (newStartsAt == null || newStartsAt.isAfter(this.endsAt)) throw new IllegalArgumentException("Please provide correct new start time");
        this.startsAt = newStartsAt;
    }

    public void updateEndsAt(LocalDateTime newEndsAt) {
        if (newEndsAt == null || newEndsAt.isBefore(this.startsAt)) throw new IllegalArgumentException("Please provide correct new end time");
        this.endsAt = newEndsAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EventDomain that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
