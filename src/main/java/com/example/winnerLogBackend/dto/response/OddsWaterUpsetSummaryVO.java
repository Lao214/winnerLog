package com.example.winnerLogBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "盘口水位冷门翻车与大数据宏观统计VO")
public class OddsWaterUpsetSummaryVO {

    @Schema(description = "累计记录批次/天数", example = "15")
    private Integer totalRecords = 0;

    @Schema(description = "累计历史总场数", example = "350")
    private Integer totalMatches = 0;

    @Schema(description = "累计历史总翻车场数", example = "118")
    private Integer totalUpsetCount = 0;

    @Schema(description = "累计历史正路打出场数", example = "232")
    private Integer totalNormalCount = 0;

    @Schema(description = "累计综合总翻车率(%)", example = "33.71")
    private BigDecimal overallUpsetRate = BigDecimal.ZERO;

    @Schema(description = "累计历史主场翻车总场数", example = "65")
    private Integer totalHomeUpsetCount = 0;

    @Schema(description = "累计历史客场翻车总场数", example = "53")
    private Integer totalAwayUpsetCount = 0;

    @Schema(description = "累计综合主场翻车率(%)", example = "18.57")
    private BigDecimal overallHomeUpsetRate = BigDecimal.ZERO;

    @Schema(description = "累计综合客场翻车率(%)", example = "15.14")
    private BigDecimal overallAwayUpsetRate = BigDecimal.ZERO;

    @Schema(description = "主场翻车在翻车总数中的占比(%)", example = "55.08")
    private BigDecimal homeUpsetRatio = BigDecimal.ZERO;

    @Schema(description = "客场翻车在翻车总数中的占比(%)", example = "44.92")
    private BigDecimal awayUpsetRatio = BigDecimal.ZERO;

    // ==================== 高水盘口统计 ====================
    @Schema(description = "累计高水场数", example = "110")
    private Integer totalHighWaterMatches = 0;

    @Schema(description = "累计高水翻车总场数", example = "46")
    private Integer totalHighWaterUpsetCount = 0;

    @Schema(description = "累计高水主场翻车场数", example = "26")
    private Integer totalHighWaterHomeUpsetCount = 0;

    @Schema(description = "累计高水客场翻车场数", example = "20")
    private Integer totalHighWaterAwayUpsetCount = 0;

    @Schema(description = "累计高水综合翻车率(%)", example = "41.82")
    private BigDecimal avgHighWaterUpsetRate = BigDecimal.ZERO;

    @Schema(description = "累计高水主场翻车率(%)", example = "23.64")
    private BigDecimal avgHighWaterHomeUpsetRate = BigDecimal.ZERO;

    @Schema(description = "累计高水客场翻车率(%)", example = "18.18")
    private BigDecimal avgHighWaterAwayUpsetRate = BigDecimal.ZERO;

    @Schema(description = "高水比赛占比(%)", example = "31.43")
    private BigDecimal highWaterShareRate = BigDecimal.ZERO;

    // ==================== 中水盘口统计 ====================
    @Schema(description = "累计中水场数", example = "140")
    private Integer totalMidWaterMatches = 0;

    @Schema(description = "累计中水翻车总场数", example = "45")
    private Integer totalMidWaterUpsetCount = 0;

    @Schema(description = "累计中水主场翻车场数", example = "25")
    private Integer totalMidWaterHomeUpsetCount = 0;

    @Schema(description = "累计中水客场翻车场数", example = "20")
    private Integer totalMidWaterAwayUpsetCount = 0;

    @Schema(description = "累计中水综合翻车率(%)", example = "32.14")
    private BigDecimal avgMidWaterUpsetRate = BigDecimal.ZERO;

    @Schema(description = "累计中水主场翻车率(%)", example = "17.86")
    private BigDecimal avgMidWaterHomeUpsetRate = BigDecimal.ZERO;

    @Schema(description = "累计中水客场翻车率(%)", example = "14.29")
    private BigDecimal avgMidWaterAwayUpsetRate = BigDecimal.ZERO;

    @Schema(description = "中水比赛占比(%)", example = "40.00")
    private BigDecimal midWaterShareRate = BigDecimal.ZERO;

    // ==================== 低水盘口统计 ====================
    @Schema(description = "累计低水场数", example = "100")
    private Integer totalLowWaterMatches = 0;

    @Schema(description = "累计低水翻车总场数", example = "27")
    private Integer totalLowWaterUpsetCount = 0;

    @Schema(description = "累计低水主场翻车场数", example = "14")
    private Integer totalLowWaterHomeUpsetCount = 0;

    @Schema(description = "累计低水客场翻车场数", example = "13")
    private Integer totalLowWaterAwayUpsetCount = 0;

    @Schema(description = "累计低水综合翻车率(%)", example = "27.00")
    private BigDecimal avgLowWaterUpsetRate = BigDecimal.ZERO;

    @Schema(description = "累计低水主场翻车率(%)", example = "14.00")
    private BigDecimal avgLowWaterHomeUpsetRate = BigDecimal.ZERO;

    @Schema(description = "累计低水客场翻车率(%)", example = "13.00")
    private BigDecimal avgLowWaterAwayUpsetRate = BigDecimal.ZERO;

    @Schema(description = "低水比赛占比(%)", example = "28.57")
    private BigDecimal lowWaterShareRate = BigDecimal.ZERO;

    // ==================== 宏观洞察研判 ====================
    @Schema(description = "历史最高翻车率水位", example = "高水盘口 (41.82%)")
    private String highestRiskWaterLevel = "暂无数据";

    @Schema(description = "翻车主客场倾向", example = "主场爆冷偏多 (55.08%)")
    private String upsetTendency = "均衡";

    @Schema(description = "大数据冷门研判建议", example = "高水场次翻车率超40%，建议关注冷门博取高赔")
    private String riskAssessmentSummary = "数据收集中";

    public OddsWaterUpsetSummaryVO() {}

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(Integer totalMatches) {
        this.totalMatches = totalMatches;
    }

    public Integer getTotalUpsetCount() {
        return totalUpsetCount;
    }

    public void setTotalUpsetCount(Integer totalUpsetCount) {
        this.totalUpsetCount = totalUpsetCount;
    }

    public Integer getTotalNormalCount() {
        return totalNormalCount;
    }

    public void setTotalNormalCount(Integer totalNormalCount) {
        this.totalNormalCount = totalNormalCount;
    }

    public BigDecimal getOverallUpsetRate() {
        return overallUpsetRate;
    }

    public void setOverallUpsetRate(BigDecimal overallUpsetRate) {
        this.overallUpsetRate = overallUpsetRate;
    }

    public Integer getTotalHomeUpsetCount() {
        return totalHomeUpsetCount;
    }

    public void setTotalHomeUpsetCount(Integer totalHomeUpsetCount) {
        this.totalHomeUpsetCount = totalHomeUpsetCount;
    }

    public Integer getTotalAwayUpsetCount() {
        return totalAwayUpsetCount;
    }

    public void setTotalAwayUpsetCount(Integer totalAwayUpsetCount) {
        this.totalAwayUpsetCount = totalAwayUpsetCount;
    }

    public BigDecimal getOverallHomeUpsetRate() {
        return overallHomeUpsetRate;
    }

    public void setOverallHomeUpsetRate(BigDecimal overallHomeUpsetRate) {
        this.overallHomeUpsetRate = overallHomeUpsetRate;
    }

    public BigDecimal getOverallAwayUpsetRate() {
        return overallAwayUpsetRate;
    }

    public void setOverallAwayUpsetRate(BigDecimal overallAwayUpsetRate) {
        this.overallAwayUpsetRate = overallAwayUpsetRate;
    }

    public BigDecimal getHomeUpsetRatio() {
        return homeUpsetRatio;
    }

    public void setHomeUpsetRatio(BigDecimal homeUpsetRatio) {
        this.homeUpsetRatio = homeUpsetRatio;
    }

    public BigDecimal getAwayUpsetRatio() {
        return awayUpsetRatio;
    }

    public void setAwayUpsetRatio(BigDecimal awayUpsetRatio) {
        this.awayUpsetRatio = awayUpsetRatio;
    }

    public Integer getTotalHighWaterMatches() {
        return totalHighWaterMatches;
    }

    public void setTotalHighWaterMatches(Integer totalHighWaterMatches) {
        this.totalHighWaterMatches = totalHighWaterMatches;
    }

    public Integer getTotalHighWaterUpsetCount() {
        return totalHighWaterUpsetCount;
    }

    public void setTotalHighWaterUpsetCount(Integer totalHighWaterUpsetCount) {
        this.totalHighWaterUpsetCount = totalHighWaterUpsetCount;
    }

    public Integer getTotalHighWaterHomeUpsetCount() {
        return totalHighWaterHomeUpsetCount;
    }

    public void setTotalHighWaterHomeUpsetCount(Integer totalHighWaterHomeUpsetCount) {
        this.totalHighWaterHomeUpsetCount = totalHighWaterHomeUpsetCount;
    }

    public Integer getTotalHighWaterAwayUpsetCount() {
        return totalHighWaterAwayUpsetCount;
    }

    public void setTotalHighWaterAwayUpsetCount(Integer totalHighWaterAwayUpsetCount) {
        this.totalHighWaterAwayUpsetCount = totalHighWaterAwayUpsetCount;
    }

    public BigDecimal getAvgHighWaterUpsetRate() {
        return avgHighWaterUpsetRate;
    }

    public void setAvgHighWaterUpsetRate(BigDecimal avgHighWaterUpsetRate) {
        this.avgHighWaterUpsetRate = avgHighWaterUpsetRate;
    }

    public BigDecimal getAvgHighWaterHomeUpsetRate() {
        return avgHighWaterHomeUpsetRate;
    }

    public void setAvgHighWaterHomeUpsetRate(BigDecimal avgHighWaterHomeUpsetRate) {
        this.avgHighWaterHomeUpsetRate = avgHighWaterHomeUpsetRate;
    }

    public BigDecimal getAvgHighWaterAwayUpsetRate() {
        return avgHighWaterAwayUpsetRate;
    }

    public void setAvgHighWaterAwayUpsetRate(BigDecimal avgHighWaterAwayUpsetRate) {
        this.avgHighWaterAwayUpsetRate = avgHighWaterAwayUpsetRate;
    }

    public BigDecimal getHighWaterShareRate() {
        return highWaterShareRate;
    }

    public void setHighWaterShareRate(BigDecimal highWaterShareRate) {
        this.highWaterShareRate = highWaterShareRate;
    }

    public Integer getTotalMidWaterMatches() {
        return totalMidWaterMatches;
    }

    public void setTotalMidWaterMatches(Integer totalMidWaterMatches) {
        this.totalMidWaterMatches = totalMidWaterMatches;
    }

    public Integer getTotalMidWaterUpsetCount() {
        return totalMidWaterUpsetCount;
    }

    public void setTotalMidWaterUpsetCount(Integer totalMidWaterUpsetCount) {
        this.totalMidWaterUpsetCount = totalMidWaterUpsetCount;
    }

    public Integer getTotalMidWaterHomeUpsetCount() {
        return totalMidWaterHomeUpsetCount;
    }

    public void setTotalMidWaterHomeUpsetCount(Integer totalMidWaterHomeUpsetCount) {
        this.totalMidWaterHomeUpsetCount = totalMidWaterHomeUpsetCount;
    }

    public Integer getTotalMidWaterAwayUpsetCount() {
        return totalMidWaterAwayUpsetCount;
    }

    public void setTotalMidWaterAwayUpsetCount(Integer totalMidWaterAwayUpsetCount) {
        this.totalMidWaterAwayUpsetCount = totalMidWaterAwayUpsetCount;
    }

    public BigDecimal getAvgMidWaterUpsetRate() {
        return avgMidWaterUpsetRate;
    }

    public void setAvgMidWaterUpsetRate(BigDecimal avgMidWaterUpsetRate) {
        this.avgMidWaterUpsetRate = avgMidWaterUpsetRate;
    }

    public BigDecimal getAvgMidWaterHomeUpsetRate() {
        return avgMidWaterHomeUpsetRate;
    }

    public void setAvgMidWaterHomeUpsetRate(BigDecimal avgMidWaterHomeUpsetRate) {
        this.avgMidWaterHomeUpsetRate = avgMidWaterHomeUpsetRate;
    }

    public BigDecimal getAvgMidWaterAwayUpsetRate() {
        return avgMidWaterAwayUpsetRate;
    }

    public void setAvgMidWaterAwayUpsetRate(BigDecimal avgMidWaterAwayUpsetRate) {
        this.avgMidWaterAwayUpsetRate = avgMidWaterAwayUpsetRate;
    }

    public BigDecimal getMidWaterShareRate() {
        return midWaterShareRate;
    }

    public void setMidWaterShareRate(BigDecimal midWaterShareRate) {
        this.midWaterShareRate = midWaterShareRate;
    }

    public Integer getTotalLowWaterMatches() {
        return totalLowWaterMatches;
    }

    public void setTotalLowWaterMatches(Integer totalLowWaterMatches) {
        this.totalLowWaterMatches = totalLowWaterMatches;
    }

    public Integer getTotalLowWaterUpsetCount() {
        return totalLowWaterUpsetCount;
    }

    public void setTotalLowWaterUpsetCount(Integer totalLowWaterUpsetCount) {
        this.totalLowWaterUpsetCount = totalLowWaterUpsetCount;
    }

    public Integer getTotalLowWaterHomeUpsetCount() {
        return totalLowWaterHomeUpsetCount;
    }

    public void setTotalLowWaterHomeUpsetCount(Integer totalLowWaterHomeUpsetCount) {
        this.totalLowWaterHomeUpsetCount = totalLowWaterHomeUpsetCount;
    }

    public Integer getTotalLowWaterAwayUpsetCount() {
        return totalLowWaterAwayUpsetCount;
    }

    public void setTotalLowWaterAwayUpsetCount(Integer totalLowWaterAwayUpsetCount) {
        this.totalLowWaterAwayUpsetCount = totalLowWaterAwayUpsetCount;
    }

    public BigDecimal getAvgLowWaterUpsetRate() {
        return avgLowWaterUpsetRate;
    }

    public void setAvgLowWaterUpsetRate(BigDecimal avgLowWaterUpsetRate) {
        this.avgLowWaterUpsetRate = avgLowWaterUpsetRate;
    }

    public BigDecimal getAvgLowWaterHomeUpsetRate() {
        return avgLowWaterHomeUpsetRate;
    }

    public void setAvgLowWaterHomeUpsetRate(BigDecimal avgLowWaterHomeUpsetRate) {
        this.avgLowWaterHomeUpsetRate = avgLowWaterHomeUpsetRate;
    }

    public BigDecimal getAvgLowWaterAwayUpsetRate() {
        return avgLowWaterAwayUpsetRate;
    }

    public void setAvgLowWaterAwayUpsetRate(BigDecimal avgLowWaterAwayUpsetRate) {
        this.avgLowWaterAwayUpsetRate = avgLowWaterAwayUpsetRate;
    }

    public BigDecimal getLowWaterShareRate() {
        return lowWaterShareRate;
    }

    public void setLowWaterShareRate(BigDecimal lowWaterShareRate) {
        this.lowWaterShareRate = lowWaterShareRate;
    }

    public String getHighestRiskWaterLevel() {
        return highestRiskWaterLevel;
    }

    public void setHighestRiskWaterLevel(String highestRiskWaterLevel) {
        this.highestRiskWaterLevel = highestRiskWaterLevel;
    }

    public String getUpsetTendency() {
        return upsetTendency;
    }

    public void setUpsetTendency(String upsetTendency) {
        this.upsetTendency = upsetTendency;
    }

    public String getRiskAssessmentSummary() {
        return riskAssessmentSummary;
    }

    public void setRiskAssessmentSummary(String riskAssessmentSummary) {
        this.riskAssessmentSummary = riskAssessmentSummary;
    }
}
