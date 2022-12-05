package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestAds;
import org.springframework.web.multipart.MultipartFile;

public interface AdsServiceInterface {
    void registerAds(MultipartFile file, RequestAds.RegisterAdsDto requestDto);
}
