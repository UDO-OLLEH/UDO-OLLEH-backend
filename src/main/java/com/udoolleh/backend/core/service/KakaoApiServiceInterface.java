package com.udoolleh.backend.core.service;

import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.core.type.UdoCoordinateType;
import org.json.simple.parser.ParseException;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface KakaoApiServiceInterface {
    boolean registerRestaurant(String response, int page, PlaceType placeType, String query);
    void callKakaoApi(String query, int page,PlaceType placeType, UdoCoordinateType coordinateType);
}
