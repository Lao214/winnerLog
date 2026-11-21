package com.example.winnerLogBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "单场比赛开奖概率与ROI模拟测算请求")
public class HandicapMatchSimulateRequest {

    @Schema(description = "赛制类型 (LEAGUE:联赛, CUP:杯赛淘汰赛, FRIENDLY:友谊赛, ALL:全部)", example = "LEAGUE")
    private String matchSystem = "ALL";

    @Schema(description = "让球方 (HOME:主让, AWAY:客让)", example = "HOME")
    private String handicapSide = "HOME";

    @Schema(description = "让球盘口数值 (如 -1.00 表示让1球)", example = "-1.00")
    private BigDecimal handicapValue = new BigDecimal("-1.00");

    @NotNull(message = "主胜赔率不能为空")
    @Schema(description = "主胜赔率", example = "1.55", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal homeWinOdds;

    @Schema(description = "平局赔率", example = "3.80")
    private BigDecimal drawOdds;

    @NotNull(message = "客胜赔率不能为空")
    @Schema(description = "客胜赔率", example = "5.20", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal awayWinOdds;

    @Schema(description = "样本赔率容差步长 (如 0.05, 0.10, 0.15, 0.20)", example = "0.10")
    private BigDecimal tolerance = new BigDecimal("0.10");

    public String getMatchSystem() {
        return matchSystem;
    }

    public void setMatchSystem(String matchSystem) {
        this.matchSystem = matchSystem;
    }

    public String getHandicapSide() {
        return handicapSide;
    }

    public void setHandicapSide(String handicapSide) {
        this.handicapSide = handicapSide;
    }

    public BigDecimal getHandicapValue() {
        return handicapValue;
    }

    public void setHandicapValue(BigDecimal handicapValue) {
        this.handicapValue = handicapValue;
    }

    public BigDecimal getHomeWinOdds() {
        return homeWinOdds;
    }

    public void setHomeWinOdds(BigDecimal homeWinOdds) {
        this.homeWinOdds = homeWinOdds;
    }

    public BigDecimal getDrawOdds() {
        return drawOdds;
    }

    public void setDrawOdds(BigDecimal drawOdds) {
        this.drawOdds = drawOdds;
    }

    public BigDecimal getAwayWinOdds() {
        return awayWinOdds;
    }

    public void setAwayWinOdds(BigDecimal awayWinOdds) {
        this.awayWinOdds = awayWinOdds;
    }

    public BigDecimal getTolerance() {
        return tolerance;
    }

    public void setTolerance(BigDecimal tolerance) {
        this.tolerance = tolerance;
    }
}
