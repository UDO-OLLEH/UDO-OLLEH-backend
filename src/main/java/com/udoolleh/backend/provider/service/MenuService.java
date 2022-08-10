package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.MenuServiceInterface;
import com.udoolleh.backend.entity.Menu;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.exception.errors.MenuDuplicatedException;
import com.udoolleh.backend.exception.errors.NotFoundRestaurantException;
import com.udoolleh.backend.repository.MenuRepository;
import com.udoolleh.backend.repository.RestaurantRepository;
import com.udoolleh.backend.web.dto.RequestMenuDto;
import com.udoolleh.backend.web.dto.ResponseMenuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService implements MenuServiceInterface {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    @Override
    @Transactional
    public void registerMenu(MultipartFile file, RequestMenuDto.register requestDto){
        Restaurant restaurant = restaurantRepository.findById(requestDto.getRestaurantId()).orElseThrow(()-> new NotFoundRestaurantException());
        Menu menu = menuRepository.findByRestaurantAndName(restaurant, requestDto.getName());

        if(menu != null){
            throw new MenuDuplicatedException();
        }

        String photoUrl = "";
            menu = Menu.builder()
                .name(requestDto.getName())
                .photo(photoUrl)
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .build();

        menu = menuRepository.save(menu);
        restaurant.addMenu(menu);
    }

    @Override
    @Transactional
    public List<ResponseMenuDto.getMenu> getMenu(String restaurantId){
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(()-> new NotFoundRestaurantException());

        List<ResponseMenuDto.getMenu> list = new ArrayList<>();
        List<Menu> menuList = restaurant.getMenuList();

        for(Menu item : menuList){
            ResponseMenuDto.getMenu response = ResponseMenuDto.getMenu.builder()
                    .name(item.getName())
                    .photo(item.getPhoto())
                    .price(item.getPrice())
                    .description(item.getDescription())
                    .build();
            list.add(response);
        }
        return list;
    }

}
