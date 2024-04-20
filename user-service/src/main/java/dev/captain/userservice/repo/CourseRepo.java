package dev.captain.userservice.repo;

import dev.captain.userservice.model.tables.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {

    Course findByDepartmentUniversityIdAndCourseCodeOrCourseName(Long universityId, String courseCode, String courseName);

}
