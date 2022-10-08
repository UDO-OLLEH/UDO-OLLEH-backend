package com.udoolleh.backend.web;

import com.amazonaws.Response;
import com.udoolleh.backend.provider.service.MenuService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestMenu;
import com.udoolleh.backend.web.dto.ResponseMenu;
import com.udoolleh.backend.web.dto.*;

import lombok.RequiredArgsConstructor;
import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.core.type.UdoCoordinateType;
import com.udoolleh.backend.provider.service.KakaoApiService;
import com.udoolleh.backend.provider.service.RestaurantService;

import com.udoolleh.backend.provider.service.S3Service;
import com.udoolleh.backend.provider.service.ShipService;
import com.udoolleh.backend.web.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import javax.validation.constraints.Min;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RestaurantController {
    private final MenuService menuService;
    private final KakaoApiService kakaoApiService;
    private final RestaurantService restaurantService;

    @PostMapping("/admin/restaurant/menu")
    public ResponseEntity<CommonResponse> registerMenu(@RequestPart MultipartFile file,
                                                       @Valid @RequestPart RequestMenu.registerDto requestDto){
        menuService.registerMenu(file, requestDto);
        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("메뉴 등록 성공")
                .build(), HttpStatus.OK);
    }
    @GetMapping("/restaurant/{name}/menu")
    public ResponseEntity<CommonResponse> getMenu(@PathVariable String name){
        List<ResponseMenu.getMenuDto> list = menuService.getMenu(name);

        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("메뉴 조회 성공")
                .list(list)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/admin/restaurant/{id}/menu/{name}")
    public ResponseEntity<CommonResponse> deleteMenu(@PathVariable String id, @PathVariable String name){

        menuService.deleteMenu(id, name);

        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("메뉴 삭제 성공")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/admin/kakao/restaurant")
    public ResponseEntity<CommonResponse> registerRestaurantInfo(@RequestBody Map<String, PlaceType> placeType){
        kakaoApiService.callKakaoApi("우도",1,placeType.get("placeType"),UdoCoordinateType.ONE_QUADRANT); //1사분면 저장
        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("카카오 맛집 등록 성공")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/admin/restaurant/images")
    public ResponseEntity<CommonResponse> registerRestaurantImage(@RequestPart(value="images") List<MultipartFile> images, @RequestPart(value="restaurantName") Map<String, String> restaurantName){
        restaurantService.registerRestaurantImage(images, restaurantName.get("restaurantName"));
        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("맛집 사진 등록 성공")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/admin/restaurant")
    public ResponseEntity<CommonResponse> registerRestaurant(@RequestBody RequestRestaurant.registerDto registerDto){
        restaurantService.registerRestaurant(registerDto);
        return new ResponseEntity<>(CommonResponse.builder()
                .message("이미지를 제외한 맛집 등록 성공")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/restaurant")
    public ResponseEntity<CommonResponse> getRestaurant(@PageableDefault (size=10, sort="totalGrade", direction = Sort.Direction.DESC) Pageable pageable) {

        List<ResponseRestaurant.restaurantDto> restaurantList = restaurantService.getRestaurant(pageable);
        return new ResponseEntity<>(CommonResponse.builder()
                .message("맛집 조회 성공")
                .list(restaurantList)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/admin/restaurant/{id}/images")
    public ResponseEntity<CommonResponse> deleteRestaurantImages(@PathVariable String id){
        restaurantService.deleteRestaurantImage(id);
        return new ResponseEntity<>(CommonResponse.builder()
                .message("맛집 이미지 삭제 성공")
                .build(), HttpStatus.OK);
    }
}
