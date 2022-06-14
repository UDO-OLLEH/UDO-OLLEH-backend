package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Wharf;
import com.udoolleh.backend.entity.WharfTimetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface WharfTimetableRepository extends JpaRepository<WharfTimetable, Long> {
    WharfTimetable findByWharfAndDepartureTime(Wharf wharfName, Date departureTime);
    List<WharfTimetable> findByWharf(Wharf wharf);
}
