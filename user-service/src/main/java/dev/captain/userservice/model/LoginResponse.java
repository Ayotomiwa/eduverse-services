package dev.captain.userservice.model;

import dev.captain.userservice.model.tables.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {
    private String token;
    private AppUser user;
}
