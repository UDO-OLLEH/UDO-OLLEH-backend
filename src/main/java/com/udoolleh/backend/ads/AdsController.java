package com.udoolleh.backend.ads;

import com.udoolleh.backend.ads.dto.ResponseAds;
import com.udoolleh.backend.web.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdsController {

    private final AdsService adsService;

    @PostMapping("/ad")
    public ResponseEntity<CommonResponse> registerAds(@RequestParam("file") MultipartFile file) {
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
    public ResponseEntity<CommonResponse> deleteAd(@PathVariable String id){
        adsService.deleteAds(id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("광고 사진 삭제 성공")
                .build());

    }
}
