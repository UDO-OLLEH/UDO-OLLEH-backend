package com.udoolleh.backend.web;

import com.udoolleh.backend.provider.service.TravelPlaceService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestPlace;
import com.udoolleh.backend.web.dto.ResponsePlace;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaceController {
    private final TravelPlaceService travelPlaceService;

    @GetMapping("/place")
    public ResponseEntity<CommonResponse> getPlaceList() {
        List<ResponsePlace.PlaceDto> response = travelPlaceService.getPlaceList();
        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("추천 관광지 조회 성공")
                .list(response)
                .build());
    }

    @GetMapping("/place/{id}")
    public ResponseEntity<CommonResponse> getPlaceDetail(@Valid @PathVariable Long id) {
        ResponsePlace.PlaceDto response = travelPlaceService.getPlaceDetail(id);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("추천 관광지 상세 조회 성공")
                .list(response)
                .build());
    }

    @PostMapping("/place")
    public ResponseEntity<CommonResponse> registerPlace(@RequestPart MultipartFile file, @RequestPart RequestPlace.RegisterPlaceDto requestDto) {
        travelPlaceService.registerPlace(file, requestDto);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("추천 관광지 등록 성공")
                .build());
    }

    @PutMapping("/place/{id}")
    public ResponseEntity<CommonResponse> updatePlace(@RequestPart MultipartFile file, @Valid @PathVariable Long id, @RequestPart RequestPlace.UpdatePlaceDto requestDto) {
        travelPlaceService.updatePlace(file, id, requestDto);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("추천 관광지 수정 성공")
                .build());
    }

    @DeleteMapping("/place/{id}")
    public ResponseEntity<CommonResponse> deletePlace(@PathVariable Long id) {
        travelPlaceService.deletePlace(id);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("추천 관광지 삭제 성공")
                .build());
    }
}
