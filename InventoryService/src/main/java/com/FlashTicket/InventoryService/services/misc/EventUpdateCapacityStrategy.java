package com.FlashTicket.InventoryService.services.misc;

import com.FlashTicket.InventoryService.models.domain.EventDomain;
import com.FlashTicket.InventoryService.models.dtos.UpdateEventDTO;
import com.FlashTicket.InventoryService.services.interfaces.EventUpdateStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventUpdateCapacityStrategy implements EventUpdateStrategy {
    @Override
    public String getType() {
        return "capacity";
    }

    @Override
    public void updateEvent(EventDomain event, UpdateEventDTO dto) {
        try {
            event.updateCapacity(dto.capacity());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error updating capacity: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
