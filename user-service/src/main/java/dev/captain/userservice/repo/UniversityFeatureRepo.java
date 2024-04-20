package dev.captain.userservice.repo;

import dev.captain.userservice.model.tables.UniversityFeature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UniversityFeatureRepo extends JpaRepository<UniversityFeature, Long> {
    List<UniversityFeature> findByUniversityId(Long universityId);
}
