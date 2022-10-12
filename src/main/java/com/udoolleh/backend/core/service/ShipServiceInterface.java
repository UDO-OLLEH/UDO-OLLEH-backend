package com.udoolleh.backend.core.service;

import com.udoolleh.backend.core.type.ShipCourseType;
import com.udoolleh.backend.core.type.ShipTimetableType;
import com.udoolleh.backend.web.dto.ResponseWharfTimetable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ShipServiceInterface {
    void registerWharfCourse(ShipCourseType wharfCourse);
    void registerWharfTimetable(ShipCourseType wharfCourse, List<String> departureTime, ShipTimetableType monthType);
    Optional<List<String>> getAllWharf();
    Optional<ResponseWharfTimetable.WharfTimetableDto> getWharfTimetable(ShipCourseType wharfCourse, ShipTimetableType monthType);
    void deleteWharf(ShipCourseType wharfCourse);
    void deleteWharfTimetable(ShipCourseType wharfCourse, ShipTimetableType monthType);
}
