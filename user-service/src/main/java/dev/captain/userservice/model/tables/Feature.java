package dev.captain.userservice.model.tables;


import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.captain.userservice.model.enums.FEATURE;
import dev.captain.userservice.model.enums.FEATURE_TYPE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private FEATURE name;
    private String description;
    @Enumerated(EnumType.STRING)
    private FEATURE_TYPE type;


    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniversityFeature> universityFeatures;

}
