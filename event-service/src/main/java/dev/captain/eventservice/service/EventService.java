package dev.captain.eventservice.service;


import dev.captain.eventservice.model.EventResponse;
import dev.captain.eventservice.model.GroupEvent;
import dev.captain.eventservice.model.ModuleEvent;
import dev.captain.eventservice.repo.GroupEventRepo;
import dev.captain.eventservice.repo.ModuleEventRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final GroupEventRepo groupEventRepo;
    private final ModuleEventRepo moduleEventRepo;


    public GroupEvent createGroupEvent(GroupEvent newGroupEvent) {
        return groupEventRepo.save(newGroupEvent);
    }

    public List<GroupEvent> getEventsByGroup(String groupId) {
        return groupEventRepo.findAllByGroupId(groupId);
    }

    public GroupEvent getGroupEventById(String eventId) {
        return groupEventRepo.findById(eventId).orElse(null);
    }

    public ModuleEvent createModuleEvent(ModuleEvent newModuleEvent) {
        return moduleEventRepo.save(newModuleEvent);
    }

    public List<ModuleEvent> getEventsByModule(Long moduleId) {
        return moduleEventRepo.findAllByModuleId(moduleId);
    }

    public ModuleEvent getModuleEventById(String eventId) {
        return moduleEventRepo.findById(eventId).orElse(null);
    }

    public List<EventResponse> getEvents(List<String> groupIds, List<Long> moduleIds) {
        List<GroupEvent> groupEvents = groupEventRepo.findAllByGroupIdIn(groupIds);
        List<ModuleEvent> moduleEvents = moduleEventRepo.findAllByModuleIdIn(moduleIds);
        return EventResponse.from(groupEvents, moduleEvents);
    }
}
