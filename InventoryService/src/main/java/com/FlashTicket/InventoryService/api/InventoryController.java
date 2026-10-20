package com.FlashTicket.InventoryService.api;

import com.FlashTicket.InventoryService.models.dtos.CreateEventDTO;
import com.FlashTicket.InventoryService.models.dtos.EventDTO;
import com.FlashTicket.InventoryService.models.dtos.UpdateEventDTO;
import com.FlashTicket.InventoryService.services.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping()
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    public ResponseEntity<Void> createEvent(@RequestBody @Valid CreateEventDTO dto) {
        inventoryService.createEvent(dto);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        inventoryService.deleteEvent(eventId);
        return ResponseEntity.status(204).build();
    }

    @PatchMapping()
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    public ResponseEntity<Void> updateEvent(@RequestBody @Valid UpdateEventDTO dto) {
        inventoryService.updateEvent(dto);
        return ResponseEntity.status(204).build();
    }

    @GetMapping()
    public ResponseEntity<List<EventDTO>> getEvents() {
        return ResponseEntity.status(200).body(inventoryService.getEvents());
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<EventDTO>> getEventsByName(@PathVariable String name) {
        return ResponseEntity.status(200).body(inventoryService.getEventsByName(name));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable UUID eventId) {
        return ResponseEntity.status(200).body(inventoryService.getEventById(eventId));
    }
}
