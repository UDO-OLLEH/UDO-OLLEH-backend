package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.entity.Ads;
import lombok.Builder;
import lombok.Getter;

public class ResponseAds {
    @Builder
    @Getter
    public static class AdsDto{
        private String id;
        private String photo;

        public static AdsDto of(Ads ads){
            return AdsDto.builder()
                    .id(ads.getId())
                    .photo(ads.getPhoto())
                    .build();
        }
    }
}
