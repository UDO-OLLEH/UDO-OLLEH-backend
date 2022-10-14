package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.TravelPlaceServiceInterface;
import com.udoolleh.backend.entity.TravelPlace;
import com.udoolleh.backend.repository.TravelPlaceRepository;
import com.udoolleh.backend.web.dto.RequestPlace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TravelPlaceService implements TravelPlaceServiceInterface {

    private final S3Service s3Service;
    private final TravelPlaceRepository travelPlaceRepository;

    @Transactional
    @Override
    public void registerPlace(MultipartFile file, RequestPlace.RegisterPlaceDto registerDto) {

        TravelPlace travelPlace = travelPlaceRepository.findByPlaceName(registerDto.getPlaceName());
        if (travelPlace != null) {
            throw new RuntimeException();
        }
        travelPlace = TravelPlace.builder()
                .placeName(registerDto.getPlaceName())
                .context(registerDto.getContext())
                .build();
        travelPlaceRepository.save(travelPlace);

        if (Optional.ofNullable(file).isPresent()) {
            String url = "";
            try {
                url = s3Service.upload(file, "travelPlace");
                travelPlace.updatePhoto(url);
            } catch (IOException e) {
                System.out.println("s3 등록 실패");
            }
        }
    }

    @Transactional
    @Override
    public void updatePlace(MultipartFile file, Long id, RequestPlace.UpdatePlaceDto updateDto) {

        TravelPlace travelPlace = travelPlaceRepository.findById(id).orElseThrow
                (() -> new RuntimeException());
        if (Optional.ofNullable(travelPlace.getPhoto()).isPresent()) {
            s3Service.deleteFile(travelPlace.getPhoto());
        }
        if (Optional.ofNullable(file).isPresent()) {
            String url = "";
            try {
                url = s3Service.upload(file, "travelPlace");
                travelPlace.updatePhoto(url);
            } catch (IOException e) {
                System.out.println("s3 등록 실패");
            }
        }
        travelPlace.updatePlace(updateDto.getPlaceName(), updateDto.getContext());
    }

    @Transactional
    @Override
    public void deletePlace(Long id) {
        TravelPlace travelPlace = travelPlaceRepository.findById(id).orElseThrow(() -> new RuntimeException());

        if (Optional.ofNullable(travelPlace.getPhoto()).isPresent()) {
            s3Service.deleteFile(travelPlace.getPhoto());
        }
        travelPlaceRepository.deleteById(id);
    }

}
