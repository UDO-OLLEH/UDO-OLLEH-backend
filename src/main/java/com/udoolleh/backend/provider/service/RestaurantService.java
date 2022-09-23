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
        System.out.println(restaurantName);
        Restaurant restaurant = restaurantRepository.findByName(restaurantName);
        System.out.println(restaurant+"======");
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

    //url은 ","로 구분한 url 여러개일 수 있음
    @Override
    @Transactional
    public void deleteRestaurantImageSelection(String name, String[] urls){
        List<String> photoList = new ArrayList<>();
        if (urls.length == 0) {
            throw new NotFoundPhotoException();
        }
        for (int i=0; i < urls.length; i++) {
            if(photoRepository.findByUrl(urls[i]) == null){ //해당 사진이 없으면 예외 던지기
                throw new NotFoundPhotoException();
            }
            photoList.add(urls[i]);
        }
        Restaurant restaurant = restaurantRepository.findByName(name);
        restaurant.getPhotoList().remove(photoList);
        for(int i=0; i < urls.length; i++){
            s3Service.deleteFile(urls[i].substring(s3BucketUrl.length()+1));
        }
        restaurant.getPhotoList().clear();
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
                    .xCoordinate(item.getXCoordinate())
                    .yCoordinate(item.getYCoordinate())
                    .build();
            restaurantLists.add(restaurantDto);
        }
        return restaurantLists;
    }

}
