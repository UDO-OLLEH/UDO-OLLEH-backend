package com.udoolleh.backend.core.type;

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
