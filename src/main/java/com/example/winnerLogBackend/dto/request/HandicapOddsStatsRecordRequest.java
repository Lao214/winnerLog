package com.example.winnerLogBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "让球赔率六区间战绩记录请求参数")
public class HandicapOddsStatsRecordRequest {

    @NotNull(message = "记录日期不能为空")
    @Schema(description = "战绩日期", example = "2026-10-26", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate recordDate;

    @Schema(description = "批次标题/说明", example = "五大联赛周日晚场让球盘口统计")
    private String batchTitle;

    // ==================== 主让球六区间 (胜/平/负/穿盘) ====================
    @Schema(description = "主让 R1(<=1.40) 胜场数", example = "3")
    private Integer homeR1Win = 0;
    @Schema(description = "主让 R1(<=1.40) 平场数", example = "0")
    private Integer homeR1Draw = 0;
    @Schema(description = "主让 R1(<=1.40) 负场数", example = "0")
    private Integer homeR1Loss = 0;
    @Schema(description = "主让 R1(<=1.40) 赢球穿盘数", example = "3")
    private Integer homeR1Break = 0;

    @Schema(description = "主让 R2(1.41-1.60) 胜场数", example = "2")
    private Integer homeR2Win = 0;
    @Schema(description = "主让 R2(1.41-1.60) 平场数", example = "1")
    private Integer homeR2Draw = 0;
    @Schema(description = "主让 R2(1.41-1.60) 负场数", example = "0")
    private Integer homeR2Loss = 0;
    @Schema(description = "主让 R2(1.41-1.60) 赢球穿盘数", example = "1")
    private Integer homeR2Break = 0;

    @Schema(description = "主让 R3(1.61-1.80) 胜场数", example = "1")
    private Integer homeR3Win = 0;
    @Schema(description = "主让 R3(1.61-1.80) 平场数", example = "1")
    private Integer homeR3Draw = 0;
    @Schema(description = "主让 R3(1.61-1.80) 负场数", example = "0")
    private Integer homeR3Loss = 0;
    @Schema(description = "主让 R3(1.61-1.80) 赢球穿盘数", example = "1")
    private Integer homeR3Break = 0;

    @Schema(description = "主让 R4(1.81-2.00) 胜场数", example = "1")
    private Integer homeR4Win = 0;
    @Schema(description = "主让 R4(1.81-2.00) 平场数", example = "0")
    private Integer homeR4Draw = 0;
    @Schema(description = "主让 R4(1.81-2.00) 负场数", example = "1")
    private Integer homeR4Loss = 0;
    @Schema(description = "主让 R4(1.81-2.00) 赢球穿盘数", example = "0")
    private Integer homeR4Break = 0;

    @Schema(description = "主让 R5(2.01-2.20) 胜场数", example = "0")
    private Integer homeR5Win = 0;
    @Schema(description = "主让 R5(2.01-2.20) 平场数", example = "1")
    private Integer homeR5Draw = 0;
    @Schema(description = "主让 R5(2.01-2.20) 负场数", example = "1")
    private Integer homeR5Loss = 0;
    @Schema(description = "主让 R5(2.01-2.20) 赢球穿盘数", example = "0")
    private Integer homeR5Break = 0;

    @Schema(description = "主让 R6(>=2.21) 胜场数", example = "0")
    private Integer homeR6Win = 0;
    @Schema(description = "主让 R6(>=2.21) 平场数", example = "0")
    private Integer homeR6Draw = 0;
    @Schema(description = "主让 R6(>=2.21) 负场数", example = "0")
    private Integer homeR6Loss = 0;
    @Schema(description = "主让 R6(>=2.21) 赢球穿盘数", example = "0")
    private Integer homeR6Break = 0;

    // ==================== 客让球六区间 (胜/平/负/穿盘) ====================
    @Schema(description = "客让 R1(<=1.40) 胜场数", example = "1")
    private Integer awayR1Win = 0;
    @Schema(description = "客让 R1(<=1.40) 平场数", example = "0")
    private Integer awayR1Draw = 0;
    @Schema(description = "客让 R1(<=1.40) 负场数", example = "0")
    private Integer awayR1Loss = 0;
    @Schema(description = "客让 R1(<=1.40) 赢球穿盘数", example = "1")
    private Integer awayR1Break = 0;

    @Schema(description = "客让 R2(1.41-1.60) 胜场数", example = "1")
    private Integer awayR2Win = 0;
    @Schema(description = "客让 R2(1.41-1.60) 平场数", example = "1")
    private Integer awayR2Draw = 0;
    @Schema(description = "客让 R2(1.41-1.60) 负场数", example = "0")
    private Integer awayR2Loss = 0;
    @Schema(description = "客让 R2(1.41-1.60) 赢球穿盘数", example = "1")
    private Integer awayR2Break = 0;

    @Schema(description = "客让 R3(1.61-1.80) 胜场数", example = "1")
    private Integer awayR3Win = 0;
    @Schema(description = "客让 R3(1.61-1.80) 平场数", example = "0")
    private Integer awayR3Draw = 0;
    @Schema(description = "客让 R3(1.61-1.80) 负场数", example = "1")
    private Integer awayR3Loss = 0;
    @Schema(description = "客让 R3(1.61-1.80) 赢球穿盘数", example = "0")
    private Integer awayR3Break = 0;

    @Schema(description = "客让 R4(1.81-2.00) 胜场数", example = "0")
    private Integer awayR4Win = 0;
    @Schema(description = "客让 R4(1.81-2.00) 平场数", example = "1")
    private Integer awayR4Draw = 0;
    @Schema(description = "客让 R4(1.81-2.00) 负场数", example = "0")
    private Integer awayR4Loss = 0;
    @Schema(description = "客让 R4(1.81-2.00) 赢球穿盘数", example = "0")
    private Integer awayR4Break = 0;

    @Schema(description = "客让 R5(2.01-2.20) 胜场数", example = "0")
    private Integer awayR5Win = 0;
    @Schema(description = "客让 R5(2.01-2.20) 平场数", example = "0")
    private Integer awayR5Draw = 0;
    @Schema(description = "客让 R5(2.01-2.20) 负场数", example = "1")
    private Integer awayR5Loss = 0;
    @Schema(description = "客让 R5(2.01-2.20) 赢球穿盘数", example = "0")
    private Integer awayR5Break = 0;

    @Schema(description = "客让 R6(>=2.21) 胜场数", example = "0")
    private Integer awayR6Win = 0;
    @Schema(description = "客让 R6(>=2.21) 平场数", example = "0")
    private Integer awayR6Draw = 0;
    @Schema(description = "客让 R6(>=2.21) 负场数", example = "0")
    private Integer awayR6Loss = 0;
    @Schema(description = "客让 R6(>=2.21) 赢球穿盘数", example = "0")
    private Integer awayR6Break = 0;

    @Schema(description = "备注复盘", example = "低赔让球打穿概率极高")
    private String remarks;

    public HandicapOddsStatsRecordRequest() {
    }

    // Getters and Setters

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
