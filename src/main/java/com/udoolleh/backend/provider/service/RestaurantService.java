package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.RestaurantServiceInterface;
import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.entity.Photo;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.exception.errors.NotFoundRestaurantException;
import com.udoolleh.backend.exception.errors.RestaurantDuplicatedException;
import com.udoolleh.backend.repository.PhotoRepository;
import com.udoolleh.backend.repository.RestaurantRepository;
import com.udoolleh.backend.web.dto.RequestRestaurant;
import com.udoolleh.backend.web.dto.ResponseRestaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService implements RestaurantServiceInterface {
    private final RestaurantRepository restaurantRepository;
    private final PhotoRepository photoRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public void registerRestaurantImage(List<MultipartFile> multipartFiles, String restaurantName) {
        Restaurant restaurant = restaurantRepository.findByName(restaurantName);
        if (restaurant == null) {
            throw new NotFoundRestaurantException();
        }
        String imageUrl = "";
        try {
            for (MultipartFile file : multipartFiles) {
                imageUrl = s3Service.upload(file, "restaurant");
                Photo photo = Photo.builder()
                        .url(imageUrl)
                        .restaurant(restaurant)
                        .build();
                photoRepository.save(photo);
                restaurant.addPhoto(photo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void registerRestaurant(RequestRestaurant.registerDto restaurantDto) {
        Restaurant restaurant = restaurantRepository.findByName(restaurantDto.getName());
        if (restaurant != null) {
            throw new RestaurantDuplicatedException();
        }
        restaurant = Restaurant.builder()
                .name(restaurantDto.getName())
                .placeType(restaurantDto.getPlaceType())
                .category(restaurantDto.getCategory())
                .address(restaurantDto.getAddress())
                .totalGrade(0.0)
                .build();
        restaurantRepository.save(restaurant);
    }

    @Override
    @Transactional
    public List<ResponseRestaurant.restaurantDto> getRestaurant(Pageable pageable){
        List<ResponseRestaurant.restaurantDto> restaurantLists = new ArrayList<>();
        Page<Restaurant> restaurants = restaurantRepository.findAll(pageable);

        if(restaurants == null){
            throw new NotFoundRestaurantException();
        }
        for(Restaurant item : restaurants){
            List<Photo> photoList = photoRepository.findByRestaurant(item);
            List<String> imageUrlList = new ArrayList<>();
            for(Photo photo : photoList) {
                imageUrlList.add(photo.getUrl());
            }
            ResponseRestaurant.restaurantDto restaurantDto = ResponseRestaurant.restaurantDto.builder()
                    .address(item.getAddress())
                    .totalGrade(item.getTotalGrade())
                    .name(item.getName())
                    .category(item.getCategory())
                    .imagesUrl(imageUrlList)
                    .build();
            restaurantLists.add(restaurantDto);
        }
        return restaurantLists;
    }

}
