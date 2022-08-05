package com.udoolleh.backend.provider.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udoolleh.backend.core.service.KakaoApiServiceInterface;
import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.core.type.UdoCoordinateType;
import com.udoolleh.backend.web.dto.ResponseKakaoApi;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:/secrets/kakao-api-secrets.properties")
@Service
@RequiredArgsConstructor
public class KakaoApiService implements KakaoApiServiceInterface {
    @Value("${kakao.restapi.key}")
    private String kakaoRestApiKey;

    private final String baseUrl = "https://dapi.kakao.com";


    @Override
    public void callKakaoApi(String query, int page, PlaceType placeType, UdoCoordinateType coordinateType){      //카카오 api를 키워드로 호출하고 해당 page값을 String으로 반환
        int pageParam = page;
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("query", query);
        params.add("category_group_code", placeType.getPlaceCode());       //음식점 FD6, 카페 CE7
        params.add("rect",coordinateType.getCoordiante());

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
        int nextPage;
        boolean finish = saveLocalInfo(mono.block(), page, query);
        if(finish) {     //is_end가 true이면 다음 사분면을 호출(기저 조건)
            int number = coordinateType.getNumber()+1;
            coordinateType = UdoCoordinateType.valueOfNumber(number);
            page=0;

            if(coordinateType == null){ //좌표타입이 4사분면 이후는 없기 때문에 null반환, 기저조건
                return;
            }
        }
        //끝 페이지가 아니라면 다음 페이지 호출
        nextPage = page +1;
        callKakaoApi(query, nextPage, placeType, coordinateType);
    }

    @Transactional
    @Override
    public boolean saveLocalInfo(String kakaoApiResponse,int page,String query){   //필요한 값들을 Dto에 넣기
        JSONObject jsonMeta = new JSONObject();
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(kakaoApiResponse);
            // 장소 정보들(documents)과 메타 데이터를 따로 받아온다.
            jsonMeta = (JSONObject) jsonObject.get("meta");
            JSONArray jsonDocuments = (JSONArray) jsonObject.get("documents");

            List<ResponseKakaoApi.PlaceDto> placeList = new ArrayList<>();
            for (int i = 0; i < jsonDocuments.size(); i++) {        //해당 가게들 리스트를
                JSONObject document = (JSONObject) jsonDocuments.get(i);
                ResponseKakaoApi.PlaceDto placeDto = ResponseKakaoApi.PlaceDto.builder()
                        .placeName((String) document.get("place_name"))
                        .placeType(PlaceType.RESTAURANT)
                        .category((String) document.get("category_name"))
                        .address((String) document.get("road_address_name"))
                        .page(page)
                        .build();
                placeList.add(placeDto);
            }
        }
        catch(ParseException e){
            e.printStackTrace();
        }

        return (boolean)jsonMeta.get("is_end");

    }
}
