package dev.captain.userservice.model.tables;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private boolean verified;
    private String phoneNumber;
    private String domain;
    private String logoUrl;
    private String primaryTheme;
    private String secondaryTheme;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "university", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Department> departments;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "university",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppUser> users;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "university",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniversityFeature> universityFeatures;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "university",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Module> modules;


    @PrePersist
    @PreUpdate
    public void toLowerCase() {
        this.name = name.toLowerCase();
        this.domain = domain.toLowerCase();
    }

    public void updateUniversityFields(University updatedUniversity) {
        this.name = updatedUniversity.name;
        this.verified = updatedUniversity.verified;
        this.phoneNumber = updatedUniversity.phoneNumber;
        this.domain = updatedUniversity.domain;
        this.logoUrl = updatedUniversity.logoUrl;
        this.primaryTheme = updatedUniversity.primaryTheme;
        this.secondaryTheme = updatedUniversity.secondaryTheme;
    }


}
