package com.udoolleh.backend.ads.dto;

import com.udoolleh.backend.ads.Ads;
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
