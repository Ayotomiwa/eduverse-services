package dev.captain.userservice.repo;

import dev.captain.userservice.model.tables.GodAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppAdminRepo extends JpaRepository<GodAdmin, String> {
}
