package dev.captain.userservice.model.tables;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String code;
    private String about;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private University university;


    @ManyToMany(mappedBy = "modules", fetch = FetchType.LAZY)
    private Set<Student> students;


    @ManyToMany(mappedBy = "modules", fetch = FetchType.LAZY)
    private Set<Staff> teachingTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;
}
