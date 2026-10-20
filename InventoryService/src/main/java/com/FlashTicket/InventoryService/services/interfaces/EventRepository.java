package com.FlashTicket.InventoryService.services.interfaces;

import com.FlashTicket.InventoryService.models.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    Optional<Event> findEventById(UUID id);

    List<Event> findEventsByEventName(String eventName);
}
