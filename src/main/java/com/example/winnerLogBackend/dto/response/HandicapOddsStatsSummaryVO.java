package com.example.winnerLogBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "让球获胜赔率六区间大数据宏观全盘汇总VO")
public class HandicapOddsStatsSummaryVO {

    @Schema(description = "累计记录批次/天数", example = "5")
    private Integer totalRecords = 0;

    @Schema(description = "累计统计比赛总场数", example = "103")
    private Integer totalMatches = 0;

    @Schema(description = "累计胜场数", example = "63")
    private Integer totalWinCount = 0;

    @Schema(description = "累计平场数", example = "23")
    private Integer totalDrawCount = 0;

    @Schema(description = "累计负场数", example = "17")
    private Integer totalLossCount = 0;

    @Schema(description = "累计赢球打穿让球盘场数", example = "43")
    private Integer totalBreakCount = 0;

    @Schema(description = "累计赢球走水/未穿盘场数", example = "20")
    private Integer totalUnbreakCount = 0;

    @Schema(description = "全盘综合胜率(%)", example = "61.17")
    private BigDecimal overallWinRate = BigDecimal.ZERO;

    @Schema(description = "全盘综合平局率(%)", example = "22.33")
    private BigDecimal overallDrawRate = BigDecimal.ZERO;

    @Schema(description = "全盘综合负率(%)", example = "16.50")
    private BigDecimal overallLossRate = BigDecimal.ZERO;

    @Schema(description = "全盘赢球穿盘率(%: 穿盘数/胜场数)", example = "68.25")
    private BigDecimal overallBreakRateOnWin = BigDecimal.ZERO;

    @Schema(description = "全盘综合穿盘率(%: 穿盘数/总场数)", example = "41.75")
    private BigDecimal overallBreakRate = BigDecimal.ZERO;

    // ==================== 主客让对比 ====================
    @Schema(description = "主让球累计比赛场数", example = "60")
    private Integer homeTotalMatches = 0;

    @Schema(description = "主让球累计胜场数", example = "41")
    private Integer homeTotalWin = 0;

    @Schema(description = "主让球累计穿盘数", example = "29")
    private Integer homeTotalBreak = 0;

    @Schema(description = "主让球综合胜率(%)", example = "68.33")
    private BigDecimal homeWinRate = BigDecimal.ZERO;

    @Schema(description = "主让球赢球穿盘率(%)", example = "70.73")
    private BigDecimal homeBreakRateOnWin = BigDecimal.ZERO;

    @Schema(description = "主让球综合穿盘率(%)", example = "48.33")
    private BigDecimal homeBreakRate = BigDecimal.ZERO;

    @Schema(description = "客让球累计比赛场数", example = "43")
    private Integer awayTotalMatches = 0;

    @Schema(description = "客让球累计胜场数", example = "22")
    private Integer awayTotalWin = 0;

    @Schema(description = "客让球累计穿盘数", example = "14")
    private Integer awayTotalBreak = 0;

    @Schema(description = "客让球综合胜率(%)", example = "51.16")
    private BigDecimal awayWinRate = BigDecimal.ZERO;

    @Schema(description = "客让球赢球穿盘率(%)", example = "63.64")
    private BigDecimal awayBreakRateOnWin = BigDecimal.ZERO;

    @Schema(description = "客让球综合穿盘率(%)", example = "32.56")
    private BigDecimal awayBreakRate = BigDecimal.ZERO;

    // ==================== 大数据研判 ====================
    @Schema(description = "历史穿盘率最高优势区间", example = "主让 R1(<=1.40)")
    private String bestBreakInterval = "暂无数据";

    @Schema(description = "历史穿盘率最高区间穿盘率(%)", example = "88.89")
    private BigDecimal bestBreakRate = BigDecimal.ZERO;

    @Schema(description = "历史穿盘率最低风险区间", example = "客让 R5(2.01-2.20)")
    private String lowestBreakInterval = "暂无数据";

    @Schema(description = "历史穿盘率最低区间穿盘率(%)", example = "0.00")
    private BigDecimal lowestBreakRate = BigDecimal.ZERO;

    @Schema(description = "主客让综合倾向 (HOME_DOMINANT / AWAY_DOMINANT / BALANCED)", example = "HOME_DOMINANT")
    private String dominantSide = "BALANCED";

    @Schema(description = "AI大数据穿盘策略建议摘要", example = "主让低赔区间极具打穿价值，客让浅盘建议防平或走受让")
    private String adviceSummary = "";

    // ==================== 区间聚合明细 ====================
    @Schema(description = "主让六区间宏观聚合明细")
    private List<OddsIntervalItemVO> homeIntervals = new ArrayList<>();

    @Schema(description = "客让六区间宏观聚合明细")
    private List<OddsIntervalItemVO> awayIntervals = new ArrayList<>();

    @Schema(description = "全盘六区间合并宏观明细")
    private List<OddsIntervalItemVO> totalIntervals = new ArrayList<>();

    public HandicapOddsStatsSummaryVO() {
    }

    // Getters and Setters

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

    public Integer getTotalWinCount() {
        return totalWinCount;
    }

    public void setTotalWinCount(Integer totalWinCount) {
        this.totalWinCount = totalWinCount;
    }

    public Integer getTotalDrawCount() {
        return totalDrawCount;
    }

    public void setTotalDrawCount(Integer totalDrawCount) {
        this.totalDrawCount = totalDrawCount;
    }

    public Integer getTotalLossCount() {
        return totalLossCount;
    }

    public void setTotalLossCount(Integer totalLossCount) {
        this.totalLossCount = totalLossCount;
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

    public BigDecimal getOverallWinRate() {
        return overallWinRate;
    }

    public void setOverallWinRate(BigDecimal overallWinRate) {
        this.overallWinRate = overallWinRate;
    }

    public BigDecimal getOverallDrawRate() {
        return overallDrawRate;
    }

    public void setOverallDrawRate(BigDecimal overallDrawRate) {
        this.overallDrawRate = overallDrawRate;
    }

    public BigDecimal getOverallLossRate() {
        return overallLossRate;
    }

    public void setOverallLossRate(BigDecimal overallLossRate) {
        this.overallLossRate = overallLossRate;
    }

    public BigDecimal getOverallBreakRateOnWin() {
        return overallBreakRateOnWin;
    }

    public void setOverallBreakRateOnWin(BigDecimal overallBreakRateOnWin) {
        this.overallBreakRateOnWin = overallBreakRateOnWin;
    }

    public BigDecimal getOverallBreakRate() {
        return overallBreakRate;
    }

    public void setOverallBreakRate(BigDecimal overallBreakRate) {
        this.overallBreakRate = overallBreakRate;
    }

    public Integer getHomeTotalMatches() {
        return homeTotalMatches;
    }

    public void setHomeTotalMatches(Integer homeTotalMatches) {
        this.homeTotalMatches = homeTotalMatches;
    }

    public Integer getHomeTotalWin() {
        return homeTotalWin;
    }

    public void setHomeTotalWin(Integer homeTotalWin) {
        this.homeTotalWin = homeTotalWin;
    }

    public Integer getHomeTotalBreak() {
        return homeTotalBreak;
    }

    public void setHomeTotalBreak(Integer homeTotalBreak) {
        this.homeTotalBreak = homeTotalBreak;
    }

    public BigDecimal getHomeWinRate() {
        return homeWinRate;
    }

    public void setHomeWinRate(BigDecimal homeWinRate) {
        this.homeWinRate = homeWinRate;
    }

    public BigDecimal getHomeBreakRateOnWin() {
        return homeBreakRateOnWin;
    }

    public void setHomeBreakRateOnWin(BigDecimal homeBreakRateOnWin) {
        this.homeBreakRateOnWin = homeBreakRateOnWin;
    }

    public BigDecimal getHomeBreakRate() {
        return homeBreakRate;
    }

    public void setHomeBreakRate(BigDecimal homeBreakRate) {
        this.homeBreakRate = homeBreakRate;
    }

    public Integer getAwayTotalMatches() {
        return awayTotalMatches;
    }

    public void setAwayTotalMatches(Integer awayTotalMatches) {
        this.awayTotalMatches = awayTotalMatches;
    }

    public Integer getAwayTotalWin() {
        return awayTotalWin;
    }

    public void setAwayTotalWin(Integer awayTotalWin) {
        this.awayTotalWin = awayTotalWin;
    }

    public Integer getAwayTotalBreak() {
        return awayTotalBreak;
    }

    public void setAwayTotalBreak(Integer awayTotalBreak) {
        this.awayTotalBreak = awayTotalBreak;
    }

    public BigDecimal getAwayWinRate() {
        return awayWinRate;
    }

    public void setAwayWinRate(BigDecimal awayWinRate) {
        this.awayWinRate = awayWinRate;
    }

    public BigDecimal getAwayBreakRateOnWin() {
        return awayBreakRateOnWin;
    }

    public void setAwayBreakRateOnWin(BigDecimal awayBreakRateOnWin) {
        this.awayBreakRateOnWin = awayBreakRateOnWin;
    }

    public BigDecimal getAwayBreakRate() {
        return awayBreakRate;
    }

    public void setAwayBreakRate(BigDecimal awayBreakRate) {
        this.awayBreakRate = awayBreakRate;
    }

    public String getBestBreakInterval() {
        return bestBreakInterval;
    }

    public void setBestBreakInterval(String bestBreakInterval) {
        this.bestBreakInterval = bestBreakInterval;
    }

    public BigDecimal getBestBreakRate() {
        return bestBreakRate;
    }

    public void setBestBreakRate(BigDecimal bestBreakRate) {
        this.bestBreakRate = bestBreakRate;
    }

    public String getLowestBreakInterval() {
        return lowestBreakInterval;
    }

    public void setLowestBreakInterval(String lowestBreakInterval) {
        this.lowestBreakInterval = lowestBreakInterval;
    }

    public BigDecimal getLowestBreakRate() {
        return lowestBreakRate;
    }

    public void setLowestBreakRate(BigDecimal lowestBreakRate) {
        this.lowestBreakRate = lowestBreakRate;
    }

    public String getDominantSide() {
        return dominantSide;
    }

    public void setDominantSide(String dominantSide) {
        this.dominantSide = dominantSide;
    }

    public String getAdviceSummary() {
        return adviceSummary;
    }

    public void setAdviceSummary(String adviceSummary) {
        this.adviceSummary = adviceSummary;
    }

    public List<OddsIntervalItemVO> getHomeIntervals() {
        return homeIntervals;
    }

    public void setHomeIntervals(List<OddsIntervalItemVO> homeIntervals) {
        this.homeIntervals = homeIntervals;
    }

    public List<OddsIntervalItemVO> getAwayIntervals() {
        return awayIntervals;
    }

    public void setAwayIntervals(List<OddsIntervalItemVO> awayIntervals) {
        this.awayIntervals = awayIntervals;
    }

    public List<OddsIntervalItemVO> getTotalIntervals() {
        return totalIntervals;
    }

    public void setTotalIntervals(List<OddsIntervalItemVO> totalIntervals) {
        this.totalIntervals = totalIntervals;
    }
}
