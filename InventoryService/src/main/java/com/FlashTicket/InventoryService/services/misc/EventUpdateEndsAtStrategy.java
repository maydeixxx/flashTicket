package com.FlashTicket.InventoryService.services.misc;

import com.FlashTicket.InventoryService.models.domain.EventDomain;
import com.FlashTicket.InventoryService.models.dtos.UpdateEventDTO;
import com.FlashTicket.InventoryService.services.interfaces.EventUpdateStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventUpdateEndsAtStrategy implements EventUpdateStrategy {
    @Override
    public String getType() {
        return "endsat";
    }

    @Override
    public void updateEvent(EventDomain event, UpdateEventDTO dto) {
        try {
            event.updateEndsAt(dto.endsAt());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error updating end time: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
