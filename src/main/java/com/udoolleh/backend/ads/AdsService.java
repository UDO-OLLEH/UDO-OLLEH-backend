package com.udoolleh.backend.ads;

import com.udoolleh.backend.ads.dto.ResponseAds;
import com.udoolleh.backend.core.service.AdsServiceInterface;
import com.udoolleh.backend.exception.errors.NotFoundAdsException;
import com.udoolleh.backend.exception.errors.RegisterFileToS3FailedException;
import com.udoolleh.backend.provider.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
                throw new RegisterFileToS3FailedException();
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
        Ads ads = adsRepository.findById(id).orElseThrow(()-> new NotFoundAdsException());

        s3Service.deleteFile(ads.getPhoto());
        adsRepository.delete(ads);
    }
}
