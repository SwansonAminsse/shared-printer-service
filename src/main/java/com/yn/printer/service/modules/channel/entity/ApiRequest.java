package com.yn.printer.service.modules.channel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequest {
    private String accessKey;
    private String appId;
    private String img;
    private String tokenId;
    private String eventId;
    private String type;
    private String imgType;
    private String txtType;
    private String callback;
    private String fileFormat;
    private String contents;
    private String interType;


}