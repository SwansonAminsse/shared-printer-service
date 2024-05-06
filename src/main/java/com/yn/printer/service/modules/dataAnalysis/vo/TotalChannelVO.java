package com.yn.printer.service.modules.dataAnalysis.vo;

import lombok.Data;

@Data
public class TotalChannelVO {
//城市合伙人
    private  long primaryChannelCount;
    //合作伙伴

    private long secondaryChannelCount;
    //终端服务商
    private  long terminalChannelCount;


    public TotalChannelVO(long l, long l1, long l2) {
        this.primaryChannelCount = l;
        this.secondaryChannelCount = l1;
        this.terminalChannelCount = l2;
    }
}