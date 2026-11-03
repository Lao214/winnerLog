package com.example.winnerLogBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "GPT 与 千问 双模型对决与统计对比视图")
public class AiComparisonVO {

    @Schema(description = "模型A (如 GPT-4o) 统计")
    private AiLeaderboardVO modelAStat;

    @Schema(description = "模型B (如 通义千问 Qwen-Max) 统计")
    private AiLeaderboardVO modelBStat;

    @Schema(description = "两模型共同预测并已结算的总场次", example = "40")
    private Integer jointSettledMatches;

    @Schema(description = "两模型预测赛果一致的场次", example = "28")
    private Integer agreeMatches;

    @Schema(description = "两模型预测一致且均命中的场次 (双红)", example = "22")
    private Integer agreeWonMatches;

    @Schema(description = "两模型预测一致率 (如 70.00%)", example = "70.00")
    private BigDecimal agreeRate;

    @Schema(description = "两模型观点分歧的场次", example = "12")
    private Integer disagreeMatches;

    @Schema(description = "分歧对决中 模型A胜出的场次 (A红B黑)", example = "7")
    private Integer modelAWonDisagreeMatches;

    @Schema(description = "分歧对决中 模型B胜出的场次 (B红A黑)", example = "4")
    private Integer modelBWonDisagreeMatches;

    @Schema(description = "分歧对决中 双双失误的场次 (双黑)", example = "1")
    private Integer bothLostDisagreeMatches;

    @Schema(description = "按各大联赛/杯赛拆分的胜率对比列表")
    private List<LeagueComparisonItem> leagueBreakdown;

    public AiComparisonVO() {}

    public AiComparisonVO(AiLeaderboardVO modelAStat, AiLeaderboardVO modelBStat, Integer jointSettledMatches, Integer agreeMatches, Integer agreeWonMatches, BigDecimal agreeRate, Integer disagreeMatches, Integer modelAWonDisagreeMatches, Integer modelBWonDisagreeMatches, Integer bothLostDisagreeMatches, List<LeagueComparisonItem> leagueBreakdown) {
        this.modelAStat = modelAStat;
        this.modelBStat = modelBStat;
        this.jointSettledMatches = jointSettledMatches;
        this.agreeMatches = agreeMatches;
        this.agreeWonMatches = agreeWonMatches;
        this.agreeRate = agreeRate;
        this.disagreeMatches = disagreeMatches;
        this.modelAWonDisagreeMatches = modelAWonDisagreeMatches;
        this.modelBWonDisagreeMatches = modelBWonDisagreeMatches;
        this.bothLostDisagreeMatches = bothLostDisagreeMatches;
        this.leagueBreakdown = leagueBreakdown;
    }

    public static AiComparisonVOBuilder builder() {
        return new AiComparisonVOBuilder();
    }

    public static class AiComparisonVOBuilder {
        private AiLeaderboardVO modelAStat;
        private AiLeaderboardVO modelBStat;
        private Integer jointSettledMatches;
        private Integer agreeMatches;
        private Integer agreeWonMatches;
        private BigDecimal agreeRate;
        private Integer disagreeMatches;
        private Integer modelAWonDisagreeMatches;
        private Integer modelBWonDisagreeMatches;
        private Integer bothLostDisagreeMatches;
        private List<LeagueComparisonItem> leagueBreakdown;

        public AiComparisonVOBuilder modelAStat(AiLeaderboardVO modelAStat) { this.modelAStat = modelAStat; return this; }
        public AiComparisonVOBuilder modelBStat(AiLeaderboardVO modelBStat) { this.modelBStat = modelBStat; return this; }
        public AiComparisonVOBuilder jointSettledMatches(Integer jointSettledMatches) { this.jointSettledMatches = jointSettledMatches; return this; }
        public AiComparisonVOBuilder agreeMatches(Integer agreeMatches) { this.agreeMatches = agreeMatches; return this; }
        public AiComparisonVOBuilder agreeWonMatches(Integer agreeWonMatches) { this.agreeWonMatches = agreeWonMatches; return this; }
        public AiComparisonVOBuilder agreeRate(BigDecimal agreeRate) { this.agreeRate = agreeRate; return this; }
        public AiComparisonVOBuilder disagreeMatches(Integer disagreeMatches) { this.disagreeMatches = disagreeMatches; return this; }
        public AiComparisonVOBuilder modelAWonDisagreeMatches(Integer modelAWonDisagreeMatches) { this.modelAWonDisagreeMatches = modelAWonDisagreeMatches; return this; }
        public AiComparisonVOBuilder modelBWonDisagreeMatches(Integer modelBWonDisagreeMatches) { this.modelBWonDisagreeMatches = modelBWonDisagreeMatches; return this; }
        public AiComparisonVOBuilder bothLostDisagreeMatches(Integer bothLostDisagreeMatches) { this.bothLostDisagreeMatches = bothLostDisagreeMatches; return this; }
        public AiComparisonVOBuilder leagueBreakdown(List<LeagueComparisonItem> leagueBreakdown) { this.leagueBreakdown = leagueBreakdown; return this; }

        public AiComparisonVO build() {
            return new AiComparisonVO(modelAStat, modelBStat, jointSettledMatches, agreeMatches, agreeWonMatches, agreeRate, disagreeMatches, modelAWonDisagreeMatches, modelBWonDisagreeMatches, bothLostDisagreeMatches, leagueBreakdown);
        }
    }

    public AiLeaderboardVO getModelAStat() { return modelAStat; }
    public void setModelAStat(AiLeaderboardVO modelAStat) { this.modelAStat = modelAStat; }
    public AiLeaderboardVO getModelBStat() { return modelBStat; }
    public void setModelBStat(AiLeaderboardVO modelBStat) { this.modelBStat = modelBStat; }
    public Integer getJointSettledMatches() { return jointSettledMatches; }
    public void setJointSettledMatches(Integer jointSettledMatches) { this.jointSettledMatches = jointSettledMatches; }
    public Integer getAgreeMatches() { return agreeMatches; }
    public void setAgreeMatches(Integer agreeMatches) { this.agreeMatches = agreeMatches; }
    public Integer getAgreeWonMatches() { return agreeWonMatches; }
    public void setAgreeWonMatches(Integer agreeWonMatches) { this.agreeWonMatches = agreeWonMatches; }
    public BigDecimal getAgreeRate() { return agreeRate; }
    public void setAgreeRate(BigDecimal agreeRate) { this.agreeRate = agreeRate; }
    public Integer getDisagreeMatches() { return disagreeMatches; }
    public void setDisagreeMatches(Integer disagreeMatches) { this.disagreeMatches = disagreeMatches; }
    public Integer getModelAWonDisagreeMatches() { return modelAWonDisagreeMatches; }
    public void setModelAWonDisagreeMatches(Integer modelAWonDisagreeMatches) { this.modelAWonDisagreeMatches = modelAWonDisagreeMatches; }
    public Integer getModelBWonDisagreeMatches() { return modelBWonDisagreeMatches; }
    public void setModelBWonDisagreeMatches(Integer modelBWonDisagreeMatches) { this.modelBWonDisagreeMatches = modelBWonDisagreeMatches; }
    public Integer getBothLostDisagreeMatches() { return bothLostDisagreeMatches; }
    public void setBothLostDisagreeMatches(Integer bothLostDisagreeMatches) { this.bothLostDisagreeMatches = bothLostDisagreeMatches; }
    public List<LeagueComparisonItem> getLeagueBreakdown() { return leagueBreakdown; }
    public void setLeagueBreakdown(List<LeagueComparisonItem> leagueBreakdown) { this.leagueBreakdown = leagueBreakdown; }

    @Schema(description = "单个联赛/杯赛的两模型对比")
    public static class LeagueComparisonItem {
        @Schema(description = "联赛编码 (如 PL, LA_LIGA, UCL)")
        private String leagueCode;

        @Schema(description = "联赛简称 (如 英超, 西甲, 欧冠)")
        private String leagueName;

        @Schema(description = "模型A已结算场次")
        private Integer modelASettledMatches;

        @Schema(description = "模型A胜率")
        private BigDecimal modelAWinRate;

        @Schema(description = "模型A净赚倍数")
        private BigDecimal modelANetProfit;

        @Schema(description = "模型B已结算场次")
        private Integer modelBSettledMatches;

        @Schema(description = "模型B胜率")
        private BigDecimal modelBWinRate;

        @Schema(description = "模型B净赚倍数")
        private BigDecimal modelBNetProfit;

        @Schema(description = "领先模型 (GPT / QWEN / TIE)")
        private String leaderModel;

        public LeagueComparisonItem() {}

        public LeagueComparisonItem(String leagueCode, String leagueName, Integer modelASettledMatches, BigDecimal modelAWinRate, BigDecimal modelANetProfit, Integer modelBSettledMatches, BigDecimal modelBWinRate, BigDecimal modelBNetProfit, String leaderModel) {
            this.leagueCode = leagueCode;
            this.leagueName = leagueName;
            this.modelASettledMatches = modelASettledMatches;
            this.modelAWinRate = modelAWinRate;
            this.modelANetProfit = modelANetProfit;
            this.modelBSettledMatches = modelBSettledMatches;
            this.modelBWinRate = modelBWinRate;
            this.modelBNetProfit = modelBNetProfit;
            this.leaderModel = leaderModel;
        }

        public static LeagueComparisonItemBuilder builder() {
            return new LeagueComparisonItemBuilder();
        }

        public static class LeagueComparisonItemBuilder {
            private String leagueCode;
            private String leagueName;
            private Integer modelASettledMatches;
            private BigDecimal modelAWinRate;
            private BigDecimal modelANetProfit;
            private Integer modelBSettledMatches;
            private BigDecimal modelBWinRate;
            private BigDecimal modelBNetProfit;
            private String leaderModel;

            public LeagueComparisonItemBuilder leagueCode(String leagueCode) { this.leagueCode = leagueCode; return this; }
            public LeagueComparisonItemBuilder leagueName(String leagueName) { this.leagueName = leagueName; return this; }
            public LeagueComparisonItemBuilder modelASettledMatches(Integer modelASettledMatches) { this.modelASettledMatches = modelASettledMatches; return this; }
            public LeagueComparisonItemBuilder modelAWinRate(BigDecimal modelAWinRate) { this.modelAWinRate = modelAWinRate; return this; }
            public LeagueComparisonItemBuilder modelANetProfit(BigDecimal modelANetProfit) { this.modelANetProfit = modelANetProfit; return this; }
            public LeagueComparisonItemBuilder modelBSettledMatches(Integer modelBSettledMatches) { this.modelBSettledMatches = modelBSettledMatches; return this; }
            public LeagueComparisonItemBuilder modelBWinRate(BigDecimal modelBWinRate) { this.modelBWinRate = modelBWinRate; return this; }
            public LeagueComparisonItemBuilder modelBNetProfit(BigDecimal modelBNetProfit) { this.modelBNetProfit = modelBNetProfit; return this; }
            public LeagueComparisonItemBuilder leaderModel(String leaderModel) { this.leaderModel = leaderModel; return this; }

            public LeagueComparisonItem build() {
                return new LeagueComparisonItem(leagueCode, leagueName, modelASettledMatches, modelAWinRate, modelANetProfit, modelBSettledMatches, modelBWinRate, modelBNetProfit, leaderModel);
            }
        }

        public String getLeagueCode() { return leagueCode; }
        public void setLeagueCode(String leagueCode) { this.leagueCode = leagueCode; }

        public String getLeagueName() { return leagueName; }
        public void setLeagueName(String leagueName) { this.leagueName = leagueName; }

        public Integer getModelASettledMatches() { return modelASettledMatches; }
        public void setModelASettledMatches(Integer modelASettledMatches) { this.modelASettledMatches = modelASettledMatches; }

        public BigDecimal getModelAWinRate() { return modelAWinRate; }
        public void setModelAWinRate(BigDecimal modelAWinRate) { this.modelAWinRate = modelAWinRate; }

        public BigDecimal getModelANetProfit() { return modelANetProfit; }
        public void setModelANetProfit(BigDecimal modelANetProfit) { this.modelANetProfit = modelANetProfit; }

        public Integer getModelBSettledMatches() { return modelBSettledMatches; }
        public void setModelBSettledMatches(Integer modelBSettledMatches) { this.modelBSettledMatches = modelBSettledMatches; }

        public BigDecimal getModelBWinRate() { return modelBWinRate; }
        public void setModelBWinRate(BigDecimal modelBWinRate) { this.modelBWinRate = modelBWinRate; }

        public BigDecimal getModelBNetProfit() { return modelBNetProfit; }
        public void setModelBNetProfit(BigDecimal modelBNetProfit) { this.modelBNetProfit = modelBNetProfit; }

        public String getLeaderModel() { return leaderModel; }
        public void setLeaderModel(String leaderModel) { this.leaderModel = leaderModel; }
    }
}
