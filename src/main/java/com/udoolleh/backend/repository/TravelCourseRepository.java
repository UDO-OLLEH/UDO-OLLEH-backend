package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.TravelCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TravelCourseRepository extends JpaRepository<TravelCourse, Long> {
    TravelCourse findByCourseName(String courseName);
    @Query("select distinct t from TravelCourse t left join t.detailList left join t.gpsList")
    List<TravelCourse> findAllCourse();

    @Query("select distinct t from TravelCourse t left join t.detailList left join t.gpsList where t.id = :id")
    Optional<TravelCourse> findCourse(Long id);
}
