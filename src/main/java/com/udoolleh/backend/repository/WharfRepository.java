package com.udoolleh.backend.repository;

import com.udoolleh.backend.core.type.ShipCourseType;
import com.udoolleh.backend.entity.Wharf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WharfRepository extends JpaRepository<Wharf, Long> {
    Wharf findByWharfCourse(ShipCourseType wharfCourse);
}
