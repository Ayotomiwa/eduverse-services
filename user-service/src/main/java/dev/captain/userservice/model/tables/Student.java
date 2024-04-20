package dev.captain.userservice.model.tables;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String studentNumber;
    private int startYear;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private AppUser user;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "student_module",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "moduleId", referencedColumnName = "id"))
    private Set<Module> modules;

}
