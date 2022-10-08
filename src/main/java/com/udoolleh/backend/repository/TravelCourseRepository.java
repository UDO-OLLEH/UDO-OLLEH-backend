package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.TravelCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelCourseRepository extends JpaRepository<TravelCourse, Long> {
    TravelCourse findByCourseName(String courseName);
}
