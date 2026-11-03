package com.example.winnerLogBackend.dto.response;

import com.example.winnerLogBackend.enums.ScopeTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "AI胜率排行榜条目视图")
public class AiLeaderboardVO {

    @Schema(description = "排名名次 (从1开始)", example = "1")
    private Integer rank;

    @Schema(description = "AI模型标识编码 (如 GPT, QWEN)", example = "GPT")
    private String aiModelCode;

    @Schema(description = "AI模型显示名称", example = "OpenAI GPT-4o")
    private String aiModelName;

    @Schema(description = "AI模型提供方", example = "OpenAI")
    private String provider;

    @Schema(description = "统计维度 (OVERALL, SEASON, LEAGUE_SEASON, LEAGUE)")
    private ScopeTypeEnum scopeType;

    @Schema(description = "联赛编码")
    private String leagueCode;

    @Schema(description = "联赛名称")
    private String leagueName;

    @Schema(description = "赛季名称")
    private String seasonName;

    @Schema(description = "总预测场次", example = "50")
    private Integer totalMatches;

    @Schema(description = "已结算场次", example = "48")
    private Integer settledMatches;

    @Schema(description = "胜负红单/命中场次", example = "36")
    private Integer wonMatches;

    @Schema(description = "黑单/未命中场次", example = "12")
    private Integer lostMatches;

    @Schema(description = "走水/无效场次", example = "0")
    private Integer voidMatches;

    @Schema(description = "比分命中场次 (3个比分命中其一)", example = "16")
    private Integer exactScoreMatches;

    @Schema(description = "进球数命中场次 (3个进球数命中其一)", example = "32")
    private Integer goalsMatches;

    @Schema(description = "胜负胜率百分比 (如 75.00 代表 75.0%)", example = "75.00")
    private BigDecimal winRate;

    @Schema(description = "比分命中率 (如 33.33 代表 33.33%)", example = "33.33")
    private BigDecimal scoreAccuracyRate;

    @Schema(description = "进球数命中率 (如 66.67 代表 66.67%)", example = "66.67")
    private BigDecimal goalsAccuracyRate;

    @Schema(description = "累计总投入本金 (注数)", example = "350.00")
    private BigDecimal totalStake;

    @Schema(description = "累计总中奖返还", example = "498.50")
    private BigDecimal totalReturn;

    @Schema(description = "累计净赚倍数 (正数为赚，负数为亏，如 +148.50 代表净赚 148.5 倍本金)", example = "148.50")
    private BigDecimal netProfit;

    @Schema(description = "投资回报率 / 盈利率 % (如 +42.43 代表 +42.43%)", example = "42.43")
    private BigDecimal roiRate;

    @Schema(description = "胜负维度净赚倍数", example = "12.50")
    private BigDecimal resultNetProfit;

    @Schema(description = "比分维度净赚倍数", example = "86.00")
    private BigDecimal scoreNetProfit;

    @Schema(description = "进球数维度净赚倍数", example = "50.00")
    private BigDecimal goalsNetProfit;

    @Schema(description = "当前胜负连红/连黑 (正数为连红, 负数为连黑, 如 +4 代表4连红, -2 代表2连黑)", example = "4")
    private Integer currentStreak;

    @Schema(description = "历史最高连红数", example = "7")
    private Integer maxWinStreak;

    @Schema(description = "近期走势 (最近5场，如 [\"WON\", \"WON\", \"LOST\", \"WON\", \"WON\"])")
    private List<String> recentForm;

    @Schema(description = "最近结算更新时间")
    private LocalDateTime lastSettledAt;

    public AiLeaderboardVO() {}

    public AiLeaderboardVO(Integer rank, String aiModelCode, String aiModelName, String provider, ScopeTypeEnum scopeType, String leagueCode, String leagueName, String seasonName, Integer totalMatches, Integer settledMatches, Integer wonMatches, Integer lostMatches, Integer voidMatches, Integer exactScoreMatches, Integer goalsMatches, BigDecimal winRate, BigDecimal scoreAccuracyRate, BigDecimal goalsAccuracyRate, BigDecimal totalStake, BigDecimal totalReturn, BigDecimal netProfit, BigDecimal roiRate, BigDecimal resultNetProfit, BigDecimal scoreNetProfit, BigDecimal goalsNetProfit, Integer currentStreak, Integer maxWinStreak, List<String> recentForm, LocalDateTime lastSettledAt) {
        this.rank = rank;
        this.aiModelCode = aiModelCode;
        this.aiModelName = aiModelName;
        this.provider = provider;
        this.scopeType = scopeType;
        this.leagueCode = leagueCode;
        this.leagueName = leagueName;
        this.seasonName = seasonName;
        this.totalMatches = totalMatches;
        this.settledMatches = settledMatches;
        this.wonMatches = wonMatches;
        this.lostMatches = lostMatches;
        this.voidMatches = voidMatches;
        this.exactScoreMatches = exactScoreMatches;
        this.goalsMatches = goalsMatches;
        this.winRate = winRate;
        this.scoreAccuracyRate = scoreAccuracyRate;
        this.goalsAccuracyRate = goalsAccuracyRate;
        this.totalStake = totalStake;
        this.totalReturn = totalReturn;
        this.netProfit = netProfit;
        this.roiRate = roiRate;
        this.resultNetProfit = resultNetProfit;
        this.scoreNetProfit = scoreNetProfit;
        this.goalsNetProfit = goalsNetProfit;
        this.currentStreak = currentStreak;
        this.maxWinStreak = maxWinStreak;
        this.recentForm = recentForm;
        this.lastSettledAt = lastSettledAt;
    }

    public static AiLeaderboardVOBuilder builder() {
        return new AiLeaderboardVOBuilder();
    }

    public static class AiLeaderboardVOBuilder {
        private Integer rank;
        private String aiModelCode;
        private String aiModelName;
        private String provider;
        private ScopeTypeEnum scopeType;
        private String leagueCode;
        private String leagueName;
        private String seasonName;
        private Integer totalMatches;
        private Integer settledMatches;
        private Integer wonMatches;
        private Integer lostMatches;
        private Integer voidMatches;
        private Integer exactScoreMatches;
        private Integer goalsMatches;
        private BigDecimal winRate;
        private BigDecimal scoreAccuracyRate;
        private BigDecimal goalsAccuracyRate;
        private BigDecimal totalStake;
        private BigDecimal totalReturn;
        private BigDecimal netProfit;
        private BigDecimal roiRate;
        private BigDecimal resultNetProfit;
        private BigDecimal scoreNetProfit;
        private BigDecimal goalsNetProfit;
        private Integer currentStreak;
        private Integer maxWinStreak;
        private List<String> recentForm;
        private LocalDateTime lastSettledAt;

        public AiLeaderboardVOBuilder rank(Integer rank) { this.rank = rank; return this; }
        public AiLeaderboardVOBuilder aiModelCode(String aiModelCode) { this.aiModelCode = aiModelCode; return this; }
        public AiLeaderboardVOBuilder aiModelName(String aiModelName) { this.aiModelName = aiModelName; return this; }
        public AiLeaderboardVOBuilder provider(String provider) { this.provider = provider; return this; }
        public AiLeaderboardVOBuilder scopeType(ScopeTypeEnum scopeType) { this.scopeType = scopeType; return this; }
        public AiLeaderboardVOBuilder leagueCode(String leagueCode) { this.leagueCode = leagueCode; return this; }
        public AiLeaderboardVOBuilder leagueName(String leagueName) { this.leagueName = leagueName; return this; }
        public AiLeaderboardVOBuilder seasonName(String seasonName) { this.seasonName = seasonName; return this; }
        public AiLeaderboardVOBuilder totalMatches(Integer totalMatches) { this.totalMatches = totalMatches; return this; }
        public AiLeaderboardVOBuilder settledMatches(Integer settledMatches) { this.settledMatches = settledMatches; return this; }
        public AiLeaderboardVOBuilder wonMatches(Integer wonMatches) { this.wonMatches = wonMatches; return this; }
        public AiLeaderboardVOBuilder lostMatches(Integer lostMatches) { this.lostMatches = lostMatches; return this; }
        public AiLeaderboardVOBuilder voidMatches(Integer voidMatches) { this.voidMatches = voidMatches; return this; }
        public AiLeaderboardVOBuilder exactScoreMatches(Integer exactScoreMatches) { this.exactScoreMatches = exactScoreMatches; return this; }
        public AiLeaderboardVOBuilder goalsMatches(Integer goalsMatches) { this.goalsMatches = goalsMatches; return this; }
        public AiLeaderboardVOBuilder winRate(BigDecimal winRate) { this.winRate = winRate; return this; }
        public AiLeaderboardVOBuilder scoreAccuracyRate(BigDecimal scoreAccuracyRate) { this.scoreAccuracyRate = scoreAccuracyRate; return this; }
        public AiLeaderboardVOBuilder goalsAccuracyRate(BigDecimal goalsAccuracyRate) { this.goalsAccuracyRate = goalsAccuracyRate; return this; }
        public AiLeaderboardVOBuilder totalStake(BigDecimal totalStake) { this.totalStake = totalStake; return this; }
        public AiLeaderboardVOBuilder totalReturn(BigDecimal totalReturn) { this.totalReturn = totalReturn; return this; }
        public AiLeaderboardVOBuilder netProfit(BigDecimal netProfit) { this.netProfit = netProfit; return this; }
        public AiLeaderboardVOBuilder roiRate(BigDecimal roiRate) { this.roiRate = roiRate; return this; }
        public AiLeaderboardVOBuilder resultNetProfit(BigDecimal resultNetProfit) { this.resultNetProfit = resultNetProfit; return this; }
        public AiLeaderboardVOBuilder scoreNetProfit(BigDecimal scoreNetProfit) { this.scoreNetProfit = scoreNetProfit; return this; }
        public AiLeaderboardVOBuilder goalsNetProfit(BigDecimal goalsNetProfit) { this.goalsNetProfit = goalsNetProfit; return this; }
        public AiLeaderboardVOBuilder currentStreak(Integer currentStreak) { this.currentStreak = currentStreak; return this; }
        public AiLeaderboardVOBuilder maxWinStreak(Integer maxWinStreak) { this.maxWinStreak = maxWinStreak; return this; }
        public AiLeaderboardVOBuilder recentForm(List<String> recentForm) { this.recentForm = recentForm; return this; }
        public AiLeaderboardVOBuilder lastSettledAt(LocalDateTime lastSettledAt) { this.lastSettledAt = lastSettledAt; return this; }

        public AiLeaderboardVO build() {
            return new AiLeaderboardVO(rank, aiModelCode, aiModelName, provider, scopeType, leagueCode, leagueName, seasonName, totalMatches, settledMatches, wonMatches, lostMatches, voidMatches, exactScoreMatches, goalsMatches, winRate, scoreAccuracyRate, goalsAccuracyRate, totalStake, totalReturn, netProfit, roiRate, resultNetProfit, scoreNetProfit, goalsNetProfit, currentStreak, maxWinStreak, recentForm, lastSettledAt);
        }
    }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }
    public String getAiModelCode() { return aiModelCode; }
    public void setAiModelCode(String aiModelCode) { this.aiModelCode = aiModelCode; }
    public String getAiModelName() { return aiModelName; }
    public void setAiModelName(String aiModelName) { this.aiModelName = aiModelName; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public ScopeTypeEnum getScopeType() { return scopeType; }
    public void setScopeType(ScopeTypeEnum scopeType) { this.scopeType = scopeType; }
    public String getLeagueCode() { return leagueCode; }
    public void setLeagueCode(String leagueCode) { this.leagueCode = leagueCode; }
    public String getLeagueName() { return leagueName; }
    public void setLeagueName(String leagueName) { this.leagueName = leagueName; }
    public String getSeasonName() { return seasonName; }
    public void setSeasonName(String seasonName) { this.seasonName = seasonName; }
    public Integer getTotalMatches() { return totalMatches; }
    public void setTotalMatches(Integer totalMatches) { this.totalMatches = totalMatches; }
    public Integer getSettledMatches() { return settledMatches; }
    public void setSettledMatches(Integer settledMatches) { this.settledMatches = settledMatches; }
    public Integer getWonMatches() { return wonMatches; }
    public void setWonMatches(Integer wonMatches) { this.wonMatches = wonMatches; }
    public Integer getLostMatches() { return lostMatches; }
    public void setLostMatches(Integer lostMatches) { this.lostMatches = lostMatches; }
    public Integer getVoidMatches() { return voidMatches; }
    public void setVoidMatches(Integer voidMatches) { this.voidMatches = voidMatches; }
    public Integer getExactScoreMatches() { return exactScoreMatches; }
    public void setExactScoreMatches(Integer exactScoreMatches) { this.exactScoreMatches = exactScoreMatches; }
    public Integer getGoalsMatches() { return goalsMatches; }
    public void setGoalsMatches(Integer goalsMatches) { this.goalsMatches = goalsMatches; }
    public BigDecimal getWinRate() { return winRate; }
    public void setWinRate(BigDecimal winRate) { this.winRate = winRate; }
    public BigDecimal getScoreAccuracyRate() { return scoreAccuracyRate; }
    public void setScoreAccuracyRate(BigDecimal scoreAccuracyRate) { this.scoreAccuracyRate = scoreAccuracyRate; }
    public BigDecimal getGoalsAccuracyRate() { return goalsAccuracyRate; }
    public void setGoalsAccuracyRate(BigDecimal goalsAccuracyRate) { this.goalsAccuracyRate = goalsAccuracyRate; }
    public BigDecimal getTotalStake() { return totalStake; }
    public void setTotalStake(BigDecimal totalStake) { this.totalStake = totalStake; }
    public BigDecimal getTotalReturn() { return totalReturn; }
    public void setTotalReturn(BigDecimal totalReturn) { this.totalReturn = totalReturn; }
    public BigDecimal getNetProfit() { return netProfit; }
    public void setNetProfit(BigDecimal netProfit) { this.netProfit = netProfit; }
    public BigDecimal getRoiRate() { return roiRate; }
    public void setRoiRate(BigDecimal roiRate) { this.roiRate = roiRate; }
    public BigDecimal getResultNetProfit() { return resultNetProfit; }
    public void setResultNetProfit(BigDecimal resultNetProfit) { this.resultNetProfit = resultNetProfit; }
    public BigDecimal getScoreNetProfit() { return scoreNetProfit; }
    public void setScoreNetProfit(BigDecimal scoreNetProfit) { this.scoreNetProfit = scoreNetProfit; }
    public BigDecimal getGoalsNetProfit() { return goalsNetProfit; }
    public void setGoalsNetProfit(BigDecimal goalsNetProfit) { this.goalsNetProfit = goalsNetProfit; }
    public Integer getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(Integer currentStreak) { this.currentStreak = currentStreak; }
    public Integer getMaxWinStreak() { return maxWinStreak; }
    public void setMaxWinStreak(Integer maxWinStreak) { this.maxWinStreak = maxWinStreak; }
    public List<String> getRecentForm() { return recentForm; }
    public void setRecentForm(List<String> recentForm) { this.recentForm = recentForm; }
    public LocalDateTime getLastSettledAt() { return lastSettledAt; }
    public void setLastSettledAt(LocalDateTime lastSettledAt) { this.lastSettledAt = lastSettledAt; }
}
