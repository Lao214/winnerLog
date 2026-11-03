package com.example.winnerLogBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "每日成本利润与大数据分析宏观统计VO")
public class DailyProfitSummaryVO {

    @Schema(description = "累计记录天数", example = "30")
    private Integer totalDays = 0;

    @Schema(description = "累计穿盘比赛总数", example = "128")
    private Integer totalBreakCount = 0;

    @Schema(description = "累计未穿盘比赛总数", example = "92")
    private Integer totalUnbreakCount = 0;

    @Schema(description = "累计分析比赛总场次", example = "220")
    private Integer totalMatches = 0;

    @Schema(description = "累计总穿盘率(%)", example = "58.18")
    private BigDecimal avgBreakRate = BigDecimal.ZERO;

    @Schema(description = "累计总投注成本(元)", example = "15000.00")
    private BigDecimal totalCost = BigDecimal.ZERO;

    @Schema(description = "累计总净利润(元)", example = "4250.80")
    private BigDecimal totalProfit = BigDecimal.ZERO;

    @Schema(description = "累计总派彩返还(元)", example = "19250.80")
    private BigDecimal totalReturn = BigDecimal.ZERO;

    @Schema(description = "累计综合投资回报率ROI(%)", example = "28.34")
    private BigDecimal overallRoi = BigDecimal.ZERO;

    // --- 策略细分统计 ---
    @Schema(description = "主三串副二串累计成本(元)", example = "6000.00")
    private BigDecimal total3m2sCost = BigDecimal.ZERO;

    @Schema(description = "主三串副二串累计净利润(元)", example = "2150.00")
    private BigDecimal total3m2sProfit = BigDecimal.ZERO;

    @Schema(description = "主三串副二串累计ROI(%)", example = "35.83")
    private BigDecimal roi3m2s = BigDecimal.ZERO;

    @Schema(description = "主二串副三串累计成本(元)", example = "9000.00")
    private BigDecimal total2m3sCost = BigDecimal.ZERO;

    @Schema(description = "主二串副三串累计净利润(元)", example = "2100.80")
    private BigDecimal total2m3sProfit = BigDecimal.ZERO;

    @Schema(description = "主二串副三串累计ROI(%)", example = "23.34")
    private BigDecimal roi2m3s = BigDecimal.ZERO;

    @Schema(description = "历史收益最优策略", example = "主三串副二串(3M2S)")
    private String bestStrategy = "暂无数据";

    // --- 战绩分布维度 ---
    @Schema(description = "盈利天数", example = "21")
    private Integer profitableDays = 0;

    @Schema(description = "亏损天数", example = "7")
    private Integer lossDays = 0;

    @Schema(description = "保本天数", example = "2")
    private Integer breakevenDays = 0;

    @Schema(description = "日胜率 (盈利天数占比 %)", example = "70.00")
    private BigDecimal winDaysRate = BigDecimal.ZERO;

    @Schema(description = "单日最高净利润(元)", example = "1580.00")
    private BigDecimal maxSingleDayProfit = BigDecimal.ZERO;

    @Schema(description = "单日最大亏损(元)", example = "-600.00")
    private BigDecimal maxSingleDayLoss = BigDecimal.ZERO;

    public DailyProfitSummaryVO() {}

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    public Integer getTotalBreakCount() {
        return totalBreakCount;
    }

    public void setTotalBreakCount(Integer totalBreakCount) {
        this.totalBreakCount = totalBreakCount;
    }

    public Integer getTotalUnbreakCount() {
        return totalUnbreakCount;
    }

    public void setTotalUnbreakCount(Integer totalUnbreakCount) {
        this.totalUnbreakCount = totalUnbreakCount;
    }

    public Integer getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(Integer totalMatches) {
        this.totalMatches = totalMatches;
    }

    public BigDecimal getAvgBreakRate() {
        return avgBreakRate;
    }

    public void setAvgBreakRate(BigDecimal avgBreakRate) {
        this.avgBreakRate = avgBreakRate;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public BigDecimal getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(BigDecimal totalReturn) {
        this.totalReturn = totalReturn;
    }

    public BigDecimal getOverallRoi() {
        return overallRoi;
    }

    public void setOverallRoi(BigDecimal overallRoi) {
        this.overallRoi = overallRoi;
    }

    public BigDecimal getTotal3m2sCost() {
        return total3m2sCost;
    }

    public void setTotal3m2sCost(BigDecimal total3m2sCost) {
        this.total3m2sCost = total3m2sCost;
    }

    public BigDecimal getTotal3m2sProfit() {
        return total3m2sProfit;
    }

    public void setTotal3m2sProfit(BigDecimal total3m2sProfit) {
        this.total3m2sProfit = total3m2sProfit;
    }

    public BigDecimal getRoi3m2s() {
        return roi3m2s;
    }

    public void setRoi3m2s(BigDecimal roi3m2s) {
        this.roi3m2s = roi3m2s;
    }

    public BigDecimal getTotal2m3sCost() {
        return total2m3sCost;
    }

    public void setTotal2m3sCost(BigDecimal total2m3sCost) {
        this.total2m3sCost = total2m3sCost;
    }

    public BigDecimal getTotal2m3sProfit() {
        return total2m3sProfit;
    }

    public void setTotal2m3sProfit(BigDecimal total2m3sProfit) {
        this.total2m3sProfit = total2m3sProfit;
    }

    public BigDecimal getRoi2m3s() {
        return roi2m3s;
    }

    public void setRoi2m3s(BigDecimal roi2m3s) {
        this.roi2m3s = roi2m3s;
    }

    public String getBestStrategy() {
        return bestStrategy;
    }

    public void setBestStrategy(String bestStrategy) {
        this.bestStrategy = bestStrategy;
    }

    public Integer getProfitableDays() {
        return profitableDays;
    }

    public void setProfitableDays(Integer profitableDays) {
        this.profitableDays = profitableDays;
    }

    public Integer getLossDays() {
        return lossDays;
    }

    public void setLossDays(Integer lossDays) {
        this.lossDays = lossDays;
    }

    public Integer getBreakevenDays() {
        return breakevenDays;
    }

    public void setBreakevenDays(Integer breakevenDays) {
        this.breakevenDays = breakevenDays;
    }

    public BigDecimal getWinDaysRate() {
        return winDaysRate;
    }

    public void setWinDaysRate(BigDecimal winDaysRate) {
        this.winDaysRate = winDaysRate;
    }

    public BigDecimal getMaxSingleDayProfit() {
        return maxSingleDayProfit;
    }

    public void setMaxSingleDayProfit(BigDecimal maxSingleDayProfit) {
        this.maxSingleDayProfit = maxSingleDayProfit;
    }

    public BigDecimal getMaxSingleDayLoss() {
        return maxSingleDayLoss;
    }

    public void setMaxSingleDayLoss(BigDecimal maxSingleDayLoss) {
        this.maxSingleDayLoss = maxSingleDayLoss;
    }
}
