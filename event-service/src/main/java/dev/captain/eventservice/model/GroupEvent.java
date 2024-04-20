package dev.captain.eventservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class GroupEvent {
    @Id
    private String id;
    private String groupId;
    private String name;
    private String groupName;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private Long creatorId;
}
