package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.TravelPlace;
import com.udoolleh.backend.repository.GpsRepository;
import com.udoolleh.backend.repository.TravelPlaceRepository;
import com.udoolleh.backend.web.dto.RequestPlace;
import com.udoolleh.backend.web.dto.ResponsePlace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
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

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test1.png",
                "image/png", "test".getBytes());

        travelPlaceService.registerPlace(mockMultipartFile, registerDto);

        assertNotNull(travelPlaceRepository.findByPlaceName(registerDto.getPlaceName()));
        assertEquals(registerDto.getPlaceName(), "우도");
    }

    @Test
    @DisplayName("추천 여행지 조회 기능 테스트(성공)")
    void getPlaceListTest() {

        List<TravelPlace> travelPlaces = travelPlaceRepository.findAll();
        List<ResponsePlace.PlaceDto> getPlaces = travelPlaceService.getPlaceList();

        assertNotNull(travelPlaces);
        assertNotNull(getPlaces);

        for (ResponsePlace.PlaceDto place : getPlaces) {
            System.out.println(place.getPlaceName() + place.getIntro() + place.getContext());
        }
    }

    @Test
    @DisplayName("추천 여행지 상세 조회 기능 테스트(성공)")
    void getPlaceDetailTest() {
        RequestPlace.RegisterPlaceDto registerDto = RequestPlace.RegisterPlaceDto.builder()
                .placeName("마라도")
                .intro("첫 여행지")
                .context("최고의 여행지 선정")
                .build();

        travelPlaceService.registerPlace(null, registerDto);

        ResponsePlace.PlaceDto travelPlace = travelPlaceService.getPlaceDetail(2L);

        assertNotNull(travelPlace);
        assertThat(registerDto.getPlaceName(), equalTo(travelPlace.getPlaceName()));
    }
}


