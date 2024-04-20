package dev.captain.postservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Poll {
    private String id;
    private String question;
    private List<String> options;
}
