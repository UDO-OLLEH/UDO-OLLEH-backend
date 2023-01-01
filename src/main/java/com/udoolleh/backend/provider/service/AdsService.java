package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.exception.CustomException;
import com.udoolleh.backend.exception.ErrorCode;
import com.udoolleh.backend.repository.AdsRepository;
import com.udoolleh.backend.web.dto.ResponseAds;
import com.udoolleh.backend.core.service.AdsServiceInterface;
import com.udoolleh.backend.entity.Ads;
import com.udoolleh.backend.exception.errors.NotFoundAdsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdsService implements AdsServiceInterface {
    private final S3Service s3Service;
    private final AdsRepository adsRepository;

    @Override
    @Transactional
    public void registerAds(MultipartFile file) {
        String url = "";
        if (Optional.ofNullable(file).isPresent()) {
            try {
                url = s3Service.upload(file, "ads");
            } catch (IOException e) {
                System.out.println("s3 등록 실패");
                throw new CustomException(ErrorCode.REGISTER_FILE_TO_S3_FAILED);
            }
        }
        Ads ads = Ads.builder()
                .photo(url)
                .build();
        adsRepository.save(ads);
    }


    @Override
    @Transactional
    public List<ResponseAds.AdsDto> getAds(){
        List<ResponseAds.AdsDto> list = new ArrayList<>();
        List<Ads> adsList = adsRepository.findAll();

        adsList.stream().forEach(ad -> list.add(ResponseAds.AdsDto.of(ad)));

        return list;
    }

    @Override
    @Transactional
    public void deleteAds(String id){
        Ads ads = adsRepository.findById(id).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_ADS));

        s3Service.deleteFile(ads.getPhoto());
        adsRepository.delete(ads);
    }
}
