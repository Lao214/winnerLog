package com.example.winnerLogBackend.dto.request;

import com.example.winnerLogBackend.enums.ScopeTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "排行榜查询请求参数")
public class LeaderboardQueryRequest {

    @Schema(description = "排行榜维度 (OVERALL:全局总榜, SEASON:赛季总榜, LEAGUE_SEASON:联赛赛季榜, LEAGUE:联赛历史总榜)", example = "LEAGUE_SEASON")
    private ScopeTypeEnum scopeType = ScopeTypeEnum.OVERALL;

    @Schema(description = "赛季名称 (当scopeType为SEASON或LEAGUE_SEASON时传入，如 2024-2025)", example = "2024-2025")
    private String seasonName;

    @Schema(description = "联赛编码 (当scopeType为LEAGUE或LEAGUE_SEASON时传入，如 PL, LA_LIGA, UCL)", example = "PL")
    private String leagueCode;

    @Schema(description = "排序字段 (winRate:胜率, totalMatches:总场次, wonMatches:红单数, currentStreak:连红数, scoreAccuracyRate:比分命中率)", example = "winRate")
    private String sortBy = "winRate";

    @Schema(description = "排序方向 (desc:降序, asc:升序)", example = "desc")
    private String sortOrder = "desc";

    public LeaderboardQueryRequest() {}

    public LeaderboardQueryRequest(ScopeTypeEnum scopeType, String seasonName, String leagueCode, String sortBy, String sortOrder) {
        this.scopeType = scopeType != null ? scopeType : ScopeTypeEnum.OVERALL;
        this.seasonName = seasonName;
        this.leagueCode = leagueCode;
        this.sortBy = sortBy != null ? sortBy : "winRate";
        this.sortOrder = sortOrder != null ? sortOrder : "desc";
    }

    public static LeaderboardQueryRequestBuilder builder() {
        return new LeaderboardQueryRequestBuilder();
    }

    public static class LeaderboardQueryRequestBuilder {
        private ScopeTypeEnum scopeType = ScopeTypeEnum.OVERALL;
        private String seasonName;
        private String leagueCode;
        private String sortBy = "winRate";
        private String sortOrder = "desc";

        public LeaderboardQueryRequestBuilder scopeType(ScopeTypeEnum scopeType) { this.scopeType = scopeType; return this; }
        public LeaderboardQueryRequestBuilder seasonName(String seasonName) { this.seasonName = seasonName; return this; }
        public LeaderboardQueryRequestBuilder leagueCode(String leagueCode) { this.leagueCode = leagueCode; return this; }
        public LeaderboardQueryRequestBuilder sortBy(String sortBy) { this.sortBy = sortBy; return this; }
        public LeaderboardQueryRequestBuilder sortOrder(String sortOrder) { this.sortOrder = sortOrder; return this; }

        public LeaderboardQueryRequest build() {
            return new LeaderboardQueryRequest(scopeType, seasonName, leagueCode, sortBy, sortOrder);
        }
    }

    public ScopeTypeEnum getScopeType() { return scopeType; }
    public void setScopeType(ScopeTypeEnum scopeType) { this.scopeType = scopeType; }
    public String getSeasonName() { return seasonName; }
    public void setSeasonName(String seasonName) { this.seasonName = seasonName; }
    public String getLeagueCode() { return leagueCode; }
    public void setLeagueCode(String leagueCode) { this.leagueCode = leagueCode; }
    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
    public String getSortOrder() { return sortOrder; }
    public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }
}
