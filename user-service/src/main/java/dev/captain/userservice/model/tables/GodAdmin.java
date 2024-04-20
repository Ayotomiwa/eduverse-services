package dev.captain.userservice.model.tables;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GodAdmin {
    @Id
    private String companyID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
