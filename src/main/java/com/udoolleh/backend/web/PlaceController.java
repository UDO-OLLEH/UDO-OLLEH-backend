package com.udoolleh.backend.web;

import com.udoolleh.backend.exception.errors.CustomJwtRuntimeException;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.AdminAuthenticationService;
import com.udoolleh.backend.provider.service.TravelPlaceService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestPlace;
import com.udoolleh.backend.web.dto.ResponsePlace;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
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
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final AdminAuthenticationService adminAuthenticationService;

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
    public ResponseEntity<CommonResponse> registerPlace(HttpServletRequest request, @RequestPart MultipartFile file, @RequestPart RequestPlace.RegisterPlaceDto requestDto) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }

        travelPlaceService.registerPlace(file, requestDto);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("추천 관광지 등록 성공")
                .build());
    }

    @PostMapping("/place/{id}")
    public ResponseEntity<CommonResponse> updatePlace(HttpServletRequest request, @RequestPart MultipartFile file, @Valid @PathVariable Long id, @RequestPart RequestPlace.UpdatePlaceDto requestDto) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }

        travelPlaceService.updatePlace(file, id, requestDto);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("추천 관광지 수정 성공")
                .build());
    }

    @DeleteMapping("/place/{id}")
    public ResponseEntity<CommonResponse> deletePlace(HttpServletRequest request, @PathVariable Long id) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }

        travelPlaceService.deletePlace(id);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("추천 관광지 삭제 성공")
                .build());
    }
}
