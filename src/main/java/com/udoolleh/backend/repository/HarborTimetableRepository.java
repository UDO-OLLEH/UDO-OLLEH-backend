package com.udoolleh.backend.repository;


import com.udoolleh.backend.entity.Harbor;
import com.udoolleh.backend.entity.HarborTimetable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HarborTimetableRepository extends JpaRepository<HarborTimetable, Long> {
    HarborTimetable findByDestinationAndPeriodAndHarborId(String destination, String period, Long harborId);
    List<HarborTimetable> findByHarbor(Harbor harbor);
}
