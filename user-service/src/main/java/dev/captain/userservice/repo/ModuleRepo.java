package dev.captain.userservice.repo;

import dev.captain.userservice.model.tables.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepo extends JpaRepository<Module, Long> {


    List<Module> findByUniversityId(Long universityId);

    List<Module> findByUniversityIdAndNameIgnoreCaseLike(Long universityId, String query);
}
