package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestShipFare;
import com.udoolleh.backend.web.dto.ResponseHarbor;
import com.udoolleh.backend.web.dto.ResponseHarborTimetable;

import com.udoolleh.backend.web.dto.ResponseShipFare;
import java.util.List;

public interface ShipServiceInterface {
    void registerHarbor(String harbor);
    void registerHarborTimetable(String harborName, String destination, String period, String operatingTime);
    void registerShipFare(RequestShipFare.RegisterShipFareDto registerShipFareDto);
    List<ResponseHarbor.HarborDto> getAllHarbors();
    ResponseHarborTimetable.HarborTimetableDto getHarborTimetable(Long id, String destination);
    ResponseShipFare.HarborShipFareDto getShipFare(Long harborId);
    void deleteHarbor(Long harborId);
    void deleteHarborTimetable(Long harborTimetableId);
    void deleteShipFare(Long shipFareId);
}
