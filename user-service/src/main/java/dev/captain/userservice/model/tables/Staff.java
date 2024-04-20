package dev.captain.userservice.model.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.captain.userservice.model.enums.STAFF_TYPE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String staffNumber;
    @Enumerated(EnumType.STRING)
    private STAFF_TYPE staffType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private AppUser user;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "staff_module",
            joinColumns = @JoinColumn(name = "staff_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "module_Id", referencedColumnName = "id"))
    private Set<Module> modules;
}
