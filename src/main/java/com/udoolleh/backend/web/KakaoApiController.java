package com.udoolleh.backend.web;

import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.core.type.ShipCourseType;
import com.udoolleh.backend.core.type.ShipTimetableType;
import com.udoolleh.backend.core.type.UdoCoordinateType;
import com.udoolleh.backend.provider.service.KakaoApiService;
import com.udoolleh.backend.provider.service.ShipService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.ResponseWharfTimetable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequiredArgsConstructor
public class KakaoApiController {
    private final KakaoApiService kakaoApiService;

    @PostMapping("/admin/restaurant/place/{place}")
    public ResponseEntity<CommonResponse> getRestaurantInfo(@PathVariable("place") PlaceType place){
        kakaoApiService.callKakaoApi("우도",1,place,UdoCoordinateType.ONE_QUADRANT); //1사분면 저장
        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("카카오 맛집 등록 성공")
                .build(), HttpStatus.OK);
    }



}
