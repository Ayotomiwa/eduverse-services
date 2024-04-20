package dev.captain.userservice.repo;

import dev.captain.userservice.model.tables.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {
}
