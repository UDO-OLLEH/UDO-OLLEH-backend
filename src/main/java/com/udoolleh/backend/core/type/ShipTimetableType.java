package com.udoolleh.backend.core.type;

import lombok.Getter;

@Getter
public enum ShipTimetableType {
    SEONGSAN_One("1월~2월, 11월~12월"),
    SEONGSAN_Two("3월, 10월"),
    SEONGSAN_Three("4월, 9월"),
    SEONGSAN_Four("5월~8월"),
    JONGDAL_One("1월~3월, 10월~12월"),
    JONGDAL_Two("4월~9월");

    private String month;

    ShipTimetableType(String month){
        this.month = month;
    }
}
