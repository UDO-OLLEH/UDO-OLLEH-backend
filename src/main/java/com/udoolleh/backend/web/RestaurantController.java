package com.udoolleh.backend.web;

import com.udoolleh.backend.provider.service.MenuService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestMenu;
import com.udoolleh.backend.web.dto.ResponseMenuDto;
import lombok.RequiredArgsConstructor;
import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.core.type.UdoCoordinateType;
import com.udoolleh.backend.provider.service.KakaoApiService;
import com.udoolleh.backend.provider.service.RestaurantService;
import com.udoolleh.backend.provider.service.S3Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaurantController {
    private final MenuService menuService;
    private final KakaoApiService kakaoApiService;
    private final RestaurantService restaurantService;

    @PostMapping("/restaurant/menu")
    public ResponseEntity<CommonResponse> registerMenu(@RequestPart MultipartFile file,
                                                       @Valid @RequestPart RequestMenu.registerDto requestDto){
        menuService.registerMenu(file, requestDto);
        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("메뉴 등록 성공")
                .build(), HttpStatus.OK);
    }
    @GetMapping("/restaurant/{id}/menu")
    public ResponseEntity<CommonResponse> getMenu(@PathVariable String id){
        List<ResponseMenuDto.getMenu> list = menuService.getMenu(id);

        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("메뉴 조회 성공")
                .list(list)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/restaurant/{id}/menu/{name}")
    public ResponseEntity<CommonResponse> deleteMenu(@PathVariable String id, @PathVariable String name){
        menuService.deleteMenu(id, name);

        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("메뉴 삭제 성공")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/admin/restaurant/place")
    public ResponseEntity<CommonResponse> registerRestaurantInfo(@RequestBody PlaceType place){
        kakaoApiService.callKakaoApi("우도",1,place,UdoCoordinateType.ONE_QUADRANT); //1사분면 저장
        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("카카오 맛집 등록 성공")
                .build(), HttpStatus.OK);
    }
    @PostMapping("/admin/restaurant/images")
    public ResponseEntity<CommonResponse> registerRestaurantImage(@RequestPart(value="images") List<MultipartFile> images, @RequestPart(value="restaurantName") String restaurantName){
        restaurantService.registerRestaurantImage(images, restaurantName);
        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("식당 사진 등록 성공")
                .build(), HttpStatus.OK);
    }
}
