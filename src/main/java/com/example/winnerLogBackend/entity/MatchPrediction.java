package com.example.winnerLogBackend.entity;

import com.example.winnerLogBackend.enums.MatchResultEnum;
import com.example.winnerLogBackend.enums.SettleStatusEnum;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "match_prediction", uniqueConstraints = {
        @UniqueConstraint(name = "uk_match_ai_model", columnNames = {"match_id", "ai_model_code"})
}, indexes = {
        @Index(name = "idx_pred_model_settle", columnList = "ai_model_code, settle_status"),
        @Index(name = "idx_pred_match", columnList = "match_id")
})
public class MatchPrediction {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_id", nullable = false)
    private Long matchId;

    @Column(name = "ai_model_id", nullable = false)
    private Long aiModelId;

    @Column(name = "ai_model_code", nullable = false, length = 50)
    private String aiModelCode;

    // 1. 胜负预测及赔率 (支持单选与多选不败列表)
    @Enumerated(EnumType.STRING)
    @Column(name = "predicted_result", length = 20)
    private MatchResultEnum predictedResult;

    @Column(name = "result_odds", precision = 6, scale = 2)
    private BigDecimal resultOdds = BigDecimal.valueOf(1.85);

    // 1.1 胜负多选项 JSON (支持不败双选等，如 [{"result":"HOME_WIN","odds":1.90},{"result":"DRAW","odds":3.40}])
    @Column(name = "predicted_results_json", columnDefinition = "TEXT")
    private String predictedResultsJson;

    // 2. 预测比分 JSON (包含最多3个比分及各自赔率，如 [{"score":"2-1","odds":7.50},...])
    @Column(name = "predicted_scores_json", columnDefinition = "TEXT")
    private String predictedScoresJson;

    // 3. 预测总进球数 JSON (包含最多3个进球数及各自赔率，如 [{"goals":3,"odds":3.80},...])
    @Column(name = "predicted_goals_json", columnDefinition = "TEXT")
    private String predictedGoalsJson;

    // 兼容历史字段
    @Column(name = "predicted_scores", length = 100)
    private String predictedScores;

    @Column(name = "predicted_goals", length = 50)
    private String predictedGoals;

    @Column(name = "predicted_home_score")
    private Integer predictedHomeScore;

    @Column(name = "predicted_away_score")
    private Integer predictedAwayScore;

    @Column(name = "confidence", precision = 5, scale = 2)
    private BigDecimal confidence;

    @Column(name = "analysis_summary", columnDefinition = "TEXT")
    private String analysisSummary;

    @Column(name = "detailed_analysis", columnDefinition = "MEDIUMTEXT")
    private String detailedAnalysis;

    @Column(name = "predicted_odds", precision = 6, scale = 2)
    private BigDecimal predictedOdds;

    @Enumerated(EnumType.STRING)
    @Column(name = "settle_status", nullable = false, length = 20)
    private SettleStatusEnum settleStatus = SettleStatusEnum.PENDING;

    @Column(name = "is_result_correct")
    private Integer isResultCorrect;

    @Column(name = "is_score_correct")
    private Integer isScoreCorrect;

    @Column(name = "hit_score", length = 20)
    private String hitScore;

    @Column(name = "is_goals_correct")
    private Integer isGoalsCorrect;

    @Column(name = "hit_goals")
    private Integer hitGoals;

    // 命中重要性等级 (1:比分命中-最重要, 2:进球数命中-次重要, 3:胜负命中-基础, 0:未命中)
    @Column(name = "hit_importance")
    private Integer hitImportance = 0;

    @Column(name = "hit_tier", length = 30)
    private String hitTier;

    @Column(name = "hit_tier_desc", length = 50)
    private String hitTierDesc;

    // 财务与倍数统计字段 (均注模型：每项下注 1 单位)
    @Column(name = "options_count")
    private Integer optionsCount = 7;

    @Column(name = "initial_profit", precision = 8, scale = 2)
    private BigDecimal initialProfit = BigDecimal.valueOf(-7.00);

    @Column(name = "stake_amount", precision = 8, scale = 2)
    private BigDecimal stakeAmount = BigDecimal.valueOf(7.00);

    @Column(name = "return_amount", precision = 8, scale = 2)
    private BigDecimal returnAmount = BigDecimal.ZERO;

    @Column(name = "net_profit", precision = 8, scale = 2)
    private BigDecimal netProfit = BigDecimal.ZERO;

    @Column(name = "result_return", precision = 6, scale = 2)
    private BigDecimal resultReturn = BigDecimal.ZERO;

    @Column(name = "score_return", precision = 6, scale = 2)
    private BigDecimal scoreReturn = BigDecimal.ZERO;

    @Column(name = "goals_return", precision = 6, scale = 2)
    private BigDecimal goalsReturn = BigDecimal.ZERO;

    @Column(name = "settled_at")
    private LocalDateTime settledAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public MatchPrediction() {}

    public MatchPrediction(Long id, Long matchId, Long aiModelId, String aiModelCode, MatchResultEnum predictedResult, BigDecimal resultOdds, String predictedResultsJson, String predictedScoresJson, String predictedGoalsJson, String predictedScores, String predictedGoals, Integer predictedHomeScore, Integer predictedAwayScore, BigDecimal confidence, String analysisSummary, String detailedAnalysis, BigDecimal predictedOdds, SettleStatusEnum settleStatus, Integer isResultCorrect, Integer isScoreCorrect, String hitScore, Integer isGoalsCorrect, Integer hitGoals, Integer hitImportance, String hitTier, String hitTierDesc, Integer optionsCount, BigDecimal initialProfit, BigDecimal stakeAmount, BigDecimal returnAmount, BigDecimal netProfit, BigDecimal resultReturn, BigDecimal scoreReturn, BigDecimal goalsReturn, LocalDateTime settledAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.matchId = matchId;
        this.aiModelId = aiModelId;
        this.aiModelCode = aiModelCode;
        this.predictedResult = predictedResult;
        this.resultOdds = resultOdds != null ? resultOdds : BigDecimal.valueOf(1.85);
        this.predictedResultsJson = predictedResultsJson;
        this.predictedScoresJson = predictedScoresJson;
        this.predictedGoalsJson = predictedGoalsJson;
        this.predictedScores = predictedScores;
        this.predictedGoals = predictedGoals;
        this.predictedHomeScore = predictedHomeScore;
        this.predictedAwayScore = predictedAwayScore;
        this.confidence = confidence;
        this.analysisSummary = analysisSummary;
        this.detailedAnalysis = detailedAnalysis;
        this.predictedOdds = predictedOdds;
        this.settleStatus = settleStatus != null ? settleStatus : SettleStatusEnum.PENDING;
        this.isResultCorrect = isResultCorrect;
        this.isScoreCorrect = isScoreCorrect;
        this.hitScore = hitScore;
        this.isGoalsCorrect = isGoalsCorrect;
        this.hitGoals = hitGoals;
        this.hitImportance = hitImportance != null ? hitImportance : 0;
        this.hitTier = hitTier;
        this.hitTierDesc = hitTierDesc;
        this.optionsCount = optionsCount != null ? optionsCount : 7;
        this.initialProfit = initialProfit != null ? initialProfit : BigDecimal.valueOf(-7.00);
        this.stakeAmount = stakeAmount != null ? stakeAmount : BigDecimal.valueOf(7.00);
        this.returnAmount = returnAmount != null ? returnAmount : BigDecimal.ZERO;
        this.netProfit = netProfit != null ? netProfit : BigDecimal.ZERO;
        this.resultReturn = resultReturn != null ? resultReturn : BigDecimal.ZERO;
        this.scoreReturn = scoreReturn != null ? scoreReturn : BigDecimal.ZERO;
        this.goalsReturn = goalsReturn != null ? goalsReturn : BigDecimal.ZERO;
        this.settledAt = settledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MatchPredictionBuilder builder() {
        return new MatchPredictionBuilder();
    }

    public static class MatchPredictionBuilder {
        private Long id;
        private Long matchId;
        private Long aiModelId;
        private String aiModelCode;
        private MatchResultEnum predictedResult;
        private BigDecimal resultOdds = BigDecimal.valueOf(1.85);
        private String predictedResultsJson;
        private String predictedScoresJson;
        private String predictedGoalsJson;
        private String predictedScores;
        private String predictedGoals;
        private Integer predictedHomeScore;
        private Integer predictedAwayScore;
        private BigDecimal confidence;
        private String analysisSummary;
        private String detailedAnalysis;
        private BigDecimal predictedOdds;
        private SettleStatusEnum settleStatus = SettleStatusEnum.PENDING;
        private Integer isResultCorrect;
        private Integer isScoreCorrect;
        private String hitScore;
        private Integer isGoalsCorrect;
        private Integer hitGoals;
        private Integer hitImportance = 0;
        private String hitTier;
        private String hitTierDesc;
        private Integer optionsCount = 7;
        private BigDecimal initialProfit = BigDecimal.valueOf(-7.00);
        private BigDecimal stakeAmount = BigDecimal.valueOf(7.00);
        private BigDecimal returnAmount = BigDecimal.ZERO;
        private BigDecimal netProfit = BigDecimal.ZERO;
        private BigDecimal resultReturn = BigDecimal.ZERO;
        private BigDecimal scoreReturn = BigDecimal.ZERO;
        private BigDecimal goalsReturn = BigDecimal.ZERO;
        private LocalDateTime settledAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public MatchPredictionBuilder id(Long id) { this.id = id; return this; }
        public MatchPredictionBuilder matchId(Long matchId) { this.matchId = matchId; return this; }
        public MatchPredictionBuilder aiModelId(Long aiModelId) { this.aiModelId = aiModelId; return this; }
        public MatchPredictionBuilder aiModelCode(String aiModelCode) { this.aiModelCode = aiModelCode; return this; }
        public MatchPredictionBuilder predictedResult(MatchResultEnum predictedResult) { this.predictedResult = predictedResult; return this; }
        public MatchPredictionBuilder resultOdds(BigDecimal resultOdds) { this.resultOdds = resultOdds; return this; }
        public MatchPredictionBuilder predictedResultsJson(String predictedResultsJson) { this.predictedResultsJson = predictedResultsJson; return this; }
        public MatchPredictionBuilder predictedScoresJson(String predictedScoresJson) { this.predictedScoresJson = predictedScoresJson; return this; }
        public MatchPredictionBuilder predictedGoalsJson(String predictedGoalsJson) { this.predictedGoalsJson = predictedGoalsJson; return this; }
        public MatchPredictionBuilder predictedScores(String predictedScores) { this.predictedScores = predictedScores; return this; }
        public MatchPredictionBuilder predictedGoals(String predictedGoals) { this.predictedGoals = predictedGoals; return this; }
        public MatchPredictionBuilder predictedHomeScore(Integer predictedHomeScore) { this.predictedHomeScore = predictedHomeScore; return this; }
        public MatchPredictionBuilder predictedAwayScore(Integer predictedAwayScore) { this.predictedAwayScore = predictedAwayScore; return this; }
        public MatchPredictionBuilder confidence(BigDecimal confidence) { this.confidence = confidence; return this; }
        public MatchPredictionBuilder analysisSummary(String analysisSummary) { this.analysisSummary = analysisSummary; return this; }
        public MatchPredictionBuilder detailedAnalysis(String detailedAnalysis) { this.detailedAnalysis = detailedAnalysis; return this; }
        public MatchPredictionBuilder predictedOdds(BigDecimal predictedOdds) { this.predictedOdds = predictedOdds; return this; }
        public MatchPredictionBuilder settleStatus(SettleStatusEnum settleStatus) { this.settleStatus = settleStatus; return this; }
        public MatchPredictionBuilder isResultCorrect(Integer isResultCorrect) { this.isResultCorrect = isResultCorrect; return this; }
        public MatchPredictionBuilder isScoreCorrect(Integer isScoreCorrect) { this.isScoreCorrect = isScoreCorrect; return this; }
        public MatchPredictionBuilder hitScore(String hitScore) { this.hitScore = hitScore; return this; }
        public MatchPredictionBuilder isGoalsCorrect(Integer isGoalsCorrect) { this.isGoalsCorrect = isGoalsCorrect; return this; }
        public MatchPredictionBuilder hitGoals(Integer hitGoals) { this.hitGoals = hitGoals; return this; }
        public MatchPredictionBuilder hitImportance(Integer hitImportance) { this.hitImportance = hitImportance; return this; }
        public MatchPredictionBuilder hitTier(String hitTier) { this.hitTier = hitTier; return this; }
        public MatchPredictionBuilder hitTierDesc(String hitTierDesc) { this.hitTierDesc = hitTierDesc; return this; }
        public MatchPredictionBuilder optionsCount(Integer optionsCount) { this.optionsCount = optionsCount; return this; }
        public MatchPredictionBuilder initialProfit(BigDecimal initialProfit) { this.initialProfit = initialProfit; return this; }
        public MatchPredictionBuilder stakeAmount(BigDecimal stakeAmount) { this.stakeAmount = stakeAmount; return this; }
        public MatchPredictionBuilder returnAmount(BigDecimal returnAmount) { this.returnAmount = returnAmount; return this; }
        public MatchPredictionBuilder netProfit(BigDecimal netProfit) { this.netProfit = netProfit; return this; }
        public MatchPredictionBuilder resultReturn(BigDecimal resultReturn) { this.resultReturn = resultReturn; return this; }
        public MatchPredictionBuilder scoreReturn(BigDecimal scoreReturn) { this.scoreReturn = scoreReturn; return this; }
        public MatchPredictionBuilder goalsReturn(BigDecimal goalsReturn) { this.goalsReturn = goalsReturn; return this; }
        public MatchPredictionBuilder settledAt(LocalDateTime settledAt) { this.settledAt = settledAt; return this; }
        public MatchPredictionBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public MatchPredictionBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public MatchPrediction build() {
            return new MatchPrediction(id, matchId, aiModelId, aiModelCode, predictedResult, resultOdds, predictedResultsJson, predictedScoresJson, predictedGoalsJson, predictedScores, predictedGoals, predictedHomeScore, predictedAwayScore, confidence, analysisSummary, detailedAnalysis, predictedOdds, settleStatus, isResultCorrect, isScoreCorrect, hitScore, isGoalsCorrect, hitGoals, hitImportance, hitTier, hitTierDesc, optionsCount, initialProfit, stakeAmount, returnAmount, netProfit, resultReturn, scoreReturn, goalsReturn, settledAt, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }
    public Long getAiModelId() { return aiModelId; }
    public void setAiModelId(Long aiModelId) { this.aiModelId = aiModelId; }
    public String getAiModelCode() { return aiModelCode; }
    public void setAiModelCode(String aiModelCode) { this.aiModelCode = aiModelCode; }
    public MatchResultEnum getPredictedResult() { return predictedResult; }
    public void setPredictedResult(MatchResultEnum predictedResult) { this.predictedResult = predictedResult; }
    public BigDecimal getResultOdds() { return resultOdds; }
    public void setResultOdds(BigDecimal resultOdds) { this.resultOdds = resultOdds; }
    public String getPredictedResultsJson() { return predictedResultsJson; }
    public void setPredictedResultsJson(String predictedResultsJson) { this.predictedResultsJson = predictedResultsJson; }
    public String getPredictedScoresJson() { return predictedScoresJson; }
    public void setPredictedScoresJson(String predictedScoresJson) { this.predictedScoresJson = predictedScoresJson; }
    public String getPredictedGoalsJson() { return predictedGoalsJson; }
    public void setPredictedGoalsJson(String predictedGoalsJson) { this.predictedGoalsJson = predictedGoalsJson; }
    public String getPredictedScores() { return predictedScores; }
    public void setPredictedScores(String predictedScores) { this.predictedScores = predictedScores; }
    public String getPredictedGoals() { return predictedGoals; }
    public void setPredictedGoals(String predictedGoals) { this.predictedGoals = predictedGoals; }
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
    public SettleStatusEnum getSettleStatus() { return settleStatus; }
    public void setSettleStatus(SettleStatusEnum settleStatus) { this.settleStatus = settleStatus; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper Models for JSON conversion
    public static class ResultItem {
        private MatchResultEnum result;
        private BigDecimal odds;

        public ResultItem() {}

        public ResultItem(MatchResultEnum result, BigDecimal odds) {
            this.result = result;
            this.odds = odds;
        }

        public static ResultItemBuilder builder() {
            return new ResultItemBuilder();
        }

        public static class ResultItemBuilder {
            private MatchResultEnum result;
            private BigDecimal odds;

            public ResultItemBuilder result(MatchResultEnum result) { this.result = result; return this; }
            public ResultItemBuilder odds(BigDecimal odds) { this.odds = odds; return this; }
            public ResultItem build() { return new ResultItem(result, odds); }
        }

        public MatchResultEnum getResult() { return result; }
        public void setResult(MatchResultEnum result) { this.result = result; }
        public BigDecimal getOdds() { return odds; }
        public void setOdds(BigDecimal odds) { this.odds = odds; }
    }

    public static class ScoreItem {
        private String score;
        private BigDecimal odds;

        public ScoreItem() {}

        public ScoreItem(String score, BigDecimal odds) {
            this.score = score;
            this.odds = odds;
        }

        public static ScoreItemBuilder builder() {
            return new ScoreItemBuilder();
        }

        public static class ScoreItemBuilder {
            private String score;
            private BigDecimal odds;

            public ScoreItemBuilder score(String score) { this.score = score; return this; }
            public ScoreItemBuilder odds(BigDecimal odds) { this.odds = odds; return this; }
            public ScoreItem build() { return new ScoreItem(score, odds); }
        }

        public String getScore() { return score; }
        public void setScore(String score) { this.score = score; }
        public BigDecimal getOdds() { return odds; }
        public void setOdds(BigDecimal odds) { this.odds = odds; }
    }

    public static class GoalItem {
        private Integer goals;
        private BigDecimal odds;

        public GoalItem() {}

        public GoalItem(Integer goals, BigDecimal odds) {
            this.goals = goals;
            this.odds = odds;
        }

        public static GoalItemBuilder builder() {
            return new GoalItemBuilder();
        }

        public static class GoalItemBuilder {
            private Integer goals;
            private BigDecimal odds;

            public GoalItemBuilder goals(Integer goals) { this.goals = goals; return this; }
            public GoalItemBuilder odds(BigDecimal odds) { this.odds = odds; return this; }
            public GoalItem build() { return new GoalItem(goals, odds); }
        }

        public Integer getGoals() { return goals; }
        public void setGoals(Integer goals) { this.goals = goals; }
        public BigDecimal getOdds() { return odds; }
        public void setOdds(BigDecimal odds) { this.odds = odds; }
    }

    public List<ResultItem> getResultItemsList() {
        if (predictedResultsJson != null && !predictedResultsJson.isBlank()) {
            try {
                return OBJECT_MAPPER.readValue(predictedResultsJson, new TypeReference<List<ResultItem>>() {});
            } catch (Exception ignored) {}
        }
        List<ResultItem> list = new ArrayList<>();
        if (predictedResult != null) {
            list.add(new ResultItem(predictedResult, resultOdds != null ? resultOdds : BigDecimal.valueOf(1.85)));
        }
        return list;
    }

    public void setResultItemsList(List<ResultItem> items) {
        if (items == null || items.isEmpty()) {
            this.predictedResultsJson = null;
        } else {
            try {
                this.predictedResultsJson = OBJECT_MAPPER.writeValueAsString(items);
            } catch (Exception ignored) {}
            this.predictedResult = items.get(0).getResult();
            this.resultOdds = items.get(0).getOdds();
        }
    }

    public List<ScoreItem> getScoreItemsList() {
        if (predictedScoresJson != null && !predictedScoresJson.isBlank()) {
            try {
                return OBJECT_MAPPER.readValue(predictedScoresJson, new TypeReference<List<ScoreItem>>() {});
            } catch (Exception e) {
                // fallback to string parsing
            }
        }
        List<ScoreItem> list = new ArrayList<>();
        if (predictedScores != null && !predictedScores.isBlank()) {
            for (String s : predictedScores.split(",")) {
                if (!s.trim().isEmpty()) {
                    list.add(new ScoreItem(s.trim(), BigDecimal.valueOf(7.00)));
                }
            }
        } else if (predictedHomeScore != null && predictedAwayScore != null) {
            list.add(new ScoreItem(predictedHomeScore + "-" + predictedAwayScore, BigDecimal.valueOf(7.00)));
        }
        return list;
    }

    public void setScoreItemsList(List<ScoreItem> items) {
        if (items == null || items.isEmpty()) {
            this.predictedScoresJson = null;
            this.predictedScores = null;
        } else {
            List<ScoreItem> limited = items.stream().limit(3).collect(Collectors.toList());
            try {
                this.predictedScoresJson = OBJECT_MAPPER.writeValueAsString(limited);
            } catch (Exception ignored) {}
            this.predictedScores = limited.stream().map(ScoreItem::getScore).collect(Collectors.joining(","));
        }
    }

    public List<GoalItem> getGoalItemsList() {
        if (predictedGoalsJson != null && !predictedGoalsJson.isBlank()) {
            try {
                return OBJECT_MAPPER.readValue(predictedGoalsJson, new TypeReference<List<GoalItem>>() {});
            } catch (Exception e) {
                // fallback
            }
        }
        List<GoalItem> list = new ArrayList<>();
        if (predictedGoals != null && !predictedGoals.isBlank()) {
            for (String g : predictedGoals.split(",")) {
                if (!g.trim().isEmpty()) {
                    try {
                        list.add(new GoalItem(Integer.parseInt(g.trim()), BigDecimal.valueOf(3.50)));
                    } catch (Exception ignored) {}
                }
            }
        }
        return list;
    }

    public void setGoalItemsList(List<GoalItem> items) {
        if (items == null || items.isEmpty()) {
            this.predictedGoalsJson = null;
            this.predictedGoals = null;
        } else {
            List<GoalItem> limited = items.stream().limit(3).collect(Collectors.toList());
            try {
                this.predictedGoalsJson = OBJECT_MAPPER.writeValueAsString(limited);
            } catch (Exception ignored) {}
            this.predictedGoals = limited.stream().map(g -> String.valueOf(g.getGoals())).collect(Collectors.joining(","));
        }
    }

    public List<String> getPredictedScoresList() {
        return getScoreItemsList().stream().map(ScoreItem::getScore).collect(Collectors.toList());
    }

    public void setPredictedScoresList(List<String> scores) {
        if (scores == null || scores.isEmpty()) {
            setScoreItemsList(new ArrayList<>());
        } else {
            setScoreItemsList(scores.stream()
                    .map(s -> new ScoreItem(s, BigDecimal.valueOf(7.00)))
                    .collect(Collectors.toList()));
        }
    }

    public List<Integer> getPredictedGoalsList() {
        return getGoalItemsList().stream().map(GoalItem::getGoals).collect(Collectors.toList());
    }

    public void setPredictedGoalsList(List<Integer> goals) {
        if (goals == null || goals.isEmpty()) {
            setGoalItemsList(new ArrayList<>());
        } else {
            setGoalItemsList(goals.stream()
                    .map(g -> new GoalItem(g, BigDecimal.valueOf(3.50)))
                    .collect(Collectors.toList()));
        }
    }
}
