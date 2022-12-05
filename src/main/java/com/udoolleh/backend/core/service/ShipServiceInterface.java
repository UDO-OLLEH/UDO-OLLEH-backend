package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.ResponseHarbor;
import com.udoolleh.backend.web.dto.ResponseHarborTimetable;

import java.util.List;

public interface ShipServiceInterface {
    void registerHarbor(String harbor);
    void registerHarborTimetable(String harborName, String destination, String period, String operatingTime);
    List<ResponseHarbor.HarborDto> getAllHarbors();
    ResponseHarborTimetable.HarborTimetableDto getHarborTimetable(String harborName, String destination);
    void deleteHarbor(Long harborId);
    void deleteHarborTimetable(Long harborTimetableId);
}
