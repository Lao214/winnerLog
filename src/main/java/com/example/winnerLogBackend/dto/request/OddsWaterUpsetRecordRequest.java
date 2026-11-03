package com.example.winnerLogBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "盘口水位冷门翻车战绩录入/更新请求")
public class OddsWaterUpsetRecordRequest {

    @Schema(description = "记录ID (更新时必填)", example = "1")
    private Long id;

    @NotNull(message = "记录日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "记录日期/比赛日", example = "2026-10-29", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate recordDate;

    @Schema(description = "批次标题/说明", example = "2026历史盘口大数据总览")
    private String batchTitle;

    @Schema(description = "历史总场数 (若不传或传0，系统将自动汇总高中低水场数之和)", example = "100")
    @Min(value = 0, message = "历史总场数不能小于0")
    private Integer totalMatches;

    // 高水
    @NotNull(message = "高水场数不能为空")
    @Min(value = 0, message = "高水场数不能小于0")
    @Schema(description = "高水场数", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer highWaterMatches;

    @NotNull(message = "高水主场翻车场数不能为空")
    @Min(value = 0, message = "高水主场翻车场数不能小于0")
    @Schema(description = "高水主场翻车场数", example = "8", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer highWaterHomeUpsetCount;

    @NotNull(message = "高水客场翻车场数不能为空")
    @Min(value = 0, message = "高水客场翻车场数不能小于0")
    @Schema(description = "高水客场翻车场数", example = "6", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer highWaterAwayUpsetCount;

    // 中水
    @NotNull(message = "中水场数不能为空")
    @Min(value = 0, message = "中水场数不能小于0")
    @Schema(description = "中水场数", example = "40", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer midWaterMatches;

    @NotNull(message = "中水主场翻车场数不能为空")
    @Min(value = 0, message = "中水主场翻车场数不能小于0")
    @Schema(description = "中水主场翻车场数", example = "7", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer midWaterHomeUpsetCount;

    @NotNull(message = "中水客场翻车场数不能为空")
    @Min(value = 0, message = "中水客场翻车场数不能小于0")
    @Schema(description = "中水客场翻车场数", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer midWaterAwayUpsetCount;

    // 低水
    @NotNull(message = "低水场数不能为空")
    @Min(value = 0, message = "低水场数不能小于0")
    @Schema(description = "低水场数", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer lowWaterMatches;

    @NotNull(message = "低水主场翻车场数不能为空")
    @Min(value = 0, message = "低水主场翻车场数不能小于0")
    @Schema(description = "低水主场翻车场数", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer lowWaterHomeUpsetCount;

    @NotNull(message = "低水客场翻车场数不能为空")
    @Min(value = 0, message = "低水客场翻车场数不能小于0")
    @Schema(description = "低水客场翻车场数", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer lowWaterAwayUpsetCount;

    @Schema(description = "复盘笔记/操盘备忘", example = "高水主场冷门多，低水相对较稳但需防大热倒灶")
    private String remarks;

    public OddsWaterUpsetRecordRequest() {}

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
