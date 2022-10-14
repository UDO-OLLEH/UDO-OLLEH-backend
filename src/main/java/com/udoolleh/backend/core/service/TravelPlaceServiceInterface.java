package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestPlace;
import org.springframework.web.multipart.MultipartFile;

public interface TravelPlaceServiceInterface {
    void registerPlace(MultipartFile file, RequestPlace.RegisterPlaceDto registerDto);

    void updatePlace(MultipartFile file, Long id, RequestPlace.UpdatePlaceDto updateDto);

    void deletePlace(Long id);
}
