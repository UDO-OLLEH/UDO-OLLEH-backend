package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.Gps;
import com.udoolleh.backend.entity.TravelPlace;
import com.udoolleh.backend.repository.GpsRepository;
import com.udoolleh.backend.repository.TravelPlaceRepository;
import com.udoolleh.backend.web.dto.RequestPlace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("test")
public class TravelPlaceServiceTests {

    @Autowired
    private TravelPlaceService travelPlaceService;
    @Autowired
    private TravelPlaceRepository travelPlaceRepository;
    @Autowired
    private GpsRepository gpsRepository;


    @Test
    @DisplayName("추천 여행지 등록 테스트(성공)")
    void registerPlaceTest() {

        RequestPlace.RegisterPlaceDto registerDto = RequestPlace.RegisterPlaceDto.builder()
                .placeName("우도")
                .intro("첫 여행지")
                .context("최고의 여행지 선정")
                .build();

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "tt.png",
                "image/png", "test".getBytes());

        travelPlaceService.registerPlace(mockMultipartFile, registerDto);

        assertNotNull(travelPlaceRepository.findByPlaceName(registerDto.getPlaceName()));
        assertThat(registerDto.getPlaceName(), equals("우도"));

    }
}
