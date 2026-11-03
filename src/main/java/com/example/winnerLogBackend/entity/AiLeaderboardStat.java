package com.example.winnerLogBackend.entity;

import com.example.winnerLogBackend.enums.ScopeTypeEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_leaderboard_stat", uniqueConstraints = {
        @UniqueConstraint(name = "uk_ai_scope_stat", columnNames = {"ai_model_code", "scope_type", "league_code", "season_name"})
}, indexes = {
        @Index(name = "idx_leaderboard_rank", columnList = "scope_type, season_name, league_code, win_rate, won_matches")
})
public class AiLeaderboardStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ai_model_code", nullable = false, length = 50)
    private String aiModelCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope_type", nullable = false, length = 30)
    private ScopeTypeEnum scopeType;

    @Column(name = "league_id")
    private Long leagueId;

    @Column(name = "league_code", length = 50)
    private String leagueCode;

    @Column(name = "season_id")
    private Long seasonId;

    @Column(name = "season_name", length = 50)
    private String seasonName;

    @Column(name = "total_matches", nullable = false)
    private Integer totalMatches = 0;

    @Column(name = "settled_matches", nullable = false)
    private Integer settledMatches = 0;

    @Column(name = "won_matches", nullable = false)
    private Integer wonMatches = 0;

    @Column(name = "lost_matches", nullable = false)
    private Integer lostMatches = 0;

    @Column(name = "void_matches", nullable = false)
    private Integer voidMatches = 0;

    @Column(name = "exact_score_matches", nullable = false)
    private Integer exactScoreMatches = 0;

    @Column(name = "goals_matches", nullable = false)
    private Integer goalsMatches = 0;

    @Column(name = "win_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal winRate = BigDecimal.ZERO;

    @Column(name = "score_accuracy_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal scoreAccuracyRate = BigDecimal.ZERO;

    @Column(name = "goals_accuracy_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal goalsAccuracyRate = BigDecimal.ZERO;

    @Column(name = "total_stake", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalStake = BigDecimal.ZERO;

    @Column(name = "total_return", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalReturn = BigDecimal.ZERO;

    @Column(name = "net_profit", nullable = false, precision = 10, scale = 2)
    private BigDecimal netProfit = BigDecimal.ZERO;

    @Column(name = "roi_rate", nullable = false, precision = 6, scale = 2)
    private BigDecimal roiRate = BigDecimal.ZERO;

    @Column(name = "result_net_profit", precision = 10, scale = 2)
    private BigDecimal resultNetProfit = BigDecimal.ZERO;

    @Column(name = "score_net_profit", precision = 10, scale = 2)
    private BigDecimal scoreNetProfit = BigDecimal.ZERO;

    @Column(name = "goals_net_profit", precision = 10, scale = 2)
    private BigDecimal goalsNetProfit = BigDecimal.ZERO;

    @Column(name = "current_streak", nullable = false)
    private Integer currentStreak = 0;

    @Column(name = "max_win_streak", nullable = false)
    private Integer maxWinStreak = 0;

    @Column(name = "last_settled_at")
    private LocalDateTime lastSettledAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public AiLeaderboardStat() {}

    public AiLeaderboardStat(Long id, String aiModelCode, ScopeTypeEnum scopeType, Long leagueId, String leagueCode, Long seasonId, String seasonName, Integer totalMatches, Integer settledMatches, Integer wonMatches, Integer lostMatches, Integer voidMatches, Integer exactScoreMatches, Integer goalsMatches, BigDecimal winRate, BigDecimal scoreAccuracyRate, BigDecimal goalsAccuracyRate, BigDecimal totalStake, BigDecimal totalReturn, BigDecimal netProfit, BigDecimal roiRate, BigDecimal resultNetProfit, BigDecimal scoreNetProfit, BigDecimal goalsNetProfit, Integer currentStreak, Integer maxWinStreak, LocalDateTime lastSettledAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.aiModelCode = aiModelCode;
        this.scopeType = scopeType;
        this.leagueId = leagueId;
        this.leagueCode = leagueCode;
        this.seasonId = seasonId;
        this.seasonName = seasonName;
        this.totalMatches = totalMatches != null ? totalMatches : 0;
        this.settledMatches = settledMatches != null ? settledMatches : 0;
        this.wonMatches = wonMatches != null ? wonMatches : 0;
        this.lostMatches = lostMatches != null ? lostMatches : 0;
        this.voidMatches = voidMatches != null ? voidMatches : 0;
        this.exactScoreMatches = exactScoreMatches != null ? exactScoreMatches : 0;
        this.goalsMatches = goalsMatches != null ? goalsMatches : 0;
        this.winRate = winRate != null ? winRate : BigDecimal.ZERO;
        this.scoreAccuracyRate = scoreAccuracyRate != null ? scoreAccuracyRate : BigDecimal.ZERO;
        this.goalsAccuracyRate = goalsAccuracyRate != null ? goalsAccuracyRate : BigDecimal.ZERO;
        this.totalStake = totalStake != null ? totalStake : BigDecimal.ZERO;
        this.totalReturn = totalReturn != null ? totalReturn : BigDecimal.ZERO;
        this.netProfit = netProfit != null ? netProfit : BigDecimal.ZERO;
        this.roiRate = roiRate != null ? roiRate : BigDecimal.ZERO;
        this.resultNetProfit = resultNetProfit != null ? resultNetProfit : BigDecimal.ZERO;
        this.scoreNetProfit = scoreNetProfit != null ? scoreNetProfit : BigDecimal.ZERO;
        this.goalsNetProfit = goalsNetProfit != null ? goalsNetProfit : BigDecimal.ZERO;
        this.currentStreak = currentStreak != null ? currentStreak : 0;
        this.maxWinStreak = maxWinStreak != null ? maxWinStreak : 0;
        this.lastSettledAt = lastSettledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AiLeaderboardStatBuilder builder() {
        return new AiLeaderboardStatBuilder();
    }

    public static class AiLeaderboardStatBuilder {
        private Long id;
        private String aiModelCode;
        private ScopeTypeEnum scopeType;
        private Long leagueId;
        private String leagueCode;
        private Long seasonId;
        private String seasonName;
        private Integer totalMatches = 0;
        private Integer settledMatches = 0;
        private Integer wonMatches = 0;
        private Integer lostMatches = 0;
        private Integer voidMatches = 0;
        private Integer exactScoreMatches = 0;
        private Integer goalsMatches = 0;
        private BigDecimal winRate = BigDecimal.ZERO;
        private BigDecimal scoreAccuracyRate = BigDecimal.ZERO;
        private BigDecimal goalsAccuracyRate = BigDecimal.ZERO;
        private BigDecimal totalStake = BigDecimal.ZERO;
        private BigDecimal totalReturn = BigDecimal.ZERO;
        private BigDecimal netProfit = BigDecimal.ZERO;
        private BigDecimal roiRate = BigDecimal.ZERO;
        private BigDecimal resultNetProfit = BigDecimal.ZERO;
        private BigDecimal scoreNetProfit = BigDecimal.ZERO;
        private BigDecimal goalsNetProfit = BigDecimal.ZERO;
        private Integer currentStreak = 0;
        private Integer maxWinStreak = 0;
        private LocalDateTime lastSettledAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public AiLeaderboardStatBuilder id(Long id) { this.id = id; return this; }
        public AiLeaderboardStatBuilder aiModelCode(String aiModelCode) { this.aiModelCode = aiModelCode; return this; }
        public AiLeaderboardStatBuilder scopeType(ScopeTypeEnum scopeType) { this.scopeType = scopeType; return this; }
        public AiLeaderboardStatBuilder leagueId(Long leagueId) { this.leagueId = leagueId; return this; }
        public AiLeaderboardStatBuilder leagueCode(String leagueCode) { this.leagueCode = leagueCode; return this; }
        public AiLeaderboardStatBuilder seasonId(Long seasonId) { this.seasonId = seasonId; return this; }
        public AiLeaderboardStatBuilder seasonName(String seasonName) { this.seasonName = seasonName; return this; }
        public AiLeaderboardStatBuilder totalMatches(Integer totalMatches) { this.totalMatches = totalMatches; return this; }
        public AiLeaderboardStatBuilder settledMatches(Integer settledMatches) { this.settledMatches = settledMatches; return this; }
        public AiLeaderboardStatBuilder wonMatches(Integer wonMatches) { this.wonMatches = wonMatches; return this; }
        public AiLeaderboardStatBuilder lostMatches(Integer lostMatches) { this.lostMatches = lostMatches; return this; }
        public AiLeaderboardStatBuilder voidMatches(Integer voidMatches) { this.voidMatches = voidMatches; return this; }
        public AiLeaderboardStatBuilder exactScoreMatches(Integer exactScoreMatches) { this.exactScoreMatches = exactScoreMatches; return this; }
        public AiLeaderboardStatBuilder goalsMatches(Integer goalsMatches) { this.goalsMatches = goalsMatches; return this; }
        public AiLeaderboardStatBuilder winRate(BigDecimal winRate) { this.winRate = winRate; return this; }
        public AiLeaderboardStatBuilder scoreAccuracyRate(BigDecimal scoreAccuracyRate) { this.scoreAccuracyRate = scoreAccuracyRate; return this; }
        public AiLeaderboardStatBuilder goalsAccuracyRate(BigDecimal goalsAccuracyRate) { this.goalsAccuracyRate = goalsAccuracyRate; return this; }
        public AiLeaderboardStatBuilder totalStake(BigDecimal totalStake) { this.totalStake = totalStake; return this; }
        public AiLeaderboardStatBuilder totalReturn(BigDecimal totalReturn) { this.totalReturn = totalReturn; return this; }
        public AiLeaderboardStatBuilder netProfit(BigDecimal netProfit) { this.netProfit = netProfit; return this; }
        public AiLeaderboardStatBuilder roiRate(BigDecimal roiRate) { this.roiRate = roiRate; return this; }
        public AiLeaderboardStatBuilder resultNetProfit(BigDecimal resultNetProfit) { this.resultNetProfit = resultNetProfit; return this; }
        public AiLeaderboardStatBuilder scoreNetProfit(BigDecimal scoreNetProfit) { this.scoreNetProfit = scoreNetProfit; return this; }
        public AiLeaderboardStatBuilder goalsNetProfit(BigDecimal goalsNetProfit) { this.goalsNetProfit = goalsNetProfit; return this; }
        public AiLeaderboardStatBuilder currentStreak(Integer currentStreak) { this.currentStreak = currentStreak; return this; }
        public AiLeaderboardStatBuilder maxWinStreak(Integer maxWinStreak) { this.maxWinStreak = maxWinStreak; return this; }
        public AiLeaderboardStatBuilder lastSettledAt(LocalDateTime lastSettledAt) { this.lastSettledAt = lastSettledAt; return this; }
        public AiLeaderboardStatBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AiLeaderboardStatBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public AiLeaderboardStat build() {
            return new AiLeaderboardStat(id, aiModelCode, scopeType, leagueId, leagueCode, seasonId, seasonName, totalMatches, settledMatches, wonMatches, lostMatches, voidMatches, exactScoreMatches, goalsMatches, winRate, scoreAccuracyRate, goalsAccuracyRate, totalStake, totalReturn, netProfit, roiRate, resultNetProfit, scoreNetProfit, goalsNetProfit, currentStreak, maxWinStreak, lastSettledAt, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAiModelCode() { return aiModelCode; }
    public void setAiModelCode(String aiModelCode) { this.aiModelCode = aiModelCode; }
    public ScopeTypeEnum getScopeType() { return scopeType; }
    public void setScopeType(ScopeTypeEnum scopeType) { this.scopeType = scopeType; }
    public Long getLeagueId() { return leagueId; }
    public void setLeagueId(Long leagueId) { this.leagueId = leagueId; }
    public String getLeagueCode() { return leagueCode; }
    public void setLeagueCode(String leagueCode) { this.leagueCode = leagueCode; }
    public Long getSeasonId() { return seasonId; }
    public void setSeasonId(Long seasonId) { this.seasonId = seasonId; }
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
    public LocalDateTime getLastSettledAt() { return lastSettledAt; }
    public void setLastSettledAt(LocalDateTime lastSettledAt) { this.lastSettledAt = lastSettledAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
