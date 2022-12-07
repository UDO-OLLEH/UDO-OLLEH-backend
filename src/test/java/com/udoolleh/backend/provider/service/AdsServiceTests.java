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

import java.io.IOException;
import java.util.Optional;

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
}
