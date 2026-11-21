package com.example.winnerLogBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "单场比赛开奖详细记录录入/更新请求")
public class HandicapMatchRecordRequest {

    @Schema(description = "记录ID (更新时必填)", example = "1")
    private Long id;

    @NotNull(message = "比赛日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "比赛日期", example = "2026-10-24", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate matchDate;

    @Schema(description = "批次/赛事组 (如: 欧冠第3轮, 周末精选)", example = "英超第10轮")
    private String batchTitle;

    @Schema(description = "联赛名称 (如: 英超, 西甲, 德甲)", example = "英超")
    private String leagueName;

    @Schema(description = "赛制类型 (LEAGUE:联赛积分制, CUP:杯赛淘汰赛, FRIENDLY:友谊赛, OTHER:其他)", example = "LEAGUE")
    private String matchSystem = "LEAGUE";


    @NotBlank(message = "主队名称不能为空")
    @Schema(description = "主队名称", example = "阿森纳", requiredMode = Schema.RequiredMode.REQUIRED)
    private String homeTeam;

    @NotBlank(message = "客队名称不能为空")
    @Schema(description = "客队名称", example = "利物浦", requiredMode = Schema.RequiredMode.REQUIRED)
    private String awayTeam;

    @NotBlank(message = "让球方不能为空 (HOME/AWAY)")
    @Schema(description = "让球方 (HOME:主让, AWAY:客让)", example = "HOME", requiredMode = Schema.RequiredMode.REQUIRED)
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

    @Schema(description = "让球方获胜赔率 (不填则自动按主让/客让取对应胜赔)", example = "1.55")
    private BigDecimal handicapOdds;

    @Schema(description = "半场比分", example = "1:0")
    private String halfScore;

    @Schema(description = "全场比分", example = "2:0")
    private String fullScore;

    @Schema(description = "最终赛果 (HOME_WIN/DRAW/AWAY_WIN)", example = "HOME_WIN")
    private String matchResult;

    @Schema(description = "赛果对应的结果赔 (不填则根据赛果自动取胜平负赔率)", example = "1.55")
    private BigDecimal resultOdds;

    @Schema(description = "让球赛果 (WIN/DRAW/LOSS)", example = "WIN")
    private String handicapResult;

    @Schema(description = "让球赛果对应的让球赔", example = "2.45")
    private BigDecimal handicapResultOdds;

    @Schema(description = "半全场结果 (如: 胜胜, 平胜)", example = "胜胜")
    private String halfFullResult;

    @Schema(description = "全场总进球数", example = "2")
    private Integer totalGoals;

    @Schema(description = "是否穿盘 (打穿盘口: true=穿盘, false=未穿盘)", example = "true")
    private Boolean isBreak;

    @Schema(description = "复盘心得与备注", example = "主队压制力十足，2-0轻松打穿-1球盘")
    private String remarks;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public BigDecimal getHandicapOdds() {
        return handicapOdds;
    }

    public void setHandicapOdds(BigDecimal handicapOdds) {
        this.handicapOdds = handicapOdds;
    }

    public String getHalfScore() {
        return halfScore;
    }

    public void setHalfScore(String halfScore) {
        this.halfScore = halfScore;
    }

    public String getFullScore() {
        return fullScore;
    }

    public void setFullScore(String fullScore) {
        this.fullScore = fullScore;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    public BigDecimal getResultOdds() {
        return resultOdds;
    }

    public void setResultOdds(BigDecimal resultOdds) {
        this.resultOdds = resultOdds;
    }

    public String getHandicapResult() {
        return handicapResult;
    }

    public void setHandicapResult(String handicapResult) {
        this.handicapResult = handicapResult;
    }

    public BigDecimal getHandicapResultOdds() {
        return handicapResultOdds;
    }

    public void setHandicapResultOdds(BigDecimal handicapResultOdds) {
        this.handicapResultOdds = handicapResultOdds;
    }

    public String getHalfFullResult() {
        return halfFullResult;
    }

    public void setHalfFullResult(String halfFullResult) {
        this.halfFullResult = halfFullResult;
    }

    public Integer getTotalGoals() {
        return totalGoals;
    }

    public void setTotalGoals(Integer totalGoals) {
        this.totalGoals = totalGoals;
    }

    public Boolean getIsBreak() {
        return isBreak;
    }

    public void setIsBreak(Boolean isBreak) {
        this.isBreak = isBreak;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMatchSystem() {
        return matchSystem;
    }

    public void setMatchSystem(String matchSystem) {
        this.matchSystem = matchSystem;
    }
}

