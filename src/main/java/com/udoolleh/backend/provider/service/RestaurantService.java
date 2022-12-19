package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.RestaurantServiceInterface;
import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.entity.Photo;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.exception.errors.NotFoundPhotoException;
import com.udoolleh.backend.exception.errors.NotFoundRestaurantException;
import com.udoolleh.backend.exception.errors.RestaurantDuplicatedException;
import com.udoolleh.backend.repository.PhotoRepository;
import com.udoolleh.backend.repository.RestaurantRepository;
import com.udoolleh.backend.web.dto.RequestRestaurant;
import com.udoolleh.backend.web.dto.ResponseRestaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService implements RestaurantServiceInterface {
    @Value("${cloud.aws.s3.bucket.url}")
    private String s3BucketUrl;
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
    public void registerRestaurant(RequestRestaurant.RegisterRestaurantDto restaurantDto) {
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

    //식당 이미지 전체 삭제
    @Override
    @Transactional
    public void deleteRestaurantImage(String id){
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new NotFoundRestaurantException());
        List<Photo> photoList = photoRepository.findByRestaurant(restaurant);
        for(Photo photo : photoList){
            s3Service.deleteFile(photo.getUrl().substring(s3BucketUrl.length()+1));
            restaurant.getPhotoList().remove(photo);
        }
        restaurantRepository.flush();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResponseRestaurant.RestaurantDto> getRestaurant(Pageable pageable){
        List<ResponseRestaurant.RestaurantDto> restaurantLists = new ArrayList<>();
        Page<Restaurant> restaurants = restaurantRepository.findAll(pageable);

        if(restaurants == null){
            throw new NotFoundRestaurantException();
        }
        return restaurants.map(ResponseRestaurant.RestaurantDto::of);
    }

}
