package com.example.winnerLogBackend.service;

import com.example.winnerLogBackend.common.BusinessException;
import com.example.winnerLogBackend.dto.request.SettleMatchRequest;
import com.example.winnerLogBackend.entity.AiLeaderboardStat;
import com.example.winnerLogBackend.entity.FootballMatch;
import com.example.winnerLogBackend.entity.MatchPrediction;
import com.example.winnerLogBackend.enums.MatchResultEnum;
import com.example.winnerLogBackend.enums.MatchStatusEnum;
import com.example.winnerLogBackend.enums.ScopeTypeEnum;
import com.example.winnerLogBackend.enums.SettleStatusEnum;
import com.example.winnerLogBackend.repository.AiLeaderboardStatRepository;
import com.example.winnerLogBackend.repository.AiModelRepository;
import com.example.winnerLogBackend.repository.FootballMatchRepository;
import com.example.winnerLogBackend.repository.MatchPredictionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MatchSettlementService {

    private static final Logger log = LoggerFactory.getLogger(MatchSettlementService.class);

    private final FootballMatchRepository matchRepository;
    private final MatchPredictionRepository predictionRepository;
    private final AiLeaderboardStatRepository leaderboardStatRepository;
    private final AiModelRepository aiModelRepository;

    public MatchSettlementService(FootballMatchRepository matchRepository,
                                 MatchPredictionRepository predictionRepository,
                                 AiLeaderboardStatRepository leaderboardStatRepository,
                                 AiModelRepository aiModelRepository) {
        this.matchRepository = matchRepository;
        this.predictionRepository = predictionRepository;
        this.leaderboardStatRepository = leaderboardStatRepository;
        this.aiModelRepository = aiModelRepository;
    }

    /**
     * 结算单场比赛结果，并级联更新所有关联AI的预测状态（胜负、比分最多3个、进球数最多3个）和多维度排行榜
     */
    @Transactional
    public FootballMatch settleMatch(SettleMatchRequest request) {
        FootballMatch match = findMatchByRequest(request);
        if (match == null) {
            throw new BusinessException("未找到待结算的比赛，请检查matchId/matchCode或主客队时间信息");
        }

        return settleMatchResult(
                match,
                request.getHomeScore(),
                request.getAwayScore(),
                request.getHalfHomeScore(),
                request.getHalfAwayScore(),
                request.getStatus() != null ? request.getStatus() : MatchStatusEnum.FINISHED
        );
    }

    /**
     * 执行具体比赛结算与排行榜重算
     */
    @Transactional
    public FootballMatch settleMatchResult(
            FootballMatch match,
            Integer homeScore,
            Integer awayScore,
            Integer halfHomeScore,
            Integer halfAwayScore,
            MatchStatusEnum status) {

        if (homeScore == null || awayScore == null) {
            throw new BusinessException("结算比赛必须提供完整的主客队最终比分");
        }

        MatchResultEnum result = MatchResultEnum.calculateResult(homeScore, awayScore);
        String actualScoreStr = homeScore + "-" + awayScore;
        int actualTotalGoals = homeScore + awayScore;

        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.setHalfHomeScore(halfHomeScore);
        match.setHalfAwayScore(halfAwayScore);
        match.setResult(result);
        match.setStatus(status != null ? status : MatchStatusEnum.FINISHED);
        match.setIsSettled(1);
        match.setSettledAt(LocalDateTime.now());

        FootballMatch savedMatch = matchRepository.save(match);
        log.info("比赛 [{}] {} VS {} 结果已更新为 {}:{} ({}, 总进球:{})",
                match.getMatchCode(), match.getHomeTeam(), match.getAwayTeam(), homeScore, awayScore, result.getDescription(), actualTotalGoals);

        // 1. 结算该场比赛下的所有AI预测 (胜平负、最多3个比分、最多3个进球数，包含赔率与净赚倍数结算)
        List<MatchPrediction> predictions = predictionRepository.findByMatchId(savedMatch.getId());
        Set<String> affectedAiModels = new HashSet<>();

        for (MatchPrediction prediction : predictions) {
            BigDecimal matchStake = BigDecimal.ZERO;
            BigDecimal matchReturn = BigDecimal.ZERO;

            // 1.1 胜负多选项维度结算 (每个胜负选项算 1 注，支持不败双选)
            List<MatchPrediction.ResultItem> resultItems = prediction.getResultItemsList();
            if (resultItems.isEmpty() && prediction.getPredictedResult() != null) {
                resultItems.add(new MatchPrediction.ResultItem(prediction.getPredictedResult(),
                        prediction.getResultOdds() != null ? prediction.getResultOdds() : BigDecimal.valueOf(1.85)));
            }
            BigDecimal resultStake = BigDecimal.valueOf(Math.max(resultItems.size(), 1));
            matchStake = matchStake.add(resultStake);

            boolean isResultWon = false;
            BigDecimal resultReturn = BigDecimal.ZERO;
            for (MatchPrediction.ResultItem item : resultItems) {
                if (item.getResult() == result) {
                    isResultWon = true;
                    BigDecimal odds = item.getOdds() != null ? item.getOdds() : BigDecimal.valueOf(1.85);
                    resultReturn = resultReturn.add(odds);
                }
            }
            prediction.setIsResultCorrect(isResultWon ? 1 : 0);
            prediction.setSettleStatus(isResultWon ? SettleStatusEnum.WON : SettleStatusEnum.LOST);
            prediction.setResultReturn(resultReturn);
            matchReturn = matchReturn.add(resultReturn);

            // 1.2 比分维度结算 (每选1个比分算 1 注，最多3注)
            List<MatchPrediction.ScoreItem> scoreItems = prediction.getScoreItemsList();
            BigDecimal scoreStake = BigDecimal.valueOf(scoreItems.size());
            matchStake = matchStake.add(scoreStake);
            BigDecimal scoreReturn = BigDecimal.ZERO;
            boolean isScoreHit = false;
            String hitScore = null;

            for (MatchPrediction.ScoreItem item : scoreItems) {
                if (item.getScore() != null && item.getScore().trim().equals(actualScoreStr)) {
                    isScoreHit = true;
                    hitScore = actualScoreStr;
                    BigDecimal odds = item.getOdds() != null ? item.getOdds() : BigDecimal.valueOf(7.00);
                    scoreReturn = scoreReturn.add(odds);
                }
            }
            prediction.setIsScoreCorrect(isScoreHit ? 1 : 0);
            prediction.setHitScore(hitScore);
            prediction.setScoreReturn(scoreReturn);
            matchReturn = matchReturn.add(scoreReturn);

            // 1.3 进球数维度结算 (每选1个进球数算 1 注，最多3注)
            List<MatchPrediction.GoalItem> goalItems = prediction.getGoalItemsList();
            BigDecimal goalsStake = BigDecimal.valueOf(goalItems.size());
            matchStake = matchStake.add(goalsStake);
            BigDecimal goalsReturn = BigDecimal.ZERO;
            boolean isGoalsHit = false;
            Integer hitGoals = null;

            for (MatchPrediction.GoalItem item : goalItems) {
                if (item.getGoals() != null && item.getGoals().equals(actualTotalGoals)) {
                    isGoalsHit = true;
                    hitGoals = actualTotalGoals;
                    BigDecimal odds = item.getOdds() != null ? item.getOdds() : BigDecimal.valueOf(3.50);
                    goalsReturn = goalsReturn.add(odds);
                }
            }
            prediction.setIsGoalsCorrect(isGoalsHit ? 1 : 0);
            prediction.setHitGoals(hitGoals);
            prediction.setGoalsReturn(goalsReturn);
            matchReturn = matchReturn.add(goalsReturn);

            // 1.4 计算选项总数、初始利润与最终净利润 (如 8项=-8初始利润，返还10 -> 净赚+2)
            int totalOptionsCount = resultItems.size() + scoreItems.size() + goalItems.size();
            if (totalOptionsCount == 0) {
                totalOptionsCount = 1;
            }
            BigDecimal initialProfit = BigDecimal.valueOf(totalOptionsCount).setScale(2, RoundingMode.HALF_UP).negate();
            BigDecimal netProfit = matchReturn.subtract(matchStake).setScale(2, RoundingMode.HALF_UP);

            prediction.setOptionsCount(totalOptionsCount);
            prediction.setInitialProfit(initialProfit);
            prediction.setStakeAmount(matchStake.setScale(2, RoundingMode.HALF_UP));
            prediction.setReturnAmount(matchReturn.setScale(2, RoundingMode.HALF_UP));
            prediction.setNetProfit(netProfit);

            // 1.5 判定命中等级与重要性 (比分最重要 > 进球数次之 > 胜负最后)
            if (isScoreHit) {
                prediction.setHitImportance(1);
                if (isGoalsHit && isResultWon) {
                    prediction.setHitTier("PERFECT_HIT");
                    prediction.setHitTierDesc("🌟 全中大满贯");
                } else if (isGoalsHit) {
                    prediction.setHitTier("SCORE_GOALS_HIT");
                    prediction.setHitTierDesc("🎯 比分+进球双中");
                } else if (isResultWon) {
                    prediction.setHitTier("SCORE_RESULT_HIT");
                    prediction.setHitTierDesc("🎯 比分+胜负命中");
                } else {
                    prediction.setHitTier("SCORE_HIT");
                    prediction.setHitTierDesc("🎯 精准比分");
                }
            } else if (isGoalsHit) {
                prediction.setHitImportance(2);
                if (isResultWon) {
                    prediction.setHitTier("GOALS_RESULT_HIT");
                    prediction.setHitTierDesc("⚽ 进球数+胜负命中");
                } else {
                    prediction.setHitTier("GOALS_HIT");
                    prediction.setHitTierDesc("⚽ 进球数命中");
                }
            } else if (isResultWon) {
                prediction.setHitImportance(3);
                prediction.setHitTier("RESULT_HIT");
                prediction.setHitTierDesc("🚩 胜负命中");
            } else {
                prediction.setHitImportance(0);
                prediction.setHitTier("MISSED");
                prediction.setHitTierDesc("⚫ 未命中");
            }

            prediction.setSettledAt(LocalDateTime.now());
            predictionRepository.save(prediction);
            affectedAiModels.add(prediction.getAiModelCode());

            log.info("AI模型 [{}] 对比赛 [{}] -> 级别:[{}]({}), 选项:{}个(初始利润:{}), 胜负:[{}](+{}), 比分:[{}](+{}), 进球:[{}](+{}), 本场净利润: {} (返还:{})",
                    prediction.getAiModelCode(), match.getMatchCode(),
                    prediction.getHitImportance(), prediction.getHitTierDesc(),
                    totalOptionsCount, initialProfit,
                    prediction.getSettleStatus(), resultReturn,
                    prediction.getIsScoreCorrect(), scoreReturn,
                    prediction.getIsGoalsCorrect(), goalsReturn,
                    netProfit, matchReturn);
        }

        // 2. 为所有受影响的AI模型更新多维度排行榜数据
        for (String aiModelCode : affectedAiModels) {
            recalculateAiStats(aiModelCode, match.getLeagueId(), match.getLeagueCode(), match.getSeasonId(), match.getSeasonName());
        }

        return savedMatch;
    }

    /**
     * 重算某个AI模型在全维度的统计数据（联赛+赛季、赛季总榜、联赛总榜、全局总榜）
     */
    @Transactional
    public void recalculateAiStats(String aiModelCode, Long leagueId, String leagueCode, Long seasonId, String seasonName) {
        // 维度1: 联赛 + 赛季维度 (LEAGUE_SEASON)
        if (leagueCode != null && seasonName != null) {
            List<MatchPrediction> leagueSeasonPreds = predictionRepository.findRecentSettledPredictionsByLeagueAndSeason(
                    aiModelCode, leagueCode, seasonName);
            updateScopeStat(aiModelCode, ScopeTypeEnum.LEAGUE_SEASON, leagueId, leagueCode, seasonId, seasonName, leagueSeasonPreds);
        }

        // 维度2: 赛季总榜维度 (SEASON)
        if (seasonName != null) {
            List<MatchPrediction> seasonPreds = predictionRepository.findRecentSettledPredictionsBySeason(
                    aiModelCode, seasonName);
            updateScopeStat(aiModelCode, ScopeTypeEnum.SEASON, null, null, seasonId, seasonName, seasonPreds);
        }

        // 维度3: 联赛历史总榜维度 (LEAGUE)
        if (leagueCode != null) {
            List<MatchPrediction> leaguePreds = predictionRepository.findRecentSettledPredictionsByLeague(
                    aiModelCode, leagueCode);
            updateScopeStat(aiModelCode, ScopeTypeEnum.LEAGUE, leagueId, leagueCode, null, null, leaguePreds);
        }

        // 维度4: 全局历史总榜 (OVERALL)
        List<MatchPrediction> allPreds = predictionRepository.findRecentSettledPredictionsByAiModel(aiModelCode);
        updateScopeStat(aiModelCode, ScopeTypeEnum.OVERALL, null, null, null, null, allPreds);
    }

    /**
     * 更新单个维度的排行榜快照
     */
    private void updateScopeStat(
            String aiModelCode,
            ScopeTypeEnum scopeType,
            Long leagueId,
            String leagueCode,
            Long seasonId,
            String seasonName,
            List<MatchPrediction> settledPredictions) {

        AiLeaderboardStat stat = leaderboardStatRepository.findByAiModelCodeAndScopeTypeAndLeagueCodeAndSeasonName(
                aiModelCode, scopeType, leagueCode, seasonName
        ).orElseGet(() -> AiLeaderboardStat.builder()
                .aiModelCode(aiModelCode)
                .scopeType(scopeType)
                .leagueId(leagueId)
                .leagueCode(leagueCode)
                .seasonId(seasonId)
                .seasonName(seasonName)
                .build());

        int totalSettled = settledPredictions.size();
        int won = 0;
        int lost = 0;
        int voidMatches = 0;
        int exactScores = 0;
        int goalsWon = 0;

        BigDecimal totalStakeSum = BigDecimal.ZERO;
        BigDecimal totalReturnSum = BigDecimal.ZERO;
        BigDecimal resultNetProfitSum = BigDecimal.ZERO;
        BigDecimal scoreNetProfitSum = BigDecimal.ZERO;
        BigDecimal goalsNetProfitSum = BigDecimal.ZERO;

        for (MatchPrediction p : settledPredictions) {
            if (p.getSettleStatus() == SettleStatusEnum.WON) {
                won++;
            } else if (p.getSettleStatus() == SettleStatusEnum.LOST) {
                lost++;
            } else if (p.getSettleStatus() == SettleStatusEnum.VOID) {
                voidMatches++;
            }

            if (p.getIsScoreCorrect() != null && p.getIsScoreCorrect() == 1) {
                exactScores++;
            }

            if (p.getIsGoalsCorrect() != null && p.getIsGoalsCorrect() == 1) {
                goalsWon++;
            }

            if (p.getStakeAmount() != null) {
                totalStakeSum = totalStakeSum.add(p.getStakeAmount());
            }
            if (p.getReturnAmount() != null) {
                totalReturnSum = totalReturnSum.add(p.getReturnAmount());
            }
            if (p.getResultReturn() != null) {
                resultNetProfitSum = resultNetProfitSum.add(p.getResultReturn().subtract(BigDecimal.ONE));
            }
            if (p.getScoreReturn() != null) {
                int scoreCount = p.getScoreItemsList().size();
                scoreNetProfitSum = scoreNetProfitSum.add(p.getScoreReturn().subtract(BigDecimal.valueOf(scoreCount)));
            }
            if (p.getGoalsReturn() != null) {
                int goalCount = p.getGoalItemsList().size();
                goalsNetProfitSum = goalsNetProfitSum.add(p.getGoalsReturn().subtract(BigDecimal.valueOf(goalCount)));
            }
        }

        // 计算胜负胜率 (WON / (WON + LOST))
        int validSettled = won + lost;
        BigDecimal winRate = BigDecimal.ZERO;
        if (validSettled > 0) {
            winRate = BigDecimal.valueOf(won)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(validSettled), 2, RoundingMode.HALF_UP);
        }

        // 计算比分命中率
        BigDecimal scoreAccuracy = BigDecimal.ZERO;
        if (totalSettled > 0) {
            scoreAccuracy = BigDecimal.valueOf(exactScores)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalSettled), 2, RoundingMode.HALF_UP);
        }

        // 计算进球数命中率
        BigDecimal goalsAccuracy = BigDecimal.ZERO;
        if (totalSettled > 0) {
            goalsAccuracy = BigDecimal.valueOf(goalsWon)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalSettled), 2, RoundingMode.HALF_UP);
        }

        // 计算净赚倍数与盈利率 (ROI)
        BigDecimal netProfit = totalReturnSum.subtract(totalStakeSum);
        BigDecimal roiRate = BigDecimal.ZERO;
        if (totalStakeSum.compareTo(BigDecimal.ZERO) > 0) {
            roiRate = netProfit.multiply(BigDecimal.valueOf(100)).divide(totalStakeSum, 2, RoundingMode.HALF_UP);
        }

        // 计算连胜/连负 (current_streak: 正数为连红，负数为连黑)
        int currentStreak = 0;
        if (!settledPredictions.isEmpty()) {
            SettleStatusEnum firstStatus = settledPredictions.get(0).getSettleStatus();
            if (firstStatus == SettleStatusEnum.WON) {
                for (MatchPrediction p : settledPredictions) {
                    if (p.getSettleStatus() == SettleStatusEnum.WON) {
                        currentStreak++;
                    } else if (p.getSettleStatus() == SettleStatusEnum.LOST) {
                        break;
                    }
                }
            } else if (firstStatus == SettleStatusEnum.LOST) {
                for (MatchPrediction p : settledPredictions) {
                    if (p.getSettleStatus() == SettleStatusEnum.LOST) {
                        currentStreak--;
                    } else if (p.getSettleStatus() == SettleStatusEnum.WON) {
                        break;
                    }
                }
            }
        }

        // 计算历史最高连红 (max_win_streak)
        int maxWinStreak = 0;
        int tempStreak = 0;
        for (int i = settledPredictions.size() - 1; i >= 0; i--) {
            MatchPrediction p = settledPredictions.get(i);
            if (p.getSettleStatus() == SettleStatusEnum.WON) {
                tempStreak++;
                if (tempStreak > maxWinStreak) {
                    maxWinStreak = tempStreak;
                }
            } else if (p.getSettleStatus() == SettleStatusEnum.LOST) {
                tempStreak = 0;
            }
        }

        stat.setTotalMatches(totalSettled);
        stat.setSettledMatches(totalSettled);
        stat.setWonMatches(won);
        stat.setLostMatches(lost);
        stat.setVoidMatches(voidMatches);
        stat.setExactScoreMatches(exactScores);
        stat.setGoalsMatches(goalsWon);
        stat.setWinRate(winRate);
        stat.setScoreAccuracyRate(scoreAccuracy);
        stat.setGoalsAccuracyRate(goalsAccuracy);

        // 财务指标
        stat.setTotalStake(totalStakeSum);
        stat.setTotalReturn(totalReturnSum);
        stat.setNetProfit(netProfit);
        stat.setRoiRate(roiRate);
        stat.setResultNetProfit(resultNetProfitSum);
        stat.setScoreNetProfit(scoreNetProfitSum);
        stat.setGoalsNetProfit(goalsNetProfitSum);

        stat.setCurrentStreak(currentStreak);
        stat.setMaxWinStreak(maxWinStreak);
        stat.setLastSettledAt(LocalDateTime.now());

        leaderboardStatRepository.save(stat);
    }

    private FootballMatch findMatchByRequest(SettleMatchRequest request) {
        if (request.getMatchId() != null) {
            return matchRepository.findById(request.getMatchId()).orElse(null);
        }
        if (request.getMatchCode() != null && !request.getMatchCode().isBlank()) {
            return matchRepository.findByMatchCode(request.getMatchCode()).orElse(null);
        }
        if (request.getHomeTeam() != null && request.getAwayTeam() != null && request.getMatchTime() != null) {
            return matchRepository.findByHomeTeamAndAwayTeamAndMatchTime(
                    request.getHomeTeam(), request.getAwayTeam(), request.getMatchTime()
            ).orElse(null);
        }
        return null;
    }
}
