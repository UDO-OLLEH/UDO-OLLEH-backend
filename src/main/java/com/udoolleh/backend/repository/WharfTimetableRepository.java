package com.udoolleh.backend.repository;

import com.udoolleh.backend.core.type.ShipTimetableType;
import com.udoolleh.backend.entity.Wharf;
import com.udoolleh.backend.entity.WharfTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WharfTimetableRepository extends JpaRepository<WharfTimetable, Long> {
    List<WharfTimetable> findByWharf(Wharf wharf);
    List<WharfTimetable> findByWharfAndMonthType(Wharf wharfName, ShipTimetableType monthType);
    @Query("delete from WharfTimetable w " +
            "where w in(:wharfTimetableList)")
    @Modifying
    void deleteWharfTimetableList(List<WharfTimetable> wharfTimetableList);
}
