package dev.captain.postservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    private String id;
    private String option;
    private List<Long> voterIds;
}
