package com.udoolleh.backend.core.type;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PlaceType {
    RESTAURANT("FD6"),
    CAFE("CE7");

    private String placeCode;

    PlaceType(String placeCode){
        this.placeCode = placeCode;
    }
    public String getPlaceCode() {
        return placeCode;
    }
}
