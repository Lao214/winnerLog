package com.example.winnerLogBackend.dto.request;

import com.example.winnerLogBackend.enums.MatchStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "比赛查询过滤请求参数")
public class MatchQueryRequest {

    @Schema(description = "联赛编码 (如 PL, LA_LIGA, SERIE_A, BUNDESLIGA, LIGUE_1, UCL, WORLD_CUP, EURO)", example = "PL")
    private String leagueCode;

    @Schema(description = "赛季名称 (如 2024-2025, 2025-2026, 2026)", example = "2024-2025")
    private String seasonName;

    @Schema(description = "比赛状态 (SCHEDULED, IN_PROGRESS, FINISHED, POSTPONED, CANCELLED)", example = "FINISHED")
    private MatchStatusEnum status;

    @Schema(description = "球队名称关键字 (主队或客队模糊搜索)", example = "阿森纳")
    private String teamName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开赛时间起始范围 (yyyy-MM-dd HH:mm:ss)", example = "2025-01-01 00:00:00")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开赛时间结束范围 (yyyy-MM-dd HH:mm:ss)", example = "2025-12-31 23:59:59")
    private LocalDateTime endTime;

    @Schema(description = "页码 (0-indexed, 默认0)", example = "0")
    private Integer page = 0;

    @Schema(description = "每页数量 (默认20)", example = "20")
    private Integer size = 20;

    public MatchQueryRequest() {}

    public MatchQueryRequest(String leagueCode, String seasonName, MatchStatusEnum status, String teamName, LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size) {
        this.leagueCode = leagueCode;
        this.seasonName = seasonName;
        this.status = status;
        this.teamName = teamName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.page = page != null ? page : 0;
        this.size = size != null ? size : 20;
    }

    public static MatchQueryRequestBuilder builder() {
        return new MatchQueryRequestBuilder();
    }

    public static class MatchQueryRequestBuilder {
        private String leagueCode;
        private String seasonName;
        private MatchStatusEnum status;
        private String teamName;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Integer page = 0;
        private Integer size = 20;

        public MatchQueryRequestBuilder leagueCode(String leagueCode) { this.leagueCode = leagueCode; return this; }
        public MatchQueryRequestBuilder seasonName(String seasonName) { this.seasonName = seasonName; return this; }
        public MatchQueryRequestBuilder status(MatchStatusEnum status) { this.status = status; return this; }
        public MatchQueryRequestBuilder teamName(String teamName) { this.teamName = teamName; return this; }
        public MatchQueryRequestBuilder startTime(LocalDateTime startTime) { this.startTime = startTime; return this; }
        public MatchQueryRequestBuilder endTime(LocalDateTime endTime) { this.endTime = endTime; return this; }
        public MatchQueryRequestBuilder page(Integer page) { this.page = page; return this; }
        public MatchQueryRequestBuilder size(Integer size) { this.size = size; return this; }

        public MatchQueryRequest build() {
            return new MatchQueryRequest(leagueCode, seasonName, status, teamName, startTime, endTime, page, size);
        }
    }

    public String getLeagueCode() { return leagueCode; }
    public void setLeagueCode(String leagueCode) { this.leagueCode = leagueCode; }
    public String getSeasonName() { return seasonName; }
    public void setSeasonName(String seasonName) { this.seasonName = seasonName; }
    public MatchStatusEnum getStatus() { return status; }
    public void setStatus(MatchStatusEnum status) { this.status = status; }
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}
