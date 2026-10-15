package com.FlashTicket.InventoryService.services;

import com.FlashTicket.InventoryService.models.domain.EventDomain;
import com.FlashTicket.InventoryService.models.dtos.CreateEventDTO;
import com.FlashTicket.InventoryService.models.dtos.EventDTO;
import com.FlashTicket.InventoryService.models.dtos.UpdateEventDTO;
import com.FlashTicket.InventoryService.models.entity.Event;
import com.FlashTicket.InventoryService.models.exceptions.EventNotFoundException;
import com.FlashTicket.InventoryService.models.exceptions.UpdateStrategyNotFoundException;
import com.FlashTicket.InventoryService.services.interfaces.EventRepository;
import com.FlashTicket.InventoryService.services.interfaces.EventUpdateStrategy;
import com.FlashTicket.InventoryService.services.misc.EventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class InventoryService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final Map<String, EventUpdateStrategy> updateStrategyMap;

    public InventoryService(EventRepository eventRepository, EventMapper eventMapper, List<EventUpdateStrategy> strategies) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.updateStrategyMap = new HashMap<>();

        for (EventUpdateStrategy strategy : strategies) {
            this.updateStrategyMap.putIfAbsent(strategy.getType().toLowerCase().trim(), strategy);
        }
    }

    public void createEvent(CreateEventDTO dto) {
        try {
            EventDomain newEvent = EventDomain.createEvent(dto.eventName(), dto.price(), dto.capacity(), dto.startsAt(), dto.endsAt());
            eventRepository.save(eventMapper.domainToEntity(newEvent));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error creating event [{}]: {}", dto.eventName(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deleteEvent(UUID eventId) {
        try {
            if (eventId == null) throw new IllegalArgumentException("Provide correct id");
            eventRepository.deleteById(eventId);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error deleting event [{}]: {}", eventId, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void updateEvent(UpdateEventDTO dto) {
        try {
            Event entity = eventRepository.findEventById(dto.eventId()).orElseThrow(() -> new EventNotFoundException("Event [%s] not found".formatted(dto.eventId())));
            EventDomain event = eventMapper.entityToDomain(entity);
            EventUpdateStrategy eventUpdateStrategy = updateStrategyMap.get(dto.typeOfUpdate().toLowerCase().trim());

            if (eventUpdateStrategy == null) throw new UpdateStrategyNotFoundException("Update strategy [%s] not found".formatted(dto.typeOfUpdate()));
            eventUpdateStrategy.updateEvent(event, dto);

            eventRepository.save(eventMapper.domainToEntity(event));
        } catch (IllegalArgumentException | EventNotFoundException | UpdateStrategyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error updating event [{}]: {}", dto.eventId(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<EventDTO> getEvents() {
        try {
            return eventRepository.findAll().stream()
                    .map(eventMapper::entityToDto)
                    .toList();
        } catch (Exception e) {
            log.error("Unexpected error getting events: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<EventDTO> getEventsByName(String name) {
        try {
            return eventRepository.findEventsByEventName(name).stream()
                    .map(eventMapper::entityToDto)
                    .toList();
        } catch (Exception e) {
            log.error("Unexpected error getting events by name - [{}]: {}", name, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public EventDTO getEventById(UUID id) {
        try {
            return eventMapper.entityToDto(eventRepository.findEventById(id).orElseThrow(() -> new EventNotFoundException("Event [%s] not found".formatted(id))));
        } catch (Exception e) {
            log.error("Unexpected error getting event [{}]: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
