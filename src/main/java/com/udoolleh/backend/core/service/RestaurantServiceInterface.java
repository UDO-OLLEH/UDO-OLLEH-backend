package com.udoolleh.backend.core.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RestaurantServiceInterface {
    void registerRestaurantImage(List<MultipartFile> multipartFile, String restaurantName);
}
