package com.example.winnerLogBackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 让球方获胜赔率六区间胜平负与穿盘率大数据战绩实体
 * 六大赔率区间：
 * R1: <= 1.40
 * R2: 1.41 - 1.60
 * R3: 1.61 - 1.80
 * R4: 1.81 - 2.00
 * R5: 2.01 - 2.20
 * R6: >= 2.21
 * 区分主让球 (Home) 与客让球 (Away)
 */
@Entity
@Table(name = "handicap_odds_stats_record", indexes = {
        @Index(name = "uk_handicap_record_date", columnList = "record_date", unique = true),
        @Index(name = "idx_handicap_record_date", columnList = "record_date"),
        @Index(name = "idx_handicap_overall_win", columnList = "overall_win_rate"),
        @Index(name = "idx_handicap_overall_break", columnList = "overall_break_rate")
})
public class HandicapOddsStatsRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_date", nullable = false, unique = true)
    private LocalDate recordDate;

    @Column(name = "batch_title", length = 100)
    private String batchTitle;

    // ==================== 1. 全盘综合汇总 ====================
    @Column(name = "total_matches", nullable = false)
    private Integer totalMatches = 0;

    @Column(name = "total_win_count", nullable = false)
    private Integer totalWinCount = 0;

    @Column(name = "total_draw_count", nullable = false)
    private Integer totalDrawCount = 0;

    @Column(name = "total_loss_count", nullable = false)
    private Integer totalLossCount = 0;

    @Column(name = "total_break_count", nullable = false)
    private Integer totalBreakCount = 0;

    @Column(name = "overall_win_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal overallWinRate = BigDecimal.ZERO;

    @Column(name = "overall_break_rate_on_win", nullable = false, precision = 5, scale = 2)
    private BigDecimal overallBreakRateOnWin = BigDecimal.ZERO;

    @Column(name = "overall_break_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal overallBreakRate = BigDecimal.ZERO;

    // ==================== 2. 主让球总体汇总 ====================
    @Column(name = "home_total_matches", nullable = false)
    private Integer homeTotalMatches = 0;

    @Column(name = "home_total_win", nullable = false)
    private Integer homeTotalWin = 0;

    @Column(name = "home_total_draw", nullable = false)
    private Integer homeTotalDraw = 0;

    @Column(name = "home_total_loss", nullable = false)
    private Integer homeTotalLoss = 0;

    @Column(name = "home_total_break", nullable = false)
    private Integer homeTotalBreak = 0;

    @Column(name = "home_win_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal homeWinRate = BigDecimal.ZERO;

    @Column(name = "home_break_rate_on_win", nullable = false, precision = 5, scale = 2)
    private BigDecimal homeBreakRateOnWin = BigDecimal.ZERO;

    @Column(name = "home_break_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal homeBreakRate = BigDecimal.ZERO;

    // ==================== 3. 客让球总体汇总 ====================
    @Column(name = "away_total_matches", nullable = false)
    private Integer awayTotalMatches = 0;

    @Column(name = "away_total_win", nullable = false)
    private Integer awayTotalWin = 0;

    @Column(name = "away_total_draw", nullable = false)
    private Integer awayTotalDraw = 0;

    @Column(name = "away_total_loss", nullable = false)
    private Integer awayTotalLoss = 0;

    @Column(name = "away_total_break", nullable = false)
    private Integer awayTotalBreak = 0;

    @Column(name = "away_win_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal awayWinRate = BigDecimal.ZERO;

    @Column(name = "away_break_rate_on_win", nullable = false, precision = 5, scale = 2)
    private BigDecimal awayBreakRateOnWin = BigDecimal.ZERO;

    @Column(name = "away_break_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal awayBreakRate = BigDecimal.ZERO;

    // ==================== 4. 主让球六区间明细 ====================
    // R1: <= 1.40
    @Column(name = "home_r1_win", nullable = false)
    private Integer homeR1Win = 0;
    @Column(name = "home_r1_draw", nullable = false)
    private Integer homeR1Draw = 0;
    @Column(name = "home_r1_loss", nullable = false)
    private Integer homeR1Loss = 0;
    @Column(name = "home_r1_break", nullable = false)
    private Integer homeR1Break = 0;

    // R2: 1.41 - 1.60
    @Column(name = "home_r2_win", nullable = false)
    private Integer homeR2Win = 0;
    @Column(name = "home_r2_draw", nullable = false)
    private Integer homeR2Draw = 0;
    @Column(name = "home_r2_loss", nullable = false)
    private Integer homeR2Loss = 0;
    @Column(name = "home_r2_break", nullable = false)
    private Integer homeR2Break = 0;

    // R3: 1.61 - 1.80
    @Column(name = "home_r3_win", nullable = false)
    private Integer homeR3Win = 0;
    @Column(name = "home_r3_draw", nullable = false)
    private Integer homeR3Draw = 0;
    @Column(name = "home_r3_loss", nullable = false)
    private Integer homeR3Loss = 0;
    @Column(name = "home_r3_break", nullable = false)
    private Integer homeR3Break = 0;

    // R4: 1.81 - 2.00
    @Column(name = "home_r4_win", nullable = false)
    private Integer homeR4Win = 0;
    @Column(name = "home_r4_draw", nullable = false)
    private Integer homeR4Draw = 0;
    @Column(name = "home_r4_loss", nullable = false)
    private Integer homeR4Loss = 0;
    @Column(name = "home_r4_break", nullable = false)
    private Integer homeR4Break = 0;

    // R5: 2.01 - 2.20
    @Column(name = "home_r5_win", nullable = false)
    private Integer homeR5Win = 0;
    @Column(name = "home_r5_draw", nullable = false)
    private Integer homeR5Draw = 0;
    @Column(name = "home_r5_loss", nullable = false)
    private Integer homeR5Loss = 0;
    @Column(name = "home_r5_break", nullable = false)
    private Integer homeR5Break = 0;

    // R6: >= 2.21
    @Column(name = "home_r6_win", nullable = false)
    private Integer homeR6Win = 0;
    @Column(name = "home_r6_draw", nullable = false)
    private Integer homeR6Draw = 0;
    @Column(name = "home_r6_loss", nullable = false)
    private Integer homeR6Loss = 0;
    @Column(name = "home_r6_break", nullable = false)
    private Integer homeR6Break = 0;

    // ==================== 5. 客让球六区间明细 ====================
    // R1: <= 1.40
    @Column(name = "away_r1_win", nullable = false)
    private Integer awayR1Win = 0;
    @Column(name = "away_r1_draw", nullable = false)
    private Integer awayR1Draw = 0;
    @Column(name = "away_r1_loss", nullable = false)
    private Integer awayR1Loss = 0;
    @Column(name = "away_r1_break", nullable = false)
    private Integer awayR1Break = 0;

    // R2: 1.41 - 1.60
    @Column(name = "away_r2_win", nullable = false)
    private Integer awayR2Win = 0;
    @Column(name = "away_r2_draw", nullable = false)
    private Integer awayR2Draw = 0;
    @Column(name = "away_r2_loss", nullable = false)
    private Integer awayR2Loss = 0;
    @Column(name = "away_r2_break", nullable = false)
    private Integer awayR2Break = 0;

    // R3: 1.61 - 1.80
    @Column(name = "away_r3_win", nullable = false)
    private Integer awayR3Win = 0;
    @Column(name = "away_r3_draw", nullable = false)
    private Integer awayR3Draw = 0;
    @Column(name = "away_r3_loss", nullable = false)
    private Integer awayR3Loss = 0;
    @Column(name = "away_r3_break", nullable = false)
    private Integer awayR3Break = 0;

    // R4: 1.81 - 2.00
    @Column(name = "away_r4_win", nullable = false)
    private Integer awayR4Win = 0;
    @Column(name = "away_r4_draw", nullable = false)
    private Integer awayR4Draw = 0;
    @Column(name = "away_r4_loss", nullable = false)
    private Integer awayR4Loss = 0;
    @Column(name = "away_r4_break", nullable = false)
    private Integer awayR4Break = 0;

    // R5: 2.01 - 2.20
    @Column(name = "away_r5_win", nullable = false)
    private Integer awayR5Win = 0;
    @Column(name = "away_r5_draw", nullable = false)
    private Integer awayR5Draw = 0;
    @Column(name = "away_r5_loss", nullable = false)
    private Integer awayR5Loss = 0;
    @Column(name = "away_r5_break", nullable = false)
    private Integer awayR5Break = 0;

    // R6: >= 2.21
    @Column(name = "away_r6_win", nullable = false)
    private Integer awayR6Win = 0;
    @Column(name = "away_r6_draw", nullable = false)
    private Integer awayR6Draw = 0;
    @Column(name = "away_r6_loss", nullable = false)
    private Integer awayR6Loss = 0;
    @Column(name = "away_r6_break", nullable = false)
    private Integer awayR6Break = 0;

    // ==================== 6. 大数据研判标签 ====================
    @Column(name = "best_break_interval", length = 50)
    private String bestBreakInterval;

    @Column(name = "lowest_break_interval", length = 50)
    private String lowestBreakInterval;

    @Column(name = "home_away_bias", length = 30)
    private String homeAwayBias = "BALANCED";

    @Column(name = "risk_evaluation", length = 100)
    private String riskEvaluation;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public HandicapOddsStatsRecord() {
    }

    /**
     * 持久化前和更新前自动重新计算汇总和比率
     */
    @PrePersist
    @PreUpdate
    public void calculateMetrics() {
        // 1. 规范化 null 值为 0
        normalizeNulls();

        // 2. 主让汇总
        this.homeTotalWin = homeR1Win + homeR2Win + homeR3Win + homeR4Win + homeR5Win + homeR6Win;
        this.homeTotalDraw = homeR1Draw + homeR2Draw + homeR3Draw + homeR4Draw + homeR5Draw + homeR6Draw;
        this.homeTotalLoss = homeR1Loss + homeR2Loss + homeR3Loss + homeR4Loss + homeR5Loss + homeR6Loss;
        this.homeTotalBreak = homeR1Break + homeR2Break + homeR3Break + homeR4Break + homeR5Break + homeR6Break;
        this.homeTotalMatches = homeTotalWin + homeTotalDraw + homeTotalLoss;

        if (this.homeTotalMatches > 0) {
            this.homeWinRate = BigDecimal.valueOf(this.homeTotalWin * 100.0 / this.homeTotalMatches)
                    .setScale(2, RoundingMode.HALF_UP);
            this.homeBreakRate = BigDecimal.valueOf(this.homeTotalBreak * 100.0 / this.homeTotalMatches)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            this.homeWinRate = BigDecimal.ZERO;
            this.homeBreakRate = BigDecimal.ZERO;
        }

        if (this.homeTotalWin > 0) {
            this.homeBreakRateOnWin = BigDecimal.valueOf(this.homeTotalBreak * 100.0 / this.homeTotalWin)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            this.homeBreakRateOnWin = BigDecimal.ZERO;
        }

        // 3. 客让汇总
        this.awayTotalWin = awayR1Win + awayR2Win + awayR3Win + awayR4Win + awayR5Win + awayR6Win;
        this.awayTotalDraw = awayR1Draw + awayR2Draw + awayR3Draw + awayR4Draw + awayR5Draw + awayR6Draw;
        this.awayTotalLoss = awayR1Loss + awayR2Loss + awayR3Loss + awayR4Loss + awayR5Loss + awayR6Loss;
        this.awayTotalBreak = awayR1Break + awayR2Break + awayR3Break + awayR4Break + awayR5Break + awayR6Break;
        this.awayTotalMatches = awayTotalWin + awayTotalDraw + awayTotalLoss;

        if (this.awayTotalMatches > 0) {
            this.awayWinRate = BigDecimal.valueOf(this.awayTotalWin * 100.0 / this.awayTotalMatches)
                    .setScale(2, RoundingMode.HALF_UP);
            this.awayBreakRate = BigDecimal.valueOf(this.awayTotalBreak * 100.0 / this.awayTotalMatches)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            this.awayWinRate = BigDecimal.ZERO;
            this.awayBreakRate = BigDecimal.ZERO;
        }

        if (this.awayTotalWin > 0) {
            this.awayBreakRateOnWin = BigDecimal.valueOf(this.awayTotalBreak * 100.0 / this.awayTotalWin)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            this.awayBreakRateOnWin = BigDecimal.ZERO;
        }

        // 4. 全盘总计
        this.totalMatches = this.homeTotalMatches + this.awayTotalMatches;
        this.totalWinCount = this.homeTotalWin + this.awayTotalWin;
        this.totalDrawCount = this.homeTotalDraw + this.awayTotalDraw;
        this.totalLossCount = this.homeTotalLoss + this.awayTotalLoss;
        this.totalBreakCount = this.homeTotalBreak + this.awayTotalBreak;

        if (this.totalMatches > 0) {
            this.overallWinRate = BigDecimal.valueOf(this.totalWinCount * 100.0 / this.totalMatches)
                    .setScale(2, RoundingMode.HALF_UP);
            this.overallBreakRate = BigDecimal.valueOf(this.totalBreakCount * 100.0 / this.totalMatches)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            this.overallWinRate = BigDecimal.ZERO;
            this.overallBreakRate = BigDecimal.ZERO;
        }

        if (this.totalWinCount > 0) {
            this.overallBreakRateOnWin = BigDecimal.valueOf(this.totalBreakCount * 100.0 / this.totalWinCount)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            this.overallBreakRateOnWin = BigDecimal.ZERO;
        }

        // 5. 自动研判主客偏向
        if (this.homeBreakRate.compareTo(this.awayBreakRate.add(BigDecimal.valueOf(10.0))) > 0) {
            this.homeAwayBias = "HOME_BIAS";
        } else if (this.awayBreakRate.compareTo(this.homeBreakRate.add(BigDecimal.valueOf(10.0))) > 0) {
            this.homeAwayBias = "AWAY_BIAS";
        } else {
            this.homeAwayBias = "BALANCED";
        }

        // 6. 找出最高与最低穿盘区间
        findExtremeIntervals();

        // 7. 风险评价摘要自动推导
        if (this.riskEvaluation == null || this.riskEvaluation.isBlank()) {
            this.riskEvaluation = generateRiskEvaluation();
        }
    }

    private void normalizeNulls() {
        if (totalMatches == null) totalMatches = 0;
        if (totalWinCount == null) totalWinCount = 0;
        if (totalDrawCount == null) totalDrawCount = 0;
        if (totalLossCount == null) totalLossCount = 0;
        if (totalBreakCount == null) totalBreakCount = 0;
        if (overallWinRate == null) overallWinRate = BigDecimal.ZERO;
        if (overallBreakRateOnWin == null) overallBreakRateOnWin = BigDecimal.ZERO;
        if (overallBreakRate == null) overallBreakRate = BigDecimal.ZERO;

        if (homeTotalMatches == null) homeTotalMatches = 0;
        if (homeTotalWin == null) homeTotalWin = 0;
        if (homeTotalDraw == null) homeTotalDraw = 0;
        if (homeTotalLoss == null) homeTotalLoss = 0;
        if (homeTotalBreak == null) homeTotalBreak = 0;
        if (homeWinRate == null) homeWinRate = BigDecimal.ZERO;
        if (homeBreakRateOnWin == null) homeBreakRateOnWin = BigDecimal.ZERO;
        if (homeBreakRate == null) homeBreakRate = BigDecimal.ZERO;

        if (awayTotalMatches == null) awayTotalMatches = 0;
        if (awayTotalWin == null) awayTotalWin = 0;
        if (awayTotalDraw == null) awayTotalDraw = 0;
        if (awayTotalLoss == null) awayTotalLoss = 0;
        if (awayTotalBreak == null) awayTotalBreak = 0;
        if (awayWinRate == null) awayWinRate = BigDecimal.ZERO;
        if (awayBreakRateOnWin == null) awayBreakRateOnWin = BigDecimal.ZERO;
        if (awayBreakRate == null) awayBreakRate = BigDecimal.ZERO;

        if (homeR1Win == null) homeR1Win = 0;
        if (homeR1Draw == null) homeR1Draw = 0;
        if (homeR1Loss == null) homeR1Loss = 0;
        if (homeR1Break == null) homeR1Break = 0;
        if (homeR2Win == null) homeR2Win = 0;
        if (homeR2Draw == null) homeR2Draw = 0;
        if (homeR2Loss == null) homeR2Loss = 0;
        if (homeR2Break == null) homeR2Break = 0;
        if (homeR3Win == null) homeR3Win = 0;
        if (homeR3Draw == null) homeR3Draw = 0;
        if (homeR3Loss == null) homeR3Loss = 0;
        if (homeR3Break == null) homeR3Break = 0;
        if (homeR4Win == null) homeR4Win = 0;
        if (homeR4Draw == null) homeR4Draw = 0;
        if (homeR4Loss == null) homeR4Loss = 0;
        if (homeR4Break == null) homeR4Break = 0;
        if (homeR5Win == null) homeR5Win = 0;
        if (homeR5Draw == null) homeR5Draw = 0;
        if (homeR5Loss == null) homeR5Loss = 0;
        if (homeR5Break == null) homeR5Break = 0;
        if (homeR6Win == null) homeR6Win = 0;
        if (homeR6Draw == null) homeR6Draw = 0;
        if (homeR6Loss == null) homeR6Loss = 0;
        if (homeR6Break == null) homeR6Break = 0;

        if (awayR1Win == null) awayR1Win = 0;
        if (awayR1Draw == null) awayR1Draw = 0;
        if (awayR1Loss == null) awayR1Loss = 0;
        if (awayR1Break == null) awayR1Break = 0;
        if (awayR2Win == null) awayR2Win = 0;
        if (awayR2Draw == null) awayR2Draw = 0;
        if (awayR2Loss == null) awayR2Loss = 0;
        if (awayR2Break == null) awayR2Break = 0;
        if (awayR3Win == null) awayR3Win = 0;
        if (awayR3Draw == null) awayR3Draw = 0;
        if (awayR3Loss == null) awayR3Loss = 0;
        if (awayR3Break == null) awayR3Break = 0;
        if (awayR4Win == null) awayR4Win = 0;
        if (awayR4Draw == null) awayR4Draw = 0;
        if (awayR4Loss == null) awayR4Loss = 0;
        if (awayR4Break == null) awayR4Break = 0;
        if (awayR5Win == null) awayR5Win = 0;
        if (awayR5Draw == null) awayR5Draw = 0;
        if (awayR5Loss == null) awayR5Loss = 0;
        if (awayR5Break == null) awayR5Break = 0;
        if (awayR6Win == null) awayR6Win = 0;
        if (awayR6Draw == null) awayR6Draw = 0;
        if (awayR6Loss == null) awayR6Loss = 0;
        if (awayR6Break == null) awayR6Break = 0;
    }

    private void findExtremeIntervals() {
        String[] names = {
                "主让 R1(<=1.40)", "主让 R2(1.41-1.60)", "主让 R3(1.61-1.80)", "主让 R4(1.81-2.00)", "主让 R5(2.01-2.20)", "主让 R6(>=2.21)",
                "客让 R1(<=1.40)", "客让 R2(1.41-1.60)", "客让 R3(1.61-1.80)", "客让 R4(1.81-2.00)", "客让 R5(2.01-2.20)", "客让 R6(>=2.21)"
        };
        int[] wins = {
                homeR1Win, homeR2Win, homeR3Win, homeR4Win, homeR5Win, homeR6Win,
                awayR1Win, awayR2Win, awayR3Win, awayR4Win, awayR5Win, awayR6Win
        };
        int[] breaks = {
                homeR1Break, homeR2Break, homeR3Break, homeR4Break, homeR5Break, homeR6Break,
                awayR1Break, awayR2Break, awayR3Break, awayR4Break, awayR5Break, awayR6Break
        };
        int[] draws = {
                homeR1Draw, homeR2Draw, homeR3Draw, homeR4Draw, homeR5Draw, homeR6Draw,
                awayR1Draw, awayR2Draw, awayR3Draw, awayR4Draw, awayR5Draw, awayR6Draw
        };
        int[] losses = {
                homeR1Loss, homeR2Loss, homeR3Loss, homeR4Loss, homeR5Loss, homeR6Loss,
                awayR1Loss, awayR2Loss, awayR3Loss, awayR4Loss, awayR5Loss, awayR6Loss
        };

        double maxBreakRate = -1.0;
        double minBreakRate = 101.0;
        String bestName = "无样本";
        String lowestName = "无样本";

        for (int i = 0; i < names.length; i++) {
            int total = wins[i] + draws[i] + losses[i];
            if (total > 0) {
                double rate = (breaks[i] * 100.0) / total;
                if (rate > maxBreakRate) {
                    maxBreakRate = rate;
                    bestName = names[i];
                }
                if (rate < minBreakRate) {
                    minBreakRate = rate;
                    lowestName = names[i];
                }
            }
        }
        this.bestBreakInterval = bestName;
        this.lowestBreakInterval = lowestName;
    }

    private String generateRiskEvaluation() {
        if (this.overallBreakRate.compareTo(BigDecimal.valueOf(45.0)) >= 0) {
            return "盘口极度正路，让球打穿概率偏高，建议顺应低水深盘穿盘逻辑";
        } else if (this.overallBreakRate.compareTo(BigDecimal.valueOf(30.0)) >= 0) {
            return "盘口表现均衡，赢球不穿盘与赢半卡盘较多，需防范一球小胜走水";
        } else {
            return "穿盘率显著偏低，受让方下盘频频打出，高危翻车预警，谨慎买让球赢";
        }
    }

    // ==================== Getters & Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public String getBatchTitle() {
        return batchTitle;
    }

    public void setBatchTitle(String batchTitle) {
        this.batchTitle = batchTitle;
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

    public BigDecimal getOverallWinRate() {
        return overallWinRate;
    }

    public void setOverallWinRate(BigDecimal overallWinRate) {
        this.overallWinRate = overallWinRate;
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

    public Integer getHomeTotalDraw() {
        return homeTotalDraw;
    }

    public void setHomeTotalDraw(Integer homeTotalDraw) {
        this.homeTotalDraw = homeTotalDraw;
    }

    public Integer getHomeTotalLoss() {
        return homeTotalLoss;
    }

    public void setHomeTotalLoss(Integer homeTotalLoss) {
        this.homeTotalLoss = homeTotalLoss;
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

    public Integer getAwayTotalDraw() {
        return awayTotalDraw;
    }

    public void setAwayTotalDraw(Integer awayTotalDraw) {
        this.awayTotalDraw = awayTotalDraw;
    }

    public Integer getAwayTotalLoss() {
        return awayTotalLoss;
    }

    public void setAwayTotalLoss(Integer awayTotalLoss) {
        this.awayTotalLoss = awayTotalLoss;
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

    public Integer getHomeR1Win() {
        return homeR1Win;
    }

    public void setHomeR1Win(Integer homeR1Win) {
        this.homeR1Win = homeR1Win;
    }

    public Integer getHomeR1Draw() {
        return homeR1Draw;
    }

    public void setHomeR1Draw(Integer homeR1Draw) {
        this.homeR1Draw = homeR1Draw;
    }

    public Integer getHomeR1Loss() {
        return homeR1Loss;
    }

    public void setHomeR1Loss(Integer homeR1Loss) {
        this.homeR1Loss = homeR1Loss;
    }

    public Integer getHomeR1Break() {
        return homeR1Break;
    }

    public void setHomeR1Break(Integer homeR1Break) {
        this.homeR1Break = homeR1Break;
    }

    public Integer getHomeR2Win() {
        return homeR2Win;
    }

    public void setHomeR2Win(Integer homeR2Win) {
        this.homeR2Win = homeR2Win;
    }

    public Integer getHomeR2Draw() {
        return homeR2Draw;
    }

    public void setHomeR2Draw(Integer homeR2Draw) {
        this.homeR2Draw = homeR2Draw;
    }

    public Integer getHomeR2Loss() {
        return homeR2Loss;
    }

    public void setHomeR2Loss(Integer homeR2Loss) {
        this.homeR2Loss = homeR2Loss;
    }

    public Integer getHomeR2Break() {
        return homeR2Break;
    }

    public void setHomeR2Break(Integer homeR2Break) {
        this.homeR2Break = homeR2Break;
    }

    public Integer getHomeR3Win() {
        return homeR3Win;
    }

    public void setHomeR3Win(Integer homeR3Win) {
        this.homeR3Win = homeR3Win;
    }

    public Integer getHomeR3Draw() {
        return homeR3Draw;
    }

    public void setHomeR3Draw(Integer homeR3Draw) {
        this.homeR3Draw = homeR3Draw;
    }

    public Integer getHomeR3Loss() {
        return homeR3Loss;
    }

    public void setHomeR3Loss(Integer homeR3Loss) {
        this.homeR3Loss = homeR3Loss;
    }

    public Integer getHomeR3Break() {
        return homeR3Break;
    }

    public void setHomeR3Break(Integer homeR3Break) {
        this.homeR3Break = homeR3Break;
    }

    public Integer getHomeR4Win() {
        return homeR4Win;
    }

    public void setHomeR4Win(Integer homeR4Win) {
        this.homeR4Win = homeR4Win;
    }

    public Integer getHomeR4Draw() {
        return homeR4Draw;
    }

    public void setHomeR4Draw(Integer homeR4Draw) {
        this.homeR4Draw = homeR4Draw;
    }

    public Integer getHomeR4Loss() {
        return homeR4Loss;
    }

    public void setHomeR4Loss(Integer homeR4Loss) {
        this.homeR4Loss = homeR4Loss;
    }

    public Integer getHomeR4Break() {
        return homeR4Break;
    }

    public void setHomeR4Break(Integer homeR4Break) {
        this.homeR4Break = homeR4Break;
    }

    public Integer getHomeR5Win() {
        return homeR5Win;
    }

    public void setHomeR5Win(Integer homeR5Win) {
        this.homeR5Win = homeR5Win;
    }

    public Integer getHomeR5Draw() {
        return homeR5Draw;
    }

    public void setHomeR5Draw(Integer homeR5Draw) {
        this.homeR5Draw = homeR5Draw;
    }

    public Integer getHomeR5Loss() {
        return homeR5Loss;
    }

    public void setHomeR5Loss(Integer homeR5Loss) {
        this.homeR5Loss = homeR5Loss;
    }

    public Integer getHomeR5Break() {
        return homeR5Break;
    }

    public void setHomeR5Break(Integer homeR5Break) {
        this.homeR5Break = homeR5Break;
    }

    public Integer getHomeR6Win() {
        return homeR6Win;
    }

    public void setHomeR6Win(Integer homeR6Win) {
        this.homeR6Win = homeR6Win;
    }

    public Integer getHomeR6Draw() {
        return homeR6Draw;
    }

    public void setHomeR6Draw(Integer homeR6Draw) {
        this.homeR6Draw = homeR6Draw;
    }

    public Integer getHomeR6Loss() {
        return homeR6Loss;
    }

    public void setHomeR6Loss(Integer homeR6Loss) {
        this.homeR6Loss = homeR6Loss;
    }

    public Integer getHomeR6Break() {
        return homeR6Break;
    }

    public void setHomeR6Break(Integer homeR6Break) {
        this.homeR6Break = homeR6Break;
    }

    public Integer getAwayR1Win() {
        return awayR1Win;
    }

    public void setAwayR1Win(Integer awayR1Win) {
        this.awayR1Win = awayR1Win;
    }

    public Integer getAwayR1Draw() {
        return awayR1Draw;
    }

    public void setAwayR1Draw(Integer awayR1Draw) {
        this.awayR1Draw = awayR1Draw;
    }

    public Integer getAwayR1Loss() {
        return awayR1Loss;
    }

    public void setAwayR1Loss(Integer awayR1Loss) {
        this.awayR1Loss = awayR1Loss;
    }

    public Integer getAwayR1Break() {
        return awayR1Break;
    }

    public void setAwayR1Break(Integer awayR1Break) {
        this.awayR1Break = awayR1Break;
    }

    public Integer getAwayR2Win() {
        return awayR2Win;
    }

    public void setAwayR2Win(Integer awayR2Win) {
        this.awayR2Win = awayR2Win;
    }

    public Integer getAwayR2Draw() {
        return awayR2Draw;
    }

    public void setAwayR2Draw(Integer awayR2Draw) {
        this.awayR2Draw = awayR2Draw;
    }

    public Integer getAwayR2Loss() {
        return awayR2Loss;
    }

    public void setAwayR2Loss(Integer awayR2Loss) {
        this.awayR2Loss = awayR2Loss;
    }

    public Integer getAwayR2Break() {
        return awayR2Break;
    }

    public void setAwayR2Break(Integer awayR2Break) {
        this.awayR2Break = awayR2Break;
    }

    public Integer getAwayR3Win() {
        return awayR3Win;
    }

    public void setAwayR3Win(Integer awayR3Win) {
        this.awayR3Win = awayR3Win;
    }

    public Integer getAwayR3Draw() {
        return awayR3Draw;
    }

    public void setAwayR3Draw(Integer awayR3Draw) {
        this.awayR3Draw = awayR3Draw;
    }

    public Integer getAwayR3Loss() {
        return awayR3Loss;
    }

    public void setAwayR3Loss(Integer awayR3Loss) {
        this.awayR3Loss = awayR3Loss;
    }

    public Integer getAwayR3Break() {
        return awayR3Break;
    }

    public void setAwayR3Break(Integer awayR3Break) {
        this.awayR3Break = awayR3Break;
    }

    public Integer getAwayR4Win() {
        return awayR4Win;
    }

    public void setAwayR4Win(Integer awayR4Win) {
        this.awayR4Win = awayR4Win;
    }

    public Integer getAwayR4Draw() {
        return awayR4Draw;
    }

    public void setAwayR4Draw(Integer awayR4Draw) {
        this.awayR4Draw = awayR4Draw;
    }

    public Integer getAwayR4Loss() {
        return awayR4Loss;
    }

    public void setAwayR4Loss(Integer awayR4Loss) {
        this.awayR4Loss = awayR4Loss;
    }

    public Integer getAwayR4Break() {
        return awayR4Break;
    }

    public void setAwayR4Break(Integer awayR4Break) {
        this.awayR4Break = awayR4Break;
    }

    public Integer getAwayR5Win() {
        return awayR5Win;
    }

    public void setAwayR5Win(Integer awayR5Win) {
        this.awayR5Win = awayR5Win;
    }

    public Integer getAwayR5Draw() {
        return awayR5Draw;
    }

    public void setAwayR5Draw(Integer awayR5Draw) {
        this.awayR5Draw = awayR5Draw;
    }

    public Integer getAwayR5Loss() {
        return awayR5Loss;
    }

    public void setAwayR5Loss(Integer awayR5Loss) {
        this.awayR5Loss = awayR5Loss;
    }

    public Integer getAwayR5Break() {
        return awayR5Break;
    }

    public void setAwayR5Break(Integer awayR5Break) {
        this.awayR5Break = awayR5Break;
    }

    public Integer getAwayR6Win() {
        return awayR6Win;
    }

    public void setAwayR6Win(Integer awayR6Win) {
        this.awayR6Win = awayR6Win;
    }

    public Integer getAwayR6Draw() {
        return awayR6Draw;
    }

    public void setAwayR6Draw(Integer awayR6Draw) {
        this.awayR6Draw = awayR6Draw;
    }

    public Integer getAwayR6Loss() {
        return awayR6Loss;
    }

    public void setAwayR6Loss(Integer awayR6Loss) {
        this.awayR6Loss = awayR6Loss;
    }

    public Integer getAwayR6Break() {
        return awayR6Break;
    }

    public void setAwayR6Break(Integer awayR6Break) {
        this.awayR6Break = awayR6Break;
    }

    public String getBestBreakInterval() {
        return bestBreakInterval;
    }

    public void setBestBreakInterval(String bestBreakInterval) {
        this.bestBreakInterval = bestBreakInterval;
    }

    public String getLowestBreakInterval() {
        return lowestBreakInterval;
    }

    public void setLowestBreakInterval(String lowestBreakInterval) {
        this.lowestBreakInterval = lowestBreakInterval;
    }

    public String getHomeAwayBias() {
        return homeAwayBias;
    }

    public void setHomeAwayBias(String homeAwayBias) {
        this.homeAwayBias = homeAwayBias;
    }

    public String getRiskEvaluation() {
        return riskEvaluation;
    }

    public void setRiskEvaluation(String riskEvaluation) {
        this.riskEvaluation = riskEvaluation;
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
