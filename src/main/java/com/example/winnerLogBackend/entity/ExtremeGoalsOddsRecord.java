package com.example.winnerLogBackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 0球赔率与7球+两极进球赔率大数据统计记录实体
 * 记录比赛日期、联赛（日职、韩职、澳超、巴甲、葡超、挪超、荷甲、五大联赛、欧冠等）、赛制、0球赔率、7球+赔率及实际打出总进球数
 */
@Entity
@Table(name = "extreme_goals_odds_record", indexes = {
        @Index(name = "idx_egor_match_date", columnList = "match_date"),
        @Index(name = "idx_egor_league_name", columnList = "league_name"),
        @Index(name = "idx_egor_match_system", columnList = "match_system"),
        @Index(name = "idx_egor_zero_odds", columnList = "zero_goals_odds"),
        @Index(name = "idx_egor_seven_odds", columnList = "seven_goals_odds"),
        @Index(name = "idx_egor_total_goals", columnList = "total_goals"),
        @Index(name = "idx_egor_goals_category", columnList = "goals_category")
})
public class ExtremeGoalsOddsRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 比赛日期 (YYYY-MM-DD) */
    @Column(name = "match_date", nullable = false)
    private LocalDate matchDate;

    /** 批次/轮次/分组名称 (如: 周六日职精选, 荷甲第12轮) */
    @Column(name = "batch_title", length = 100)
    private String batchTitle;

    /** 联赛名称 (如: 日职联, 韩职K联, 澳超, 巴甲, 葡超, 挪超, 荷甲, 英超, 西甲, 意甲, 德甲, 法甲, 欧冠等) */
    @Column(name = "league_name", nullable = false, length = 100)
    private String leagueName;

    /** 赛制类型: LEAGUE(联赛积分制), CUP(杯赛/淘汰赛), FRIENDLY(友谊赛), OTHER(其他) */
    @Column(name = "match_system", nullable = false, length = 30)
    private String matchSystem = "LEAGUE";

    /** 主队名称 (可选) */
    @Column(name = "home_team", length = 100)
    private String homeTeam;

    /** 客队名称 (可选) */
    @Column(name = "away_team", length = 100)
    private String awayTeam;

    /** 0球打出赔率 (总进球玩法0球初盘/终盘赔率，如 9.50, 12.00) */
    @Column(name = "zero_goals_odds", nullable = false, precision = 6, scale = 2)
    private BigDecimal zeroGoalsOdds;

    /** 7球+打出赔率 (总进球玩法7+初盘/终盘赔率，如 26.00, 35.00) */
    @Column(name = "seven_goals_odds", nullable = false, precision = 6, scale = 2)
    private BigDecimal sevenGoalsOdds;

    /**
     * 7球赔率与0球赔率比值 (Ratio = sevenGoalsOdds / zeroGoalsOdds)
     * 比值高 (>3.5) 说明机构极力防范0球小比分；比值低 (<2.5) 说明机构极力防范大球
     */
    @Column(name = "odds_ratio", precision = 6, scale = 2)
    private BigDecimal oddsRatio;

    /** 全场实际开奖总进球数 (0, 1, 2, 3, 4, 5, 6, 7及以上) */
    @Column(name = "total_goals", nullable = false)
    private Integer totalGoals;

    /** 全场比分 (如 "1:1", "3:2"，可选) */
    @Column(name = "full_score", length = 20)
    private String fullScore;

    /**
     * 进球区间分类:
     * ZERO_ONE(0-1球小球), TWO_THREE(2-3球黄金区), FOUR_SIX(4-6球大球), SEVEN_PLUS(7+超大球)
     */
    @Column(name = "goals_category", length = 30)
    private String goalsCategory;

    /** 复盘心得与备忘 */
    @Column(name = "remarks", length = 500)
    private String remarks;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ExtremeGoalsOddsRecord() {}

    /**
     * 自动推导计算衍生字段
     */
    public void autoCalculateDerivedFields() {
        // 1. 根据比分推导总进球数
        if (fullScore != null && fullScore.contains(":")) {
            String[] parts = fullScore.trim().split(":");
            if (parts.length == 2) {
                try {
                    int hg = Integer.parseInt(parts[0].trim());
                    int ag = Integer.parseInt(parts[1].trim());
                    this.totalGoals = hg + ag;
                } catch (NumberFormatException ignored) {}
            }
        }

        // 2. 推导赛制类型
        if (this.matchSystem == null || this.matchSystem.trim().isEmpty()) {
            if (leagueName != null) {
                String ln = leagueName.trim();
                if (ln.contains("杯") || ln.contains("冠") || ln.contains("淘汰") || ln.contains("锦标") || ln.contains("盾")) {
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

        // 3. 计算两极赔率比率 (sevenGoalsOdds / zeroGoalsOdds)
        if (zeroGoalsOdds != null && sevenGoalsOdds != null && zeroGoalsOdds.compareTo(BigDecimal.ZERO) > 0) {
            this.oddsRatio = sevenGoalsOdds.divide(zeroGoalsOdds, 2, RoundingMode.HALF_UP);
        }

        // 4. 判定进球数分类
        if (totalGoals != null) {
            if (totalGoals <= 1) {
                this.goalsCategory = "ZERO_ONE";
            } else if (totalGoals <= 3) {
                this.goalsCategory = "TWO_THREE";
            } else if (totalGoals <= 6) {
                this.goalsCategory = "FOUR_SIX";
            } else {
                this.goalsCategory = "SEVEN_PLUS";
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

    public BigDecimal getOddsRatio() {
        return oddsRatio;
    }

    public void setOddsRatio(BigDecimal oddsRatio) {
        this.oddsRatio = oddsRatio;
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

    public String getGoalsCategory() {
        return goalsCategory;
    }

    public void setGoalsCategory(String goalsCategory) {
        this.goalsCategory = goalsCategory;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
