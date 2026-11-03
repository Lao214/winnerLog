package com.example.winnerLogBackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 每日水位规律串子战绩与大数据收益实体
 * 记录日期、比赛场数、打票成本、最终利润，自动分析ROI、总派彩回款、场均指标与大数据风控走势
 */
@Entity
@Table(name = "daily_water_combo_record", indexes = {
        @Index(name = "uk_combo_record_date", columnList = "record_date", unique = true),
        @Index(name = "idx_combo_record_date", columnList = "record_date"),
        @Index(name = "idx_combo_final_profit", columnList = "final_profit"),
        @Index(name = "idx_combo_roi_rate", columnList = "roi_rate"),
        @Index(name = "idx_combo_win_loss", columnList = "win_loss_status")
})
public class DailyWaterComboRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_date", nullable = false, unique = true)
    private LocalDate recordDate;

    // ==================== 1. 核心战绩数据 (常规串关) ====================
    @Column(name = "match_count", nullable = false)
    private Integer matchCount = 0;

    @Column(name = "ticket_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal ticketCost = BigDecimal.ZERO;

    @Column(name = "final_profit", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalProfit = BigDecimal.ZERO;

    // ==================== 2. 长串战绩数据 (梦想高赔长串) ====================
    @Column(name = "long_combo_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal longComboCost = BigDecimal.ZERO;

    @Column(name = "long_combo_profit", nullable = false, precision = 10, scale = 2)
    private BigDecimal longComboProfit = BigDecimal.ZERO;

    @Column(name = "long_combo_roi", nullable = false, precision = 6, scale = 2)
    private BigDecimal longComboRoi = BigDecimal.ZERO;

    // ==================== 3. 策略属性 ====================
    @Column(name = "combo_type", length = 50)
    private String comboType = "水位对冲串关";

    // ==================== 4. 自动计算综合大数据衍生指标 ====================
    @Column(name = "total_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCost = BigDecimal.ZERO;

    @Column(name = "total_profit", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalProfit = BigDecimal.ZERO;

    @Column(name = "total_return", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalReturn = BigDecimal.ZERO;

    @Column(name = "roi_rate", nullable = false, precision = 6, scale = 2)
    private BigDecimal roiRate = BigDecimal.ZERO;

    @Column(name = "win_loss_status", length = 20)
    private String winLossStatus = "PROFIT"; // PROFIT(盈利红单) / BREAKEVEN(保本) / LOSS(亏损黑单)

    @Column(name = "profit_level", length = 20)
    private String profitLevel = "PROFIT"; // GREAT_PROFIT / PROFIT / BREAKEVEN / LOSS / HEAVY_LOSS

    @Column(name = "avg_match_cost", precision = 10, scale = 2)
    private BigDecimal avgMatchCost = BigDecimal.ZERO;

    @Column(name = "avg_match_profit", precision = 10, scale = 2)
    private BigDecimal avgMatchProfit = BigDecimal.ZERO;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public DailyWaterComboRecord() {}

    /**
     * 前置校验与自动计算衍生财务与大数据指标
     */
    @PrePersist
    @PreUpdate
    public void calculateMetrics() {
        if (matchCount == null || matchCount < 0) matchCount = 0;
        if (ticketCost == null) ticketCost = BigDecimal.ZERO;
        if (finalProfit == null) finalProfit = BigDecimal.ZERO;
        if (longComboCost == null) longComboCost = BigDecimal.ZERO;
        if (longComboProfit == null) longComboProfit = BigDecimal.ZERO;

        // 1. 长串 ROI 计算
        if (this.longComboCost.compareTo(BigDecimal.ZERO) > 0) {
            this.longComboRoi = this.longComboProfit.multiply(BigDecimal.valueOf(100))
                    .divide(this.longComboCost, 2, RoundingMode.HALF_UP);
        } else {
            this.longComboRoi = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // 2. 综合总打票成本与综合总利润 (常规 + 长串)
        this.totalCost = this.ticketCost.add(this.longComboCost).setScale(2, RoundingMode.HALF_UP);
        this.totalProfit = this.finalProfit.add(this.longComboProfit).setScale(2, RoundingMode.HALF_UP);

        // 3. 综合总回款 = 总成本 + 总利润
        this.totalReturn = this.totalCost.add(this.totalProfit).setScale(2, RoundingMode.HALF_UP);

        // 4. 综合投资回报率 ROI (%) = (综合总利润 / 综合总成本) * 100
        if (this.totalCost.compareTo(BigDecimal.ZERO) > 0) {
            this.roiRate = this.totalProfit.multiply(BigDecimal.valueOf(100))
                    .divide(this.totalCost, 2, RoundingMode.HALF_UP);
        } else {
            this.roiRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // 5. 场均指标计算 (基于综合总成本与综合总利润)
        if (this.matchCount > 0) {
            BigDecimal matchDecimal = BigDecimal.valueOf(this.matchCount);
            this.avgMatchCost = this.totalCost.divide(matchDecimal, 2, RoundingMode.HALF_UP);
            this.avgMatchProfit = this.totalProfit.divide(matchDecimal, 2, RoundingMode.HALF_UP);
        } else {
            this.avgMatchCost = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            this.avgMatchProfit = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // 6. 盈亏状态判定
        if (this.totalProfit.compareTo(BigDecimal.ZERO) > 0) {
            this.winLossStatus = "PROFIT";
        } else if (this.totalProfit.compareTo(BigDecimal.ZERO) < 0) {
            this.winLossStatus = "LOSS";
        } else {
            this.winLossStatus = "BREAKEVEN";
        }

        // 7. 收益表现等级
        if (this.roiRate.compareTo(BigDecimal.valueOf(100)) >= 0) {
            this.profitLevel = "GREAT_PROFIT"; // 翻倍大红暴击
        } else if (this.roiRate.compareTo(BigDecimal.ZERO) > 0) {
            this.profitLevel = "PROFIT"; // 稳健盈利
        } else if (this.roiRate.compareTo(BigDecimal.ZERO) == 0) {
            this.profitLevel = "BREAKEVEN"; // 保本
        } else if (this.roiRate.compareTo(BigDecimal.valueOf(-50)) >= 0) {
            this.profitLevel = "LOSS"; // 小幅回撤
        } else {
            this.profitLevel = "HEAVY_LOSS"; // 深度回撤
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

    public Integer getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(Integer matchCount) {
        this.matchCount = matchCount;
    }

    public BigDecimal getTicketCost() {
        return ticketCost;
    }

    public void setTicketCost(BigDecimal ticketCost) {
        this.ticketCost = ticketCost;
    }

    public BigDecimal getFinalProfit() {
        return finalProfit;
    }

    public void setFinalProfit(BigDecimal finalProfit) {
        this.finalProfit = finalProfit;
    }

    public String getComboType() {
        return comboType;
    }

    public void setComboType(String comboType) {
        this.comboType = comboType;
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

    public String getWinLossStatus() {
        return winLossStatus;
    }

    public void setWinLossStatus(String winLossStatus) {
        this.winLossStatus = winLossStatus;
    }

    public String getProfitLevel() {
        return profitLevel;
    }

    public void setProfitLevel(String profitLevel) {
        this.profitLevel = profitLevel;
    }

    public BigDecimal getAvgMatchCost() {
        return avgMatchCost;
    }

    public void setAvgMatchCost(BigDecimal avgMatchCost) {
        this.avgMatchCost = avgMatchCost;
    }

    public BigDecimal getAvgMatchProfit() {
        return avgMatchProfit;
    }

    public void setAvgMatchProfit(BigDecimal avgMatchProfit) {
        this.avgMatchProfit = avgMatchProfit;
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

    public BigDecimal getLongComboCost() {
        return longComboCost;
    }

    public void setLongComboCost(BigDecimal longComboCost) {
        this.longComboCost = longComboCost;
    }

    public BigDecimal getLongComboProfit() {
        return longComboProfit;
    }

    public void setLongComboProfit(BigDecimal longComboProfit) {
        this.longComboProfit = longComboProfit;
    }

    public BigDecimal getLongComboRoi() {
        return longComboRoi;
    }

    public void setLongComboRoi(BigDecimal longComboRoi) {
        this.longComboRoi = longComboRoi;
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
}
