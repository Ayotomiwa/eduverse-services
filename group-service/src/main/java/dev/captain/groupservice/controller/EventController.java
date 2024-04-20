package dev.captain.groupservice.controller;

import dev.captain.groupservice.model.Event;
import dev.captain.groupservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/group-service/")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;


    @PostMapping("groups/{groupId}/events")
    public ResponseEntity<?> addEvent(@RequestBody Event event, @PathVariable String groupId) {
        if (event == null) {
            return ResponseEntity.badRequest().body("Event cannot be empty");
        }
        if (groupId == null || event.getName() == null) {
            return ResponseEntity.badRequest().body("Please provide a group id & event name");
        }
        if (event.getEventDate() == null || event.getEventTime() == null) {
            return ResponseEntity.badRequest().body("Please provide the event date & time");
        }
        if (event.getGroupId() == null) {
            return ResponseEntity.badRequest().body("Please provide the group id");
        }
        Event savedEvent = eventService.create(event, groupId);
        return ResponseEntity.ok(savedEvent);
    }

    @GetMapping("groups/{groupId}/events")
    public ResponseEntity<?> getEvents(@PathVariable String groupId) {
        List<Event> events = eventService.getEvents(groupId);
        if (events.isEmpty()) {
            return ResponseEntity.badRequest().body("No events found for this group");
        }
        return ResponseEntity.ok(events);
    }


    @GetMapping("events/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable String eventId) {
        Event event = eventService.getEvent(eventId);
        if (event == null) {
            return ResponseEntity.badRequest().body("Event not found");
        }
        return ResponseEntity.ok(event);
    }


}
