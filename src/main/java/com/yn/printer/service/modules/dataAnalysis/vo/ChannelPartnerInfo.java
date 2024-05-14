package com.yn.printer.service.modules.dataAnalysis.vo;

import lombok.Data;

@Data
public class ChannelPartnerInfo {
    private String name;
    private Long id;


    public ChannelPartnerInfo(String name, Long id) {
        this.name = name;
        this.id = id;
    }
}
