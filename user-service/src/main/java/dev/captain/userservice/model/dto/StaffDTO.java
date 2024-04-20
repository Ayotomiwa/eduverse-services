package dev.captain.userservice.model.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StaffDTO {
    private String staffNumber;
    private String staffType;
    private String department;
}
