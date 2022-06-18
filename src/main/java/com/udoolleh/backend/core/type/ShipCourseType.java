package com.udoolleh.backend.core.type;

import lombok.Getter;

@Getter
public enum ShipCourseType {
    JONGDAL_UDO("우도발"),
    UDO_JONGDAL("종달발"),
    SEONGSAN_UDO("우도발"),
    UDO_SEONGSAN("성산발");

    private String destination;

    ShipCourseType(String destination){
        this.destination = destination;
    }
}
