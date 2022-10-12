package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.MenuServiceInterface;
import com.udoolleh.backend.entity.Menu;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.exception.errors.MenuDuplicatedException;
import com.udoolleh.backend.exception.errors.NotFoundMenuException;
import com.udoolleh.backend.exception.errors.NotFoundRestaurantException;
import com.udoolleh.backend.repository.MenuRepository;
import com.udoolleh.backend.repository.RestaurantRepository;
import com.udoolleh.backend.web.dto.RequestMenu;
import com.udoolleh.backend.web.dto.ResponseMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService implements MenuServiceInterface {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public void registerMenu(MultipartFile file, RequestMenu.RegisterMenuDto requestDto){
        Restaurant restaurant = restaurantRepository.findByName(requestDto.getRestaurantName());
        if(restaurant == null){
            throw new NotFoundRestaurantException();
        }
        Menu menu = menuRepository.findByRestaurantAndName(restaurant, requestDto.getName());

        if(menu != null){
            throw new MenuDuplicatedException();
        }

            menu = Menu.builder()
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .restaurant(restaurant)
                    .build();

        menu = menuRepository.save(menu);
        restaurant.addMenu(menu);

        //사진이 있으면 등록
        if(Optional.ofNullable(file).isPresent()){
            String url= "";
            try{
                url = s3Service.upload(file, "menu");
                menu.updatePhoto(url);
            }catch (IOException e){
                System.out.println("s3 등록 실패");
            }
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseMenu.MenuDto> getMenu(String restaurantName){
        Restaurant restaurant = restaurantRepository.findByName(restaurantName);
        if(restaurant == null){
            throw new NotFoundRestaurantException();
        }
        List<ResponseMenu.MenuDto> list = new ArrayList<>();
        List<Menu> menuList = restaurant.getMenuList();

        for(Menu item : menuList){
            ResponseMenu.MenuDto response = ResponseMenu.MenuDto.builder()
                    .name(item.getName())
                    .photo(item.getPhoto())
                    .price(item.getPrice())
                    .description(item.getDescription())
                    .build();
            list.add(response);
        }
        return list;
    }

    @Override
    @Transactional
    public void deleteMenu(String restaurantId, String menuName) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundRestaurantException());
        Menu menu = menuRepository.findByRestaurantAndName(restaurant, menuName);
        if (menu == null) { //메뉴가 없다면
            throw new NotFoundMenuException();
        }
        if(Optional.ofNullable(menu.getPhoto()).isPresent()){ //사진 파일이 존재한다면
            s3Service.deleteFile(menu.getPhoto());
        }
        restaurant.getMenuList().remove(menu);
        menuRepository.delete(menu);
    }
}
