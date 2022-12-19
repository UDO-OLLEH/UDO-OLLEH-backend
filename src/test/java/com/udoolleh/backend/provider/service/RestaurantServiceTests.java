package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.entity.Photo;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.repository.PhotoRepository;
import com.udoolleh.backend.repository.RestaurantRepository;
import com.udoolleh.backend.web.dto.RequestRestaurant;
import com.udoolleh.backend.web.dto.ResponseRestaurant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@PropertySource("classpath:/secrets/kakao-api-secrets.properties")
@SpringBootTest
@ActiveProfiles("test")
public class RestaurantServiceTests {
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private S3Service s3Service;

    @DisplayName("식당 사진 등록 테스트")
    @Transactional
    @Test
    void registerRestaurantImagesTest() {
        //given
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
        //when
        restaurantService.registerRestaurantImage(mockMultipartFile, "식당");
        //then
        assertEquals(restaurantRepository.findByName("식당").getPhotoList().size(), 4);
    }
    @DisplayName("식당 사진 삭제 테스트")
    @Transactional
    @Test
    void deleteRestaurantImageTest() {
        //given
        Restaurant res = Restaurant.builder()
                .name("식당")
                .placeType(PlaceType.RESTAURANT)
                .address("주소")
                .build();
        restaurantRepository.save(res);

        List<MultipartFile> mockMultipartFile = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            MultipartFile file = new MockMultipartFile("file", "test" + i + ".png",
                    "image/png", "test data".getBytes());
            mockMultipartFile.add(file);
        }
        Restaurant restaurant = restaurantRepository.findByName("식당");

        String[] imageUrls = new String[4];
        try {
            for (int i =0; i < mockMultipartFile.size(); i++) {
                imageUrls[i] = s3Service.upload(mockMultipartFile.get(i), "test");
                Photo photo = Photo.builder()
                        .url(imageUrls[i])
                        .restaurant(restaurant)
                        .build();
                photoRepository.save(photo);
                restaurant.addPhoto(photo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(4,restaurant.getPhotoList().size());
        assertEquals(4,photoRepository.findByRestaurant(restaurant).size());
        //when
        restaurantService.deleteRestaurantImage(restaurant.getId());
        //then
        assertEquals(0, restaurantRepository.findByName("식당").getPhotoList().size());
        assertEquals(0, photoRepository.findAll().size());
    }

    @DisplayName("식당 등록 테스트")
    @Transactional
    @Test
    void registerRestaurantTest() {
        //given
        RequestRestaurant.RegisterRestaurantDto registerDto = RequestRestaurant.RegisterRestaurantDto.builder()
                .name("타코집")
                .category("음식점>한식")
                .address("우도 ~~~")
                .placeType(PlaceType.RESTAURANT)
                .build();
        //when
        restaurantService.registerRestaurant(registerDto);
        Restaurant restaurant= restaurantRepository.findByName("타코집");
        //then
        assertNotNull(restaurant);
    }
    @DisplayName("식당 별점기준 내림차순 조회 테스트")
    @Transactional
    @Test
    void getRestaurantTest() {
        //given
        for(int i=0; i<20; i++) {
            Restaurant res = Restaurant.builder()
                    .name("타코집"+i)
                    .category("음식점>한식"+i)
                    .address("우도 ~~~")
                    .placeType(PlaceType.RESTAURANT)
                    .totalGrade(Double.valueOf(i))
                    .build();
            restaurantRepository.save(res);
            Restaurant restaurant = restaurantRepository.findByName("타코집"+i);

            for (int j = 0; j < 4; j++) {
                Photo photo = Photo.builder()
                        .restaurant(restaurant)
                        .url("이미지 url" + j)
                        .build();
                photoRepository.save(photo);
                restaurant.addPhoto(photo);
            }
        }
        Pageable pageable =  PageRequest.of(0, 10, Sort.by("totalGrade").descending());
        //when
        Page<ResponseRestaurant.RestaurantDto> restaurantDtos = restaurantService.getRestaurant(pageable);
        //then
        assertEquals(restaurantDtos.toList().size(),10);  //페이징 처리가 되어 사이즈가 10이 맞는지
    }
}
