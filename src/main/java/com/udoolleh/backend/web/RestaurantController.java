package com.udoolleh.backend.web;

import com.udoolleh.backend.provider.service.MenuService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestMenuDto;
import com.udoolleh.backend.web.dto.ResponseMenuDto;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/restaurant/menu")
    public ResponseEntity<CommonResponse> registerMenu(@RequestPart MultipartFile file,
                                                       @Valid @RequestPart RequestMenuDto.register requestDto){
        menuService.registerMenu(file, requestDto);
        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("메뉴 등록 성공")
                .build(), HttpStatus.OK);
    }
    @GetMapping("/restaurant/{restaurantId}/menu")
    public ResponseEntity<CommonResponse> getMenu(@PathVariable String restaurantId){
        List<ResponseMenuDto.getMenu> list = menuService.getMenu(restaurantId);

        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("메뉴 조회 성공")
                .list(list)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/restaurant/{restaurantId}/{menu}")
    public ResponseEntity<CommonResponse> deleteMenu(@PathVariable String restaurantId, @PathVariable String menu){
        menuService.deleteMenu(restaurantId, menu);

        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("메뉴 삭제 성공")
                .build(), HttpStatus.OK);

    }

}
