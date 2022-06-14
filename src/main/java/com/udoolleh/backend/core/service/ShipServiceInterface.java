package com.udoolleh.backend.core.service;

import java.util.Date;

public interface ShipServiceInterface {
    void registerWharf(String wharfName);
    void registerWharfTimetable(String wharfName, Date departureTime);
}
