package dev.captain.userservice.repo;

import dev.captain.userservice.model.tables.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepo extends JpaRepository<Staff, Long> {
}
