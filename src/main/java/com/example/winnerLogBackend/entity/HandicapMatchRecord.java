package com.example.winnerLogBackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 足球单场精细化开奖明细与让球赛果记录实体
 * 记录每场比赛的胜赔（分主客）、开奖结果（结果及结果赔、让球赔、半全场、总进球）、是否穿盘
 */
@Entity
@Table(name = "handicap_match_record", indexes = {
        @Index(name = "idx_hmr_match_date", columnList = "match_date"),
        @Index(name = "idx_hmr_handicap_side", columnList = "handicap_side"),
        @Index(name = "idx_hmr_handicap_odds", columnList = "handicap_odds"),
        @Index(name = "idx_hmr_is_break", columnList = "is_break"),
        @Index(name = "idx_hmr_match_result", columnList = "match_result"),
        @Index(name = "idx_hmr_match_system", columnList = "match_system"),
        @Index(name = "idx_hmr_batch_title", columnList = "batch_title")
})
public class HandicapMatchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 比赛日期 (YYYY-MM-DD) */
    @Column(name = "match_date", nullable = false)
    private LocalDate matchDate;

    /** 批次/轮次/分组名称 (如: 英超第10轮, 周六早盘精选) */
    @Column(name = "batch_title", length = 100)
    private String batchTitle;

    /** 联赛名称 (如: 英超, 西甲, 意甲, 德甲, 欧冠) */
    @Column(name = "league_name", length = 100)
    private String leagueName;

    /** 赛制类型: LEAGUE(联赛积分制), CUP(杯赛/淘汰赛), FRIENDLY(友谊赛), OTHER(其他) */
    @Column(name = "match_system", length = 30)
    private String matchSystem = "LEAGUE";

    /** 主队名称 */
    @Column(name = "home_team", nullable = false, length = 100)
    private String homeTeam;

    /** 客队名称 */
    @Column(name = "away_team", nullable = false, length = 100)
    private String awayTeam;

    /** 让球方: HOME(主让), AWAY(客让) */
    @Column(name = "handicap_side", nullable = false, length = 20)
    private String handicapSide = "HOME";

    /**
     * 让球盘口数值 (如 -1.00 表示让1球, -0.75 半一, -1.25 一球球半, -2.00 让2球)
     * 若让球方让1球，数值存 -1.00
     */
    @Column(name = "handicap_value", nullable = false, precision = 4, scale = 2)
    private BigDecimal handicapValue = new BigDecimal("-1.00");

    /** 主胜赔率 */
    @Column(name = "home_win_odds", nullable = false, precision = 6, scale = 2)
    private BigDecimal homeWinOdds;

    /** 平局赔率 */
    @Column(name = "draw_odds", precision = 6, scale = 2)
    private BigDecimal drawOdds;

    /** 客胜赔率 */
    @Column(name = "away_win_odds", nullable = false, precision = 6, scale = 2)
    private BigDecimal awayWinOdds;

    /**
     * 让球方获胜赔率 (核心步长统计字段)
     * 主让时通常为 homeWinOdds, 客让时为 awayWinOdds
     */
    @Column(name = "handicap_odds", nullable = false, precision = 6, scale = 2)
    private BigDecimal handicapOdds;

    /** 半场比分 (如 "1:0", "0:0") */
    @Column(name = "half_score", length = 20)
    private String halfScore;

    /** 全场比分 (如 "2:0", "3:1") */
    @Column(name = "full_score", length = 20)
    private String fullScore;

    /** 最终开奖赛果: HOME_WIN(主胜), DRAW(平), AWAY_WIN(客胜) */
    @Column(name = "match_result", nullable = false, length = 20)
    private String matchResult;

    /** 赛果对应的结果赔 (如打出主胜则为当时主胜赔率 1.55) */
    @Column(name = "result_odds", nullable = false, precision = 6, scale = 2)
    private BigDecimal resultOdds;

    /** 让球开奖结果: WIN(让胜), DRAW(让平/走水), LOSS(让负) */
    @Column(name = "handicap_result", length = 20)
    private String handicapResult;

    /** 让球开奖结果对应的赔率 (如让球胜赔 2.45) */
    @Column(name = "handicap_result_odds", precision = 6, scale = 2)
    private BigDecimal handicapResultOdds;

    /** 半全场开奖结果 (如: 胜胜, 平胜, 胜平, 平平, 负负 等) */
    @Column(name = "half_full_result", length = 20)
    private String halfFullResult;

    /** 全场总进球数 (如 0, 1, 2, 3, 4...) */
    @Column(name = "total_goals")
    private Integer totalGoals;

    /** 是否穿盘 (打穿盘口: true=穿盘, false=未穿盘/卡盘走水/输盘) */
    @Column(name = "is_break", nullable = false)
    private Boolean isBreak = false;

    /** 复盘心得与备注 */
    @Column(name = "remarks", length = 500)
    private String remarks;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public HandicapMatchRecord() {}

    /**
     * 辅助方法：若比分存在，自动根据比分推导赛果、总进球、半全场与穿盘状态
     */
    public void autoCalculateDerivedFields() {
        // 0. 智能推导赛制类型 (若未指定)
        if (this.matchSystem == null || this.matchSystem.trim().isEmpty()) {
            if (leagueName != null) {
                String ln = leagueName.trim();
                if (ln.contains("杯") || ln.contains("冠") || ln.contains("欧联") || ln.contains("淘汰") || ln.contains("锦标") || ln.contains("盾")) {
                    this.matchSystem = "CUP";
                } else if (ln.contains("友谊") || ln.contains("热身")) {
                    this.matchSystem = "FRIENDLY";
                } else {
                    this.matchSystem = "LEAGUE";
                }
            } else {
                this.matchSystem = "LEAGUE";
            }
        }

        // 1. 同步让球方赔率
        if (handicapOdds == null) {
            if ("HOME".equalsIgnoreCase(handicapSide)) {
                this.handicapOdds = homeWinOdds;
            } else {
                this.handicapOdds = awayWinOdds;
            }
        }


        // 2. 解析全场比分
        if (fullScore != null && fullScore.contains(":")) {
            String[] fullParts = fullScore.trim().split(":");
            if (fullParts.length == 2) {
                try {
                    int hg = Integer.parseInt(fullParts[0].trim());
                    int ag = Integer.parseInt(fullParts[1].trim());
                    this.totalGoals = hg + ag;

                    // 赛果判定
                    if (matchResult == null || matchResult.trim().isEmpty()) {
                        if (hg > ag) {
                            this.matchResult = "HOME_WIN";
                        } else if (hg == ag) {
                            this.matchResult = "DRAW";
                        } else {
                            this.matchResult = "AWAY_WIN";
                        }
                    }

                    // 结果赔自动绑定
                    if (resultOdds == null) {
                        if ("HOME_WIN".equals(matchResult)) {
                            this.resultOdds = homeWinOdds;
                        } else if ("DRAW".equals(matchResult)) {
                            this.resultOdds = drawOdds != null ? drawOdds : BigDecimal.ZERO;
                        } else if ("AWAY_WIN".equals(matchResult)) {
                            this.resultOdds = awayWinOdds;
                        }
                    }

                    // 让球赛果推算与是否穿盘
                    // 让球盘口：如果 handicapValue 为 -1.0，主让1球；如果为 1.0，主受让1球
                    double handicapVal = (handicapValue != null) ? handicapValue.doubleValue() : -1.0;
                    double diff;
                    if ("HOME".equalsIgnoreCase(handicapSide)) {
                        // 主让球方: 主队净胜球 + handicapVal (通常盘口如-1.0，净胜2球则 2 - 1 = +1 > 0)
                        diff = (hg - ag) + handicapVal;
                    } else {
                        // 客让球方: 客队净胜球 + handicapVal
                        diff = (ag - hg) + handicapVal;
                    }

                    if (diff > 0.001) {
                        this.handicapResult = "WIN";
                        this.isBreak = true; // 净胜球超过让球数，打穿盘口
                    } else if (Math.abs(diff) <= 0.001) {
                        this.handicapResult = "DRAW"; // 刚好卡盘/走水
                        this.isBreak = false;
                    } else {
                        this.handicapResult = "LOSS";
                        this.isBreak = false;
                    }

                } catch (NumberFormatException ignored) {}
            }
        }

        // 3. 解析半全场
        if (halfScore != null && halfScore.contains(":") && fullScore != null && fullScore.contains(":")) {
            String[] halfParts = halfScore.trim().split(":");
            String[] fullParts = fullScore.trim().split(":");
            if (halfParts.length == 2 && fullParts.length == 2) {
                try {
                    int hh = Integer.parseInt(halfParts[0].trim());
                    int ha = Integer.parseInt(halfParts[1].trim());
                    int fh = Integer.parseInt(fullParts[0].trim());
                    int fa = Integer.parseInt(fullParts[1].trim());

                    String halfRes = hh > ha ? "胜" : (hh == ha ? "平" : "负");
                    String fullRes = fh > fa ? "胜" : (fh == fa ? "平" : "负");
                    this.halfFullResult = halfRes + fullRes;
                } catch (NumberFormatException ignored) {}
            }
        }
    }

    // ==================== Getters & Setters ====================

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getMatchSystem() {
        return matchSystem;
    }

    public void setMatchSystem(String matchSystem) {
        this.matchSystem = matchSystem;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

