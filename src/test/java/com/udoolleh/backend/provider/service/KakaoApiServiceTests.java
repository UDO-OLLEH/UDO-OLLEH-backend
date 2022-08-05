package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.core.type.ShipCourseType;
import com.udoolleh.backend.core.type.UdoCoordinateType;
import com.udoolleh.backend.entity.Wharf;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class KakaoApiServiceTests {
    @Autowired
    KakaoApiService kakaoApiService;


    @DisplayName("카카오 지역 검색 api 테스트")
    @Transactional
    @Test
    void callKakaoApiTest() throws ParseException {
        kakaoApiService.callKakaoApi("우도",1,PlaceType.RESTAURANT, UdoCoordinateType.ONE_QUADRANT);

    }
}
