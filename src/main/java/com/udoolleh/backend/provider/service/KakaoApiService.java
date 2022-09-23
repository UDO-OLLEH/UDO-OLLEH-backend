package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.KakaoApiServiceInterface;
import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.core.type.UdoCoordinateType;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@PropertySource("classpath:/secrets/kakao-api-secrets.properties")
@Service
@RequiredArgsConstructor
public class KakaoApiService implements KakaoApiServiceInterface {
    @Value("${kakao.restapi.key}")
    private String kakaoRestApiKey;
    private String baseUrl = "https://dapi.kakao.com";

    private final RestaurantRepository restaurantRepository;

    //레스토랑 등록을 위한 카카오 오픈 api서비스와 자동 레스토랑 등록

    @Transactional
    @Override
    public void callKakaoApi(String query, int page, PlaceType placeType, UdoCoordinateType coordinateType) {      //카카오 api를 키워드로 호출하고 해당 page값을 String으로 반환
        int pageParam = page;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("query", query);
        params.add("category_group_code", placeType.getPlaceCode());       //음식점 FD6, 카페 CE7
        params.add("rect", coordinateType.getCoordiante());      //x : 경도, y : 위도

        Mono<String> mono = WebClient.builder()
                .baseUrl(baseUrl)
                .build().get()
                .uri(uriBuilder -> uriBuilder.path("/v2/local/search/keyword.json")
                        .queryParams(params)
                        .queryParam("page", pageParam)
                        .build())
                .header("Authorization", "KakaoAK " + kakaoRestApiKey)
                .retrieve()
                .bodyToMono(String.class);
        boolean finish = registerRestaurant(mono.block(), page, placeType, query);

        if (finish) {     //is_end가 true이면 다음 사분면을 호출(기저 조건)
            int number = coordinateType.getNumber() + 1;
            coordinateType = UdoCoordinateType.valueOfNumber(number);
            page = 0;
            if (coordinateType == null) { //좌표타입이 4사분면 이후는 없기 때문에 null반환, 기저조건
                return;
            }
        }
            //끝 페이지가 아니라면 다음 페이지 호출
            callKakaoApi(query, page + 1, placeType, coordinateType);
    }


    @Override
    @Transactional
    public boolean registerRestaurant(String kakaoApiResponse, int page, PlaceType placeType,String query){   //필요한 값들을 Dto에 넣기
        JSONObject jsonMeta = new JSONObject();
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(kakaoApiResponse);
            // 장소 정보들(documents)과 메타 데이터를 따로 받아온다.
            jsonMeta = (JSONObject) jsonObject.get("meta");
            JSONArray jsonDocuments = (JSONArray) jsonObject.get("documents");

            for (int i = 0; i < jsonDocuments.size(); i++) {        //해당 가게들 리스트를
                JSONObject document = (JSONObject) jsonDocuments.get(i);
                Restaurant restaurant = Restaurant.builder()
                        .name(document.get("place_name").toString())
                        .placeType(placeType)
                        .category((String) document.get("category_name")) //상의 필요
                        .address(document.get("road_address_name").toString())
                        .xCoordinate(document.get("x").toString())
                        .yCoordinate(document.get("y").toString())
                        .build();
                        restaurantRepository.save(restaurant);
            }
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        return (boolean)jsonMeta.get("is_end");
    }
}
