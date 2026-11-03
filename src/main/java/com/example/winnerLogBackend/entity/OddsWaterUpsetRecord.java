package com.example.winnerLogBackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 盘口水位冷门翻车与大数据分析战绩实体
 * 记录历史总场数、高中低水场数及各自主客场翻车场数，实时计算各项翻车率与大数据衍生指标
 */
@Entity
@Table(name = "odds_water_upset_record", indexes = {
        @Index(name = "uk_water_record_date", columnList = "record_date", unique = true),
        @Index(name = "idx_water_record_date", columnList = "record_date"),
        @Index(name = "idx_water_overall_rate", columnList = "overall_upset_rate"),
        @Index(name = "idx_water_risk_level", columnList = "risk_level")
})
public class OddsWaterUpsetRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_date", nullable = false, unique = true)
    private LocalDate recordDate;

    @Column(name = "batch_title", length = 100)
    private String batchTitle;

    // ==================== 1. 基础比赛场数 ====================
    @Column(name = "total_matches", nullable = false)
    private Integer totalMatches = 0;

    // ==================== 2. 高水盘口属性与翻车统计 ====================
    @Column(name = "high_water_matches", nullable = false)
    private Integer highWaterMatches = 0;

    @Column(name = "high_water_home_upset_count", nullable = false)
    private Integer highWaterHomeUpsetCount = 0;

    @Column(name = "high_water_away_upset_count", nullable = false)
    private Integer highWaterAwayUpsetCount = 0;

    @Column(name = "high_water_upset_count", nullable = false)
    private Integer highWaterUpsetCount = 0;

    @Column(name = "high_water_normal_count", nullable = false)
    private Integer highWaterNormalCount = 0;

    @Column(name = "high_water_upset_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal highWaterUpsetRate = BigDecimal.ZERO;

    @Column(name = "high_water_home_upset_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal highWaterHomeUpsetRate = BigDecimal.ZERO;

    @Column(name = "high_water_away_upset_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal highWaterAwayUpsetRate = BigDecimal.ZERO;

    // ==================== 3. 中水盘口属性与翻车统计 ====================
    @Column(name = "mid_water_matches", nullable = false)
    private Integer midWaterMatches = 0;

    @Column(name = "mid_water_home_upset_count", nullable = false)
    private Integer midWaterHomeUpsetCount = 0;

    @Column(name = "mid_water_away_upset_count", nullable = false)
    private Integer midWaterAwayUpsetCount = 0;

    @Column(name = "mid_water_upset_count", nullable = false)
    private Integer midWaterUpsetCount = 0;

    @Column(name = "mid_water_normal_count", nullable = false)
    private Integer midWaterNormalCount = 0;

    @Column(name = "mid_water_upset_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal midWaterUpsetRate = BigDecimal.ZERO;

    @Column(name = "mid_water_home_upset_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal midWaterHomeUpsetRate = BigDecimal.ZERO;

    @Column(name = "mid_water_away_upset_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal midWaterAwayUpsetRate = BigDecimal.ZERO;

    // ==================== 4. 低水盘口属性与翻车统计 ====================
    @Column(name = "low_water_matches", nullable = false)
    private Integer lowWaterMatches = 0;

    @Column(name = "low_water_home_upset_count", nullable = false)
    private Integer lowWaterHomeUpsetCount = 0;

    @Column(name = "low_water_away_upset_count", nullable = false)
    private Integer lowWaterAwayUpsetCount = 0;

    @Column(name = "low_water_upset_count", nullable = false)
    private Integer lowWaterUpsetCount = 0;

    @Column(name = "low_water_normal_count", nullable = false)
    private Integer lowWaterNormalCount = 0;

    @Column(name = "low_water_upset_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal lowWaterUpsetRate = BigDecimal.ZERO;

    @Column(name = "low_water_home_upset_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal lowWaterHomeUpsetRate = BigDecimal.ZERO;

    @Column(name = "low_water_away_upset_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal lowWaterAwayUpsetRate = BigDecimal.ZERO;

    // ==================== 5. 大数据综合衍生指标 ====================
    @Column(name = "total_upset_count", nullable = false)
    private Integer totalUpsetCount = 0;

    @Column(name = "total_normal_count", nullable = false)
    private Integer totalNormalCount = 0;

    @Column(name = "total_home_upset_count", nullable = false)
    private Integer totalHomeUpsetCount = 0;

    @Column(name = "total_away_upset_count", nullable = false)
    private Integer totalAwayUpsetCount = 0;

    @Column(name = "overall_upset_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal overallUpsetRate = BigDecimal.ZERO;

    @Column(name = "overall_home_upset_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal overallHomeUpsetRate = BigDecimal.ZERO;

    @Column(name = "overall_away_upset_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal overallAwayUpsetRate = BigDecimal.ZERO;

    @Column(name = "highest_upset_water_level", length = 30)
    private String highestUpsetWaterLevel = "BALANCED";

    @Column(name = "upset_tendency", length = 30)
    private String upsetTendency = "BALANCED";

    @Column(name = "risk_level", length = 30)
    private String riskLevel = "SAFE_NORMAL";

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public OddsWaterUpsetRecord() {}

    /**
     * 前置校验与自动计算各项翻车率及大数据衍生指标
     */
    @PrePersist
    @PreUpdate
    public void calculateMetrics() {
        if (highWaterMatches == null) highWaterMatches = 0;
        if (highWaterHomeUpsetCount == null) highWaterHomeUpsetCount = 0;
        if (highWaterAwayUpsetCount == null) highWaterAwayUpsetCount = 0;

        if (midWaterMatches == null) midWaterMatches = 0;
        if (midWaterHomeUpsetCount == null) midWaterHomeUpsetCount = 0;
        if (midWaterAwayUpsetCount == null) midWaterAwayUpsetCount = 0;

        if (lowWaterMatches == null) lowWaterMatches = 0;
        if (lowWaterHomeUpsetCount == null) lowWaterHomeUpsetCount = 0;
        if (lowWaterAwayUpsetCount == null) lowWaterAwayUpsetCount = 0;

        // 1. 高水指标计算
        this.highWaterUpsetCount = this.highWaterHomeUpsetCount + this.highWaterAwayUpsetCount;
        this.highWaterNormalCount = Math.max(0, this.highWaterMatches - this.highWaterUpsetCount);
        if (this.highWaterMatches > 0) {
            BigDecimal hwDecimal = BigDecimal.valueOf(this.highWaterMatches);
            this.highWaterUpsetRate = BigDecimal.valueOf(this.highWaterUpsetCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(hwDecimal, 2, RoundingMode.HALF_UP);
            this.highWaterHomeUpsetRate = BigDecimal.valueOf(this.highWaterHomeUpsetCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(hwDecimal, 2, RoundingMode.HALF_UP);
            this.highWaterAwayUpsetRate = BigDecimal.valueOf(this.highWaterAwayUpsetCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(hwDecimal, 2, RoundingMode.HALF_UP);
        } else {
            this.highWaterUpsetRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            this.highWaterHomeUpsetRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            this.highWaterAwayUpsetRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // 2. 中水指标计算
        this.midWaterUpsetCount = this.midWaterHomeUpsetCount + this.midWaterAwayUpsetCount;
        this.midWaterNormalCount = Math.max(0, this.midWaterMatches - this.midWaterUpsetCount);
        if (this.midWaterMatches > 0) {
            BigDecimal mwDecimal = BigDecimal.valueOf(this.midWaterMatches);
            this.midWaterUpsetRate = BigDecimal.valueOf(this.midWaterUpsetCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(mwDecimal, 2, RoundingMode.HALF_UP);
            this.midWaterHomeUpsetRate = BigDecimal.valueOf(this.midWaterHomeUpsetCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(mwDecimal, 2, RoundingMode.HALF_UP);
            this.midWaterAwayUpsetRate = BigDecimal.valueOf(this.midWaterAwayUpsetCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(mwDecimal, 2, RoundingMode.HALF_UP);
        } else {
            this.midWaterUpsetRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            this.midWaterHomeUpsetRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            this.midWaterAwayUpsetRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // 3. 低水指标计算
        this.lowWaterUpsetCount = this.lowWaterHomeUpsetCount + this.lowWaterAwayUpsetCount;
        this.lowWaterNormalCount = Math.max(0, this.lowWaterMatches - this.lowWaterUpsetCount);
        if (this.lowWaterMatches > 0) {
            BigDecimal lwDecimal = BigDecimal.valueOf(this.lowWaterMatches);
            this.lowWaterUpsetRate = BigDecimal.valueOf(this.lowWaterUpsetCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(lwDecimal, 2, RoundingMode.HALF_UP);
            this.lowWaterHomeUpsetRate = BigDecimal.valueOf(this.lowWaterHomeUpsetCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(lwDecimal, 2, RoundingMode.HALF_UP);
            this.lowWaterAwayUpsetRate = BigDecimal.valueOf(this.lowWaterAwayUpsetCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(lwDecimal, 2, RoundingMode.HALF_UP);
        } else {
            this.lowWaterUpsetRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            this.lowWaterHomeUpsetRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            this.lowWaterAwayUpsetRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // 4. 汇总场数
        int sumOfWaterMatches = this.highWaterMatches + this.midWaterMatches + this.lowWaterMatches;
        if (this.totalMatches == null || this.totalMatches <= 0 || this.totalMatches < sumOfWaterMatches) {
            this.totalMatches = sumOfWaterMatches;
        }

        this.totalHomeUpsetCount = this.highWaterHomeUpsetCount + this.midWaterHomeUpsetCount + this.lowWaterHomeUpsetCount;
        this.totalAwayUpsetCount = this.highWaterAwayUpsetCount + this.midWaterAwayUpsetCount + this.lowWaterAwayUpsetCount;
        this.totalUpsetCount = this.highWaterUpsetCount + this.midWaterUpsetCount + this.lowWaterUpsetCount;
        this.totalNormalCount = Math.max(0, this.totalMatches - this.totalUpsetCount);

        // 5. 综合总翻车率与主客场翻车率
        if (this.totalMatches > 0) {
            BigDecimal totalDecimal = BigDecimal.valueOf(this.totalMatches);
            this.overallUpsetRate = BigDecimal.valueOf(this.totalUpsetCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalDecimal, 2, RoundingMode.HALF_UP);
            this.overallHomeUpsetRate = BigDecimal.valueOf(this.totalHomeUpsetCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalDecimal, 2, RoundingMode.HALF_UP);
            this.overallAwayUpsetRate = BigDecimal.valueOf(this.totalAwayUpsetCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalDecimal, 2, RoundingMode.HALF_UP);
        } else {
            this.overallUpsetRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            this.overallHomeUpsetRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            this.overallAwayUpsetRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // 6. 最高翻车水位评定
        BigDecimal maxRate = this.highWaterUpsetRate;
        String maxLevel = "HIGH_WATER";
        if (this.midWaterUpsetRate.compareTo(maxRate) > 0) {
            maxRate = this.midWaterUpsetRate;
            maxLevel = "MID_WATER";
        }
        if (this.lowWaterUpsetRate.compareTo(maxRate) > 0) {
            maxRate = this.lowWaterUpsetRate;
            maxLevel = "LOW_WATER";
        }
        if (this.highWaterUpsetRate.compareTo(this.midWaterUpsetRate) == 0 
                && this.midWaterUpsetRate.compareTo(this.lowWaterUpsetRate) == 0) {
            maxLevel = "BALANCED";
        }
        this.highestUpsetWaterLevel = maxLevel;

        // 7. 主客场爆冷倾向
        if (this.totalHomeUpsetCount > this.totalAwayUpsetCount) {
            this.upsetTendency = "HOME_BIAS"; // 主场翻车更频繁
        } else if (this.totalAwayUpsetCount > this.totalHomeUpsetCount) {
            this.upsetTendency = "AWAY_BIAS"; // 客场翻车更频繁
        } else {
            this.upsetTendency = "BALANCED";
        }

        // 8. 综合冷门风险评级
        if (this.overallUpsetRate.compareTo(BigDecimal.valueOf(45)) >= 0) {
            this.riskLevel = "HIGH_RISK"; // 冷门频发，高危
        } else if (this.overallUpsetRate.compareTo(BigDecimal.valueOf(25)) >= 0) {
            this.riskLevel = "MODERATE"; // 中度波动
        } else {
            this.riskLevel = "SAFE_NORMAL"; // 正路为主
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

    public Integer getHighWaterMatches() {
        return highWaterMatches;
    }

    public void setHighWaterMatches(Integer highWaterMatches) {
        this.highWaterMatches = highWaterMatches;
    }

    public Integer getHighWaterHomeUpsetCount() {
        return highWaterHomeUpsetCount;
    }

    public void setHighWaterHomeUpsetCount(Integer highWaterHomeUpsetCount) {
        this.highWaterHomeUpsetCount = highWaterHomeUpsetCount;
    }

    public Integer getHighWaterAwayUpsetCount() {
        return highWaterAwayUpsetCount;
    }

    public void setHighWaterAwayUpsetCount(Integer highWaterAwayUpsetCount) {
        this.highWaterAwayUpsetCount = highWaterAwayUpsetCount;
    }

    public Integer getHighWaterUpsetCount() {
        return highWaterUpsetCount;
    }

    public void setHighWaterUpsetCount(Integer highWaterUpsetCount) {
        this.highWaterUpsetCount = highWaterUpsetCount;
    }

    public Integer getHighWaterNormalCount() {
        return highWaterNormalCount;
    }

    public void setHighWaterNormalCount(Integer highWaterNormalCount) {
        this.highWaterNormalCount = highWaterNormalCount;
    }

    public BigDecimal getHighWaterUpsetRate() {
        return highWaterUpsetRate;
    }

    public void setHighWaterUpsetRate(BigDecimal highWaterUpsetRate) {
        this.highWaterUpsetRate = highWaterUpsetRate;
    }

    public BigDecimal getHighWaterHomeUpsetRate() {
        return highWaterHomeUpsetRate;
    }

    public void setHighWaterHomeUpsetRate(BigDecimal highWaterHomeUpsetRate) {
        this.highWaterHomeUpsetRate = highWaterHomeUpsetRate;
    }

    public BigDecimal getHighWaterAwayUpsetRate() {
        return highWaterAwayUpsetRate;
    }

    public void setHighWaterAwayUpsetRate(BigDecimal highWaterAwayUpsetRate) {
        this.highWaterAwayUpsetRate = highWaterAwayUpsetRate;
    }

    public Integer getMidWaterMatches() {
        return midWaterMatches;
    }

    public void setMidWaterMatches(Integer midWaterMatches) {
        this.midWaterMatches = midWaterMatches;
    }

    public Integer getMidWaterHomeUpsetCount() {
        return midWaterHomeUpsetCount;
    }

    public void setMidWaterHomeUpsetCount(Integer midWaterHomeUpsetCount) {
        this.midWaterHomeUpsetCount = midWaterHomeUpsetCount;
    }

    public Integer getMidWaterAwayUpsetCount() {
        return midWaterAwayUpsetCount;
    }

    public void setMidWaterAwayUpsetCount(Integer midWaterAwayUpsetCount) {
        this.midWaterAwayUpsetCount = midWaterAwayUpsetCount;
    }

    public Integer getMidWaterUpsetCount() {
        return midWaterUpsetCount;
    }

    public void setMidWaterUpsetCount(Integer midWaterUpsetCount) {
        this.midWaterUpsetCount = midWaterUpsetCount;
    }

    public Integer getMidWaterNormalCount() {
        return midWaterNormalCount;
    }

    public void setMidWaterNormalCount(Integer midWaterNormalCount) {
        this.midWaterNormalCount = midWaterNormalCount;
    }

    public BigDecimal getMidWaterUpsetRate() {
        return midWaterUpsetRate;
    }

    public void setMidWaterUpsetRate(BigDecimal midWaterUpsetRate) {
        this.midWaterUpsetRate = midWaterUpsetRate;
    }

    public BigDecimal getMidWaterHomeUpsetRate() {
        return midWaterHomeUpsetRate;
    }

    public void setMidWaterHomeUpsetRate(BigDecimal midWaterHomeUpsetRate) {
        this.midWaterHomeUpsetRate = midWaterHomeUpsetRate;
    }

    public BigDecimal getMidWaterAwayUpsetRate() {
        return midWaterAwayUpsetRate;
    }

    public void setMidWaterAwayUpsetRate(BigDecimal midWaterAwayUpsetRate) {
        this.midWaterAwayUpsetRate = midWaterAwayUpsetRate;
    }

    public Integer getLowWaterMatches() {
        return lowWaterMatches;
    }

    public void setLowWaterMatches(Integer lowWaterMatches) {
        this.lowWaterMatches = lowWaterMatches;
    }

    public Integer getLowWaterHomeUpsetCount() {
        return lowWaterHomeUpsetCount;
    }

    public void setLowWaterHomeUpsetCount(Integer lowWaterHomeUpsetCount) {
        this.lowWaterHomeUpsetCount = lowWaterHomeUpsetCount;
    }

    public Integer getLowWaterAwayUpsetCount() {
        return lowWaterAwayUpsetCount;
    }

    public void setLowWaterAwayUpsetCount(Integer lowWaterAwayUpsetCount) {
        this.lowWaterAwayUpsetCount = lowWaterAwayUpsetCount;
    }

    public Integer getLowWaterUpsetCount() {
        return lowWaterUpsetCount;
    }

    public void setLowWaterUpsetCount(Integer lowWaterUpsetCount) {
        this.lowWaterUpsetCount = lowWaterUpsetCount;
    }

    public Integer getLowWaterNormalCount() {
        return lowWaterNormalCount;
    }

    public void setLowWaterNormalCount(Integer lowWaterNormalCount) {
        this.lowWaterNormalCount = lowWaterNormalCount;
    }

    public BigDecimal getLowWaterUpsetRate() {
        return lowWaterUpsetRate;
    }

    public void setLowWaterUpsetRate(BigDecimal lowWaterUpsetRate) {
        this.lowWaterUpsetRate = lowWaterUpsetRate;
    }

    public BigDecimal getLowWaterHomeUpsetRate() {
        return lowWaterHomeUpsetRate;
    }

    public void setLowWaterHomeUpsetRate(BigDecimal lowWaterHomeUpsetRate) {
        this.lowWaterHomeUpsetRate = lowWaterHomeUpsetRate;
    }

    public BigDecimal getLowWaterAwayUpsetRate() {
        return lowWaterAwayUpsetRate;
    }

    public void setLowWaterAwayUpsetRate(BigDecimal lowWaterAwayUpsetRate) {
        this.lowWaterAwayUpsetRate = lowWaterAwayUpsetRate;
    }

    public Integer getTotalUpsetCount() {
        return totalUpsetCount;
    }

    public void setTotalUpsetCount(Integer totalUpsetCount) {
        this.totalUpsetCount = totalUpsetCount;
    }

    public Integer getTotalNormalCount() {
        return totalNormalCount;
    }

    public void setTotalNormalCount(Integer totalNormalCount) {
        this.totalNormalCount = totalNormalCount;
    }

    public Integer getTotalHomeUpsetCount() {
        return totalHomeUpsetCount;
    }

    public void setTotalHomeUpsetCount(Integer totalHomeUpsetCount) {
        this.totalHomeUpsetCount = totalHomeUpsetCount;
    }

    public Integer getTotalAwayUpsetCount() {
        return totalAwayUpsetCount;
    }

    public void setTotalAwayUpsetCount(Integer totalAwayUpsetCount) {
        this.totalAwayUpsetCount = totalAwayUpsetCount;
    }

    public BigDecimal getOverallUpsetRate() {
        return overallUpsetRate;
    }

    public void setOverallUpsetRate(BigDecimal overallUpsetRate) {
        this.overallUpsetRate = overallUpsetRate;
    }

    public BigDecimal getOverallHomeUpsetRate() {
        return overallHomeUpsetRate;
    }

    public void setOverallHomeUpsetRate(BigDecimal overallHomeUpsetRate) {
        this.overallHomeUpsetRate = overallHomeUpsetRate;
    }

    public BigDecimal getOverallAwayUpsetRate() {
        return overallAwayUpsetRate;
    }

    public void setOverallAwayUpsetRate(BigDecimal overallAwayUpsetRate) {
        this.overallAwayUpsetRate = overallAwayUpsetRate;
    }

    public String getHighestUpsetWaterLevel() {
        return highestUpsetWaterLevel;
    }

    public void setHighestUpsetWaterLevel(String highestUpsetWaterLevel) {
        this.highestUpsetWaterLevel = highestUpsetWaterLevel;
    }

    public String getUpsetTendency() {
        return upsetTendency;
    }

    public void setUpsetTendency(String upsetTendency) {
        this.upsetTendency = upsetTendency;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
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
