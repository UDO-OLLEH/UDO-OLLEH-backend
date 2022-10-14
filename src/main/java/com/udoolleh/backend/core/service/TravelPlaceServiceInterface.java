package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestPlace;
import com.udoolleh.backend.web.dto.ResponsePlace;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TravelPlaceServiceInterface {

    List<ResponsePlace.ListPlaceDto> getPlaceList();
    void registerPlace(MultipartFile file, RequestPlace.RegisterPlaceDto registerDto);

    void updatePlace(MultipartFile file, Long id, RequestPlace.UpdatePlaceDto updateDto);

    void deletePlace(Long id);
}
