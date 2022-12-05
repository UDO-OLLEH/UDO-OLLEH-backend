package com.udoolleh.backend.web;

import com.udoolleh.backend.provider.service.AdsService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestAds;
import com.udoolleh.backend.web.dto.RequestBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AdsController {

    @Autowired
    private AdsService adsService;

    @PostMapping("/ad")
    public ResponseEntity<CommonResponse> registerAds(HttpServletRequest request, @RequestPart(required = false) MultipartFile file,
                                                      @Valid @RequestPart RequestAds.RegisterAdsDto requestDto) {
        adsService.registerAds(file, requestDto);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("광고 사진 등록 성공")
                .build());
    }
}
