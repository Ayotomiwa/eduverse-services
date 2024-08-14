package dev.captain.eventservice.controller;


import dev.captain.eventservice.model.EventResponse;
import dev.captain.eventservice.model.templates.Group;
import dev.captain.eventservice.model.templates.Module;
import dev.captain.eventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/event-service/")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final RestTemplate restTemplate;

    @GetMapping("users/{userId}/events")
    public ResponseEntity<?> getEvents(@PathVariable String userId) {

        try {
            restTemplate.getForObject("https://user-service-dgrsoybfsa-ew.a.run.app/api/user-service/users/" + userId, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.badRequest().body("User does not exist");
        } catch (RestClientException ignored) {


        }
        List<String> groupIds = new ArrayList<>();
        List<Long> moduleIds = new ArrayList<>();
        List<Group> groups = null;
        List<Module> modules = null;

        try {
            groups = restTemplate.exchange("https://group-service-dgrsoybfsa-ew.a.run.app/api/group-service/users/" + userId +
                    "/groups", HttpMethod.GET, null, new ParameterizedTypeReference<List<Group>>() {
            }).getBody();
            System.out.println("Groups: " + groups);
            modules = restTemplate.exchange("https://user-service-dgrsoybfsa-ew.a.run.app/api/user-service/users/" +
                    userId + "/modules", HttpMethod.GET, null, new ParameterizedTypeReference<List<Module>>() {
            }).getBody();

            System.out.println("Modules: " + modules);
        } catch (Exception ignored) {

        }
        if (groups == null) {
            groups = new ArrayList<>();
        }
        if (modules == null) {
            modules = new ArrayList<>();
        }
        if (!groups.isEmpty()) {
            groupIds.addAll(groups.stream().map(Group::getId).toList());
        }
        if (!modules.isEmpty()) {
            moduleIds.addAll(modules.stream().map(Module::getId).toList());
        }

        List<EventResponse> events = eventService.getEvents(groupIds, moduleIds);
        return ResponseEntity.ok(events);
    }
}
