package dev.captain.userservice.model.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.captain.userservice.model.enums.AUTHORITY;
import dev.captain.userservice.model.enums.USER_TYPE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private AUTHORITY authority;

    @Enumerated(EnumType.STRING)
    private USER_TYPE userType;

    private boolean isActive;
    private boolean isBanned;
    private LocalDateTime modifiedAt;


    @JsonIgnore()
    private String password;
    private String email;


    @ManyToOne(fetch = FetchType.LAZY)
    private University university;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "followers")
    private List<AppUser> followedUsers = new ArrayList<>();


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "Relationship",
            joinColumns = @JoinColumn(name = "followed_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id"))
    private List<AppUser> followers = new ArrayList<>();


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfileInfo profileInfo;


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Student student;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Staff staff;


    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Faculty faculty;


    public static boolean checkRequiredData(AppUser user) {
        if (user.getFirstName() == null) {
            return false;
        }
        if (user.getUsername() == null) {
            return false;
        }
        if (!user.getUserType().equals(USER_TYPE.FACULTY)) {
            if (user.getLastName() == null) {
                return false;
            }
        }
        return user.getEmail() != null;
    }


    @PrePersist
    @PreUpdate
    public void toLowerCase() {
        this.email = email.toLowerCase();
        this.username = username.toLowerCase();
        this.firstName = firstName.toLowerCase();
        this.lastName = lastName.toLowerCase();
    }

}
