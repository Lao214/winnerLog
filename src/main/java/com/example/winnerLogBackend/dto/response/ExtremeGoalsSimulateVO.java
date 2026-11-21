package com.example.winnerLogBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Schema(description = "单场0球与7球赔率测算与进球数预测响应")
public class ExtremeGoalsSimulateVO {

    @Schema(description = "匹配历史样本场数", example = "12")
    private Integer matchedCount;

    @Schema(description = "0球赔率匹配区间", example = "8.50 ~ 10.50 (基准 9.50 ± 1.00)")
    private String zeroOddsRangeDesc;

    @Schema(description = "7球赔率匹配区间", example = "24.00 ~ 32.00 (基准 28.00 ± 4.00)")
    private String sevenOddsRangeDesc;

    @Schema(description = "输入联赛名称", example = "日职联")
    private String leagueName;

    @Schema(description = "输入赛制类型", example = "LEAGUE")
    private String matchSystem;

    @Schema(description = "样本实际平均总进球数", example = "2.25")
    private BigDecimal avgGoalsInSample;

    @Schema(description = "0-1球小球打出概率(%)", example = "25.00")
    private BigDecimal predZeroOneRate;

    @Schema(description = "2-3球黄金区打出概率(%)", example = "58.33")
    private BigDecimal predTwoThreeRate;

    @Schema(description = "4球+大球打出概率(%)", example = "16.67")
    private BigDecimal predFourPlusRate;

    @Schema(description = "预测总进球数概率排行 Top 3")
    private List<GoalRankItemVO> topGoalsRanking;

    @Schema(description = "各进球数具体场次映射")
    private Map<String, Integer> goalsCountMap;

    @Schema(description = "智能打单策略分析与建议", example = "同类样本下 2-3 球黄金中球打出概率高达 58.3%，且 0球 极少打出，建议重点布局 2球、3球 防范小比分。")
    private String advice;

    @Schema(description = "置信度/风险评级 (LOW:样本充足高置信, MEDIUM:中等参考, HIGH:样本偏少谨慎参考)", example = "LOW")
    private String riskLevel;

    @Schema(description = "匹配到的历史样本明细列表")
    private List<MatchedMatchSampleVO> matchedSamples;

    public static class GoalRankItemVO {
        private String goalText;       // 如 "2球"
        private Integer goalValue;     // 2
        private Integer count;         // 7
        private BigDecimal rate;       // 58.33%

        public String getGoalText() {
            return goalText;
        }

        public void setGoalText(String goalText) {
            this.goalText = goalText;
        }

        public Integer getGoalValue() {
            return goalValue;
        }

        public void setGoalValue(Integer goalValue) {
            this.goalValue = goalValue;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public BigDecimal getRate() {
            return rate;
        }

        public void setRate(BigDecimal rate) {
            this.rate = rate;
        }
    }

    public static class MatchedMatchSampleVO {
        private Long id;
        private String matchDate;
        private String leagueName;
        private String matchSystem;
        private String homeTeam;
        private String awayTeam;
        private BigDecimal zeroGoalsOdds;
        private BigDecimal sevenGoalsOdds;
        private Integer totalGoals;
        private String fullScore;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getMatchDate() {
            return matchDate;
        }

        public void setMatchDate(String matchDate) {
            this.matchDate = matchDate;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public String getMatchSystem() {
            return matchSystem;
        }

        public void setMatchSystem(String matchSystem) {
            this.matchSystem = matchSystem;
        }

        public String getHomeTeam() {
            return homeTeam;
        }

        public void setHomeTeam(String homeTeam) {
            this.homeTeam = homeTeam;
        }

        public String getAwayTeam() {
            return awayTeam;
        }

        public void setAwayTeam(String awayTeam) {
            this.awayTeam = awayTeam;
        }

        public BigDecimal getZeroGoalsOdds() {
            return zeroGoalsOdds;
        }

        public void setZeroGoalsOdds(BigDecimal zeroGoalsOdds) {
            this.zeroGoalsOdds = zeroGoalsOdds;
        }

        public BigDecimal getSevenGoalsOdds() {
            return sevenGoalsOdds;
        }

        public void setSevenGoalsOdds(BigDecimal sevenGoalsOdds) {
            this.sevenGoalsOdds = sevenGoalsOdds;
        }

        public Integer getTotalGoals() {
            return totalGoals;
        }

        public void setTotalGoals(Integer totalGoals) {
            this.totalGoals = totalGoals;
        }

        public String getFullScore() {
            return fullScore;
        }

        public void setFullScore(String fullScore) {
            this.fullScore = fullScore;
        }
    }

    // ==================== Getters & Setters ====================

    public Integer getMatchedCount() {
        return matchedCount;
    }

    public void setMatchedCount(Integer matchedCount) {
        this.matchedCount = matchedCount;
    }

    public String getZeroOddsRangeDesc() {
        return zeroOddsRangeDesc;
    }

    public void setZeroOddsRangeDesc(String zeroOddsRangeDesc) {
        this.zeroOddsRangeDesc = zeroOddsRangeDesc;
    }

    public String getSevenOddsRangeDesc() {
        return sevenOddsRangeDesc;
    }

    public void setSevenOddsRangeDesc(String sevenOddsRangeDesc) {
        this.sevenOddsRangeDesc = sevenOddsRangeDesc;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getMatchSystem() {
        return matchSystem;
    }

    public void setMatchSystem(String matchSystem) {
        this.matchSystem = matchSystem;
    }

    public BigDecimal getAvgGoalsInSample() {
        return avgGoalsInSample;
    }

    public void setAvgGoalsInSample(BigDecimal avgGoalsInSample) {
        this.avgGoalsInSample = avgGoalsInSample;
    }

    public BigDecimal getPredZeroOneRate() {
        return predZeroOneRate;
    }

    public void setPredZeroOneRate(BigDecimal predZeroOneRate) {
        this.predZeroOneRate = predZeroOneRate;
    }

    public BigDecimal getPredTwoThreeRate() {
        return predTwoThreeRate;
    }

    public void setPredTwoThreeRate(BigDecimal predTwoThreeRate) {
        this.predTwoThreeRate = predTwoThreeRate;
    }

    public BigDecimal getPredFourPlusRate() {
        return predFourPlusRate;
    }

    public void setPredFourPlusRate(BigDecimal predFourPlusRate) {
        this.predFourPlusRate = predFourPlusRate;
    }

    public List<GoalRankItemVO> getTopGoalsRanking() {
        return topGoalsRanking;
    }

    public void setTopGoalsRanking(List<GoalRankItemVO> topGoalsRanking) {
        this.topGoalsRanking = topGoalsRanking;
    }

    public Map<String, Integer> getGoalsCountMap() {
        return goalsCountMap;
    }

    public void setGoalsCountMap(Map<String, Integer> goalsCountMap) {
        this.goalsCountMap = goalsCountMap;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public List<MatchedMatchSampleVO> getMatchedSamples() {
        return matchedSamples;
    }

    public void setMatchedSamples(List<MatchedMatchSampleVO> matchedSamples) {
        this.matchedSamples = matchedSamples;
    }
}
