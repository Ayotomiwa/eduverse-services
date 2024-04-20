package dev.captain.userservice.repo;


import dev.captain.userservice.model.tables.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepo extends JpaRepository<Feature, Long> {

}
