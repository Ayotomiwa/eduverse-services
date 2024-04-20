package dev.captain.eventservice.model;

import dev.captain.eventservice.model.enums.EVENT_TYPE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventResponse {
    private String id;
    private String name;
    private String description;
    private EVENT_TYPE type;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private String host;
    private String hostId;
    private Long creatorId;
    private String updatedDate;

    public static List<EventResponse> from(List<GroupEvent> groupEvents, List<ModuleEvent> moduleEvents) {
        List<EventResponse> eventResponses = new ArrayList<>();
        for (GroupEvent groupEvent : groupEvents) {
            EventResponse eventResponse = getEventResponse(groupEvent);
            eventResponses.add(eventResponse);
        }
        for (ModuleEvent moduleEvent : moduleEvents) {
            EventResponse eventResponse = getEventResponse(moduleEvent);
            eventResponses.add(eventResponse);
        }
        return eventResponses;
    }

    private static EventResponse getEventResponse(ModuleEvent moduleEvent) {
        EventResponse eventResponse = new EventResponse();
        eventResponse.setId(moduleEvent.getId());
        eventResponse.setName(moduleEvent.getName());
        eventResponse.setDescription(moduleEvent.getDescription());
        eventResponse.setType(EVENT_TYPE.MODULE);
        eventResponse.setDate(moduleEvent.getDate());
        eventResponse.setTime(moduleEvent.getTime());
        eventResponse.setLocation(moduleEvent.getLocation());
        eventResponse.setHostId(String.valueOf(moduleEvent.getModuleId()));
        eventResponse.setHost(moduleEvent.getModuleName());
        eventResponse.setCreatorId(moduleEvent.getCreatorId());
        return eventResponse;
    }

    private static EventResponse getEventResponse(GroupEvent groupEvent) {
        EventResponse eventResponse = new EventResponse();
        eventResponse.setId(groupEvent.getId());
        eventResponse.setName(groupEvent.getName());
        eventResponse.setDescription(groupEvent.getDescription());
        eventResponse.setType(EVENT_TYPE.GROUP);
        if (groupEvent.getDate() != null) {
            eventResponse.setDate(groupEvent.getDate());
        }
        if (groupEvent.getTime() != null) {
            eventResponse.setTime(groupEvent.getTime());
        }
        eventResponse.setLocation(groupEvent.getLocation());
        eventResponse.setHostId(groupEvent.getGroupId());
        eventResponse.setHost(groupEvent.getGroupName());
        eventResponse.setCreatorId(groupEvent.getCreatorId());
        return eventResponse;
    }
}
