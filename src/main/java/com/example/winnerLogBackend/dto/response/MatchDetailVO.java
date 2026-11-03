package com.example.winnerLogBackend.dto.response;

import com.example.winnerLogBackend.enums.MatchResultEnum;
import com.example.winnerLogBackend.enums.MatchStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "比赛详情与多AI预测对比视图")
public class MatchDetailVO {

    @Schema(description = "比赛ID")
    private Long id;

    @Schema(description = "比赛业务编号")
    private String matchCode;

    @Schema(description = "联赛ID")
    private Long leagueId;

    @Schema(description = "联赛编码 (如 PL, LA_LIGA, UCL)")
    private String leagueCode;

    @Schema(description = "联赛名称")
    private String leagueName;

    @Schema(description = "赛季ID")
    private Long seasonId;

    @Schema(description = "赛季名称 (如 2024-2025)")
    private String seasonName;

    @Schema(description = "比赛轮次/阶段")
    private String roundName;

    @Schema(description = "主队名称")
    private String homeTeam;

    @Schema(description = "客队名称")
    private String awayTeam;

    @Schema(description = "比赛开球时间")
    private LocalDateTime matchTime;

    @Schema(description = "球场场地")
    private String venue;

    @Schema(description = "主队完场得分")
    private Integer homeScore;

    @Schema(description = "客队完场得分")
    private Integer awayScore;

    @Schema(description = "完场比分格式化 (如 2-1)")
    private String scoreStr;

    @Schema(description = "半场主队得分")
    private Integer halfHomeScore;

    @Schema(description = "半场客队得分")
    private Integer halfAwayScore;

    @Schema(description = "真实赛果 (HOME_WIN:主胜, DRAW:平局, AWAY_WIN:客胜)")
    private MatchResultEnum result;

    @Schema(description = "真实赛果描述")
    private String resultDesc;

    @Schema(description = "比赛状态 (SCHEDULED:未开赛, IN_PROGRESS:进行中, FINISHED:已完赛)")
    private MatchStatusEnum status;

    @Schema(description = "比赛状态描述")
    private String statusDesc;

    @Schema(description = "是否已结算预测 (1:已结算, 0:未结算)")
    private Integer isSettled;

    @Schema(description = "结算时间")
    private LocalDateTime settledAt;

    @Schema(description = "该场比赛所有AI模型的预测列表 (GPT, 千问等)")
    private List<MatchPredictionVO> predictions;

    public MatchDetailVO() {}

    public MatchDetailVO(Long id, String matchCode, Long leagueId, String leagueCode, String leagueName, Long seasonId, String seasonName, String roundName, String homeTeam, String awayTeam, LocalDateTime matchTime, String venue, Integer homeScore, Integer awayScore, String scoreStr, Integer halfHomeScore, Integer halfAwayScore, MatchResultEnum result, String resultDesc, MatchStatusEnum status, String statusDesc, Integer isSettled, LocalDateTime settledAt, List<MatchPredictionVO> predictions) {
        this.id = id;
        this.matchCode = matchCode;
        this.leagueId = leagueId;
        this.leagueCode = leagueCode;
        this.leagueName = leagueName;
        this.seasonId = seasonId;
        this.seasonName = seasonName;
        this.roundName = roundName;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchTime = matchTime;
        this.venue = venue;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.scoreStr = scoreStr;
        this.halfHomeScore = halfHomeScore;
        this.halfAwayScore = halfAwayScore;
        this.result = result;
        this.resultDesc = resultDesc;
        this.status = status;
        this.statusDesc = statusDesc;
        this.isSettled = isSettled;
        this.settledAt = settledAt;
        this.predictions = predictions;
    }

    public static MatchDetailVOBuilder builder() {
        return new MatchDetailVOBuilder();
    }

    public static class MatchDetailVOBuilder {
        private Long id;
        private String matchCode;
        private Long leagueId;
        private String leagueCode;
        private String leagueName;
        private Long seasonId;
        private String seasonName;
        private String roundName;
        private String homeTeam;
        private String awayTeam;
        private LocalDateTime matchTime;
        private String venue;
        private Integer homeScore;
        private Integer awayScore;
        private String scoreStr;
        private Integer halfHomeScore;
        private Integer halfAwayScore;
        private MatchResultEnum result;
        private String resultDesc;
        private MatchStatusEnum status;
        private String statusDesc;
        private Integer isSettled;
        private LocalDateTime settledAt;
        private List<MatchPredictionVO> predictions;

        public MatchDetailVOBuilder id(Long id) { this.id = id; return this; }
        public MatchDetailVOBuilder matchCode(String matchCode) { this.matchCode = matchCode; return this; }
        public MatchDetailVOBuilder leagueId(Long leagueId) { this.leagueId = leagueId; return this; }
        public MatchDetailVOBuilder leagueCode(String leagueCode) { this.leagueCode = leagueCode; return this; }
        public MatchDetailVOBuilder leagueName(String leagueName) { this.leagueName = leagueName; return this; }
        public MatchDetailVOBuilder seasonId(Long seasonId) { this.seasonId = seasonId; return this; }
        public MatchDetailVOBuilder seasonName(String seasonName) { this.seasonName = seasonName; return this; }
        public MatchDetailVOBuilder roundName(String roundName) { this.roundName = roundName; return this; }
        public MatchDetailVOBuilder homeTeam(String homeTeam) { this.homeTeam = homeTeam; return this; }
        public MatchDetailVOBuilder awayTeam(String awayTeam) { this.awayTeam = awayTeam; return this; }
        public MatchDetailVOBuilder matchTime(LocalDateTime matchTime) { this.matchTime = matchTime; return this; }
        public MatchDetailVOBuilder venue(String venue) { this.venue = venue; return this; }
        public MatchDetailVOBuilder homeScore(Integer homeScore) { this.homeScore = homeScore; return this; }
        public MatchDetailVOBuilder awayScore(Integer awayScore) { this.awayScore = awayScore; return this; }
        public MatchDetailVOBuilder scoreStr(String scoreStr) { this.scoreStr = scoreStr; return this; }
        public MatchDetailVOBuilder halfHomeScore(Integer halfHomeScore) { this.halfHomeScore = halfHomeScore; return this; }
        public MatchDetailVOBuilder halfAwayScore(Integer halfAwayScore) { this.halfAwayScore = halfAwayScore; return this; }
        public MatchDetailVOBuilder result(MatchResultEnum result) { this.result = result; return this; }
        public MatchDetailVOBuilder resultDesc(String resultDesc) { this.resultDesc = resultDesc; return this; }
        public MatchDetailVOBuilder status(MatchStatusEnum status) { this.status = status; return this; }
        public MatchDetailVOBuilder statusDesc(String statusDesc) { this.statusDesc = statusDesc; return this; }
        public MatchDetailVOBuilder isSettled(Integer isSettled) { this.isSettled = isSettled; return this; }
        public MatchDetailVOBuilder settledAt(LocalDateTime settledAt) { this.settledAt = settledAt; return this; }
        public MatchDetailVOBuilder predictions(List<MatchPredictionVO> predictions) { this.predictions = predictions; return this; }

        public MatchDetailVO build() {
            return new MatchDetailVO(id, matchCode, leagueId, leagueCode, leagueName, seasonId, seasonName, roundName, homeTeam, awayTeam, matchTime, venue, homeScore, awayScore, scoreStr, halfHomeScore, halfAwayScore, result, resultDesc, status, statusDesc, isSettled, settledAt, predictions);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMatchCode() { return matchCode; }
    public void setMatchCode(String matchCode) { this.matchCode = matchCode; }
    public Long getLeagueId() { return leagueId; }
    public void setLeagueId(Long leagueId) { this.leagueId = leagueId; }
    public String getLeagueCode() { return leagueCode; }
    public void setLeagueCode(String leagueCode) { this.leagueCode = leagueCode; }
    public String getLeagueName() { return leagueName; }
    public void setLeagueName(String leagueName) { this.leagueName = leagueName; }
    public Long getSeasonId() { return seasonId; }
    public void setSeasonId(Long seasonId) { this.seasonId = seasonId; }
    public String getSeasonName() { return seasonName; }
    public void setSeasonName(String seasonName) { this.seasonName = seasonName; }
    public String getRoundName() { return roundName; }
    public void setRoundName(String roundName) { this.roundName = roundName; }
    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }
    public LocalDateTime getMatchTime() { return matchTime; }
    public void setMatchTime(LocalDateTime matchTime) { this.matchTime = matchTime; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public Integer getHomeScore() { return homeScore; }
    public void setHomeScore(Integer homeScore) { this.homeScore = homeScore; }
    public Integer getAwayScore() { return awayScore; }
    public void setAwayScore(Integer awayScore) { this.awayScore = awayScore; }
    public String getScoreStr() { return scoreStr; }
    public void setScoreStr(String scoreStr) { this.scoreStr = scoreStr; }
    public Integer getHalfHomeScore() { return halfHomeScore; }
    public void setHalfHomeScore(Integer halfHomeScore) { this.halfHomeScore = halfHomeScore; }
    public Integer getHalfAwayScore() { return halfAwayScore; }
    public void setHalfAwayScore(Integer halfAwayScore) { this.halfAwayScore = halfAwayScore; }
    public MatchResultEnum getResult() { return result; }
    public void setResult(MatchResultEnum result) { this.result = result; }
    public String getResultDesc() { return resultDesc; }
    public void setResultDesc(String resultDesc) { this.resultDesc = resultDesc; }
    public MatchStatusEnum getStatus() { return status; }
    public void setStatus(MatchStatusEnum status) { this.status = status; }
    public String getStatusDesc() { return statusDesc; }
    public void setStatusDesc(String statusDesc) { this.statusDesc = statusDesc; }
    public Integer getIsSettled() { return isSettled; }
    public void setIsSettled(Integer isSettled) { this.isSettled = isSettled; }
    public LocalDateTime getSettledAt() { return settledAt; }
    public void setSettledAt(LocalDateTime settledAt) { this.settledAt = settledAt; }
    public List<MatchPredictionVO> getPredictions() { return predictions; }
    public void setPredictions(List<MatchPredictionVO> predictions) { this.predictions = predictions; }
}
