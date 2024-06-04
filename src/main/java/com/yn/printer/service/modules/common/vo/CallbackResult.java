package com.yn.printer.service.modules.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class CallbackResult {
    private int code;
    private String message;
    private String requestId;
    private int score;
    private String riskLevel;
    private String description;
    private String model;
    private int riskType;
    private List<RiskDetail> riskDetail;
    private int status;
    private AuxInfo auxInfo;

    // getters and setters
}
