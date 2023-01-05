package com.udoolleh.backend.web;

import com.amazonaws.Response;
import com.udoolleh.backend.exception.errors.CustomJwtRuntimeException;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.AdminAuthenticationService;
import com.udoolleh.backend.provider.service.MenuService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestMenu;
import com.udoolleh.backend.web.dto.ResponseMenu;
import com.udoolleh.backend.web.dto.*;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
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

import org.springframework.data.domain.Page;
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
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final AdminAuthenticationService adminAuthenticationService;

    @PostMapping("/restaurant/menu")
    public ResponseEntity<CommonResponse> registerMenu(HttpServletRequest request, @RequestPart MultipartFile file,
                                                       @Valid @RequestPart RequestMenu.RegisterMenuDto requestDto){
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }
        menuService.registerMenu(file, requestDto);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("메뉴 등록 성공")
                .build());
    }
    @GetMapping("/restaurant/{id}/menu")
    public ResponseEntity<CommonResponse> getMenu(HttpServletRequest request, @PathVariable String id){
        List<ResponseMenu.MenuDto> list = menuService.getMenu(id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("메뉴 조회 성공")
                .list(list)
                .build());
    }

    @DeleteMapping("/restaurant/{id}/menu/{name}")  //수정 필요 name -> id
    public ResponseEntity<CommonResponse> deleteMenu(HttpServletRequest request, @PathVariable String id, @PathVariable String name){
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }

        menuService.deleteMenu(id, name);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("메뉴 삭제 성공")
                .build());
    }

    @PostMapping("/kakao/restaurant")
    public ResponseEntity<CommonResponse> registerRestaurantInfo(HttpServletRequest request, @RequestBody Map<String, PlaceType> placeType){
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }

        kakaoApiService.callKakaoApi("우도",1,placeType.get("placeType"),UdoCoordinateType.ONE_QUADRANT); //1사분면 저장

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("카카오 맛집 등록 성공")
                .build());
    }

    @PostMapping("/restaurant/images")
    public ResponseEntity<CommonResponse> registerRestaurantImage(HttpServletRequest request, @RequestPart(value="images") List<MultipartFile> images,
                                                                  @RequestPart(value="restaurantName") Map<String, String> restaurantName){
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }

        restaurantService.registerRestaurantImage(images, restaurantName.get("restaurantName"));

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("맛집 사진 등록 성공")
                .build());
    }

    @PostMapping("/restaurant")
        public ResponseEntity<CommonResponse> registerRestaurant(HttpServletRequest request, @Valid @RequestBody RequestRestaurant.RegisterRestaurantDto registerDto){
            Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
            if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
                throw new CustomJwtRuntimeException();
            }

            restaurantService.registerRestaurant(registerDto);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("이미지를 제외한 맛집 등록 성공")
                .build());
    }

    @GetMapping("/restaurant")
    public ResponseEntity<CommonResponse> getRestaurant(@PageableDefault (size=10, sort="totalGrade", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ResponseRestaurant.RestaurantDto> restaurants = restaurantService.getRestaurant(pageable);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("맛집 전체 조회 성공")
                .list(restaurants)
                .build());
    }

    @DeleteMapping("/restaurant/{id}/images")
    public ResponseEntity<CommonResponse> deleteRestaurantImages(HttpServletRequest request, @PathVariable String id){
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }

        restaurantService.deleteRestaurantImage(id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("맛집 이미지 삭제 성공")
                .build());
    }
}
