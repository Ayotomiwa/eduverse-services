package dev.captain.userservice.model.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.captain.userservice.model.enums.AUTHORITY;
import dev.captain.userservice.model.enums.USER_TYPE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    private AUTHORITY authorizedUsers;

    @Enumerated(EnumType.STRING)
    private USER_TYPE authorizedMembers;

    @ManyToOne
    private Feature feature;

    @JsonIgnore
    @ManyToOne
    private University university;

}
