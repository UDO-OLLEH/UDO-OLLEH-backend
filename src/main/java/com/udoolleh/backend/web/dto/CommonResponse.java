package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder
public class CommonResponse {
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    @Builder.Default
    private Date dateTime = new Date();
    private int status;
    private String message;
    private Object list;
}
