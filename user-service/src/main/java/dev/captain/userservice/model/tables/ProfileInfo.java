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
public class ProfileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private AppUser user;

    private String bio;
    private String profilePicUrl;
    private String coverPicUrl;

}
