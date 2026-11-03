package com.example.winnerLogBackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 每日比赛成本利润与串关策略大数据战绩实体类
 */
@Entity
@Table(name = "daily_profit_record", indexes = {
        @Index(name = "uk_match_date", columnList = "match_date", unique = true),
        @Index(name = "idx_profit_date", columnList = "match_date"),
        @Index(name = "idx_total_profit", columnList = "total_profit"),
        @Index(name = "idx_roi_rate", columnList = "roi_rate"),
        @Index(name = "idx_dominant_strategy", columnList = "dominant_strategy")
})
public class DailyProfitRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_date", nullable = false, unique = true)
    private LocalDate matchDate;

    // ==================== 1. 穿盘属性 ====================
    @Column(name = "break_count", nullable = false)
    private Integer breakCount = 0;

    @Column(name = "unbreak_count", nullable = false)
    private Integer unbreakCount = 0;

    @Column(name = "total_matches", nullable = false)
    private Integer totalMatches = 0;

    @Column(name = "break_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal breakRate = BigDecimal.ZERO;

    // ==================== 2. 主三副二策略 (3M2S) ====================
    @Column(name = "strategy_3m2s_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal strategy3m2sCost = BigDecimal.ZERO;

    @Column(name = "strategy_3m2s_profit", nullable = false, precision = 10, scale = 2)
    private BigDecimal strategy3m2sProfit = BigDecimal.ZERO;

    @Column(name = "strategy_3m2s_roi", nullable = false, precision = 6, scale = 2)
    private BigDecimal strategy3m2sRoi = BigDecimal.ZERO;

    // ==================== 3. 主二副三策略 (2M3S) ====================
    @Column(name = "strategy_2m3s_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal strategy2m3sCost = BigDecimal.ZERO;

    @Column(name = "strategy_2m3s_profit", nullable = false, precision = 10, scale = 2)
    private BigDecimal strategy2m3sProfit = BigDecimal.ZERO;

    @Column(name = "strategy_2m3s_roi", nullable = false, precision = 6, scale = 2)
    private BigDecimal strategy2m3sRoi = BigDecimal.ZERO;

    // ==================== 4. 大数据分析综合聚合指标 ====================
    @Column(name = "total_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCost = BigDecimal.ZERO;

    @Column(name = "total_profit", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalProfit = BigDecimal.ZERO;

    @Column(name = "total_return", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalReturn = BigDecimal.ZERO;

    @Column(name = "roi_rate", nullable = false, precision = 6, scale = 2)
    private BigDecimal roiRate = BigDecimal.ZERO;

    @Column(name = "dominant_strategy", length = 20)
    private String dominantStrategy = "NONE"; // 3M2S / 2M3S / DRAW / NONE

    @Column(name = "market_sentiment", length = 30)
    private String marketSentiment = "BALANCED"; // DEEP_BREAK / BALANCED / UNDERDOG_COLD

    @Column(name = "performance_level", length = 20)
    private String performanceLevel = "PROFIT"; // BIG_WIN / PROFIT / BREAK_EVEN / LOSS / BIG_LOSS

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public DailyProfitRecord() {}

    /**
     * 前置校验与自动计算所有衍生指标
     */
    @PrePersist
    @PreUpdate
    public void calculateMetrics() {
        if (breakCount == null) breakCount = 0;
        if (unbreakCount == null) unbreakCount = 0;
        this.totalMatches = breakCount + unbreakCount;

        if (totalMatches > 0) {
            this.breakRate = BigDecimal.valueOf(breakCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalMatches), 2, RoundingMode.HALF_UP);
        } else {
            this.breakRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        if (strategy3m2sCost == null) strategy3m2sCost = BigDecimal.ZERO;
        if (strategy3m2sProfit == null) strategy3m2sProfit = BigDecimal.ZERO;
        if (strategy2m3sCost == null) strategy2m3sCost = BigDecimal.ZERO;
        if (strategy2m3sProfit == null) strategy2m3sProfit = BigDecimal.ZERO;

        // 计算总成本与总利润
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

        // 优势策略判定
        int compare = strategy3m2sProfit.compareTo(strategy2m3sProfit);
        if (compare > 0) {
            this.dominantStrategy = "3M2S";
        } else if (compare < 0) {
            this.dominantStrategy = "2M3S";
        } else {
            this.dominantStrategy = (strategy3m2sProfit.compareTo(BigDecimal.ZERO) == 0) ? "NONE" : "DRAW";
        }

        // 盘面冷热态势判定
        if (this.breakRate.compareTo(BigDecimal.valueOf(65)) >= 0) {
            this.marketSentiment = "DEEP_BREAK"; // 深盘打穿日
        } else if (this.breakRate.compareTo(BigDecimal.valueOf(35)) <= 0 && this.totalMatches > 0) {
            this.marketSentiment = "UNDERDOG_COLD"; // 下盘冷门卡盘日
        } else {
            this.marketSentiment = "BALANCED"; // 中庸均衡日
        }

        // 收益战绩评级
        if (totalProfit.compareTo(BigDecimal.ZERO) > 0) {
            if (this.roiRate.compareTo(BigDecimal.valueOf(80)) >= 0) {
                this.performanceLevel = "BIG_WIN";
            } else {
                this.performanceLevel = "PROFIT";
            }
        } else if (totalProfit.compareTo(BigDecimal.ZERO) == 0) {
            this.performanceLevel = "BREAK_EVEN";
        } else {
            if (this.roiRate.compareTo(BigDecimal.valueOf(-30)) >= 0) {
                this.performanceLevel = "LOSS";
            } else {
                this.performanceLevel = "BIG_LOSS";
            }
        }
    }

    // ==================== Getters & Setters ====================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getMatchDate() { return matchDate; }
    public void setMatchDate(LocalDate matchDate) { this.matchDate = matchDate; }
    public Integer getBreakCount() { return breakCount; }
    public void setBreakCount(Integer breakCount) { this.breakCount = breakCount; }
    public Integer getUnbreakCount() { return unbreakCount; }
    public void setUnbreakCount(Integer unbreakCount) { this.unbreakCount = unbreakCount; }
    public Integer getTotalMatches() { return totalMatches; }
    public void setTotalMatches(Integer totalMatches) { this.totalMatches = totalMatches; }
    public BigDecimal getBreakRate() { return breakRate; }
    public void setBreakRate(BigDecimal breakRate) { this.breakRate = breakRate; }
    public BigDecimal getStrategy3m2sCost() { return strategy3m2sCost; }
    public void setStrategy3m2sCost(BigDecimal strategy3m2sCost) { this.strategy3m2sCost = strategy3m2sCost; }
    public BigDecimal getStrategy3m2sProfit() { return strategy3m2sProfit; }
    public void setStrategy3m2sProfit(BigDecimal strategy3m2sProfit) { this.strategy3m2sProfit = strategy3m2sProfit; }
    public BigDecimal getStrategy3m2sRoi() { return strategy3m2sRoi; }
    public void setStrategy3m2sRoi(BigDecimal strategy3m2sRoi) { this.strategy3m2sRoi = strategy3m2sRoi; }
    public BigDecimal getStrategy2m3sCost() { return strategy2m3sCost; }
    public void setStrategy2m3sCost(BigDecimal strategy2m3sCost) { this.strategy2m3sCost = strategy2m3sCost; }
    public BigDecimal getStrategy2m3sProfit() { return strategy2m3sProfit; }
    public void setStrategy2m3sProfit(BigDecimal strategy2m3sProfit) { this.strategy2m3sProfit = strategy2m3sProfit; }
    public BigDecimal getStrategy2m3sRoi() { return strategy2m3sRoi; }
    public void setStrategy2m3sRoi(BigDecimal strategy2m3sRoi) { this.strategy2m3sRoi = strategy2m3sRoi; }
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public BigDecimal getTotalProfit() { return totalProfit; }
    public void setTotalProfit(BigDecimal totalProfit) { this.totalProfit = totalProfit; }
    public BigDecimal getTotalReturn() { return totalReturn; }
    public void setTotalReturn(BigDecimal totalReturn) { this.totalReturn = totalReturn; }
    public BigDecimal getRoiRate() { return roiRate; }
    public void setRoiRate(BigDecimal roiRate) { this.roiRate = roiRate; }
    public String getDominantStrategy() { return dominantStrategy; }
    public void setDominantStrategy(String dominantStrategy) { this.dominantStrategy = dominantStrategy; }
    public String getMarketSentiment() { return marketSentiment; }
    public void setMarketSentiment(String marketSentiment) { this.marketSentiment = marketSentiment; }
    public String getPerformanceLevel() { return performanceLevel; }
    public void setPerformanceLevel(String performanceLevel) { this.performanceLevel = performanceLevel; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
