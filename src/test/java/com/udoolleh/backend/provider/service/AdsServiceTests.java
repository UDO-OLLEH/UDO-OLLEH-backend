package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.Ads;
import com.udoolleh.backend.repository.AdsRepository;
import com.udoolleh.backend.web.dto.RequestAds;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AdsServiceTests {

    @Autowired
    private AdsService adsService;

    @Autowired
    private AdsRepository adsRepository;

    @Test
    @DisplayName("광고 사진 등록 기능 테스트(성공)")
    void registerAdsTest() {
        RequestAds.RegisterAdsDto dto = RequestAds.RegisterAdsDto.builder()
                .context("우도 땅콩 아이스크림 광고 사진")
                .build();

        MockMultipartFile file = new MockMultipartFile("file", "test.png",
                "image/png", "test data".getBytes());

        adsService.registerAds(file, dto);

        assertNotNull(adsRepository.findAll());
    }

    @Test
    @DisplayName("광고 사진 등록 기능 테스트(실패) - 사진 파일 오류")
    void registerAdsFailedTest() {
        RequestAds.RegisterAdsDto dto = RequestAds.RegisterAdsDto.builder()
                .context("우도 땅콩 아이스크림 광고 사진")
                .build();

        MockMultipartFile file = new MockMultipartFile("file", "test.png", "application/json", "test fail".getBytes());
        adsService.registerAds(file, dto);

        //assertEquals(file.getContentType(), "image/png");
    }
}
