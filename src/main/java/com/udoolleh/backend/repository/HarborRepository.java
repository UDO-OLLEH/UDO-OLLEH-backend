package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Harbor;
import com.udoolleh.backend.entity.HarborTimetable;
import com.udoolleh.backend.entity.ShipFare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HarborRepository extends JpaRepository<Harbor, Long> {
    Harbor findByHarborName(String harborName);
    Harbor findByHarborTimetables(HarborTimetable harborTimetable);
}
