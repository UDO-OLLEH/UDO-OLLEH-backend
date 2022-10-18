package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestPlace;
import com.udoolleh.backend.web.dto.ResponsePlace;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TravelPlaceServiceInterface {

    List<ResponsePlace.PlaceDto> getPlaceList();
    ResponsePlace.PlaceDto getPlaceDetail(Long id);
    void registerPlace(MultipartFile file, RequestPlace.RegisterPlaceDto registerDto);

    void updatePlace(MultipartFile file, Long id, RequestPlace.UpdatePlaceDto updateDto);

    void deletePlace(Long id);
}
