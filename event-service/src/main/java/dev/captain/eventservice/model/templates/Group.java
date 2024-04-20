package dev.captain.eventservice.model.templates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Group {
    String id;
}
