package com.yn.printer.service.modules.common.vo;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {

    private int code;

    private String message;

    private List<Content> contents;

    // Getters and setters
    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        private String requestId;
        private MachineResult machineResult;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class MachineResult {
        private int code;
        private Detail detail;
        private String message;
        private String requestId;
        private String riskLevel;
        private int score;
        private AuxInfo auxInfo;
        private MergeResult mergeResult;

        // Getters and setters
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class Detail {
        private String description;
        private List<Hit> hits;
        private String model;
        private List<RiskDetail> riskDetail;
        private String riskHtml;

        private RiskSummary riskSummary;

        // Getters and setters
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class Hit {
        private String description;
        private String descriptionV2;
        private String model;
        private String riskLevel;
        private int riskType;
        private int score;
        private String type;

        // Getters and setters
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class RiskDetail {
        private int beginPosition;
        private String content;
        private String description;
        private int endPosition;
        private int index;
        private String keywordsPosition;
        private List<MatchedDetail> matchedDetail;
        private String matchedItem;
        private String matchedList;
        private String model;
        private String riskLevel;
        private int riskType;
        private String type;

        // Getters and setters
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class MatchedDetail {
        private String listId;
        private List<String> matchedFiled;
        private String name;
        private String organization;
        private List<WordPosition> wordPositions;
        private List<String> words;

        // Getters and setters
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class WordPosition {
        private String position;
        private String word;

        // Getters and setters
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class AuxInfo {
        private String textNum;
        private String imgNum;

        // Getters and setters
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class MergeResult {
        private String riskLevel;

        // Getters and setters
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class RiskSummary {
        private Map<String, Integer> riskSummaryMap = new HashMap<>();

        @JsonAnySetter
        public void setRiskSummary(String key, Integer value) {
            this.riskSummaryMap.put(key, value);
        }

        public Map<String, Integer> getRiskSummaryMap() {
            return riskSummaryMap;
        }

        public void setRiskSummaryMap(Map<String, Integer> riskSummaryMap) {
            this.riskSummaryMap = riskSummaryMap;
        }
    }
}
