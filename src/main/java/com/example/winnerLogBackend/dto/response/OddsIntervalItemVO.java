package com.example.winnerLogBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 让球方获胜赔率单区间多维分析VO
 * 便于前端矩阵图表统一对齐渲染
 */
@Schema(description = "让球赔率区间明细统计VO")
public class OddsIntervalItemVO {

    @Schema(description = "区间代码", example = "R1")
    private String intervalCode;

    @Schema(description = "区间名称与赔率范围", example = "1.40及以下")
    private String intervalName;

    @Schema(description = "主客让球方向(HOME/AWAY/TOTAL)", example = "HOME")
    private String side;

    @Schema(description = "主客让球中文名称", example = "主让球")
    private String sideName;

    @Schema(description = "胜场数", example = "4")
    private Integer winCount = 0;

    @Schema(description = "平场数", example = "1")
    private Integer drawCount = 0;

    @Schema(description = "负场数", example = "0")
    private Integer lossCount = 0;

    @Schema(description = "区间总场数", example = "5")
    private Integer totalMatches = 0;

    @Schema(description = "赢球穿盘数", example = "3")
    private Integer breakCount = 0;

    @Schema(description = "赢球未穿盘数 (赢球走水/卡盘)", example = "1")
    private Integer unbreakCount = 0;

    @Schema(description = "胜率(%)", example = "80.00")
    private BigDecimal winRate = BigDecimal.ZERO;

    @Schema(description = "平局率(%)", example = "20.00")
    private BigDecimal drawRate = BigDecimal.ZERO;

    @Schema(description = "负率/下盘爆冷率(%)", example = "0.00")
    private BigDecimal lossRate = BigDecimal.ZERO;

    @Schema(description = "赢球时的穿盘率(%: 穿盘数/胜场数)", example = "75.00")
    private BigDecimal breakRateOnWin = BigDecimal.ZERO;

    @Schema(description = "区间综合穿盘率(%: 穿盘数/总场数)", example = "60.00")
    private BigDecimal overallBreakRate = BigDecimal.ZERO;

    public OddsIntervalItemVO() {
    }

    public OddsIntervalItemVO(String intervalCode, String intervalName, String side, String sideName,
                              Integer win, Integer draw, Integer loss, Integer breakCnt) {
        this.intervalCode = intervalCode;
        this.intervalName = intervalName;
        this.side = side;
        this.sideName = sideName;
        this.winCount = win != null ? win : 0;
        this.drawCount = draw != null ? draw : 0;
        this.lossCount = loss != null ? loss : 0;
        this.breakCount = breakCnt != null ? breakCnt : 0;

        this.totalMatches = this.winCount + this.drawCount + this.lossCount;
        this.unbreakCount = Math.max(0, this.winCount - this.breakCount);

        if (this.totalMatches > 0) {
            this.winRate = BigDecimal.valueOf(this.winCount * 100.0 / this.totalMatches)
                    .setScale(2, RoundingMode.HALF_UP);
            this.drawRate = BigDecimal.valueOf(this.drawCount * 100.0 / this.totalMatches)
                    .setScale(2, RoundingMode.HALF_UP);
            this.lossRate = BigDecimal.valueOf(this.lossCount * 100.0 / this.totalMatches)
                    .setScale(2, RoundingMode.HALF_UP);
            this.overallBreakRate = BigDecimal.valueOf(this.breakCount * 100.0 / this.totalMatches)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        if (this.winCount > 0) {
            this.breakRateOnWin = BigDecimal.valueOf(this.breakCount * 100.0 / this.winCount)
                    .setScale(2, RoundingMode.HALF_UP);
        }
    }

    // Getters and Setters

    public String getIntervalCode() {
        return intervalCode;
    }

    public void setIntervalCode(String intervalCode) {
        this.intervalCode = intervalCode;
    }

    public String getIntervalName() {
        return intervalName;
    }

    public void setIntervalName(String intervalName) {
        this.intervalName = intervalName;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getSideName() {
        return sideName;
    }

    public void setSideName(String sideName) {
        this.sideName = sideName;
    }

    public Integer getWinCount() {
        return winCount;
    }

    public void setWinCount(Integer winCount) {
        this.winCount = winCount;
    }

    public Integer getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(Integer drawCount) {
        this.drawCount = drawCount;
    }

    public Integer getLossCount() {
        return lossCount;
    }

    public void setLossCount(Integer lossCount) {
        this.lossCount = lossCount;
    }

    public Integer getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(Integer totalMatches) {
        this.totalMatches = totalMatches;
    }

    public Integer getBreakCount() {
        return breakCount;
    }

    public void setBreakCount(Integer breakCount) {
        this.breakCount = breakCount;
    }

    public Integer getUnbreakCount() {
        return unbreakCount;
    }

    public void setUnbreakCount(Integer unbreakCount) {
        this.unbreakCount = unbreakCount;
    }

    public BigDecimal getWinRate() {
        return winRate;
    }

    public void setWinRate(BigDecimal winRate) {
        this.winRate = winRate;
    }

    public BigDecimal getDrawRate() {
        return drawRate;
    }

    public void setDrawRate(BigDecimal drawRate) {
        this.drawRate = drawRate;
    }

    public BigDecimal getLossRate() {
        return lossRate;
    }

    public void setLossRate(BigDecimal lossRate) {
        this.lossRate = lossRate;
    }

    public BigDecimal getBreakRateOnWin() {
        return breakRateOnWin;
    }

    public void setBreakRateOnWin(BigDecimal breakRateOnWin) {
        this.breakRateOnWin = breakRateOnWin;
    }

    public BigDecimal getOverallBreakRate() {
        return overallBreakRate;
    }

    public void setOverallBreakRate(BigDecimal overallBreakRate) {
        this.overallBreakRate = overallBreakRate;
    }
}
