package com.FlashTicket.InventoryService.services.misc;

import com.FlashTicket.InventoryService.models.domain.EventDomain;
import com.FlashTicket.InventoryService.models.dtos.EventDTO;
import com.FlashTicket.InventoryService.models.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public EventDomain entityToDomain(Event entity) {
        return EventDomain.restoreEvent(
                entity.getId(),
                entity.getEventName(),
                entity.getPrice(),
                entity.getCapacity(),
                entity.getStartsAt(),
                entity.getEndsAt()
        );
    }

    public Event domainToEntity(EventDomain domain) {
        Event event = new Event();
        event.setId(domain.getId().id());
        event.setEventName(domain.getEventName());
        event.setPrice(domain.getPrice().price());
        event.setCapacity(domain.getCapacity());
        event.setStartsAt(domain.getStartsAt());
        event.setEndsAt(domain.getEndsAt());
        return event;
    }

    public EventDTO entityToDto(Event entity) {
        return new EventDTO(
                entity.getId(),
                entity.getEventName(),
                entity.getPrice(),
                entity.getCapacity(),
                entity.getStartsAt(),
                entity.getEndsAt()
        );
    }
}
