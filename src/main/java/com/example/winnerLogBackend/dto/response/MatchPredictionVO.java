package com.example.winnerLogBackend.dto.response;

import com.example.winnerLogBackend.entity.MatchPrediction;
import com.example.winnerLogBackend.enums.MatchResultEnum;
import com.example.winnerLogBackend.enums.SettleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "比赛预测明细视图对象")
public class MatchPredictionVO {

    @Schema(description = "预测记录ID")
    private Long id;

    @Schema(description = "AI模型编码 (如 GPT, QWEN)")
    private String aiModelCode;

    @Schema(description = "AI模型名称 (如 OpenAI GPT-4o, 通义千问 Qwen-Max)")
    private String aiModelName;

    @Schema(description = "预测胜负赛果 (HOME_WIN:主胜, DRAW:平局, AWAY_WIN:客胜)")
    private MatchResultEnum predictedResult;

    @Schema(description = "预测赛果描述")
    private String predictedResultDesc;

    @Schema(description = "胜负预测参考赔率")
    private BigDecimal resultOdds;

    @Schema(description = "胜平负多项预测列表 (如不败[主胜+平局])")
    private List<MatchPrediction.ResultItem> resultItems;

    @Schema(description = "预测比分详细列表 (包含比分与赔率)")
    private List<MatchPrediction.ScoreItem> scoreItems;

    @Schema(description = "预测总进球数详细列表 (包含进球数与赔率)")
    private List<MatchPrediction.GoalItem> goalItems;

    @Schema(description = "预测比分列表 (最多3个，如 [\"2-1\", \"1-0\", \"2-0\"])")
    private List<String> predictedScores;

    @Schema(description = "预测总进球数列表 (最多3个，如 [2, 3, 4])")
    private List<Integer> predictedGoals;

    @Schema(description = "预测主队得分 (兼容)")
    private Integer predictedHomeScore;

    @Schema(description = "预测客队得分 (兼容)")
    private Integer predictedAwayScore;

    @Schema(description = "预测比分格式化显示字符串")
    private String predictedScoreStr;

    @Schema(description = "AI预测置信度 (0-100%)")
    private BigDecimal confidence;

    @Schema(description = "AI预测简述理由")
    private String analysisSummary;

    @Schema(description = "AI详细技战术分析报告")
    private String detailedAnalysis;

    @Schema(description = "预测参考赔率 (兼容)")
    private BigDecimal predictedOdds;

    @Schema(description = "结算状态 (PENDING:未结算, WON:红单/命中, LOST:黑单/未命中, VOID:走水)")
    private SettleStatusEnum settleStatus;

    @Schema(description = "结算状态描述")
    private String settleStatusDesc;

    @Schema(description = "胜平负方向是否命中 (1:命中, 0:未命中)")
    private Integer isResultCorrect;

    @Schema(description = "比分是否命中 (1:命中, 0:未命中)")
    private Integer isScoreCorrect;

    @Schema(description = "命中的具体比分 (如 2-1)")
    private String hitScore;

    @Schema(description = "总进球数是否命中 (1:命中, 0:未命中)")
    private Integer isGoalsCorrect;

    @Schema(description = "命中的具体进球数 (如 3)")
    private Integer hitGoals;

    @Schema(description = "命中重要性等级 (1:比分命中-最高, 2:进球数命中-次之, 3:胜负命中-基础, 0:未命中)")
    private Integer hitImportance;

    @Schema(description = "命中等级标识 (PERFECT_HIT, SCORE_HIT, GOALS_HIT, RESULT_HIT, MISSED)")
    private String hitTier;

    @Schema(description = "命中等级描述 (如 🎯精准比分, ⚽进球数命中, 🌟全中大满贯)")
    private String hitTierDesc;

    @Schema(description = "本次预测的全部选项总数 (如 胜负双选2 + 比分3 + 进球3 = 8项)")
    private Integer optionsCount;

    @Schema(description = "初始利润 (选项数负值，如 -8.00)")
    private BigDecimal initialProfit;

    @Schema(description = "本场投入总注数 (每项均注1单位)")
    private BigDecimal stakeAmount;

    @Schema(description = "本场中奖总返还")
    private BigDecimal returnAmount;

    @Schema(description = "本场净赚倍数 (返还 - 本金)")
    private BigDecimal netProfit;

    @Schema(description = "胜负中奖返还")
    private BigDecimal resultReturn;

    @Schema(description = "比分中奖返还")
    private BigDecimal scoreReturn;

    @Schema(description = "进球数中奖返还")
    private BigDecimal goalsReturn;

    @Schema(description = "结算时间")
    private LocalDateTime settledAt;

    public MatchPredictionVO() {}

    public MatchPredictionVO(Long id, String aiModelCode, String aiModelName, MatchResultEnum predictedResult, String predictedResultDesc, BigDecimal resultOdds, List<MatchPrediction.ResultItem> resultItems, List<MatchPrediction.ScoreItem> scoreItems, List<MatchPrediction.GoalItem> goalItems, List<String> predictedScores, List<Integer> predictedGoals, Integer predictedHomeScore, Integer predictedAwayScore, String predictedScoreStr, BigDecimal confidence, String analysisSummary, String detailedAnalysis, BigDecimal predictedOdds, SettleStatusEnum settleStatus, String settleStatusDesc, Integer isResultCorrect, Integer isScoreCorrect, String hitScore, Integer isGoalsCorrect, Integer hitGoals, Integer hitImportance, String hitTier, String hitTierDesc, Integer optionsCount, BigDecimal initialProfit, BigDecimal stakeAmount, BigDecimal returnAmount, BigDecimal netProfit, BigDecimal resultReturn, BigDecimal scoreReturn, BigDecimal goalsReturn, LocalDateTime settledAt) {
        this.id = id;
        this.aiModelCode = aiModelCode;
        this.aiModelName = aiModelName;
        this.predictedResult = predictedResult;
        this.predictedResultDesc = predictedResultDesc;
        this.resultOdds = resultOdds;
        this.resultItems = resultItems;
        this.scoreItems = scoreItems;
        this.goalItems = goalItems;
        this.predictedScores = predictedScores;
        this.predictedGoals = predictedGoals;
        this.predictedHomeScore = predictedHomeScore;
        this.predictedAwayScore = predictedAwayScore;
        this.predictedScoreStr = predictedScoreStr;
        this.confidence = confidence;
        this.analysisSummary = analysisSummary;
        this.detailedAnalysis = detailedAnalysis;
        this.predictedOdds = predictedOdds;
        this.settleStatus = settleStatus;
        this.settleStatusDesc = settleStatusDesc;
        this.isResultCorrect = isResultCorrect;
        this.isScoreCorrect = isScoreCorrect;
        this.hitScore = hitScore;
        this.isGoalsCorrect = isGoalsCorrect;
        this.hitGoals = hitGoals;
        this.hitImportance = hitImportance;
        this.hitTier = hitTier;
        this.hitTierDesc = hitTierDesc;
        this.optionsCount = optionsCount;
        this.initialProfit = initialProfit;
        this.stakeAmount = stakeAmount;
        this.returnAmount = returnAmount;
        this.netProfit = netProfit;
        this.resultReturn = resultReturn;
        this.scoreReturn = scoreReturn;
        this.goalsReturn = goalsReturn;
        this.settledAt = settledAt;
    }

    public static MatchPredictionVOBuilder builder() {
        return new MatchPredictionVOBuilder();
    }

    public static class MatchPredictionVOBuilder {
        private Long id;
        private String aiModelCode;
        private String aiModelName;
        private MatchResultEnum predictedResult;
        private String predictedResultDesc;
        private BigDecimal resultOdds;
        private List<MatchPrediction.ResultItem> resultItems;
        private List<MatchPrediction.ScoreItem> scoreItems;
        private List<MatchPrediction.GoalItem> goalItems;
        private List<String> predictedScores;
        private List<Integer> predictedGoals;
        private Integer predictedHomeScore;
        private Integer predictedAwayScore;
        private String predictedScoreStr;
        private BigDecimal confidence;
        private String analysisSummary;
        private String detailedAnalysis;
        private BigDecimal predictedOdds;
        private SettleStatusEnum settleStatus;
        private String settleStatusDesc;
        private Integer isResultCorrect;
        private Integer isScoreCorrect;
        private String hitScore;
        private Integer isGoalsCorrect;
        private Integer hitGoals;
        private Integer hitImportance;
        private String hitTier;
        private String hitTierDesc;
        private Integer optionsCount;
        private BigDecimal initialProfit;
        private BigDecimal stakeAmount;
        private BigDecimal returnAmount;
        private BigDecimal netProfit;
        private BigDecimal resultReturn;
        private BigDecimal scoreReturn;
        private BigDecimal goalsReturn;
        private LocalDateTime settledAt;

        public MatchPredictionVOBuilder id(Long id) { this.id = id; return this; }
        public MatchPredictionVOBuilder aiModelCode(String aiModelCode) { this.aiModelCode = aiModelCode; return this; }
        public MatchPredictionVOBuilder aiModelName(String aiModelName) { this.aiModelName = aiModelName; return this; }
        public MatchPredictionVOBuilder predictedResult(MatchResultEnum predictedResult) { this.predictedResult = predictedResult; return this; }
        public MatchPredictionVOBuilder predictedResultDesc(String predictedResultDesc) { this.predictedResultDesc = predictedResultDesc; return this; }
        public MatchPredictionVOBuilder resultOdds(BigDecimal resultOdds) { this.resultOdds = resultOdds; return this; }
        public MatchPredictionVOBuilder resultItems(List<MatchPrediction.ResultItem> resultItems) { this.resultItems = resultItems; return this; }
        public MatchPredictionVOBuilder scoreItems(List<MatchPrediction.ScoreItem> scoreItems) { this.scoreItems = scoreItems; return this; }
        public MatchPredictionVOBuilder goalItems(List<MatchPrediction.GoalItem> goalItems) { this.goalItems = goalItems; return this; }
        public MatchPredictionVOBuilder predictedScores(List<String> predictedScores) { this.predictedScores = predictedScores; return this; }
        public MatchPredictionVOBuilder predictedGoals(List<Integer> predictedGoals) { this.predictedGoals = predictedGoals; return this; }
        public MatchPredictionVOBuilder predictedHomeScore(Integer predictedHomeScore) { this.predictedHomeScore = predictedHomeScore; return this; }
        public MatchPredictionVOBuilder predictedAwayScore(Integer predictedAwayScore) { this.predictedAwayScore = predictedAwayScore; return this; }
        public MatchPredictionVOBuilder predictedScoreStr(String predictedScoreStr) { this.predictedScoreStr = predictedScoreStr; return this; }
        public MatchPredictionVOBuilder confidence(BigDecimal confidence) { this.confidence = confidence; return this; }
        public MatchPredictionVOBuilder analysisSummary(String analysisSummary) { this.analysisSummary = analysisSummary; return this; }
        public MatchPredictionVOBuilder detailedAnalysis(String detailedAnalysis) { this.detailedAnalysis = detailedAnalysis; return this; }
        public MatchPredictionVOBuilder predictedOdds(BigDecimal predictedOdds) { this.predictedOdds = predictedOdds; return this; }
        public MatchPredictionVOBuilder settleStatus(SettleStatusEnum settleStatus) { this.settleStatus = settleStatus; return this; }
        public MatchPredictionVOBuilder settleStatusDesc(String settleStatusDesc) { this.settleStatusDesc = settleStatusDesc; return this; }
        public MatchPredictionVOBuilder isResultCorrect(Integer isResultCorrect) { this.isResultCorrect = isResultCorrect; return this; }
        public MatchPredictionVOBuilder isScoreCorrect(Integer isScoreCorrect) { this.isScoreCorrect = isScoreCorrect; return this; }
        public MatchPredictionVOBuilder hitScore(String hitScore) { this.hitScore = hitScore; return this; }
        public MatchPredictionVOBuilder isGoalsCorrect(Integer isGoalsCorrect) { this.isGoalsCorrect = isGoalsCorrect; return this; }
        public MatchPredictionVOBuilder hitGoals(Integer hitGoals) { this.hitGoals = hitGoals; return this; }
        public MatchPredictionVOBuilder hitImportance(Integer hitImportance) { this.hitImportance = hitImportance; return this; }
        public MatchPredictionVOBuilder hitTier(String hitTier) { this.hitTier = hitTier; return this; }
        public MatchPredictionVOBuilder hitTierDesc(String hitTierDesc) { this.hitTierDesc = hitTierDesc; return this; }
        public MatchPredictionVOBuilder optionsCount(Integer optionsCount) { this.optionsCount = optionsCount; return this; }
        public MatchPredictionVOBuilder initialProfit(BigDecimal initialProfit) { this.initialProfit = initialProfit; return this; }
        public MatchPredictionVOBuilder stakeAmount(BigDecimal stakeAmount) { this.stakeAmount = stakeAmount; return this; }
        public MatchPredictionVOBuilder returnAmount(BigDecimal returnAmount) { this.returnAmount = returnAmount; return this; }
        public MatchPredictionVOBuilder netProfit(BigDecimal netProfit) { this.netProfit = netProfit; return this; }
        public MatchPredictionVOBuilder resultReturn(BigDecimal resultReturn) { this.resultReturn = resultReturn; return this; }
        public MatchPredictionVOBuilder scoreReturn(BigDecimal scoreReturn) { this.scoreReturn = scoreReturn; return this; }
        public MatchPredictionVOBuilder goalsReturn(BigDecimal goalsReturn) { this.goalsReturn = goalsReturn; return this; }
        public MatchPredictionVOBuilder settledAt(LocalDateTime settledAt) { this.settledAt = settledAt; return this; }

        public MatchPredictionVO build() {
            return new MatchPredictionVO(id, aiModelCode, aiModelName, predictedResult, predictedResultDesc, resultOdds, resultItems, scoreItems, goalItems, predictedScores, predictedGoals, predictedHomeScore, predictedAwayScore, predictedScoreStr, confidence, analysisSummary, detailedAnalysis, predictedOdds, settleStatus, settleStatusDesc, isResultCorrect, isScoreCorrect, hitScore, isGoalsCorrect, hitGoals, hitImportance, hitTier, hitTierDesc, optionsCount, initialProfit, stakeAmount, returnAmount, netProfit, resultReturn, scoreReturn, goalsReturn, settledAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAiModelCode() { return aiModelCode; }
    public void setAiModelCode(String aiModelCode) { this.aiModelCode = aiModelCode; }
    public String getAiModelName() { return aiModelName; }
    public void setAiModelName(String aiModelName) { this.aiModelName = aiModelName; }
    public MatchResultEnum getPredictedResult() { return predictedResult; }
    public void setPredictedResult(MatchResultEnum predictedResult) { this.predictedResult = predictedResult; }
    public String getPredictedResultDesc() { return predictedResultDesc; }
    public void setPredictedResultDesc(String predictedResultDesc) { this.predictedResultDesc = predictedResultDesc; }
    public BigDecimal getResultOdds() { return resultOdds; }
    public void setResultOdds(BigDecimal resultOdds) { this.resultOdds = resultOdds; }
    public List<MatchPrediction.ResultItem> getResultItems() { return resultItems; }
    public void setResultItems(List<MatchPrediction.ResultItem> resultItems) { this.resultItems = resultItems; }
    public List<MatchPrediction.ScoreItem> getScoreItems() { return scoreItems; }
    public void setScoreItems(List<MatchPrediction.ScoreItem> scoreItems) { this.scoreItems = scoreItems; }
    public List<MatchPrediction.GoalItem> getGoalItems() { return goalItems; }
    public void setGoalItems(List<MatchPrediction.GoalItem> goalItems) { this.goalItems = goalItems; }
    public List<String> getPredictedScores() { return predictedScores; }
    public void setPredictedScores(List<String> predictedScores) { this.predictedScores = predictedScores; }
    public List<Integer> getPredictedGoals() { return predictedGoals; }
    public void setPredictedGoals(List<Integer> predictedGoals) { this.predictedGoals = predictedGoals; }
    public Integer getPredictedHomeScore() { return predictedHomeScore; }
    public void setPredictedHomeScore(Integer predictedHomeScore) { this.predictedHomeScore = predictedHomeScore; }
    public Integer getPredictedAwayScore() { return predictedAwayScore; }
    public void setPredictedAwayScore(Integer predictedAwayScore) { this.predictedAwayScore = predictedAwayScore; }
    public String getPredictedScoreStr() { return predictedScoreStr; }
    public void setPredictedScoreStr(String predictedScoreStr) { this.predictedScoreStr = predictedScoreStr; }
    public BigDecimal getConfidence() { return confidence; }
    public void setConfidence(BigDecimal confidence) { this.confidence = confidence; }
    public String getAnalysisSummary() { return analysisSummary; }
    public void setAnalysisSummary(String analysisSummary) { this.analysisSummary = analysisSummary; }
    public String getDetailedAnalysis() { return detailedAnalysis; }
    public void setDetailedAnalysis(String detailedAnalysis) { this.detailedAnalysis = detailedAnalysis; }
    public BigDecimal getPredictedOdds() { return predictedOdds; }
    public void setPredictedOdds(BigDecimal predictedOdds) { this.predictedOdds = predictedOdds; }
    public SettleStatusEnum getSettleStatus() { return settleStatus; }
    public void setSettleStatus(SettleStatusEnum settleStatus) { this.settleStatus = settleStatus; }
    public String getSettleStatusDesc() { return settleStatusDesc; }
    public void setSettleStatusDesc(String settleStatusDesc) { this.settleStatusDesc = settleStatusDesc; }
    public Integer getIsResultCorrect() { return isResultCorrect; }
    public void setIsResultCorrect(Integer isResultCorrect) { this.isResultCorrect = isResultCorrect; }
    public Integer getIsScoreCorrect() { return isScoreCorrect; }
    public void setIsScoreCorrect(Integer isScoreCorrect) { this.isScoreCorrect = isScoreCorrect; }
    public String getHitScore() { return hitScore; }
    public void setHitScore(String hitScore) { this.hitScore = hitScore; }
    public Integer getIsGoalsCorrect() { return isGoalsCorrect; }
    public void setIsGoalsCorrect(Integer isGoalsCorrect) { this.isGoalsCorrect = isGoalsCorrect; }
    public Integer getHitGoals() { return hitGoals; }
    public void setHitGoals(Integer hitGoals) { this.hitGoals = hitGoals; }
    public Integer getHitImportance() { return hitImportance; }
    public void setHitImportance(Integer hitImportance) { this.hitImportance = hitImportance; }
    public String getHitTier() { return hitTier; }
    public void setHitTier(String hitTier) { this.hitTier = hitTier; }
    public String getHitTierDesc() { return hitTierDesc; }
    public void setHitTierDesc(String hitTierDesc) { this.hitTierDesc = hitTierDesc; }
    public Integer getOptionsCount() { return optionsCount; }
    public void setOptionsCount(Integer optionsCount) { this.optionsCount = optionsCount; }
    public BigDecimal getInitialProfit() { return initialProfit; }
    public void setInitialProfit(BigDecimal initialProfit) { this.initialProfit = initialProfit; }
    public BigDecimal getStakeAmount() { return stakeAmount; }
    public void setStakeAmount(BigDecimal stakeAmount) { this.stakeAmount = stakeAmount; }
    public BigDecimal getReturnAmount() { return returnAmount; }
    public void setReturnAmount(BigDecimal returnAmount) { this.returnAmount = returnAmount; }
    public BigDecimal getNetProfit() { return netProfit; }
    public void setNetProfit(BigDecimal netProfit) { this.netProfit = netProfit; }
    public BigDecimal getResultReturn() { return resultReturn; }
    public void setResultReturn(BigDecimal resultReturn) { this.resultReturn = resultReturn; }
    public BigDecimal getScoreReturn() { return scoreReturn; }
    public void setScoreReturn(BigDecimal scoreReturn) { this.scoreReturn = scoreReturn; }
    public BigDecimal getGoalsReturn() { return goalsReturn; }
    public void setGoalsReturn(BigDecimal goalsReturn) { this.goalsReturn = goalsReturn; }
    public LocalDateTime getSettledAt() { return settledAt; }
    public void setSettledAt(LocalDateTime settledAt) { this.settledAt = settledAt; }
}
