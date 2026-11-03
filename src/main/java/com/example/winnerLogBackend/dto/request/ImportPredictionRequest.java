package com.example.winnerLogBackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "导入单场比赛AI预测请求")
public class ImportPredictionRequest {

    @NotBlank(message = "联赛编码不能为空 (如 PL, LA_LIGA, SERIE_A, BUNDESLIGA, LIGUE_1, UCL, WORLD_CUP, EURO)")
    @Schema(description = "联赛编码 (PL:英超, LA_LIGA:西甲, SERIE_A:意甲, BUNDESLIGA:德甲, LIGUE_1:法甲, UCL:欧冠, WORLD_CUP:世界杯, EURO:欧洲杯)", example = "PL")
    private String leagueCode;

    @Schema(description = "赛季名称 (如 2024-2025, 2025-2026, 2026，若不传则自动匹配该联赛当前活跃赛季)", example = "2024-2025")
    private String seasonName;

    @Schema(description = "比赛轮次/阶段 (如 第28轮, 小组赛第1轮, 1/4决赛)", example = "第28轮")
    private String roundName;

    @NotBlank(message = "主队名称不能为空")
    @Schema(description = "主队名称", example = "阿森纳")
    private String homeTeam;

    @NotBlank(message = "客队名称不能为空")
    @Schema(description = "客队名称", example = "切尔西")
    private String awayTeam;

    @NotNull(message = "比赛时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "比赛开球时间 (yyyy-MM-dd HH:mm:ss)", example = "2025-03-15 23:00:00")
    private LocalDateTime matchTime;

    @Schema(description = "比赛外部/业务自定义编码 (可选)", example = "PL_202425_ARS_CHE_20250315")
    private String matchCode;

    @Schema(description = "比赛场地/球场", example = "酋长球场")
    private String venue;

    @Schema(description = "已完场主队进球数 (如果比赛已结束并直接导入结果)", example = "2")
    private Integer homeScore;

    @Schema(description = "已完场客队进球数 (如果比赛已结束并直接导入结果)", example = "1")
    private Integer awayScore;

    @Schema(description = "半场主队得分 (可选)", example = "1")
    private Integer halfHomeScore;

    @Schema(description = "半场客队得分 (可选)", example = "0")
    private Integer halfAwayScore;

    @Valid
    @NotEmpty(message = "至少需要传入一个AI模型(如GPT或千问)的预测数据")
    @Schema(description = "AI预测数据列表 (支持同时传入GPT与千问等多模型预测)")
    private List<SingleAiPredictionDTO> predictions;

    public ImportPredictionRequest() {}

    public ImportPredictionRequest(String leagueCode, String seasonName, String roundName, String homeTeam, String awayTeam, LocalDateTime matchTime, String matchCode, String venue, Integer homeScore, Integer awayScore, Integer halfHomeScore, Integer halfAwayScore, List<SingleAiPredictionDTO> predictions) {
        this.leagueCode = leagueCode;
        this.seasonName = seasonName;
        this.roundName = roundName;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchTime = matchTime;
        this.matchCode = matchCode;
        this.venue = venue;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.halfHomeScore = halfHomeScore;
        this.halfAwayScore = halfAwayScore;
        this.predictions = predictions;
    }

    public static ImportPredictionRequestBuilder builder() {
        return new ImportPredictionRequestBuilder();
    }

    public static class ImportPredictionRequestBuilder {
        private String leagueCode;
        private String seasonName;
        private String roundName;
        private String homeTeam;
        private String awayTeam;
        private LocalDateTime matchTime;
        private String matchCode;
        private String venue;
        private Integer homeScore;
        private Integer awayScore;
        private Integer halfHomeScore;
        private Integer halfAwayScore;
        private List<SingleAiPredictionDTO> predictions;

        public ImportPredictionRequestBuilder leagueCode(String leagueCode) { this.leagueCode = leagueCode; return this; }
        public ImportPredictionRequestBuilder seasonName(String seasonName) { this.seasonName = seasonName; return this; }
        public ImportPredictionRequestBuilder roundName(String roundName) { this.roundName = roundName; return this; }
        public ImportPredictionRequestBuilder homeTeam(String homeTeam) { this.homeTeam = homeTeam; return this; }
        public ImportPredictionRequestBuilder awayTeam(String awayTeam) { this.awayTeam = awayTeam; return this; }
        public ImportPredictionRequestBuilder matchTime(LocalDateTime matchTime) { this.matchTime = matchTime; return this; }
        public ImportPredictionRequestBuilder matchCode(String matchCode) { this.matchCode = matchCode; return this; }
        public ImportPredictionRequestBuilder venue(String venue) { this.venue = venue; return this; }
        public ImportPredictionRequestBuilder homeScore(Integer homeScore) { this.homeScore = homeScore; return this; }
        public ImportPredictionRequestBuilder awayScore(Integer awayScore) { this.awayScore = awayScore; return this; }
        public ImportPredictionRequestBuilder halfHomeScore(Integer halfHomeScore) { this.halfHomeScore = halfHomeScore; return this; }
        public ImportPredictionRequestBuilder halfAwayScore(Integer halfAwayScore) { this.halfAwayScore = halfAwayScore; return this; }
        public ImportPredictionRequestBuilder predictions(List<SingleAiPredictionDTO> predictions) { this.predictions = predictions; return this; }

        public ImportPredictionRequest build() {
            return new ImportPredictionRequest(leagueCode, seasonName, roundName, homeTeam, awayTeam, matchTime, matchCode, venue, homeScore, awayScore, halfHomeScore, halfAwayScore, predictions);
        }
    }

    public String getLeagueCode() { return leagueCode; }
    public void setLeagueCode(String leagueCode) { this.leagueCode = leagueCode; }
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
    public String getMatchCode() { return matchCode; }
    public void setMatchCode(String matchCode) { this.matchCode = matchCode; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public Integer getHomeScore() { return homeScore; }
    public void setHomeScore(Integer homeScore) { this.homeScore = homeScore; }
    public Integer getAwayScore() { return awayScore; }
    public void setAwayScore(Integer awayScore) { this.awayScore = awayScore; }
    public Integer getHalfHomeScore() { return halfHomeScore; }
    public void setHalfHomeScore(Integer halfHomeScore) { this.halfHomeScore = halfHomeScore; }
    public Integer getHalfAwayScore() { return halfAwayScore; }
    public void setHalfAwayScore(Integer halfAwayScore) { this.halfAwayScore = halfAwayScore; }
    public List<SingleAiPredictionDTO> getPredictions() { return predictions; }
    public void setPredictions(List<SingleAiPredictionDTO> predictions) { this.predictions = predictions; }
}
