package com.udoolleh.backend.repository;

import com.udoolleh.backend.core.type.ShipTimetableType;
import com.udoolleh.backend.entity.Wharf;
import com.udoolleh.backend.entity.WharfTimetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface WharfTimetableRepository extends JpaRepository<WharfTimetable, Long> {
    WharfTimetable findByWharfAndDepartureTimeAndMonthType(Wharf wharfName, Date departureTime, ShipTimetableType monthType);
    List<WharfTimetable> findByWharf(Wharf wharf);
}
