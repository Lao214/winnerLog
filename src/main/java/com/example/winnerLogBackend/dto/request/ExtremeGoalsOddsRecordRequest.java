package com.example.winnerLogBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "0球与7球+两极进球赔率录入请求")
public class ExtremeGoalsOddsRecordRequest {

    @NotNull(message = "比赛日期不能为空")
    @Schema(description = "比赛日期 (YYYY-MM-DD)", example = "2026-10-25", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate matchDate;

    @Schema(description = "批次/轮次/分组名称 (如 日职周六精选, 荷甲第10轮)", example = "日职联第30轮")
    private String batchTitle;

    @NotNull(message = "联赛名称不能为空")
    @Schema(description = "联赛名称 (如 日职联, 韩职K联, 澳超, 巴甲, 葡超, 挪超, 荷甲, 英超, 西甲, 意甲, 德甲, 法甲, 欧冠等)", example = "日职联", requiredMode = Schema.RequiredMode.REQUIRED)
    private String leagueName;

    @Schema(description = "赛制类型 (LEAGUE:联赛, CUP:杯赛淘汰赛, FRIENDLY:友谊赛, OTHER:其他)", example = "LEAGUE")
    private String matchSystem = "LEAGUE";

    @Schema(description = "主队名称", example = "神户胜利船")
    private String homeTeam;

    @Schema(description = "客队名称", example = "横滨水手")
    private String awayTeam;

    @NotNull(message = "0球赔率不能为空")
    @Schema(description = "0球打出赔率", example = "9.50", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal zeroGoalsOdds;

    @NotNull(message = "7球+赔率不能为空")
    @Schema(description = "7球+打出赔率", example = "28.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal sevenGoalsOdds;

    @NotNull(message = "实际总进球数不能为空")
    @Schema(description = "全场实际开奖总进球数 (0, 1, 2, 3, 4, 5, 6, 7+)", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer totalGoals;

    @Schema(description = "全场比分 (如 2:1)", example = "2:0")
    private String fullScore;

    @Schema(description = "复盘备忘/心得", example = "日职强强对话，0球9.50防守盘，最终2球平稳打出")
    private String remarks;

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public String getBatchTitle() {
        return batchTitle;
    }

    public void setBatchTitle(String batchTitle) {
        this.batchTitle = batchTitle;
    }

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

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
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

    public Integer getTotalGoals() {
        return totalGoals;
    }

    public void setTotalGoals(Integer totalGoals) {
        this.totalGoals = totalGoals;
    }

    public String getFullScore() {
        return fullScore;
    }

    public void setFullScore(String fullScore) {
        this.fullScore = fullScore;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
