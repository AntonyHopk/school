package org.example.courseservice.repository;

import org.example.courseservice.entity.Course;
import org.example.courseservice.entity.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCourseStatus(CourseStatus status);

    List<Course> findByOwnerUserId(Long userId);
}
