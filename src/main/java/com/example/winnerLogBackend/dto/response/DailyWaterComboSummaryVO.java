package com.example.winnerLogBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "每日水位规律串子战绩与大数据宏观统计VO")
public class DailyWaterComboSummaryVO {

    @Schema(description = "累计记录战绩天数", example = "25")
    private Integer totalDays = 0;

    @Schema(description = "累计分析与下注比赛总场数", example = "120")
    private Integer totalMatches = 0;

    // --- 综合总财务指标 ---
    @Schema(description = "累计综合打票总成本(元)", example = "12500.00")
    private BigDecimal totalCost = BigDecimal.ZERO;

    @Schema(description = "累计综合最终净利润(元)", example = "5680.00")
    private BigDecimal totalProfit = BigDecimal.ZERO;

    @Schema(description = "累计总派彩回款(元)", example = "18180.00")
    private BigDecimal totalReturn = BigDecimal.ZERO;

    @Schema(description = "累计综合投资回报率ROI(%)", example = "45.44")
    private BigDecimal overallRoi = BigDecimal.ZERO;

    // --- 常规打票细分指标 ---
    @Schema(description = "累计常规打票成本(元)", example = "10000.00")
    private BigDecimal totalRegularCost = BigDecimal.ZERO;

    @Schema(description = "累计常规最终利润(元)", example = "3800.00")
    private BigDecimal totalRegularProfit = BigDecimal.ZERO;

    @Schema(description = "累计常规串投资回报率ROI(%)", example = "38.00")
    private BigDecimal regularRoi = BigDecimal.ZERO;

    // --- 长串打票细分指标 ---
    @Schema(description = "累计长串打票成本(元)", example = "2500.00")
    private BigDecimal totalLongComboCost = BigDecimal.ZERO;

    @Schema(description = "累计长串最终净利润(元)", example = "1880.00")
    private BigDecimal totalLongComboProfit = BigDecimal.ZERO;

    @Schema(description = "累计长串投资回报率ROI(%)", example = "75.20")
    private BigDecimal longComboRoi = BigDecimal.ZERO;

    @Schema(description = "累计长串红单命中天数", example = "8")
    private Integer longComboWinDays = 0;

    @Schema(description = "盈利天数 (红单天数)", example = "18")
    private Integer profitableDays = 0;

    @Schema(description = "亏损天数 (黑单天数)", example = "5")
    private Integer lossDays = 0;

    @Schema(description = "保本天数", example = "2")
    private Integer breakevenDays = 0;

    @Schema(description = "盈利日胜率(%)", example = "72.00")
    private BigDecimal winDaysRate = BigDecimal.ZERO;

    @Schema(description = "单日最高净利润(元)", example = "1850.00")
    private BigDecimal maxSingleDayProfit = BigDecimal.ZERO;

    @Schema(description = "单日最大亏损(元)", example = "-400.00")
    private BigDecimal maxSingleDayLoss = BigDecimal.ZERO;

    @Schema(description = "日均打票成本(元)", example = "500.00")
    private BigDecimal avgDailyCost = BigDecimal.ZERO;

    @Schema(description = "日均净利润(元)", example = "227.20")
    private BigDecimal avgDailyProfit = BigDecimal.ZERO;

    @Schema(description = "场均打票成本(元)", example = "104.17")
    private BigDecimal avgMatchCost = BigDecimal.ZERO;

    @Schema(description = "场均净利润(元)", example = "47.33")
    private BigDecimal avgMatchProfit = BigDecimal.ZERO;

    @Schema(description = "大数据收益风控评级", example = "🔥 高效爆发型策略 (累计ROI超40%)")
    private String riskEvaluation = "数据积累中";

    public DailyWaterComboSummaryVO() {}

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    public Integer getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(Integer totalMatches) {
        this.totalMatches = totalMatches;
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

    public BigDecimal getAvgDailyCost() {
        return avgDailyCost;
    }

    public void setAvgDailyCost(BigDecimal avgDailyCost) {
        this.avgDailyCost = avgDailyCost;
    }

    public BigDecimal getAvgDailyProfit() {
        return avgDailyProfit;
    }

    public void setAvgDailyProfit(BigDecimal avgDailyProfit) {
        this.avgDailyProfit = avgDailyProfit;
    }

    public BigDecimal getAvgMatchCost() {
        return avgMatchCost;
    }

    public void setAvgMatchCost(BigDecimal avgMatchCost) {
        this.avgMatchCost = avgMatchCost;
    }

    public BigDecimal getAvgMatchProfit() {
        return avgMatchProfit;
    }

    public void setAvgMatchProfit(BigDecimal avgMatchProfit) {
        this.avgMatchProfit = avgMatchProfit;
    }

    public String getRiskEvaluation() {
        return riskEvaluation;
    }

    public void setRiskEvaluation(String riskEvaluation) {
        this.riskEvaluation = riskEvaluation;
    }

    public BigDecimal getTotalRegularCost() {
        return totalRegularCost;
    }

    public void setTotalRegularCost(BigDecimal totalRegularCost) {
        this.totalRegularCost = totalRegularCost;
    }

    public BigDecimal getTotalRegularProfit() {
        return totalRegularProfit;
    }

    public void setTotalRegularProfit(BigDecimal totalRegularProfit) {
        this.totalRegularProfit = totalRegularProfit;
    }

    public BigDecimal getRegularRoi() {
        return regularRoi;
    }

    public void setRegularRoi(BigDecimal regularRoi) {
        this.regularRoi = regularRoi;
    }

    public BigDecimal getTotalLongComboCost() {
        return totalLongComboCost;
    }

    public void setTotalLongComboCost(BigDecimal totalLongComboCost) {
        this.totalLongComboCost = totalLongComboCost;
    }

    public BigDecimal getTotalLongComboProfit() {
        return totalLongComboProfit;
    }

    public void setTotalLongComboProfit(BigDecimal totalLongComboProfit) {
        this.totalLongComboProfit = totalLongComboProfit;
    }

    public BigDecimal getLongComboRoi() {
        return longComboRoi;
    }

    public void setLongComboRoi(BigDecimal longComboRoi) {
        this.longComboRoi = longComboRoi;
    }

    public Integer getLongComboWinDays() {
        return longComboWinDays;
    }

    public void setLongComboWinDays(Integer longComboWinDays) {
        this.longComboWinDays = longComboWinDays;
    }
}
