package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;


@PropertySource("classpath:/secrets/kakao-api-secrets.properties")
@SpringBootTest
@ActiveProfiles("test")
public class RestaurantServiceTests {
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @DisplayName("식당 사진 등록 테스트")
    @Transactional
    @Test
    void registerRestaurantImages() {
        Restaurant restaurant = Restaurant.builder()
                .name("식당")
                .placeType(PlaceType.RESTAURANT)
                .address("주소")
                .build();
        restaurantRepository.save(restaurant);

        List<MultipartFile> mockMultipartFile = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            MultipartFile file = new MockMultipartFile("file", "test" + i + ".png",
                    "image/png", "test data".getBytes());
            mockMultipartFile.add(file);
        }
        restaurantService.registerRestaurantImage(mockMultipartFile, "식당");
        assertEquals(restaurantRepository.findByName("식당").getPhotoList().size(), 4);
    }
}
