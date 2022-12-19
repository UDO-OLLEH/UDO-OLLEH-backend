package com.udoolleh.backend.web.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class ResponseShipFare {

    @Builder
    @Getter
    public static class HarborShipFareDto {
        private String harborName;
        private List<ShipFareDto> shipFareDtos;
    }

    @Builder
    @Getter
    public static class ShipFareDto {
        private Long id;
        private String ageGroup;
        private Integer roundTrip;
        private Integer enterIsland;
        private Integer leaveIsland;
    }
}
