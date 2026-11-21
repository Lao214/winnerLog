package com.example.winnerLogBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Schema(description = "单场比赛胜平负概率与期望ROI智能测算结果")
public class HandicapMatchSimulationVO {

    @Schema(description = "匹配到的历史样本总场数", example = "14")
    private Integer sampleCount;

    @Schema(description = "匹配的赔率区间描述", example = "1.45 ~ 1.65 (基准让球胜赔 1.55 ± 0.10)")
    private String oddsRangeDesc;

    @Schema(description = "样本最低赔率", example = "1.45")
    private BigDecimal minOdds;

    @Schema(description = "样本最高赔率", example = "1.65")
    private BigDecimal maxOdds;

    @Schema(description = "赛制类型", example = "LEAGUE")
    private String matchSystem;

    @Schema(description = "赛制类型中文说明", example = "联赛积分制")
    private String matchSystemDesc;

    @Schema(description = "让球方 (HOME/AWAY)", example = "HOME")
    private String handicapSide;

    @Schema(description = "让球方中文说明", example = "主让")
    private String handicapSideDesc;

    @Schema(description = "基准让球胜赔", example = "1.55")
    private BigDecimal targetOdds;

    @Schema(description = "容差步幅", example = "0.10")
    private BigDecimal tolerance;

    @Schema(description = "输入的主胜赔率", example = "1.55")
    private BigDecimal inputHomeWinOdds;

    @Schema(description = "输入的平局赔率", example = "3.80")
    private BigDecimal inputDrawOdds;

    @Schema(description = "输入的客胜赔率", example = "5.20")
    private BigDecimal inputAwayWinOdds;

    // ==================== 概率与场次统计 ====================

    @Schema(description = "主胜打出概率(%)", example = "64.29")
    private BigDecimal homeWinProb;

    @Schema(description = "平局打出概率(%)", example = "21.43")
    private BigDecimal drawProb;

    @Schema(description = "客胜打出概率(%)", example = "14.28")
    private BigDecimal awayWinProb;

    @Schema(description = "穿盘概率(%)", example = "42.86")
    private BigDecimal breakProb;

    @Schema(description = "主胜场次", example = "9")
    private Integer homeWinCount;

    @Schema(description = "平局场次", example = "3")
    private Integer drawCount;

    @Schema(description = "客胜场次", example = "2")
    private Integer awayWinCount;

    @Schema(description = "穿盘场次", example = "6")
    private Integer breakCount;

    // ==================== 期望 ROI 投资回报率 ====================

    @Schema(description = "主胜期望ROI(%) 公式: (P(主胜) * 主胜赔 - 1) * 100", example = "-0.35")
    private BigDecimal homeWinRoi;

    @Schema(description = "平局期望ROI(%) 公式: (P(平局) * 平赔 - 1) * 100", example = "-18.57")
    private BigDecimal drawRoi;

    @Schema(description = "客胜期望ROI(%) 公式: (P(客胜) * 客胜赔 - 1) * 100", example = "-25.74")
    private BigDecimal awayWinRoi;

    // ==================== 智能决策与策略推荐 ====================

    @Schema(description = "最优期望选项 (HOME_WIN/DRAW/AWAY_WIN/NONE)", example = "HOME_WIN")
    private String bestChoice;

    @Schema(description = "最优期望选项名称", example = "主胜")
    private String bestChoiceName;

    @Schema(description = "最高期望ROI(%)", example = "-0.35")
    private BigDecimal maxRoi;

    @Schema(description = "量化策略与复盘建议", example = "历史样本主胜率达64.3%，主胜期望ROI为-0.35%，庄家抽水率低，属较稳妥选项；穿盘率42.9%，需警惕赢球输盘。")
    private String advice;

    @Schema(description = "风险等级 (LOW:样本充足高置信度, MEDIUM:中等参考, HIGH:样本过少谨慎参考)", example = "LOW")
    private String riskLevel;

    // ==================== 辅助进阶统计 ====================

    @Schema(description = "总进球数分布统计 Top3 (如 {'2球': 5, '3球': 4})")
    private Map<String, Long> goalsDistribution;

    @Schema(description = "半全场分布统计 Top3 (如 {'胜胜': 6, '平胜': 3})")
    private Map<String, Long> halfFullDistribution;

    @Schema(description = "匹配到的历史样本比赛明细列表")
    private List<MatchedSampleItemVO> sampleMatches;

    public static class MatchedSampleItemVO {
        private Long id;
        private String matchDate;
        private String leagueName;
        private String homeTeam;
        private String awayTeam;
        private String fullScore;
        private String halfScore;
        private String matchResult;
        private String matchResultName;
        private BigDecimal resultOdds;
        private BigDecimal handicapOdds;
        private Boolean isBreak;
        private String halfFullResult;
        private Integer totalGoals;

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

        public String getFullScore() {
            return fullScore;
        }

        public void setFullScore(String fullScore) {
            this.fullScore = fullScore;
        }

        public String getHalfScore() {
            return halfScore;
        }

        public void setHalfScore(String halfScore) {
            this.halfScore = halfScore;
        }

        public String getMatchResult() {
            return matchResult;
        }

        public void setMatchResult(String matchResult) {
            this.matchResult = matchResult;
        }

        public String getMatchResultName() {
            return matchResultName;
        }

        public void setMatchResultName(String matchResultName) {
            this.matchResultName = matchResultName;
        }

        public BigDecimal getResultOdds() {
            return resultOdds;
        }

        public void setResultOdds(BigDecimal resultOdds) {
            this.resultOdds = resultOdds;
        }

        public BigDecimal getHandicapOdds() {
            return handicapOdds;
        }

        public void setHandicapOdds(BigDecimal handicapOdds) {
            this.handicapOdds = handicapOdds;
        }

        public Boolean getIsBreak() {
            return isBreak;
        }

        public void setIsBreak(Boolean isBreak) {
            this.isBreak = isBreak;
        }

        public String getHalfFullResult() {
            return halfFullResult;
        }

        public void setHalfFullResult(String halfFullResult) {
            this.halfFullResult = halfFullResult;
        }

        public Integer getTotalGoals() {
            return totalGoals;
        }

        public void setTotalGoals(Integer totalGoals) {
            this.totalGoals = totalGoals;
        }
    }

    // ==================== Getters & Setters ====================

    public Integer getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(Integer sampleCount) {
        this.sampleCount = sampleCount;
    }

    public String getOddsRangeDesc() {
        return oddsRangeDesc;
    }

    public void setOddsRangeDesc(String oddsRangeDesc) {
        this.oddsRangeDesc = oddsRangeDesc;
    }

    public BigDecimal getMinOdds() {
        return minOdds;
    }

    public void setMinOdds(BigDecimal minOdds) {
        this.minOdds = minOdds;
    }

    public BigDecimal getMaxOdds() {
        return maxOdds;
    }

    public void setMaxOdds(BigDecimal maxOdds) {
        this.maxOdds = maxOdds;
    }

    public String getMatchSystem() {
        return matchSystem;
    }

    public void setMatchSystem(String matchSystem) {
        this.matchSystem = matchSystem;
    }

    public String getMatchSystemDesc() {
        return matchSystemDesc;
    }

    public void setMatchSystemDesc(String matchSystemDesc) {
        this.matchSystemDesc = matchSystemDesc;
    }

    public String getHandicapSide() {
        return handicapSide;
    }

    public void setHandicapSide(String handicapSide) {
        this.handicapSide = handicapSide;
    }

    public String getHandicapSideDesc() {
        return handicapSideDesc;
    }

    public void setHandicapSideDesc(String handicapSideDesc) {
        this.handicapSideDesc = handicapSideDesc;
    }

    public BigDecimal getTargetOdds() {
        return targetOdds;
    }

    public void setTargetOdds(BigDecimal targetOdds) {
        this.targetOdds = targetOdds;
    }

    public BigDecimal getTolerance() {
        return tolerance;
    }

    public void setTolerance(BigDecimal tolerance) {
        this.tolerance = tolerance;
    }

    public BigDecimal getInputHomeWinOdds() {
        return inputHomeWinOdds;
    }

    public void setInputHomeWinOdds(BigDecimal inputHomeWinOdds) {
        this.inputHomeWinOdds = inputHomeWinOdds;
    }

    public BigDecimal getInputDrawOdds() {
        return inputDrawOdds;
    }

    public void setInputDrawOdds(BigDecimal inputDrawOdds) {
        this.inputDrawOdds = inputDrawOdds;
    }

    public BigDecimal getInputAwayWinOdds() {
        return inputAwayWinOdds;
    }

    public void setInputAwayWinOdds(BigDecimal inputAwayWinOdds) {
        this.inputAwayWinOdds = inputAwayWinOdds;
    }

    public BigDecimal getHomeWinProb() {
        return homeWinProb;
    }

    public void setHomeWinProb(BigDecimal homeWinProb) {
        this.homeWinProb = homeWinProb;
    }

    public BigDecimal getDrawProb() {
        return drawProb;
    }

    public void setDrawProb(BigDecimal drawProb) {
        this.drawProb = drawProb;
    }

    public BigDecimal getAwayWinProb() {
        return awayWinProb;
    }

    public void setAwayWinProb(BigDecimal awayWinProb) {
        this.awayWinProb = awayWinProb;
    }

    public BigDecimal getBreakProb() {
        return breakProb;
    }

    public void setBreakProb(BigDecimal breakProb) {
        this.breakProb = breakProb;
    }

    public Integer getHomeWinCount() {
        return homeWinCount;
    }

    public void setHomeWinCount(Integer homeWinCount) {
        this.homeWinCount = homeWinCount;
    }

    public Integer getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(Integer drawCount) {
        this.drawCount = drawCount;
    }

    public Integer getAwayWinCount() {
        return awayWinCount;
    }

    public void setAwayWinCount(Integer awayWinCount) {
        this.awayWinCount = awayWinCount;
    }

    public Integer getBreakCount() {
        return breakCount;
    }

    public void setBreakCount(Integer breakCount) {
        this.breakCount = breakCount;
    }

    public BigDecimal getHomeWinRoi() {
        return homeWinRoi;
    }

    public void setHomeWinRoi(BigDecimal homeWinRoi) {
        this.homeWinRoi = homeWinRoi;
    }

    public BigDecimal getDrawRoi() {
        return drawRoi;
    }

    public void setDrawRoi(BigDecimal drawRoi) {
        this.drawRoi = drawRoi;
    }

    public BigDecimal getAwayWinRoi() {
        return awayWinRoi;
    }

    public void setAwayWinRoi(BigDecimal awayWinRoi) {
        this.awayWinRoi = awayWinRoi;
    }

    public String getBestChoice() {
        return bestChoice;
    }

    public void setBestChoice(String bestChoice) {
        this.bestChoice = bestChoice;
    }

    public String getBestChoiceName() {
        return bestChoiceName;
    }

    public void setBestChoiceName(String bestChoiceName) {
        this.bestChoiceName = bestChoiceName;
    }

    public BigDecimal getMaxRoi() {
        return maxRoi;
    }

    public void setMaxRoi(BigDecimal maxRoi) {
        this.maxRoi = maxRoi;
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

    public Map<String, Long> getGoalsDistribution() {
        return goalsDistribution;
    }

    public void setGoalsDistribution(Map<String, Long> goalsDistribution) {
        this.goalsDistribution = goalsDistribution;
    }

    public Map<String, Long> getHalfFullDistribution() {
        return halfFullDistribution;
    }

    public void setHalfFullDistribution(Map<String, Long> halfFullDistribution) {
        this.halfFullDistribution = halfFullDistribution;
    }

    public List<MatchedSampleItemVO> getSampleMatches() {
        return sampleMatches;
    }

    public void setSampleMatches(List<MatchedSampleItemVO> sampleMatches) {
        this.sampleMatches = sampleMatches;
    }
}
