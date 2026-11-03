package com.example.winnerLogBackend.entity;

import com.example.winnerLogBackend.enums.MatchResultEnum;
import com.example.winnerLogBackend.enums.MatchStatusEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "football_match", indexes = {
        @Index(name = "uk_match_code", columnList = "match_code", unique = true),
        @Index(name = "idx_match_league_season", columnList = "league_code, season_name"),
        @Index(name = "idx_match_time", columnList = "match_time"),
        @Index(name = "idx_match_status", columnList = "status"),
        @Index(name = "idx_match_settled", columnList = "is_settled"),
        @Index(name = "idx_match_teams", columnList = "home_team, away_team, match_time")
})
public class FootballMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_code", length = 64, unique = true)
    private String matchCode;

    @Column(name = "league_id", nullable = false)
    private Long leagueId;

    @Column(name = "league_code", nullable = false, length = 50)
    private String leagueCode;

    @Column(name = "league_name", nullable = false, length = 100)
    private String leagueName;

    @Column(name = "season_id", nullable = false)
    private Long seasonId;

    @Column(name = "season_name", nullable = false, length = 50)
    private String seasonName;

    @Column(name = "round_name", length = 50)
    private String roundName;

    @Column(name = "home_team", nullable = false, length = 100)
    private String homeTeam;

    @Column(name = "away_team", nullable = false, length = 100)
    private String awayTeam;

    @Column(name = "match_time", nullable = false)
    private LocalDateTime matchTime;

    @Column(name = "venue", length = 100)
    private String venue;

    @Column(name = "home_score")
    private Integer homeScore;

    @Column(name = "away_score")
    private Integer awayScore;

    @Column(name = "half_home_score")
    private Integer halfHomeScore;

    @Column(name = "half_away_score")
    private Integer halfAwayScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", length = 20)
    private MatchResultEnum result;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MatchStatusEnum status = MatchStatusEnum.SCHEDULED;

    @Column(name = "is_settled", nullable = false)
    private Integer isSettled = 0;

    @Column(name = "settled_at")
    private LocalDateTime settledAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public FootballMatch() {}

    public FootballMatch(Long id, String matchCode, Long leagueId, String leagueCode, String leagueName, Long seasonId, String seasonName, String roundName, String homeTeam, String awayTeam, LocalDateTime matchTime, String venue, Integer homeScore, Integer awayScore, Integer halfHomeScore, Integer halfAwayScore, MatchResultEnum result, MatchStatusEnum status, Integer isSettled, LocalDateTime settledAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.matchCode = matchCode;
        this.leagueId = leagueId;
        this.leagueCode = leagueCode;
        this.leagueName = leagueName;
        this.seasonId = seasonId;
        this.seasonName = seasonName;
        this.roundName = roundName;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchTime = matchTime;
        this.venue = venue;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.halfHomeScore = halfHomeScore;
        this.halfAwayScore = halfAwayScore;
        this.result = result;
        this.status = status != null ? status : MatchStatusEnum.SCHEDULED;
        this.isSettled = isSettled != null ? isSettled : 0;
        this.settledAt = settledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static FootballMatchBuilder builder() {
        return new FootballMatchBuilder();
    }

    public static class FootballMatchBuilder {
        private Long id;
        private String matchCode;
        private Long leagueId;
        private String leagueCode;
        private String leagueName;
        private Long seasonId;
        private String seasonName;
        private String roundName;
        private String homeTeam;
        private String awayTeam;
        private LocalDateTime matchTime;
        private String venue;
        private Integer homeScore;
        private Integer awayScore;
        private Integer halfHomeScore;
        private Integer halfAwayScore;
        private MatchResultEnum result;
        private MatchStatusEnum status = MatchStatusEnum.SCHEDULED;
        private Integer isSettled = 0;
        private LocalDateTime settledAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public FootballMatchBuilder id(Long id) { this.id = id; return this; }
        public FootballMatchBuilder matchCode(String matchCode) { this.matchCode = matchCode; return this; }
        public FootballMatchBuilder leagueId(Long leagueId) { this.leagueId = leagueId; return this; }
        public FootballMatchBuilder leagueCode(String leagueCode) { this.leagueCode = leagueCode; return this; }
        public FootballMatchBuilder leagueName(String leagueName) { this.leagueName = leagueName; return this; }
        public FootballMatchBuilder seasonId(Long seasonId) { this.seasonId = seasonId; return this; }
        public FootballMatchBuilder seasonName(String seasonName) { this.seasonName = seasonName; return this; }
        public FootballMatchBuilder roundName(String roundName) { this.roundName = roundName; return this; }
        public FootballMatchBuilder homeTeam(String homeTeam) { this.homeTeam = homeTeam; return this; }
        public FootballMatchBuilder awayTeam(String awayTeam) { this.awayTeam = awayTeam; return this; }
        public FootballMatchBuilder matchTime(LocalDateTime matchTime) { this.matchTime = matchTime; return this; }
        public FootballMatchBuilder venue(String venue) { this.venue = venue; return this; }
        public FootballMatchBuilder homeScore(Integer homeScore) { this.homeScore = homeScore; return this; }
        public FootballMatchBuilder awayScore(Integer awayScore) { this.awayScore = awayScore; return this; }
        public FootballMatchBuilder halfHomeScore(Integer halfHomeScore) { this.halfHomeScore = halfHomeScore; return this; }
        public FootballMatchBuilder halfAwayScore(Integer halfAwayScore) { this.halfAwayScore = halfAwayScore; return this; }
        public FootballMatchBuilder result(MatchResultEnum result) { this.result = result; return this; }
        public FootballMatchBuilder status(MatchStatusEnum status) { this.status = status; return this; }
        public FootballMatchBuilder isSettled(Integer isSettled) { this.isSettled = isSettled; return this; }
        public FootballMatchBuilder settledAt(LocalDateTime settledAt) { this.settledAt = settledAt; return this; }
        public FootballMatchBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public FootballMatchBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public FootballMatch build() {
            return new FootballMatch(id, matchCode, leagueId, leagueCode, leagueName, seasonId, seasonName, roundName, homeTeam, awayTeam, matchTime, venue, homeScore, awayScore, halfHomeScore, halfAwayScore, result, status, isSettled, settledAt, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMatchCode() { return matchCode; }
    public void setMatchCode(String matchCode) { this.matchCode = matchCode; }
    public Long getLeagueId() { return leagueId; }
    public void setLeagueId(Long leagueId) { this.leagueId = leagueId; }
    public String getLeagueCode() { return leagueCode; }
    public void setLeagueCode(String leagueCode) { this.leagueCode = leagueCode; }
    public String getLeagueName() { return leagueName; }
    public void setLeagueName(String leagueName) { this.leagueName = leagueName; }
    public Long getSeasonId() { return seasonId; }
    public void setSeasonId(Long seasonId) { this.seasonId = seasonId; }
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
    public MatchResultEnum getResult() { return result; }
    public void setResult(MatchResultEnum result) { this.result = result; }
    public MatchStatusEnum getStatus() { return status; }
    public void setStatus(MatchStatusEnum status) { this.status = status; }
    public Integer getIsSettled() { return isSettled; }
    public void setIsSettled(Integer isSettled) { this.isSettled = isSettled; }
    public LocalDateTime getSettledAt() { return settledAt; }
    public void setSettledAt(LocalDateTime settledAt) { this.settledAt = settledAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
