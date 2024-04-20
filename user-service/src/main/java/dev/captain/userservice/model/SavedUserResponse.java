package dev.captain.userservice.model;


import dev.captain.userservice.model.dto.AppUserDTO;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SavedUserResponse {
    private String saveMessage;
    private long savedUsersNo;
    private long unSavedUsersNo;
    private List<UnSavedUsers> unSavedUsers;


    @Data
    public static class UnSavedUsers {
        private AppUserDTO user;
        private String reason;
    }


}
