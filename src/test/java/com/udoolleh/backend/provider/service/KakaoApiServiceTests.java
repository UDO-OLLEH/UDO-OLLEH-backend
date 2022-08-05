package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.core.type.UdoCoordinateType;
import com.udoolleh.backend.repository.RestaurantRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PropertySource("classpath:/secrets/kakao-api-secrets.properties")
@SpringBootTest
@ActiveProfiles("test")
public class KakaoApiServiceTests {
    @Value("${kakao.restapi.key}")
    private String kakaoRestApiKey;
    private String baseUrl = "https://dapi.kakao.com";

    @Autowired
    private KakaoApiService kakaoApiService;
    @Autowired
    private RestaurantRepository restaurantRepository;


    @Test
    @DisplayName("카카오 지역 검색 api 테스트 및 저장 테스트")
    @Transactional
    void callKakaoApiTest() {
        //우도 전체 저장
        kakaoApiService.callKakaoApi("우도", 1, PlaceType.RESTAURANT, UdoCoordinateType.ONE_QUADRANT);
        //api를 쐈을 때 총 검색량
        int totalCount=0;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("query", "우도");
        params.add("category_group_code", PlaceType.RESTAURANT.getPlaceCode());       //음식점 FD6, 카페 CE7

        Mono<String> mono = WebClient.builder()
                .baseUrl(baseUrl)
                .build().get()
                .uri(uriBuilder -> uriBuilder.path("/v2/local/search/keyword.json")
                        .queryParams(params)
                        .queryParam("page", 1)
                        .build())
                .header("Authorization", "KakaoAK " + kakaoRestApiKey)
                .retrieve()
                .bodyToMono(String.class);
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(mono.block());

            JSONObject jsonMeta = (JSONObject) jsonObject.get("meta");
            totalCount = Integer.parseInt(jsonMeta.get("total_count").toString());
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        assertEquals(totalCount, restaurantRepository.findAll().size());

    }
}
