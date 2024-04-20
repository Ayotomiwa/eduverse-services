package dev.captain.userservice.repo;

import dev.captain.userservice.model.tables.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepo extends JpaRepository<Faculty, Long> {

}
