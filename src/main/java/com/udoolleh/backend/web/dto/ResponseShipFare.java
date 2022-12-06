package com.udoolleh.backend.web.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class ResponseShipFare {

    @Builder
    @Getter
    public static class HarborShipFare {
        private String harborName;
        private List<ShipFare> shipFares;
    }

    @Builder
    @Getter
    public static class ShipFare {
        private Long id;
        private String ageGroup;
        private Integer roundTrip;
        private Integer enterIsland;
        private Integer leaveIsland;
    }
}
