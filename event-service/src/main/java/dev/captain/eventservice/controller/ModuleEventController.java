package dev.captain.eventservice.controller;


import dev.captain.eventservice.model.ModuleEvent;
import dev.captain.eventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/event-service/modules/")
@RequiredArgsConstructor
public class ModuleEventController {

    private final RestTemplate restTemplate;
    private final EventService eventService;


    @PostMapping("{moduleId}/events")
    public ResponseEntity<?> createGroup(@RequestBody ModuleEvent newModuleEvent) {
        if (newModuleEvent == null) {
            return ResponseEntity.badRequest().body("Event cannot be empty");
        }
        if (newModuleEvent.getModuleId() == null) {
            return ResponseEntity.badRequest().body("Please provide a Module Id");
        }

        //check if group exists in group service using rest template

//       Object groupExist =  restTemplate.getForObject("http://GROUP_SERVICE/api/group-service/groups/" + newGroupEvent.getGroupId(), Object.class);
//        if(groupExist == null){
//                return ResponseEntity.badRequest().body("Group does not exist");
//        }

        ModuleEvent savedModuleEvent = eventService.createModuleEvent(newModuleEvent);
        return ResponseEntity.ok(savedModuleEvent);
    }

    @GetMapping("/{moduleId}/events")
    public ResponseEntity<?> getEventsByModule(@PathVariable("moduleId") Long moduleId) {
        List<ModuleEvent> events = eventService.getEventsByModule(moduleId);
        if (events.isEmpty()) {
            return ResponseEntity.badRequest().body("No events found for this module");
        }
        return ResponseEntity.ok(events);
    }


    @GetMapping("events/{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable("eventId") String eventId) {
        ModuleEvent event = eventService.getModuleEventById(eventId);
        if (event == null) {
            return ResponseEntity.badRequest().body("No event found with this id");
        }
        return ResponseEntity.ok(event);
    }


}
