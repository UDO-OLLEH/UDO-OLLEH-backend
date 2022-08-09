package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.MenuServiceInterface;
import com.udoolleh.backend.entity.Menu;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.exception.errors.NotFoundRestaurantException;
import com.udoolleh.backend.repository.MenuRepository;
import com.udoolleh.backend.repository.RestaurantRepository;
import com.udoolleh.backend.web.dto.RequestMenuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MenuService implements MenuServiceInterface {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    @Override
    @Transactional
    public void registerMenu(MultipartFile file, RequestMenuDto.register requestDto){
        Restaurant restaurant = restaurantRepository.findById(requestDto.getRestaurantId()).orElseThrow(()-> new NotFoundRestaurantException());

        String photoUrl = "";
        Menu menu = Menu.builder()
                .name(requestDto.getName())
                .photo(photoUrl)
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .build();

        menu = menuRepository.save(menu);
        restaurant.addMenu(menu);
    }


}
