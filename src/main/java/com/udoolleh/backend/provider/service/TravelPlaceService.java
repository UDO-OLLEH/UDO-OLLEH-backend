package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.TravelPlaceServiceInterface;
import com.udoolleh.backend.entity.Gps;
import com.udoolleh.backend.entity.TravelPlace;
import com.udoolleh.backend.exception.errors.NotFoundTravelPlaceException;
import com.udoolleh.backend.exception.errors.TravelPlaceDuplicatedException;
import com.udoolleh.backend.repository.GpsRepository;
import com.udoolleh.backend.repository.TravelPlaceRepository;
import com.udoolleh.backend.web.dto.RequestPlace;
import com.udoolleh.backend.web.dto.ResponsePlace;
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
public class TravelPlaceService implements TravelPlaceServiceInterface {

    private final S3Service s3Service;
    private final TravelPlaceRepository travelPlaceRepository;
    private final GpsRepository gpsRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ResponsePlace.PlaceDto> getPlaceList() {

        List<TravelPlace> all = travelPlaceRepository.findAll();
        List<ResponsePlace.PlaceDto> list = new ArrayList<>();
        List<ResponsePlace.GpsDto> gpsList = new ArrayList<>();

        for (TravelPlace item : all) {
            for (Gps gps : item.getGpsList()) {
                gpsList.add(ResponsePlace.GpsDto.of(gps));
            }
            ResponsePlace.PlaceDto dto = ResponsePlace.PlaceDto.builder()
                    .id(item.getId())
                    .placeName(item.getPlaceName())
                    .intro(item.getIntro())
                    .context(item.getContext())
                    .gps(gpsList)
                    .photo(item.getPhoto())
                    .build();
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    @Override
    public ResponsePlace.PlaceDto getPlaceDetail(Long id) {
        TravelPlace travelPlace = travelPlaceRepository.findById(id).orElseThrow(() -> new NotFoundTravelPlaceException());

        return ResponsePlace.PlaceDto.builder()
                .id(travelPlace.getId())
                .placeName(travelPlace.getPlaceName())
                .intro(travelPlace.getIntro())
                .context(travelPlace.getContext())
                .photo(travelPlace.getPhoto())
                .build();
    }

    @Transactional
    @Override
    public void registerPlace(MultipartFile file, RequestPlace.RegisterPlaceDto registerDto) {

        TravelPlace travelPlace = travelPlaceRepository.findByPlaceName(registerDto.getPlaceName());
        if (travelPlace != null) {
            throw new TravelPlaceDuplicatedException();
        }
        travelPlace = TravelPlace.builder()
                .placeName(registerDto.getPlaceName())
                .intro(registerDto.getIntro())
                .context(registerDto.getContext())
                .build();
        travelPlace = travelPlaceRepository.save(travelPlace);

        if (registerDto.getGps() != null) {
            for (RequestPlace.GpsDto item : registerDto.getGps()) {
                Gps gps = Gps.builder()
                        .latitude(item.getLatitude())
                        .longitude(item.getLongitude())
                        .travelPlace(travelPlace)
                        .build();
                gps = gpsRepository.save(gps);
                travelPlace.addGps(gps);
            }
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
    }

    @Transactional
    @Override
    public void updatePlace(MultipartFile file, Long id, RequestPlace.UpdatePlaceDto updateDto) {

        TravelPlace travelPlace = travelPlaceRepository.findById(id).orElseThrow
                (() -> new NotFoundTravelPlaceException());
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
        TravelPlace travelPlace = travelPlaceRepository.findById(id).orElseThrow(() -> new NotFoundTravelPlaceException());

        if (Optional.ofNullable(travelPlace.getPhoto()).isPresent()) {
            s3Service.deleteFile(travelPlace.getPhoto());
        }
        travelPlaceRepository.deleteById(id);
    }

}
