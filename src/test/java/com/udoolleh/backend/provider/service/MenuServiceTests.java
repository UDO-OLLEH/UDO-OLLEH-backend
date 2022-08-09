package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.exception.errors.NotFoundRestaurantException;
import com.udoolleh.backend.repository.MenuRepository;
import com.udoolleh.backend.repository.RestaurantRepository;
import com.udoolleh.backend.web.dto.RequestMenuDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class MenuServiceTests {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 등록 테스트(성공)")
    @Transactional
    void registerMenuTest(){
        Restaurant restaurant = Restaurant.builder()
                .name("음식점")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        RequestMenuDto.register requestDto = RequestMenuDto.register.builder()
                .restaurantId(restaurant.getId())
                .name("메뉴 이름")
                .photo("")
                .description("설명")
                .price(2000)
                .build();
        menuService.registerMenu(null, requestDto);

        restaurant = restaurantRepository.findById(restaurant.getId()).orElseThrow(()-> new NotFoundRestaurantException());
        assertNotNull(restaurant.getMenuList());

    }
}
