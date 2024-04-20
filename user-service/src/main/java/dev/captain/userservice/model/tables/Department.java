package dev.captain.userservice.model.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.captain.userservice.model.enums.DEPARTMENT_TYPE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String departmentName;
    @Enumerated(EnumType.STRING)

    private DEPARTMENT_TYPE departmentType;

    private String departmentCode;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Staff> staff;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Faculty> faculties;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private University university;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses;
}
