package com.yn.printer.service.modules.common.vo;

import lombok.Data;

@Data
public class RiskDetail {
    private int beginPosition;
    private String content;
    private String description;
    private int endPosition;
    private int index;
    private String model;
    private String riskLevel;
    private int riskType;
    private String type;

    // getters and setters
}
