package com.udoolleh.backend.core.service;

import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.core.type.UdoCoordinateType;
import org.json.simple.parser.ParseException;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface KakaoApiServiceInterface {
    boolean saveLocalInfo(String response, int page, String query) throws ParseException;;
    void callKakaoApi(String query, int page,PlaceType placeType, UdoCoordinateType coordinateType) throws ParseException;
//    void saveUdoLocalInfo(UdoCoordinateType coordinateType, PlaceType placeType);
}
