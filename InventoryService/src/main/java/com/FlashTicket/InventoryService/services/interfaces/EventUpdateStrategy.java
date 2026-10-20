package com.FlashTicket.InventoryService.services.interfaces;

import com.FlashTicket.InventoryService.models.domain.EventDomain;
import com.FlashTicket.InventoryService.models.dtos.UpdateEventDTO;

public interface EventUpdateStrategy {
    String getType();
    void updateEvent(EventDomain event, UpdateEventDTO dto);
}
