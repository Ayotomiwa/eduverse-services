package dev.captain.groupservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    String id;
    String groupId;
    String name;
    String description;
    String location;
    LocalDate eventDate;
    LocalTime eventTime;
    String host;
    String imageUrl;
}
