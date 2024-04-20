package dev.captain.userservice.repo;

import dev.captain.userservice.model.tables.ProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ProfileInfoRepo extends JpaRepository<ProfileInfo, Long> {

}
