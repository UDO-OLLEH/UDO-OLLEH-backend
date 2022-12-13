package com.udoolleh.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {

    @Builder.Default // Builder default 지정
    private String id = UUID.randomUUID().toString(); // uuid
    @Builder.Default
    private Date dateTime = new Date(); // date
    private String message;
    private Object list;
}
