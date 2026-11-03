package com.example.winnerLogBackend.dto.request;

import com.example.winnerLogBackend.enums.MatchStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "比赛结果录入与预测结算请求")
public class SettleMatchRequest {

    @Schema(description = "比赛ID (若已知比赛ID可直接传入)", example = "1")
    private Long matchId;

    @Schema(description = "比赛业务唯一编码 (可选，支持按比赛编码匹配)", example = "PL_202425_ARS_CHE_20250315")
    private String matchCode;

    @Schema(description = "主队名称 (若未传matchId，支持按主客队与时间匹配)", example = "阿森纳")
    private String homeTeam;

    @Schema(description = "客队名称 (若未传matchId，支持按主客队与时间匹配)", example = "切尔西")
    private String awayTeam;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "比赛时间 (若未传matchId，支持按主客队与时间匹配)", example = "2025-03-15 23:00:00")
    private LocalDateTime matchTime;

    @NotNull(message = "主队完场得分不能为空")
    @Schema(description = "主队完场得分", example = "2")
    private Integer homeScore;

    @NotNull(message = "客队完场得分不能为空")
    @Schema(description = "客队完场得分", example = "1")
    private Integer awayScore;

    @Schema(description = "半场主队得分 (可选)", example = "1")
    private Integer halfHomeScore;

    @Schema(description = "半场客队得分 (可选)", example = "0")
    private Integer halfAwayScore;

    @Schema(description = "比赛状态 (默认为 FINISHED 已完赛)", example = "FINISHED")
    private MatchStatusEnum status = MatchStatusEnum.FINISHED;

    public SettleMatchRequest() {}

    public SettleMatchRequest(Long matchId, String matchCode, String homeTeam, String awayTeam, LocalDateTime matchTime, Integer homeScore, Integer awayScore, Integer halfHomeScore, Integer halfAwayScore, MatchStatusEnum status) {
        this.matchId = matchId;
        this.matchCode = matchCode;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchTime = matchTime;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.halfHomeScore = halfHomeScore;
        this.halfAwayScore = halfAwayScore;
        this.status = status != null ? status : MatchStatusEnum.FINISHED;
    }

    public static SettleMatchRequestBuilder builder() {
        return new SettleMatchRequestBuilder();
    }

    public static class SettleMatchRequestBuilder {
        private Long matchId;
        private String matchCode;
        private String homeTeam;
        private String awayTeam;
        private LocalDateTime matchTime;
        private Integer homeScore;
        private Integer awayScore;
        private Integer halfHomeScore;
        private Integer halfAwayScore;
        private MatchStatusEnum status = MatchStatusEnum.FINISHED;

        public SettleMatchRequestBuilder matchId(Long matchId) { this.matchId = matchId; return this; }
        public SettleMatchRequestBuilder matchCode(String matchCode) { this.matchCode = matchCode; return this; }
        public SettleMatchRequestBuilder homeTeam(String homeTeam) { this.homeTeam = homeTeam; return this; }
        public SettleMatchRequestBuilder awayTeam(String awayTeam) { this.awayTeam = awayTeam; return this; }
        public SettleMatchRequestBuilder matchTime(LocalDateTime matchTime) { this.matchTime = matchTime; return this; }
        public SettleMatchRequestBuilder homeScore(Integer homeScore) { this.homeScore = homeScore; return this; }
        public SettleMatchRequestBuilder awayScore(Integer awayScore) { this.awayScore = awayScore; return this; }
        public SettleMatchRequestBuilder halfHomeScore(Integer halfHomeScore) { this.halfHomeScore = halfHomeScore; return this; }
        public SettleMatchRequestBuilder halfAwayScore(Integer halfAwayScore) { this.halfAwayScore = halfAwayScore; return this; }
        public SettleMatchRequestBuilder status(MatchStatusEnum status) { this.status = status; return this; }

        public SettleMatchRequest build() {
            return new SettleMatchRequest(matchId, matchCode, homeTeam, awayTeam, matchTime, homeScore, awayScore, halfHomeScore, halfAwayScore, status);
        }
    }

    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }
    public String getMatchCode() { return matchCode; }
    public void setMatchCode(String matchCode) { this.matchCode = matchCode; }
    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }
    public LocalDateTime getMatchTime() { return matchTime; }
    public void setMatchTime(LocalDateTime matchTime) { this.matchTime = matchTime; }
    public Integer getHomeScore() { return homeScore; }
    public void setHomeScore(Integer homeScore) { this.homeScore = homeScore; }
    public Integer getAwayScore() { return awayScore; }
    public void setAwayScore(Integer awayScore) { this.awayScore = awayScore; }
    public Integer getHalfHomeScore() { return halfHomeScore; }
    public void setHalfHomeScore(Integer halfHomeScore) { this.halfHomeScore = halfHomeScore; }
    public Integer getHalfAwayScore() { return halfAwayScore; }
    public void setHalfAwayScore(Integer halfAwayScore) { this.halfAwayScore = halfAwayScore; }
    public MatchStatusEnum getStatus() { return status; }
    public void setStatus(MatchStatusEnum status) { this.status = status; }
}
