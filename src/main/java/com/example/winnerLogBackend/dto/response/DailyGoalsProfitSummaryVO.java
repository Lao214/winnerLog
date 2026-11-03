package com.example.winnerLogBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "每日总进球数成本利润与规律大数据宏观统计VO")
public class DailyGoalsProfitSummaryVO {

    @Schema(description = "累计记录比赛天数", example = "30")
    private Integer totalDays = 0;

    @Schema(description = "累计符合规律场数", example = "135")
    private Integer totalFitCount = 0;

    @Schema(description = "累计稍微符合规律场数", example = "78")
    private Integer totalSlightCount = 0;

    @Schema(description = "累计完全不符合规律场数", example = "37")
    private Integer totalMismatchCount = 0;

    @Schema(description = "累计分析总场次", example = "250")
    private Integer totalMatches = 0;

    @Schema(description = "平均完全符合规律比例(%)", example = "54.00")
    private BigDecimal avgFitRate = BigDecimal.ZERO;

    @Schema(description = "平均稍微符合规律比例(%)", example = "31.20")
    private BigDecimal avgSlightFitRate = BigDecimal.ZERO;

    @Schema(description = "平均完全不符合规律比例(%)", example = "14.80")
    private BigDecimal avgMismatchRate = BigDecimal.ZERO;

    @Schema(description = "广义规律覆盖吻合率(%) ((符合+稍微)/总场次)", example = "85.20")
    private BigDecimal avgGeneralizedFitRate = BigDecimal.ZERO;

    @Schema(description = "累计总投注成本(元)", example = "18000.00")
    private BigDecimal totalCost = BigDecimal.ZERO;

    @Schema(description = "累计总净利润(元)", example = "5860.50")
    private BigDecimal totalProfit = BigDecimal.ZERO;

    @Schema(description = "累计总派彩返还(元)", example = "23860.50")
    private BigDecimal totalReturn = BigDecimal.ZERO;

    @Schema(description = "累计综合投资回报率ROI(%)", example = "32.56")
    private BigDecimal overallRoi = BigDecimal.ZERO;

    // --- 策略细分统计 ---
    @Schema(description = "主三串副二串累计成本(元)", example = "9000.00")
    private BigDecimal total3m2sCost = BigDecimal.ZERO;

    @Schema(description = "主三串副二串累计净利润(元)", example = "3420.00")
    private BigDecimal total3m2sProfit = BigDecimal.ZERO;

    @Schema(description = "主三串副二串累计ROI(%)", example = "38.00")
    private BigDecimal roi3m2s = BigDecimal.ZERO;

    @Schema(description = "主二串副三串累计成本(元)", example = "9000.00")
    private BigDecimal total2m3sCost = BigDecimal.ZERO;

    @Schema(description = "主二串副三串累计净利润(元)", example = "2440.50")
    private BigDecimal total2m3sProfit = BigDecimal.ZERO;

    @Schema(description = "主二串副三串累计ROI(%)", example = "27.12")
    private BigDecimal roi2m3s = BigDecimal.ZERO;

    @Schema(description = "总进球数历史收益最优策略", example = "主三串副二串 (3M2S 进攻爆发力强)")
    private String bestStrategy = "暂无数据";

    // --- 战绩分布维度 ---
    @Schema(description = "盈利天数", example = "22")
    private Integer profitableDays = 0;

    @Schema(description = "亏损天数", example = "6")
    private Integer lossDays = 0;

    @Schema(description = "保本天数", example = "2")
    private Integer breakevenDays = 0;

    @Schema(description = "日胜率 (盈利天数占比 %)", example = "73.33")
    private BigDecimal winDaysRate = BigDecimal.ZERO;

    @Schema(description = "单日最高净利润(元)", example = "1680.00")
    private BigDecimal maxSingleDayProfit = BigDecimal.ZERO;

    @Schema(description = "单日最大亏损(元)", example = "-550.00")
    private BigDecimal maxSingleDayLoss = BigDecimal.ZERO;

    public DailyGoalsProfitSummaryVO() {}

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    public Integer getTotalFitCount() {
        return totalFitCount;
    }

    public void setTotalFitCount(Integer totalFitCount) {
        this.totalFitCount = totalFitCount;
    }

    public Integer getTotalSlightCount() {
        return totalSlightCount;
    }

    public void setTotalSlightCount(Integer totalSlightCount) {
        this.totalSlightCount = totalSlightCount;
    }

    public Integer getTotalMismatchCount() {
        return totalMismatchCount;
    }

    public void setTotalMismatchCount(Integer totalMismatchCount) {
        this.totalMismatchCount = totalMismatchCount;
    }

    public Integer getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(Integer totalMatches) {
        this.totalMatches = totalMatches;
    }

    public BigDecimal getAvgFitRate() {
        return avgFitRate;
    }

    public void setAvgFitRate(BigDecimal avgFitRate) {
        this.avgFitRate = avgFitRate;
    }

    public BigDecimal getAvgSlightFitRate() {
        return avgSlightFitRate;
    }

    public void setAvgSlightFitRate(BigDecimal avgSlightFitRate) {
        this.avgSlightFitRate = avgSlightFitRate;
    }

    public BigDecimal getAvgMismatchRate() {
        return avgMismatchRate;
    }

    public void setAvgMismatchRate(BigDecimal avgMismatchRate) {
        this.avgMismatchRate = avgMismatchRate;
    }

    public BigDecimal getAvgGeneralizedFitRate() {
        return avgGeneralizedFitRate;
    }

    public void setAvgGeneralizedFitRate(BigDecimal avgGeneralizedFitRate) {
        this.avgGeneralizedFitRate = avgGeneralizedFitRate;
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
