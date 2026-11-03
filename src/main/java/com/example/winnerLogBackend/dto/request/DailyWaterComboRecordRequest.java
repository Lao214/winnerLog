package com.example.winnerLogBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "每日水位规律串子战绩录入/更新请求")
public class DailyWaterComboRecordRequest {

    @Schema(description = "记录ID (更新时必填)", example = "1")
    private Long id;

    @NotNull(message = "比赛日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "日期 / 比赛日", example = "2026-10-29", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate recordDate;

    @NotNull(message = "比赛场数不能为空")
    @Min(value = 0, message = "比赛场数不能小于0")
    @Schema(description = "比赛场数", example = "6", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer matchCount;

    @NotNull(message = "打票成本不能为空")
    @Min(value = 0, message = "打票成本不能小于0")
    @Schema(description = "打票成本(元)", example = "400.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal ticketCost;

    @NotNull(message = "最终利润不能为空")
    @Schema(description = "最终利润(元，正数为盈利，负数为亏损)", example = "680.50", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal finalProfit;

    @Min(value = 0, message = "长串成本不能小于0")
    @Schema(description = "长串打票成本(元，如无则填0或留空)", example = "100.00")
    private BigDecimal longComboCost;

    @Schema(description = "长串最终利润(元，正数为盈利，负数为亏损)", example = "850.00")
    private BigDecimal longComboProfit;

    @Schema(description = "串关类型/策略名称", example = "水位高低配2串1+3串1")
    private String comboType;

    @Schema(description = "复盘心得与特征总结", example = "高水博冷单关打出，低水中水稳健串关双红")
    private String remarks;

    public DailyWaterComboRecordRequest() {}

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
}
