package com.udoolleh.backend.core.type;

import lombok.Getter;

@Getter
public enum ShipTimetableType {
    typeOne("1월~2월, 11월~12월"),
    typeTwo("3월, 10월"),
    typeThree("4월, 9월"),
    typeFour("5월~8월");

    private String month;

    ShipTimetableType(String month){
        this.month = month;
    }
}
