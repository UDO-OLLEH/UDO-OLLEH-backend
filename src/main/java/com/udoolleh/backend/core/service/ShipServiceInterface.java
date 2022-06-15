package com.udoolleh.backend.core.service;

import com.udoolleh.backend.core.type.ShipTimetableType;

import java.util.Date;

public interface ShipServiceInterface {
    void registerWharf(String wharfName);
    void registerWharfTimetable(String wharfName, Date departureTime, ShipTimetableType monthType);
}
