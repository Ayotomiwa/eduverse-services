package dev.captain.groupservice.service;

import dev.captain.groupservice.model.Event;
import dev.captain.groupservice.model.Group;
import dev.captain.groupservice.repository.EventRepo;
import dev.captain.groupservice.repository.GroupRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepo eventRepo;
    private final GroupRepo groupRepo;


    public Event create(Event event, String groupId) {
        Group group = groupRepo.findById(groupId).orElse(null);
        if (group == null) {
            throw new NoSuchElementException("Group not found");
        }
        event.setHost(group.getName());
        return eventRepo.save(event);
    }

    public Event getEvent(String eventId) {
        return eventRepo.findById(eventId).orElse(null);
    }

    public List<Event> getEvents(String groupId) {
        return eventRepo.findByGroupId(groupId);
    }

}
