package dev.captain.userservice.repo;

import dev.captain.userservice.model.tables.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityRepo extends JpaRepository<University, Long> {


    Optional<University> findByName(String name);

    boolean existsUniversityByName(String name);

    boolean existsUniversityById(Long universityId);

    University findUniversityById(Long universityId);
}
