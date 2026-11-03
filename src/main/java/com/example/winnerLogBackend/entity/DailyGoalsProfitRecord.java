package com.example.winnerLogBackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 每日总进球数成本利润与规律大数据分析实体类
 */
@Entity
@Table(name = "daily_goals_profit_record", indexes = {
        @Index(name = "uk_goals_match_date", columnList = "match_date", unique = true),
        @Index(name = "idx_goals_profit_date", columnList = "match_date"),
        @Index(name = "idx_goals_total_profit", columnList = "total_profit"),
        @Index(name = "idx_goals_roi_rate", columnList = "roi_rate"),
        @Index(name = "idx_goals_dominant_strategy", columnList = "dominant_strategy"),
        @Index(name = "idx_goals_fit_rate", columnList = "fit_rate")
})
public class DailyGoalsProfitRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_date", nullable = false, unique = true)
    private LocalDate matchDate;

    // ==================== 1. 总进球数规律核心分布 ====================
    @Column(name = "pattern_fit_count", nullable = false)
    private Integer patternFitCount = 0;

    @Column(name = "pattern_slight_count", nullable = false)
    private Integer patternSlightCount = 0;

    @Column(name = "pattern_mismatch_count", nullable = false)
    private Integer patternMismatchCount = 0;

    @Column(name = "total_matches", nullable = false)
    private Integer totalMatches = 0;

    // ==================== 2. 总进球数规律大数据衍生指标 ====================
    @Column(name = "fit_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal fitRate = BigDecimal.ZERO;

    @Column(name = "slight_fit_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal slightFitRate = BigDecimal.ZERO;

    @Column(name = "mismatch_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal mismatchRate = BigDecimal.ZERO;

    @Column(name = "generalized_fit_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal generalizedFitRate = BigDecimal.ZERO;

    @Column(name = "pattern_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal patternScore = BigDecimal.ZERO;

    // ==================== 3. 主三副二策略 (3M2S) ====================
    @Column(name = "strategy_3m2s_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal strategy3m2sCost = BigDecimal.ZERO;

    @Column(name = "strategy_3m2s_profit", nullable = false, precision = 10, scale = 2)
    private BigDecimal strategy3m2sProfit = BigDecimal.ZERO;

    @Column(name = "strategy_3m2s_roi", nullable = false, precision = 6, scale = 2)
    private BigDecimal strategy3m2sRoi = BigDecimal.ZERO;

    // ==================== 4. 主二副三策略 (2M3S) ====================
    @Column(name = "strategy_2m3s_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal strategy2m3sCost = BigDecimal.ZERO;

    @Column(name = "strategy_2m3s_profit", nullable = false, precision = 10, scale = 2)
    private BigDecimal strategy2m3sProfit = BigDecimal.ZERO;

    @Column(name = "strategy_2m3s_roi", nullable = false, precision = 6, scale = 2)
    private BigDecimal strategy2m3sRoi = BigDecimal.ZERO;

    // ==================== 5. 大数据综合财务与表现指标 ====================
    @Column(name = "total_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCost = BigDecimal.ZERO;

    @Column(name = "total_profit", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalProfit = BigDecimal.ZERO;

    @Column(name = "total_return", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalReturn = BigDecimal.ZERO;

    @Column(name = "roi_rate", nullable = false, precision = 6, scale = 2)
    private BigDecimal roiRate = BigDecimal.ZERO;

    @Column(name = "dominant_strategy", length = 20)
    private String dominantStrategy = "NONE";

    @Column(name = "goals_trend", length = 30)
    private String goalsTrend = "BALANCED";

    @Column(name = "performance_level", length = 20)
    private String performanceLevel = "PROFIT";

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public DailyGoalsProfitRecord() {}

    /**
     * 前置校验与自动计算衍生指标
     */
    @PrePersist
    @PreUpdate
    public void calculateMetrics() {
        if (patternFitCount == null) patternFitCount = 0;
        if (patternSlightCount == null) patternSlightCount = 0;
        if (patternMismatchCount == null) patternMismatchCount = 0;

        this.totalMatches = patternFitCount + patternSlightCount + patternMismatchCount;

        if (totalMatches > 0) {
            BigDecimal totalDecimal = BigDecimal.valueOf(totalMatches);
            this.fitRate = BigDecimal.valueOf(patternFitCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalDecimal, 2, RoundingMode.HALF_UP);

            this.slightFitRate = BigDecimal.valueOf(patternSlightCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalDecimal, 2, RoundingMode.HALF_UP);

            this.mismatchRate = BigDecimal.valueOf(patternMismatchCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalDecimal, 2, RoundingMode.HALF_UP);

            int combinedFit = patternFitCount + patternSlightCount;
            this.generalizedFitRate = BigDecimal.valueOf(combinedFit)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalDecimal, 2, RoundingMode.HALF_UP);

            // 进球规律综合得分: 符合*1.0 + 稍微符合*0.5
            double score = patternFitCount * 1.0 + patternSlightCount * 0.5;
            this.patternScore = BigDecimal.valueOf(score).setScale(2, RoundingMode.HALF_UP);
        } else {
            this.fitRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            this.slightFitRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            this.mismatchRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            this.generalizedFitRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            this.patternScore = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        if (strategy3m2sCost == null) strategy3m2sCost = BigDecimal.ZERO;
        if (strategy3m2sProfit == null) strategy3m2sProfit = BigDecimal.ZERO;
        if (strategy2m3sCost == null) strategy2m3sCost = BigDecimal.ZERO;
        if (strategy2m3sProfit == null) strategy2m3sProfit = BigDecimal.ZERO;

        // 计算总投入、总利润与返还
        this.totalCost = strategy3m2sCost.add(strategy2m3sCost).setScale(2, RoundingMode.HALF_UP);
        this.totalProfit = strategy3m2sProfit.add(strategy2m3sProfit).setScale(2, RoundingMode.HALF_UP);
        this.totalReturn = totalCost.add(totalProfit).setScale(2, RoundingMode.HALF_UP);

        // 主3副2 ROI
        if (strategy3m2sCost.compareTo(BigDecimal.ZERO) > 0) {
            this.strategy3m2sRoi = strategy3m2sProfit.multiply(BigDecimal.valueOf(100))
                    .divide(strategy3m2sCost, 2, RoundingMode.HALF_UP);
        } else {
            this.strategy3m2sRoi = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // 主2副3 ROI
        if (strategy2m3sCost.compareTo(BigDecimal.ZERO) > 0) {
            this.strategy2m3sRoi = strategy2m3sProfit.multiply(BigDecimal.valueOf(100))
                    .divide(strategy2m3sCost, 2, RoundingMode.HALF_UP);
        } else {
            this.strategy2m3sRoi = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // 综合 ROI
        if (totalCost.compareTo(BigDecimal.ZERO) > 0) {
            this.roiRate = totalProfit.multiply(BigDecimal.valueOf(100))
                    .divide(totalCost, 2, RoundingMode.HALF_UP);
        } else {
            this.roiRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // 判定优势策略
        if (strategy3m2sProfit.compareTo(strategy2m3sProfit) > 0) {
            this.dominantStrategy = "STRATEGY_3M2S";
        } else if (strategy2m3sProfit.compareTo(strategy3m2sProfit) > 0) {
            this.dominantStrategy = "STRATEGY_2M3S";
        } else {
            this.dominantStrategy = "DRAW";
        }

        // 进球数规律走势研判
        if (this.fitRate.compareTo(BigDecimal.valueOf(50)) >= 0) {
            this.goalsTrend = "STABLE_PATTERN"; // 规律高度稳定
        } else if (this.generalizedFitRate.compareTo(BigDecimal.valueOf(70)) >= 0) {
            this.goalsTrend = "SLIGHT_FLUCTUATION"; // 存在微幅偏离，但泛规律仍覆盖
        } else {
            this.goalsTrend = "HIGH_CHAOS"; // 异常比分极端多
        }

        // 盈利表现评级
        if (this.roiRate.compareTo(BigDecimal.valueOf(50)) >= 0) {
            this.performanceLevel = "GREAT_PROFIT"; // 大红大赚
        } else if (this.roiRate.compareTo(BigDecimal.ZERO) > 0) {
            this.performanceLevel = "PROFIT"; // 稳健盈利
        } else if (this.roiRate.compareTo(BigDecimal.ZERO) == 0) {
            this.performanceLevel = "BREAKEVEN"; // 保本
        } else if (this.roiRate.compareTo(BigDecimal.valueOf(-30)) >= 0) {
            this.performanceLevel = "LOSS"; // 小幅回撤
        } else {
            this.performanceLevel = "HEAVY_LOSS"; // 深度回撤
        }
    }

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

    public Integer getPatternFitCount() {
        return patternFitCount;
    }

    public void setPatternFitCount(Integer patternFitCount) {
        this.patternFitCount = patternFitCount;
    }

    public Integer getPatternSlightCount() {
        return patternSlightCount;
    }

    public void setPatternSlightCount(Integer patternSlightCount) {
        this.patternSlightCount = patternSlightCount;
    }

    public Integer getPatternMismatchCount() {
        return patternMismatchCount;
    }

    public void setPatternMismatchCount(Integer patternMismatchCount) {
        this.patternMismatchCount = patternMismatchCount;
    }

    public Integer getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(Integer totalMatches) {
        this.totalMatches = totalMatches;
    }

    public BigDecimal getFitRate() {
        return fitRate;
    }

    public void setFitRate(BigDecimal fitRate) {
        this.fitRate = fitRate;
    }

    public BigDecimal getSlightFitRate() {
        return slightFitRate;
    }

    public void setSlightFitRate(BigDecimal slightFitRate) {
        this.slightFitRate = slightFitRate;
    }

    public BigDecimal getMismatchRate() {
        return mismatchRate;
    }

    public void setMismatchRate(BigDecimal mismatchRate) {
        this.mismatchRate = mismatchRate;
    }

    public BigDecimal getGeneralizedFitRate() {
        return generalizedFitRate;
    }

    public void setGeneralizedFitRate(BigDecimal generalizedFitRate) {
        this.generalizedFitRate = generalizedFitRate;
    }

    public BigDecimal getPatternScore() {
        return patternScore;
    }

    public void setPatternScore(BigDecimal patternScore) {
        this.patternScore = patternScore;
    }

    public BigDecimal getStrategy3m2sCost() {
        return strategy3m2sCost;
    }

    public void setStrategy3m2sCost(BigDecimal strategy3m2sCost) {
        this.strategy3m2sCost = strategy3m2sCost;
    }

    public BigDecimal getStrategy3m2sProfit() {
        return strategy3m2sProfit;
    }

    public void setStrategy3m2sProfit(BigDecimal strategy3m2sProfit) {
        this.strategy3m2sProfit = strategy3m2sProfit;
    }

    public BigDecimal getStrategy3m2sRoi() {
        return strategy3m2sRoi;
    }

    public void setStrategy3m2sRoi(BigDecimal strategy3m2sRoi) {
        this.strategy3m2sRoi = strategy3m2sRoi;
    }

    public BigDecimal getStrategy2m3sCost() {
        return strategy2m3sCost;
    }

    public void setStrategy2m3sCost(BigDecimal strategy2m3sCost) {
        this.strategy2m3sCost = strategy2m3sCost;
    }

    public BigDecimal getStrategy2m3sProfit() {
        return strategy2m3sProfit;
    }

    public void setStrategy2m3sProfit(BigDecimal strategy2m3sProfit) {
        this.strategy2m3sProfit = strategy2m3sProfit;
    }

    public BigDecimal getStrategy2m3sRoi() {
        return strategy2m3sRoi;
    }

    public void setStrategy2m3sRoi(BigDecimal strategy2m3sRoi) {
        this.strategy2m3sRoi = strategy2m3sRoi;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public BigDecimal getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(BigDecimal totalReturn) {
        this.totalReturn = totalReturn;
    }

    public BigDecimal getRoiRate() {
        return roiRate;
    }

    public void setRoiRate(BigDecimal roiRate) {
        this.roiRate = roiRate;
    }

    public String getDominantStrategy() {
        return dominantStrategy;
    }

    public void setDominantStrategy(String dominantStrategy) {
        this.dominantStrategy = dominantStrategy;
    }

    public String getGoalsTrend() {
        return goalsTrend;
    }

    public void setGoalsTrend(String goalsTrend) {
        this.goalsTrend = goalsTrend;
    }

    public String getPerformanceLevel() {
        return performanceLevel;
    }

    public void setPerformanceLevel(String performanceLevel) {
        this.performanceLevel = performanceLevel;
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
