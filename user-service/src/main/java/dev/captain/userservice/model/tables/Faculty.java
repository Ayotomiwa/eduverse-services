package dev.captain.userservice.model.tables;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;


    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private AppUser userAccount;


}
