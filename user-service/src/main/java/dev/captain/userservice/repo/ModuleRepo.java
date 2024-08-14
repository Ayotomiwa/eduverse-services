package dev.captain.userservice.repo;

import dev.captain.userservice.model.enums.USER_TYPE;
import dev.captain.userservice.model.tables.AppUser;
import dev.captain.userservice.model.tables.Department;
import dev.captain.userservice.model.tables.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepo extends JpaRepository<Module, Long> {


    List<Module> findByUniversityId(Long universityId);

    List<Module> findByUniversityIdAndNameIgnoreCaseLike(Long universityId, String query);


    @Query("SELECT u FROM Module u WHERE u.university.id = :universityId AND (lower(u.name) LIKE lower(:name) OR lower(u.code) LIKE lower(:code))")
    List<Module> findByUniversityIdAndNameOrCode(@Param("universityId") Long universityId, @Param("name") String name, @Param("code") String code);


}
