package com.example.winnerLogBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;

@Schema(description = "单场比赛开奖概率与ROI模拟测算请求")
public class HandicapMatchSimulateRequest {

    @Schema(description = "赛制类型 (LEAGUE:联赛, CUP:杯赛淘汰赛, FRIENDLY:友谊赛, ALL:全部)", example = "LEAGUE")
    private String matchSystem = "ALL";

    @Schema(description = "联赛名称筛选 (如: 英超, 西甲, 日职联, ALL:全部)", example = "ALL")
    private String leagueName = "ALL";


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

    @Schema(description = "让球主胜 (让胜) 赔率", example = "2.45")
    private BigDecimal handicapHomeWinOdds;

    @Schema(description = "让球平局 (让平) 赔率", example = "3.50")
    private BigDecimal handicapDrawOdds;

    @Schema(description = "让球客胜 (让负) 赔率", example = "2.30")
    private BigDecimal handicapAwayWinOdds;


    @Schema(description = "样本赔率容差步长 (如 0.05, 0.10, 0.15, 0.20)", example = "0.10")
    private BigDecimal tolerance = new BigDecimal("0.10");

    @Schema(description = "进球数对应赔率字典 (如 {'0': 9.5, '1': 4.2, '2': 3.4, '3': 3.8, ...})")
    private Map<String, BigDecimal> goalsOdds;

    @Schema(description = "半场胜平负对应赔率字典 (如 {'胜胜': 2.30, '平胜': 4.20, ...})")
    private Map<String, BigDecimal> halfFullOdds;

    @Schema(description = "比分对应赔率字典 (如 {'1:0': 6.5, '2:1': 8.0, ...})")
    private Map<String, BigDecimal> scoreOdds;

    public String getMatchSystem() {
        return matchSystem;
    }

    public void setMatchSystem(String matchSystem) {
        this.matchSystem = matchSystem;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
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

    public Map<String, BigDecimal> getGoalsOdds() {
        return goalsOdds;
    }

    public void setGoalsOdds(Map<String, BigDecimal> goalsOdds) {
        this.goalsOdds = goalsOdds;
    }

    public Map<String, BigDecimal> getHalfFullOdds() {
        return halfFullOdds;
    }

    public void setHalfFullOdds(Map<String, BigDecimal> halfFullOdds) {
        this.halfFullOdds = halfFullOdds;
    }

    public Map<String, BigDecimal> getScoreOdds() {
        return scoreOdds;
    }

    public void setScoreOdds(Map<String, BigDecimal> scoreOdds) {
        this.scoreOdds = scoreOdds;
    }

    public BigDecimal getHandicapHomeWinOdds() {
        return handicapHomeWinOdds;
    }

    public void setHandicapHomeWinOdds(BigDecimal handicapHomeWinOdds) {
        this.handicapHomeWinOdds = handicapHomeWinOdds;
    }

    public BigDecimal getHandicapDrawOdds() {
        return handicapDrawOdds;
    }

    public void setHandicapDrawOdds(BigDecimal handicapDrawOdds) {
        this.handicapDrawOdds = handicapDrawOdds;
    }

    public BigDecimal getHandicapAwayWinOdds() {
        return handicapAwayWinOdds;
    }

    public void setHandicapAwayWinOdds(BigDecimal handicapAwayWinOdds) {
        this.handicapAwayWinOdds = handicapAwayWinOdds;
    }
}

