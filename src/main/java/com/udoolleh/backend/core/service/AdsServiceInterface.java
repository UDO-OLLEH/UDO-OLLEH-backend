package com.udoolleh.backend.core.service;

import com.udoolleh.backend.ads.dto.ResponseAds;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdsServiceInterface {
    void registerAds(MultipartFile file);
    List<ResponseAds.AdsDto> getAds();
    void deleteAds(String id);
}
