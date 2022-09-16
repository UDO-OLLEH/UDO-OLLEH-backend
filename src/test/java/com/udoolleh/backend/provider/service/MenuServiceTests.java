package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.Menu;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.exception.errors.NotFoundMenuException;
import com.udoolleh.backend.exception.errors.NotFoundRestaurantException;
import com.udoolleh.backend.repository.MenuRepository;
import com.udoolleh.backend.repository.RestaurantRepository;
import com.udoolleh.backend.web.dto.RequestMenu;
import com.udoolleh.backend.web.dto.ResponseMenu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("메뉴 등록 테스트(성공 - 사진 파일이 없을 경우)")
    @Transactional
    void registerMenuTestWhenNotExistPhoto(){
        Restaurant restaurant = Restaurant.builder()
                .name("음식점")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        RequestMenu.registerDto requestDto = RequestMenu.registerDto.builder()
                .restaurantName(restaurant.getName())
                .name("메뉴 이름")
                .description("설명")
                .price(2000)
                .build();

        menuService.registerMenu(null, requestDto);

        restaurant = restaurantRepository.findById(restaurant.getId()).orElseThrow(()-> new NotFoundRestaurantException());
        assertNotNull(restaurant.getMenuList());
        assertNull(menuRepository.findByRestaurantAndName(restaurant, "메뉴 이름").getPhoto());
    }

    @Test
    @DisplayName("메뉴 등록 테스트(성공 - 사진 파일이 있을 경우)")
    @Transactional
    void registerMenuTestWhenExistPhoto(){
        Restaurant restaurant = Restaurant.builder()
                .name("음식점")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        RequestMenu.registerDto requestDto = RequestMenu.registerDto.builder()
                .restaurantName(restaurant.getName())
                .name("메뉴 이름")
                .description("설명")
                .price(2000)
                .build();

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.png",
                "image/png", "test data".getBytes());


        menuService.registerMenu(mockMultipartFile, requestDto);

        restaurant = restaurantRepository.findById(restaurant.getId()).orElseThrow(()-> new NotFoundRestaurantException());
        assertNotNull(restaurant.getMenuList());
        assertNotNull(menuRepository.findByRestaurantAndName(restaurant, "메뉴 이름").getPhoto());
    }

    @Test
    @DisplayName("메뉴 조회 테스트(성공)")
    @Transactional
    void getMenuTest(){
        Restaurant restaurant = Restaurant.builder()
                .name("음식점")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        Menu menu = Menu.builder()
                .name("메뉴 이름")
                .price(2000)
                .restaurant(restaurant)
                .build();
        menu = menuRepository.save(menu);
        restaurant.addMenu(menu);
        menu.updatePhoto("사진 url");

        //메뉴 조회
        List<ResponseMenu.getMenuDto> result = menuService.getMenu(restaurant.getId());
        assertNotNull(result);
    }

    @Test
    @DisplayName("메뉴 조회 테스트(성공 - 사진이 없을 경우)")
    @Transactional
    void getMenuTestWhenNotExistPhoto(){
        Restaurant restaurant = Restaurant.builder()
                .name("음식점")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        Menu menu = Menu.builder()
                .name("메뉴 이름")
                .price(2000)
                .restaurant(restaurant)
                .build();
        menu = menuRepository.save(menu);
        restaurant.addMenu(menu);

        //메뉴 조회
        List<ResponseMenu.getMenuDto> result = menuService.getMenu(restaurant.getId());
        assertNotNull(result);
    }

    @Test
    @DisplayName("메뉴 삭제 테스트(성공)")
    @Transactional
    void deleteMenuTest(){
        Restaurant restaurant = Restaurant.builder()
                .name("음식점")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        Menu menu = Menu.builder()
                .name("메뉴 이름")
                .price(2000)
                .restaurant(restaurant)
                .build();
        menu = menuRepository.save(menu);
        restaurant.addMenu(menu);

        //메뉴 삭제
        menuService.deleteMenu(restaurant.getId(), "메뉴 이름");
        assertNull(menuRepository.findByRestaurantAndName(restaurant, "메뉴 이름"));
    }

    @Test
    @DisplayName("메뉴 삭제 테스트(실패 - 메뉴 이름이 잘못됐을 경우)")
    @Transactional
    void deleteMenuTestWhenWrongMenuName(){
        Restaurant restaurant = Restaurant.builder()
                .name("음식점")
                .build();
        Restaurant restaurant1 = restaurantRepository.save(restaurant);

        Menu menu = Menu.builder()
                .name("메뉴 이름")
                .price(2000)
                .restaurant(restaurant)
                .build();
        menu = menuRepository.save(menu);
        restaurant.addMenu(menu);

        //메뉴 삭제
        assertThrows(NotFoundMenuException.class, ()-> menuService.deleteMenu(restaurant1.getId(), "잘못된 메뉴 이름"));
    }
}
