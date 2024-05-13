package dev.captain.eventservice.controller;


import dev.captain.eventservice.model.GroupEvent;
import dev.captain.eventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/event-service/groups/")
@RequiredArgsConstructor
public class GroupEventController {

    private final RestTemplate restTemplate;
    private final EventService eventService;


    @PostMapping("events")
    public ResponseEntity<?> createGroupEvent(@RequestBody GroupEvent newGroupEvent) {
        if (newGroupEvent == null) {
            return ResponseEntity.badRequest().body("Event cannot be empty");
        }
        if (newGroupEvent.getGroupId() == null || newGroupEvent.getName() == null) {
            return ResponseEntity.badRequest().body("Please provide a group id & event name");
        }
        if (newGroupEvent.getDate() == null || newGroupEvent.getTime() == null) {
            return ResponseEntity.badRequest().body("Please provide the event date & time");
        }

        Object group = restTemplate.getForObject("http://GROUP-SERVICE/api/group-service/groups/" + newGroupEvent.getGroupId(), Object.class);
        System.out.println("Group exist: " + group);
        if (group == null) {
            return ResponseEntity.badRequest().body("Group does not exist");
        }

        GroupEvent savedGroupEvent = eventService.createGroupEvent(newGroupEvent);
        return ResponseEntity.ok(savedGroupEvent);
    }

    @GetMapping("{groupId}/events")
    public ResponseEntity<?> getEventsByGroup(@PathVariable("groupId") String groupId) {

        Object group = restTemplate.getForObject("http://GROUP-SERVICE/api/group-service/groups/" + groupId, Object.class);
        System.out.println("Group exist: " + group);
        if (group == null) {
            return ResponseEntity.badRequest().body("Group does not exist");
        }

        List<GroupEvent> events = eventService.getEventsByGroup(groupId);
        if (events.isEmpty()) {
            return ResponseEntity.badRequest().body("No events found for this group");
        }
        return ResponseEntity.ok(events);
    }


    @GetMapping("events/{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable("eventId") String eventId) {
        GroupEvent event = eventService.getGroupEventById(eventId);
        if (event == null) {
            return ResponseEntity.badRequest().body("No event found with this id");
        }
        return ResponseEntity.ok(event);
    }

}
