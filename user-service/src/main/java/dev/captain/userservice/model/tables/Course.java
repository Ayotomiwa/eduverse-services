package dev.captain.userservice.model.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String courseCode;
    private String courseName;
    private Long duration;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Module> modules;

}
