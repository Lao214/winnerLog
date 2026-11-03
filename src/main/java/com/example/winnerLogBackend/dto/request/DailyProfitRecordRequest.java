package com.example.winnerLogBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "每日成本与利润战绩录入/更新请求")
public class DailyProfitRecordRequest {

    @Schema(description = "记录ID (更新时必填)", example = "1")
    private Long id;

    @NotNull(message = "比赛日不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "比赛日", example = "2026-10-24", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate matchDate;

    @NotNull(message = "穿盘比赛数量不能为空")
    @Min(value = 0, message = "穿盘比赛数量不能小于0")
    @Schema(description = "穿盘比赛数量", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer breakCount;

    @NotNull(message = "未穿盘比赛数量不能为空")
    @Min(value = 0, message = "未穿盘比赛数量不能小于0")
    @Schema(description = "未穿盘比赛数量", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer unbreakCount;

    @NotNull(message = "主三串副二串成本不能为空")
    @Min(value = 0, message = "主三串副二串成本不能小于0")
    @Schema(description = "主三串副二串成本(元)", example = "200.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal strategy3m2sCost;

    @NotNull(message = "主三串副二串利润不能为空")
    @Schema(description = "主三串副二串利润(元，负数为亏损)", example = "350.50", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal strategy3m2sProfit;

    @NotNull(message = "主二串副三串成本不能为空")
    @Min(value = 0, message = "主二串副三串成本不能小于0")
    @Schema(description = "主二串副三串成本(元)", example = "300.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal strategy2m3sCost;

    @NotNull(message = "主二串副三串利润不能为空")
    @Schema(description = "主二串副三串利润(元，负数为亏损)", example = "120.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal strategy2m3sProfit;

    @Schema(description = "备注复盘/特征总结", example = "周六早场五大联赛深盘全打穿，主推3串2单大红")
    private String remarks;

    public DailyProfitRecordRequest() {}

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

    public Integer getBreakCount() {
        return breakCount;
    }

    public void setBreakCount(Integer breakCount) {
        this.breakCount = breakCount;
    }

    public Integer getUnbreakCount() {
        return unbreakCount;
    }

    public void setUnbreakCount(Integer unbreakCount) {
        this.unbreakCount = unbreakCount;
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
