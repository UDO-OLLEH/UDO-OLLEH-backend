package com.udoolleh.backend.web;

import com.udoolleh.backend.exception.CustomException;
import com.udoolleh.backend.exception.ErrorCode;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.AdminAuthenticationService;
import com.udoolleh.backend.web.dto.ResponseAds;
import com.udoolleh.backend.provider.service.AdsService;
import com.udoolleh.backend.web.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AdsController {

    private final AdsService adsService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final AdminAuthenticationService adminAuthenticationService;

    @PostMapping("/ad")
    public ResponseEntity<CommonResponse> registerAds(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if (!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomException(ErrorCode.AUTHENTICATION_FAILED)))) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }
        adsService.registerAds(file);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("광고 사진 등록 성공")
                .build());
    }

    @GetMapping("/ad")
    public ResponseEntity<CommonResponse> getAds() {
        List<ResponseAds.AdsDto> list = adsService.getAds();
        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("광고 사진 조회 성공")
                .list(list)
                .build());
    }

    @DeleteMapping("/ad/{id}")
    public ResponseEntity<CommonResponse> deleteAd(HttpServletRequest request, @PathVariable String id) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if (!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomException(ErrorCode.AUTHENTICATION_FAILED)))) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }

        adsService.deleteAds(id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("광고 사진 삭제 성공")
                .build());

    }
}
