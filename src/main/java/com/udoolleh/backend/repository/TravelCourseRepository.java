package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.TravelCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TravelCourseRepository extends JpaRepository<TravelCourse, Long> {
    TravelCourse findByCourseName(String courseName);
    @Query("select distinct t from TravelCourse t left join t.detailList left join t.gpsList")
    List<TravelCourse> findAllCourse();
}
