package dev.captain.eventservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class ModuleEvent {
    @Id
    private String id;
    private Long moduleId;
    private String moduleName;
    private String name;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private Long creatorId;
}
