package com.example.winnerLogBackend.service;

import com.example.winnerLogBackend.dto.request.LeaderboardQueryRequest;
import com.example.winnerLogBackend.dto.response.AiComparisonVO;
import com.example.winnerLogBackend.dto.response.AiLeaderboardVO;
import com.example.winnerLogBackend.entity.AiLeaderboardStat;
import com.example.winnerLogBackend.entity.League;
import com.example.winnerLogBackend.entity.MatchPrediction;
import com.example.winnerLogBackend.enums.ScopeTypeEnum;
import com.example.winnerLogBackend.enums.SettleStatusEnum;
import com.example.winnerLogBackend.repository.AiLeaderboardStatRepository;
import com.example.winnerLogBackend.repository.FootballMatchRepository;
import com.example.winnerLogBackend.repository.LeagueRepository;
import com.example.winnerLogBackend.repository.MatchPredictionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AiStatisticsService {

    private static final Logger log = LoggerFactory.getLogger(AiStatisticsService.class);

    private final LeaderboardService leaderboardService;
    private final AiLeaderboardStatRepository leaderboardStatRepository;
    private final LeagueRepository leagueRepository;
    private final MatchPredictionRepository predictionRepository;
    private final FootballMatchRepository matchRepository;

    public AiStatisticsService(LeaderboardService leaderboardService,
                               AiLeaderboardStatRepository leaderboardStatRepository,
                               LeagueRepository leagueRepository,
                               MatchPredictionRepository predictionRepository,
                               FootballMatchRepository matchRepository) {
        this.leaderboardService = leaderboardService;
        this.leaderboardStatRepository = leaderboardStatRepository;
        this.leagueRepository = leagueRepository;
        this.predictionRepository = predictionRepository;
        this.matchRepository = matchRepository;
    }

    /**
     * GPT 与 千问 (或任意两个AI模型) 的深度对决与统计对比
     */
    public AiComparisonVO compareTwoModels(String modelACode, String modelBCode, String seasonName) {
        String codeA = (modelACode != null && !modelACode.isBlank()) ? modelACode.trim().toUpperCase() : "GPT";
        String codeB = (modelBCode != null && !modelBCode.isBlank()) ? modelBCode.trim().toUpperCase() : "QWEN";

        // 1. 获取两模型的基础统计
        ScopeTypeEnum scopeType = seasonName != null ? ScopeTypeEnum.SEASON : ScopeTypeEnum.OVERALL;
        LeaderboardQueryRequest statReq = LeaderboardQueryRequest.builder()
                .scopeType(scopeType)
                .seasonName(seasonName)
                .build();
        List<AiLeaderboardVO> leaderboard = leaderboardService.getLeaderboard(statReq);

        AiLeaderboardVO statA = leaderboard.stream()
                .filter(v -> codeA.equalsIgnoreCase(v.getAiModelCode()))
                .findFirst()
                .orElse(null);

        AiLeaderboardVO statB = leaderboard.stream()
                .filter(v -> codeB.equalsIgnoreCase(v.getAiModelCode()))
                .findFirst()
                .orElse(null);

        // 2. 查询两模型均已预测且已完赛结算的比赛
        List<MatchPrediction> predsA = predictionRepository.findByAiModelCodeAndSettleStatus(codeA, SettleStatusEnum.WON);
        predsA.addAll(predictionRepository.findByAiModelCodeAndSettleStatus(codeA, SettleStatusEnum.LOST));

        List<MatchPrediction> predsB = predictionRepository.findByAiModelCodeAndSettleStatus(codeB, SettleStatusEnum.WON);
        predsB.addAll(predictionRepository.findByAiModelCodeAndSettleStatus(codeB, SettleStatusEnum.LOST));

        Map<Long, MatchPrediction> mapA = predsA.stream()
                .collect(Collectors.toMap(MatchPrediction::getMatchId, p -> p, (p1, p2) -> p1));
        Map<Long, MatchPrediction> mapB = predsB.stream()
                .collect(Collectors.toMap(MatchPrediction::getMatchId, p -> p, (p1, p2) -> p1));

        Set<Long> jointMatchIds = new HashSet<>(mapA.keySet());
        jointMatchIds.retainAll(mapB.keySet());

        int jointTotal = jointMatchIds.size();
        int agreeCount = 0;
        int agreeWonCount = 0;
        int disagreeCount = 0;
        int aWonDisagree = 0;
        int bWonDisagree = 0;
        int bothLostDisagree = 0;

        for (Long matchId : jointMatchIds) {
            MatchPrediction pA = mapA.get(matchId);
            MatchPrediction pB = mapB.get(matchId);

            boolean samePrediction = pA.getPredictedResult() == pB.getPredictedResult();
            if (samePrediction) {
                agreeCount++;
                if (pA.getSettleStatus() == SettleStatusEnum.WON) {
                    agreeWonCount++;
                }
            } else {
                disagreeCount++;
                if (pA.getSettleStatus() == SettleStatusEnum.WON && pB.getSettleStatus() == SettleStatusEnum.LOST) {
                    aWonDisagree++;
                } else if (pB.getSettleStatus() == SettleStatusEnum.WON && pA.getSettleStatus() == SettleStatusEnum.LOST) {
                    bWonDisagree++;
                } else if (pA.getSettleStatus() == SettleStatusEnum.LOST && pB.getSettleStatus() == SettleStatusEnum.LOST) {
                    bothLostDisagree++;
                }
            }
        }

        BigDecimal agreeRate = BigDecimal.ZERO;
        if (jointTotal > 0) {
            agreeRate = BigDecimal.valueOf(agreeCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(jointTotal), 2, RoundingMode.HALF_UP);
        }

        // 3. 各大联赛分项对比 (五大联赛 + 欧冠 + 世界杯 + 欧洲杯)
        List<League> leagues = leagueRepository.findByStatusOrderBySortOrderAsc(1);
        List<AiComparisonVO.LeagueComparisonItem> leagueBreakdown = new ArrayList<>();

        for (League league : leagues) {
            Optional<AiLeaderboardStat> statItemA = seasonName != null
                    ? leaderboardStatRepository.findByAiModelCodeAndScopeTypeAndLeagueCodeAndSeasonName(
                            codeA, ScopeTypeEnum.LEAGUE_SEASON, league.getCode(), seasonName)
                    : leaderboardStatRepository.findByAiModelCodeAndScopeTypeAndLeagueCodeAndSeasonName(
                            codeA, ScopeTypeEnum.LEAGUE, league.getCode(), null);

            Optional<AiLeaderboardStat> statItemB = seasonName != null
                    ? leaderboardStatRepository.findByAiModelCodeAndScopeTypeAndLeagueCodeAndSeasonName(
                            codeB, ScopeTypeEnum.LEAGUE_SEASON, league.getCode(), seasonName)
                    : leaderboardStatRepository.findByAiModelCodeAndScopeTypeAndLeagueCodeAndSeasonName(
                            codeB, ScopeTypeEnum.LEAGUE, league.getCode(), null);

            BigDecimal winRateA = statItemA.map(AiLeaderboardStat::getWinRate).orElse(BigDecimal.ZERO);
            BigDecimal netProfitA = statItemA.map(AiLeaderboardStat::getNetProfit).orElse(BigDecimal.ZERO);
            int settledA = statItemA.map(AiLeaderboardStat::getSettledMatches).orElse(0);

            BigDecimal winRateB = statItemB.map(AiLeaderboardStat::getWinRate).orElse(BigDecimal.ZERO);
            BigDecimal netProfitB = statItemB.map(AiLeaderboardStat::getNetProfit).orElse(BigDecimal.ZERO);
            int settledB = statItemB.map(AiLeaderboardStat::getSettledMatches).orElse(0);

            String leader = "TIE";
            if (netProfitA.compareTo(netProfitB) > 0) {
                leader = codeA;
            } else if (netProfitB.compareTo(netProfitA) > 0) {
                leader = codeB;
            } else if (winRateA.compareTo(winRateB) > 0) {
                leader = codeA;
            } else if (winRateB.compareTo(winRateA) > 0) {
                leader = codeB;
            }

            leagueBreakdown.add(AiComparisonVO.LeagueComparisonItem.builder()
                    .leagueCode(league.getCode())
                    .leagueName(league.getShortName())
                    .modelASettledMatches(settledA)
                    .modelAWinRate(winRateA)
                    .modelANetProfit(netProfitA)
                    .modelBSettledMatches(settledB)
                    .modelBWinRate(winRateB)
                    .modelBNetProfit(netProfitB)
                    .leaderModel(leader)
                    .build());
        }

        return AiComparisonVO.builder()
                .modelAStat(statA)
                .modelBStat(statB)
                .jointSettledMatches(jointTotal)
                .agreeMatches(agreeCount)
                .agreeWonMatches(agreeWonCount)
                .agreeRate(agreeRate)
                .disagreeMatches(disagreeCount)
                .modelAWonDisagreeMatches(aWonDisagree)
                .modelBWonDisagreeMatches(bWonDisagree)
                .bothLostDisagreeMatches(bothLostDisagree)
                .leagueBreakdown(leagueBreakdown)
                .build();
    }
}
