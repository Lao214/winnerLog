package com.example.winnerLogBackend.service;

import com.example.winnerLogBackend.dto.request.LeaderboardQueryRequest;
import com.example.winnerLogBackend.dto.response.AiLeaderboardVO;
import com.example.winnerLogBackend.entity.AiLeaderboardStat;
import com.example.winnerLogBackend.entity.AiModel;
import com.example.winnerLogBackend.entity.League;
import com.example.winnerLogBackend.entity.MatchPrediction;
import com.example.winnerLogBackend.enums.ScopeTypeEnum;
import com.example.winnerLogBackend.repository.AiLeaderboardStatRepository;
import com.example.winnerLogBackend.repository.AiModelRepository;
import com.example.winnerLogBackend.repository.LeagueRepository;
import com.example.winnerLogBackend.repository.MatchPredictionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    private static final Logger log = LoggerFactory.getLogger(LeaderboardService.class);

    private final AiLeaderboardStatRepository leaderboardStatRepository;
    private final AiModelRepository aiModelRepository;
    private final LeagueRepository leagueRepository;
    private final MatchPredictionRepository predictionRepository;

    public LeaderboardService(AiLeaderboardStatRepository leaderboardStatRepository,
                              AiModelRepository aiModelRepository,
                              LeagueRepository leagueRepository,
                              MatchPredictionRepository predictionRepository) {
        this.leaderboardStatRepository = leaderboardStatRepository;
        this.aiModelRepository = aiModelRepository;
        this.leagueRepository = leagueRepository;
        this.predictionRepository = predictionRepository;
    }

    /**
     * 查询排行榜（按全局、赛季、联赛赛季、联赛多维度）
     */
    public List<AiLeaderboardVO> getLeaderboard(LeaderboardQueryRequest request) {
        ScopeTypeEnum scopeType = request.getScopeType() != null ? request.getScopeType() : ScopeTypeEnum.OVERALL;
        String seasonName = request.getSeasonName();
        String leagueCode = request.getLeagueCode() != null ? request.getLeagueCode().trim().toUpperCase() : null;

        List<AiLeaderboardStat> statList;
        switch (scopeType) {
            case LEAGUE_SEASON:
                if (seasonName != null && leagueCode != null) {
                    statList = leaderboardStatRepository.findByScopeTypeAndSeasonNameAndLeagueCode(scopeType, seasonName, leagueCode);
                } else if (seasonName != null) {
                    statList = leaderboardStatRepository.findByScopeTypeAndSeasonName(scopeType, seasonName);
                } else if (leagueCode != null) {
                    statList = leaderboardStatRepository.findByScopeTypeAndLeagueCode(scopeType, leagueCode);
                } else {
                    statList = leaderboardStatRepository.findByScopeType(scopeType);
                }
                break;
            case SEASON:
                if (seasonName != null) {
                    statList = leaderboardStatRepository.findByScopeTypeAndSeasonName(scopeType, seasonName);
                } else {
                    statList = leaderboardStatRepository.findByScopeType(scopeType);
                }
                break;
            case LEAGUE:
                if (leagueCode != null) {
                    statList = leaderboardStatRepository.findByScopeTypeAndLeagueCode(scopeType, leagueCode);
                } else {
                    statList = leaderboardStatRepository.findByScopeType(scopeType);
                }
                break;
            case OVERALL:
            default:
                statList = leaderboardStatRepository.findByScopeType(ScopeTypeEnum.OVERALL);
                break;
        }

        Map<String, AiModel> aiModelMap = aiModelRepository.findAll().stream()
                .collect(Collectors.toMap(AiModel::getModelCode, m -> m, (a, b) -> a));
        Map<String, League> leagueMap = leagueRepository.findAll().stream()
                .collect(Collectors.toMap(League::getCode, l -> l, (a, b) -> a));

        List<AiLeaderboardVO> voList = new ArrayList<>();

        for (AiLeaderboardStat stat : statList) {
            AiModel aiModel = aiModelMap.get(stat.getAiModelCode());
            League league = stat.getLeagueCode() != null ? leagueMap.get(stat.getLeagueCode()) : null;

            List<String> recentForm = getRecentForm(stat);

            AiLeaderboardVO vo = AiLeaderboardVO.builder()
                    .aiModelCode(stat.getAiModelCode())
                    .aiModelName(aiModel != null ? aiModel.getModelName() : stat.getAiModelCode())
                    .provider(aiModel != null ? aiModel.getProvider() : null)
                    .scopeType(stat.getScopeType())
                    .leagueCode(stat.getLeagueCode())
                    .leagueName(league != null ? league.getShortName() : (stat.getLeagueCode() != null ? stat.getLeagueCode() : null))
                    .seasonName(stat.getSeasonName())
                    .totalMatches(stat.getTotalMatches())
                    .settledMatches(stat.getSettledMatches())
                    .wonMatches(stat.getWonMatches())
                    .lostMatches(stat.getLostMatches())
                    .voidMatches(stat.getVoidMatches())
                    .exactScoreMatches(stat.getExactScoreMatches())
                    .goalsMatches(stat.getGoalsMatches())
                    .winRate(stat.getWinRate())
                    .scoreAccuracyRate(stat.getScoreAccuracyRate())
                    .goalsAccuracyRate(stat.getGoalsAccuracyRate())
                    .totalStake(stat.getTotalStake())
                    .totalReturn(stat.getTotalReturn())
                    .netProfit(stat.getNetProfit())
                    .roiRate(stat.getRoiRate())
                    .resultNetProfit(stat.getResultNetProfit())
                    .scoreNetProfit(stat.getScoreNetProfit())
                    .goalsNetProfit(stat.getGoalsNetProfit())
                    .currentStreak(stat.getCurrentStreak())
                    .maxWinStreak(stat.getMaxWinStreak())
                    .recentForm(recentForm)
                    .lastSettledAt(stat.getLastSettledAt())
                    .build();

            voList.add(vo);
        }

        sortLeaderboard(voList, request.getSortBy(), request.getSortOrder());

        for (int i = 0; i < voList.size(); i++) {
            voList.get(i).setRank(i + 1);
        }

        return voList;
    }

    private List<String> getRecentForm(AiLeaderboardStat stat) {
        List<MatchPrediction> recentPreds;
        if (stat.getScopeType() == ScopeTypeEnum.LEAGUE_SEASON && stat.getLeagueCode() != null && stat.getSeasonName() != null) {
            recentPreds = predictionRepository.findRecentSettledPredictionsByLeagueAndSeason(
                    stat.getAiModelCode(), stat.getLeagueCode(), stat.getSeasonName());
        } else if (stat.getScopeType() == ScopeTypeEnum.SEASON && stat.getSeasonName() != null) {
            recentPreds = predictionRepository.findRecentSettledPredictionsBySeason(
                    stat.getAiModelCode(), stat.getSeasonName());
        } else if (stat.getScopeType() == ScopeTypeEnum.LEAGUE && stat.getLeagueCode() != null) {
            recentPreds = predictionRepository.findRecentSettledPredictionsByLeague(
                    stat.getAiModelCode(), stat.getLeagueCode());
        } else {
            recentPreds = predictionRepository.findRecentSettledPredictionsByAiModel(stat.getAiModelCode());
        }

        return recentPreds.stream()
                .limit(5)
                .map(p -> p.getSettleStatus() != null ? p.getSettleStatus().name() : "PENDING")
                .collect(Collectors.toList());
    }

    private void sortLeaderboard(List<AiLeaderboardVO> list, String sortBy, String sortOrder) {
        boolean asc = "asc".equalsIgnoreCase(sortOrder);
        Comparator<AiLeaderboardVO> comparator;

        if ("netProfit".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(AiLeaderboardVO::getNetProfit, Comparator.nullsFirst(BigDecimal::compareTo));
        } else if ("roiRate".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(AiLeaderboardVO::getRoiRate, Comparator.nullsFirst(BigDecimal::compareTo));
        } else if ("winRate".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(AiLeaderboardVO::getWinRate, Comparator.nullsFirst(BigDecimal::compareTo));
        } else if ("wonMatches".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(AiLeaderboardVO::getWonMatches);
        } else if ("totalMatches".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(AiLeaderboardVO::getTotalMatches);
        } else if ("currentStreak".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(AiLeaderboardVO::getCurrentStreak);
        } else if ("scoreAccuracyRate".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(AiLeaderboardVO::getScoreAccuracyRate, Comparator.nullsFirst(BigDecimal::compareTo));
        } else if ("goalsAccuracyRate".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(AiLeaderboardVO::getGoalsAccuracyRate, Comparator.nullsFirst(BigDecimal::compareTo));
        } else {
            comparator = Comparator.comparing(AiLeaderboardVO::getNetProfit, Comparator.nullsFirst(BigDecimal::compareTo))
                    .thenComparing(AiLeaderboardVO::getWinRate, Comparator.nullsFirst(BigDecimal::compareTo))
                    .thenComparing(AiLeaderboardVO::getWonMatches);
        }

        if (!asc) {
            comparator = comparator.reversed();
        }

        list.sort(comparator);
    }
}
