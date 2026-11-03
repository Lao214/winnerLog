package com.example.winnerLogBackend.dto.request;

import com.example.winnerLogBackend.enums.MatchResultEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "单个AI模型的预测数据（包含胜负赔率、最多3个比分及各自赔率、最多3个进球数及各自赔率）")
public class SingleAiPredictionDTO {

    @NotBlank(message = "AI模型标识不能为空 (如 GPT, QWEN)")
    @Schema(description = "AI模型标识编码 (如 GPT, QWEN, CLAUDE, DEEPSEEK)", example = "GPT")
    private String modelCode;

    @Schema(description = "预测胜负赛果 (单个，如 HOME_WIN:主胜, DRAW:平局, AWAY_WIN:客胜)", example = "HOME_WIN")
    private MatchResultEnum predictedResult;

    @Schema(description = "胜负预测参考赔率 (单选时)", example = "1.85")
    private BigDecimal resultOdds;

    @Schema(description = "胜平负预测选项列表 (支持多选如不败[主胜+平局]及各自赔率)")
    private List<ResultItemDTO> resultItems;

    @Schema(description = "预测赛果列表 (兼容如 [\"HOME_WIN\", \"DRAW\"])")
    private List<MatchResultEnum> predictedResults;

    @Schema(description = "预测比分列表 (最多3个，包含比分与赔率)")
    private List<ScoreItemDTO> scoreItems;

    @Schema(description = "预测总进球数列表 (最多3个，包含进球数与赔率)")
    private List<GoalItemDTO> goalItems;

    @Schema(description = "预测比分列表 (如 [\"2-1\", \"1-0\", \"2-0\"])", example = "[\"2-1\", \"1-0\", \"2-0\"]")
    private List<String> predictedScores;

    @Schema(description = "预测总进球数列表 (如 [2, 3, 4])", example = "[2, 3, 4]")
    private List<Integer> predictedGoals;

    @Schema(description = "单个预测主队进球数 (兼容单比分字段)", example = "2")
    private Integer predictedHomeScore;

    @Schema(description = "单个预测客队进球数 (兼容单比分字段)", example = "1")
    private Integer predictedAwayScore;

    @Schema(description = "AI预测置信度/胜率 (0.00 ~ 100.00%)", example = "78.50")
    private BigDecimal confidence;

    @Schema(description = "AI预测简述/核心理由", example = "主队近期主场5连胜，锋线核心状态火热；客队主力后腰伤缺防守存隐患。")
    private String analysisSummary;

    @Schema(description = "AI完整推理与战术分析报告")
    private String detailedAnalysis;

    @Schema(description = "全局参考赔率 (兼容)", example = "1.95")
    private BigDecimal predictedOdds;

    public SingleAiPredictionDTO() {}

    public SingleAiPredictionDTO(String modelCode, MatchResultEnum predictedResult, BigDecimal resultOdds, List<ResultItemDTO> resultItems, List<MatchResultEnum> predictedResults, List<ScoreItemDTO> scoreItems, List<GoalItemDTO> goalItems, List<String> predictedScores, List<Integer> predictedGoals, Integer predictedHomeScore, Integer predictedAwayScore, BigDecimal confidence, String analysisSummary, String detailedAnalysis, BigDecimal predictedOdds) {
        this.modelCode = modelCode;
        this.predictedResult = predictedResult;
        this.resultOdds = resultOdds;
        this.resultItems = resultItems;
        this.predictedResults = predictedResults;
        this.scoreItems = scoreItems;
        this.goalItems = goalItems;
        this.predictedScores = predictedScores;
        this.predictedGoals = predictedGoals;
        this.predictedHomeScore = predictedHomeScore;
        this.predictedAwayScore = predictedAwayScore;
        this.confidence = confidence;
        this.analysisSummary = analysisSummary;
        this.detailedAnalysis = detailedAnalysis;
        this.predictedOdds = predictedOdds;
    }

    public static SingleAiPredictionDTOBuilder builder() {
        return new SingleAiPredictionDTOBuilder();
    }

    public static class SingleAiPredictionDTOBuilder {
        private String modelCode;
        private MatchResultEnum predictedResult;
        private BigDecimal resultOdds;
        private List<ResultItemDTO> resultItems;
        private List<MatchResultEnum> predictedResults;
        private List<ScoreItemDTO> scoreItems;
        private List<GoalItemDTO> goalItems;
        private List<String> predictedScores;
        private List<Integer> predictedGoals;
        private Integer predictedHomeScore;
        private Integer predictedAwayScore;
        private BigDecimal confidence;
        private String analysisSummary;
        private String detailedAnalysis;
        private BigDecimal predictedOdds;

        public SingleAiPredictionDTOBuilder modelCode(String modelCode) { this.modelCode = modelCode; return this; }
        public SingleAiPredictionDTOBuilder predictedResult(MatchResultEnum predictedResult) { this.predictedResult = predictedResult; return this; }
        public SingleAiPredictionDTOBuilder resultOdds(BigDecimal resultOdds) { this.resultOdds = resultOdds; return this; }
        public SingleAiPredictionDTOBuilder resultItems(List<ResultItemDTO> resultItems) { this.resultItems = resultItems; return this; }
        public SingleAiPredictionDTOBuilder predictedResults(List<MatchResultEnum> predictedResults) { this.predictedResults = predictedResults; return this; }
        public SingleAiPredictionDTOBuilder scoreItems(List<ScoreItemDTO> scoreItems) { this.scoreItems = scoreItems; return this; }
        public SingleAiPredictionDTOBuilder goalItems(List<GoalItemDTO> goalItems) { this.goalItems = goalItems; return this; }
        public SingleAiPredictionDTOBuilder predictedScores(List<String> predictedScores) { this.predictedScores = predictedScores; return this; }
        public SingleAiPredictionDTOBuilder predictedGoals(List<Integer> predictedGoals) { this.predictedGoals = predictedGoals; return this; }
        public SingleAiPredictionDTOBuilder predictedHomeScore(Integer predictedHomeScore) { this.predictedHomeScore = predictedHomeScore; return this; }
        public SingleAiPredictionDTOBuilder predictedAwayScore(Integer predictedAwayScore) { this.predictedAwayScore = predictedAwayScore; return this; }
        public SingleAiPredictionDTOBuilder confidence(BigDecimal confidence) { this.confidence = confidence; return this; }
        public SingleAiPredictionDTOBuilder analysisSummary(String analysisSummary) { this.analysisSummary = analysisSummary; return this; }
        public SingleAiPredictionDTOBuilder detailedAnalysis(String detailedAnalysis) { this.detailedAnalysis = detailedAnalysis; return this; }
        public SingleAiPredictionDTOBuilder predictedOdds(BigDecimal predictedOdds) { this.predictedOdds = predictedOdds; return this; }

        public SingleAiPredictionDTO build() {
            return new SingleAiPredictionDTO(modelCode, predictedResult, resultOdds, resultItems, predictedResults, scoreItems, goalItems, predictedScores, predictedGoals, predictedHomeScore, predictedAwayScore, confidence, analysisSummary, detailedAnalysis, predictedOdds);
        }
    }

    public String getModelCode() { return modelCode; }
    public void setModelCode(String modelCode) { this.modelCode = modelCode; }
    public MatchResultEnum getPredictedResult() { return predictedResult; }
    public void setPredictedResult(MatchResultEnum predictedResult) { this.predictedResult = predictedResult; }
    public BigDecimal getResultOdds() { return resultOdds; }
    public void setResultOdds(BigDecimal resultOdds) { this.resultOdds = resultOdds; }
    public List<ResultItemDTO> getResultItems() { return resultItems; }
    public void setResultItems(List<ResultItemDTO> resultItems) { this.resultItems = resultItems; }
    public List<MatchResultEnum> getPredictedResults() { return predictedResults; }
    public void setPredictedResults(List<MatchResultEnum> predictedResults) { this.predictedResults = predictedResults; }
    public List<ScoreItemDTO> getScoreItems() { return scoreItems; }
    public void setScoreItems(List<ScoreItemDTO> scoreItems) { this.scoreItems = scoreItems; }
    public List<GoalItemDTO> getGoalItems() { return goalItems; }
    public void setGoalItems(List<GoalItemDTO> goalItems) { this.goalItems = goalItems; }
    public List<String> getPredictedScores() { return predictedScores; }
    public void setPredictedScores(List<String> predictedScores) { this.predictedScores = predictedScores; }
    public List<Integer> getPredictedGoals() { return predictedGoals; }
    public void setPredictedGoals(List<Integer> predictedGoals) { this.predictedGoals = predictedGoals; }
    public Integer getPredictedHomeScore() { return predictedHomeScore; }
    public void setPredictedHomeScore(Integer predictedHomeScore) { this.predictedHomeScore = predictedHomeScore; }
    public Integer getPredictedAwayScore() { return predictedAwayScore; }
    public void setPredictedAwayScore(Integer predictedAwayScore) { this.predictedAwayScore = predictedAwayScore; }
    public BigDecimal getConfidence() { return confidence; }
    public void setConfidence(BigDecimal confidence) { this.confidence = confidence; }
    public String getAnalysisSummary() { return analysisSummary; }
    public void setAnalysisSummary(String analysisSummary) { this.analysisSummary = analysisSummary; }
    public String getDetailedAnalysis() { return detailedAnalysis; }
    public void setDetailedAnalysis(String detailedAnalysis) { this.detailedAnalysis = detailedAnalysis; }
    public BigDecimal getPredictedOdds() { return predictedOdds; }
    public void setPredictedOdds(BigDecimal predictedOdds) { this.predictedOdds = predictedOdds; }

    @Schema(description = "单项胜负/平局赛果与赔率 (如 不败包含主胜和平局各一项)")
    public static class ResultItemDTO {
        @Schema(description = "预测赛果 (HOME_WIN, DRAW, AWAY_WIN)", example = "HOME_WIN")
        private MatchResultEnum result;

        @Schema(description = "该赛果选项赔率", example = "1.90")
        private BigDecimal odds;

        public ResultItemDTO() {}

        public ResultItemDTO(MatchResultEnum result, BigDecimal odds) {
            this.result = result;
            this.odds = odds;
        }

        public static ResultItemDTOBuilder builder() {
            return new ResultItemDTOBuilder();
        }

        public static class ResultItemDTOBuilder {
            private MatchResultEnum result;
            private BigDecimal odds;

            public ResultItemDTOBuilder result(MatchResultEnum result) { this.result = result; return this; }
            public ResultItemDTOBuilder odds(BigDecimal odds) { this.odds = odds; return this; }
            public ResultItemDTO build() { return new ResultItemDTO(result, odds); }
        }

        public MatchResultEnum getResult() { return result; }
        public void setResult(MatchResultEnum result) { this.result = result; }
        public BigDecimal getOdds() { return odds; }
        public void setOdds(BigDecimal odds) { this.odds = odds; }
    }

    @Schema(description = "单项比分与赔率")
    public static class ScoreItemDTO {
        @Schema(description = "比分 (如 2-1)", example = "2-1")
        private String score;

        @Schema(description = "该比分赔率", example = "7.50")
        private BigDecimal odds;

        public ScoreItemDTO() {}

        public ScoreItemDTO(String score, BigDecimal odds) {
            this.score = score;
            this.odds = odds;
        }

        public static ScoreItemDTOBuilder builder() {
            return new ScoreItemDTOBuilder();
        }

        public static class ScoreItemDTOBuilder {
            private String score;
            private BigDecimal odds;

            public ScoreItemDTOBuilder score(String score) { this.score = score; return this; }
            public ScoreItemDTOBuilder odds(BigDecimal odds) { this.odds = odds; return this; }
            public ScoreItemDTO build() { return new ScoreItemDTO(score, odds); }
        }

        public String getScore() { return score; }
        public void setScore(String score) { this.score = score; }
        public BigDecimal getOdds() { return odds; }
        public void setOdds(BigDecimal odds) { this.odds = odds; }
    }

    @Schema(description = "单项进球数与赔率")
    public static class GoalItemDTO {
        @Schema(description = "进球数 (如 3)", example = "3")
        private Integer goals;

        @Schema(description = "该进球数赔率", example = "3.80")
        private BigDecimal odds;

        public GoalItemDTO() {}

        public GoalItemDTO(Integer goals, BigDecimal odds) {
            this.goals = goals;
            this.odds = odds;
        }

        public static GoalItemDTOBuilder builder() {
            return new GoalItemDTOBuilder();
        }

        public static class GoalItemDTOBuilder {
            private Integer goals;
            private BigDecimal odds;

            public GoalItemDTOBuilder goals(Integer goals) { this.goals = goals; return this; }
            public GoalItemDTOBuilder odds(BigDecimal odds) { this.odds = odds; return this; }
            public GoalItemDTO build() { return new GoalItemDTO(goals, odds); }
        }

        public Integer getGoals() { return goals; }
        public void setGoals(Integer goals) { this.goals = goals; }
        public BigDecimal getOdds() { return odds; }
        public void setOdds(BigDecimal odds) { this.odds = odds; }
    }
}
