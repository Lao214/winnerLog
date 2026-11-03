package com.example.winnerLogBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "每日总进球数成本与利润战绩录入/更新请求")
public class DailyGoalsProfitRecordRequest {

    @Schema(description = "记录ID (更新时必填)", example = "1")
    private Long id;

    @NotNull(message = "比赛日不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "比赛日", example = "2026-10-25", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate matchDate;

    @NotNull(message = "符合规律比赛数量不能为空")
    @Min(value = 0, message = "符合规律比赛数量不能小于0")
    @Schema(description = "符合规律比赛数量", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer patternFitCount;

    @NotNull(message = "稍微符合规律比赛数量不能为空")
    @Min(value = 0, message = "稍微符合规律比赛数量不能小于0")
    @Schema(description = "稍微符合规律比赛数量", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer patternSlightCount;

    @NotNull(message = "完全不符合规律比赛数量不能为空")
    @Min(value = 0, message = "完全不符合规律比赛数量不能小于0")
    @Schema(description = "完全不符合规律比赛数量", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer patternMismatchCount;

    @NotNull(message = "主三串副二串成本不能为空")
    @Min(value = 0, message = "主三串副二串成本不能小于0")
    @Schema(description = "主三串副二串成本(元)", example = "300.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal strategy3m2sCost;

    @NotNull(message = "主三串副二串利润不能为空")
    @Schema(description = "主三串副二串利润(元，负数为亏损)", example = "420.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal strategy3m2sProfit;

    @NotNull(message = "主二串副三串成本不能为空")
    @Min(value = 0, message = "主二串副三串成本不能小于0")
    @Schema(description = "主二串副三串成本(元)", example = "200.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal strategy2m3sCost;

    @NotNull(message = "主二串副三串利润不能为空")
    @Schema(description = "主二串副三串利润(元，负数为亏损)", example = "150.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal strategy2m3sProfit;

    @Schema(description = "总进球数战报特征与复盘备注", example = "德甲进球数如期爆发，3M2S命中高赔率组合")
    private String remarks;

    public DailyGoalsProfitRecordRequest() {}

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
