package com.example.winnerLogBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "根据0球赔率与7球赔率测算进球数分布与打单建议请求")
public class ExtremeGoalsSimulateRequest {

    @Schema(description = "联赛名称 (如 日职联, 荷甲, 英超, 全部 ALL)", example = "ALL")
    private String leagueName = "ALL";

    @Schema(description = "赛制类型 (LEAGUE:联赛, CUP:杯赛淘汰赛, FRIENDLY:友谊赛, ALL:全部)", example = "ALL")
    private String matchSystem = "ALL";

    @NotNull(message = "0球赔率不能为空")
    @Schema(description = "待测算比赛的0球赔率", example = "9.50", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal zeroGoalsOdds;

    @NotNull(message = "7球+赔率不能为空")
    @Schema(description = "待测算比赛的7球+赔率", example = "28.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal sevenGoalsOdds;

    @Schema(description = "0球赔率匹配容差", example = "1.00")
    private BigDecimal zeroTolerance = new BigDecimal("1.00");

    @Schema(description = "7球赔率匹配容差", example = "4.00")
    private BigDecimal sevenTolerance = new BigDecimal("4.00");

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getMatchSystem() {
        return matchSystem;
    }

    public void setMatchSystem(String matchSystem) {
        this.matchSystem = matchSystem;
    }

    public BigDecimal getZeroGoalsOdds() {
        return zeroGoalsOdds;
    }

    public void setZeroGoalsOdds(BigDecimal zeroGoalsOdds) {
        this.zeroGoalsOdds = zeroGoalsOdds;
    }

    public BigDecimal getSevenGoalsOdds() {
        return sevenGoalsOdds;
    }

    public void setSevenGoalsOdds(BigDecimal sevenGoalsOdds) {
        this.sevenGoalsOdds = sevenGoalsOdds;
    }

    public BigDecimal getZeroTolerance() {
        return zeroTolerance;
    }

    public void setZeroTolerance(BigDecimal zeroTolerance) {
        this.zeroTolerance = zeroTolerance;
    }

    public BigDecimal getSevenTolerance() {
        return sevenTolerance;
    }

    public void setSevenTolerance(BigDecimal sevenTolerance) {
        this.sevenTolerance = sevenTolerance;
    }
}
