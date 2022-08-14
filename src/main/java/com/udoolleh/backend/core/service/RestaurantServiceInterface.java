package com.udoolleh.backend.core.service;

import org.springframework.web.multipart.MultipartFile;

public interface RestaurantServiceInterface {
    void registerRestaurantImage(MultipartFile multipartFile,String restaurantName);
}
