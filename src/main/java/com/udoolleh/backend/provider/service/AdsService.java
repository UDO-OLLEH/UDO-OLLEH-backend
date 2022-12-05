package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.AdsServiceInterface;
import com.udoolleh.backend.entity.Ads;
import com.udoolleh.backend.repository.AdsRepository;
import com.udoolleh.backend.web.dto.RequestAds;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdsService implements AdsServiceInterface {
    @Autowired
    private S3Service s3Service;
    @Autowired
    private AdsRepository adsRepository;

    @Override
    @Transactional
    public void registerAds(MultipartFile file, RequestAds.RegisterAdsDto requestDto) {
        Ads ads = Ads.builder()
                .context(requestDto.getContext())
                .build();
        adsRepository.save(ads);

        if (Optional.ofNullable(file).isPresent()) {
            String url = "";
            try {
                url = s3Service.upload(file, "ads");
                ads.updatePhoto(url);
            } catch (IOException e) {
                System.out.println("s3 등록 실패");
            }
        }
    }
}
