package dev.captain.userservice.repo;

import dev.captain.userservice.model.tables.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Long> {
    Department findByUniversityIdAndDepartmentCodeOrDepartmentName(Long universityId, String departmentCode, String departmentName);
}
