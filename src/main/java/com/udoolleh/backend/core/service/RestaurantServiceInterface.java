package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestRestaurant;
import com.udoolleh.backend.web.dto.ResponseRestaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RestaurantServiceInterface {
    void registerRestaurantImage(List<MultipartFile> multipartFile, String restaurantName);
    void registerRestaurant(RequestRestaurant.RegisterRestaurantDto restaurantDto);
    void deleteRestaurantImage(String id);
    Page<ResponseRestaurant.RestaurantDto> getRestaurant(Pageable pageable);
}
