package com.udoolleh.backend.core.type;

import java.util.Arrays;

public enum UdoCoordinateType {
    ONE_QUADRANT(1,"126.955669,33.506972,126.975226,33.527403"),
    TWO_QUADRANT(2,"126.935805,33.527508,126.955669,33.506972"),
    THREE_QUADRANT(3,"126.937426,33.490855,126.955669,33.506972"),
    FOUR_QUADRANT(4,"126.955669,33.506972,126.978418,33.487412");

    private int number;
    private String coordiante;

    UdoCoordinateType(int number,String coordiante){
        this.coordiante = coordiante;
        this.number = number;
    }

    public String getCoordiante() {
        return coordiante;
    }
    public int getNumber(){
        return number;
    }

    public static UdoCoordinateType valueOfNumber(int number) {
        return Arrays.stream(values())
                .filter(value -> value.number == number)
                .findAny()
                .orElse(null);
    }
}
