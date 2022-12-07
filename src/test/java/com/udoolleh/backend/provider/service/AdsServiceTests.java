package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.ads.Ads;
import com.udoolleh.backend.ads.AdsService;
import com.udoolleh.backend.ads.AdsRepository;
import com.udoolleh.backend.exception.errors.NotFoundAdsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        MockMultipartFile mockMultipartfile = new MockMultipartFile("file", "test2.png",
                "image/png", "test data".getBytes());

        adsService.registerAds(mockMultipartfile);

        assertNotNull(adsRepository.findAll());
    }

    @Test
    @DisplayName("광고 사진 조회 기능 테스트(성공)")
    void getAdsTest() {
        Ads ads = Ads.builder()
                .photo("url").build();
        adsRepository.save(ads);

        assertEquals(1, adsService.getAds().size());
    }

    @Test
    @DisplayName("광고 사진 삭제 기능 테스트(실패-해당 광고가 없을 경우)")
    void deleteAdsWhenNotExistAdsTest() {
        assertThrows(NotFoundAdsException.class, () -> adsService.deleteAds("id"));
    }

    @Test
    @DisplayName("광고 사진 삭제 기능 테스트(성공)")
    void deleteAdsTest() {
        Ads ads = Ads.builder()
                .photo("url").build();
        ads = adsRepository.save(ads);

        adsService.deleteAds(ads.getId());
        assertEquals(0, adsRepository.findAll().size());
    }
}
