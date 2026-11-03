package com.example.winnerLogBackend.service;

import com.example.winnerLogBackend.common.BusinessException;
import com.example.winnerLogBackend.dto.request.BatchImportPredictionRequest;
import com.example.winnerLogBackend.dto.request.ImportPredictionRequest;
import com.example.winnerLogBackend.dto.request.SingleAiPredictionDTO;
import com.example.winnerLogBackend.dto.response.MatchDetailVO;
import com.example.winnerLogBackend.dto.response.MatchPredictionVO;
import com.example.winnerLogBackend.entity.*;
import com.example.winnerLogBackend.enums.LeagueCategoryEnum;
import com.example.winnerLogBackend.enums.MatchStatusEnum;
import com.example.winnerLogBackend.enums.SettleStatusEnum;
import com.example.winnerLogBackend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PredictionImportService {

    private static final Logger log = LoggerFactory.getLogger(PredictionImportService.class);

    private final LeagueRepository leagueRepository;
    private final SeasonRepository seasonRepository;
    private final AiModelRepository aiModelRepository;
    private final FootballMatchRepository matchRepository;
    private final MatchPredictionRepository predictionRepository;
    private final MatchSettlementService matchSettlementService;

    public PredictionImportService(LeagueRepository leagueRepository,
                                  SeasonRepository seasonRepository,
                                  AiModelRepository aiModelRepository,
                                  FootballMatchRepository matchRepository,
                                  MatchPredictionRepository predictionRepository,
                                  MatchSettlementService matchSettlementService) {
        this.leagueRepository = leagueRepository;
        this.seasonRepository = seasonRepository;
        this.aiModelRepository = aiModelRepository;
        this.matchRepository = matchRepository;
        this.predictionRepository = predictionRepository;
        this.matchSettlementService = matchSettlementService;
    }

    /**
     * 导入单场比赛的AI预测数据（支持未开赛建档与完赛自动结算，包含胜负、最多3个比分、最多3个进球数）
     */
    @Transactional
    public MatchDetailVO importSinglePrediction(ImportPredictionRequest request) {
        // 1. 校验并获取联赛信息
        String leagueCode = request.getLeagueCode().trim().toUpperCase();
        League league = leagueRepository.findByCode(leagueCode)
                .orElseGet(() -> {
                    log.info("未找到联赛 [{}], 自动创建默认联赛记录", leagueCode);
                    return leagueRepository.save(League.builder()
                            .code(leagueCode)
                            .name(leagueCode)
                            .shortName(leagueCode)
                            .category(LeagueCategoryEnum.OTHER)
                            .status(1)
                            .sortOrder(99)
                            .build());
                });

        // 2. 校验并获取赛季信息
        String seasonName = request.getSeasonName();
        if (seasonName == null || seasonName.isBlank()) {
            seasonName = seasonRepository.findByLeagueCodeAndIsCurrent(leagueCode, 1)
                    .map(Season::getName)
                    .orElse("2024-2025");
        }
        final String finalSeasonName = seasonName;
        Season season = seasonRepository.findByLeagueCodeAndName(leagueCode, finalSeasonName)
                .orElseGet(() -> {
                    log.info("联赛 [{}] 未找到赛季 [{}], 自动创建赛季记录", leagueCode, finalSeasonName);
                    return seasonRepository.save(Season.builder()
                            .leagueId(league.getId())
                            .leagueCode(leagueCode)
                            .name(finalSeasonName)
                            .isCurrent(1)
                            .status(1)
                            .build());
                });

        // 3. 查找或创建足球比赛档案
        FootballMatch match = findOrCreateMatch(request, league, season);

        // 4. 保存/更新传入的各AI模型预测记录 (如 GPT, 千问等)
        for (SingleAiPredictionDTO predDTO : request.getPredictions()) {
            saveOrUpdateAiPrediction(match, predDTO);
        }

        // 5. 如果导入请求中已包含比赛得分，或者比赛标记为已结束，立即触发结算
        if (request.getHomeScore() != null && request.getAwayScore() != null) {
            match = matchSettlementService.settleMatchResult(
                    match,
                    request.getHomeScore(),
                    request.getAwayScore(),
                    request.getHalfHomeScore(),
                    request.getHalfAwayScore(),
                    MatchStatusEnum.FINISHED
            );
        }

        // 6. 构造详情VO返回
        return buildMatchDetailVO(match);
    }

    /**
     * 批量导入多场比赛AI预测
     */
    @Transactional
    public List<MatchDetailVO> batchImportPredictions(BatchImportPredictionRequest request) {
        if (request.getMatches() == null || request.getMatches().isEmpty()) {
            throw new BusinessException("批量导入的比赛列表不能为空");
        }

        List<MatchDetailVO> resultList = new ArrayList<>();
        for (ImportPredictionRequest singleReq : request.getMatches()) {
            resultList.add(importSinglePrediction(singleReq));
        }
        return resultList;
    }

    private FootballMatch findOrCreateMatch(ImportPredictionRequest request, League league, Season season) {
        Optional<FootballMatch> existingMatch = Optional.empty();

        if (request.getMatchCode() != null && !request.getMatchCode().isBlank()) {
            existingMatch = matchRepository.findByMatchCode(request.getMatchCode().trim());
        }

        if (existingMatch.isEmpty()) {
            existingMatch = matchRepository.findByHomeTeamAndAwayTeamAndMatchTime(
                    request.getHomeTeam().trim(), request.getAwayTeam().trim(), request.getMatchTime()
            );
        }

        if (existingMatch.isPresent()) {
            FootballMatch match = existingMatch.get();
            if (request.getRoundName() != null && !request.getRoundName().isBlank()) {
                match.setRoundName(request.getRoundName());
            }
            if (request.getVenue() != null && !request.getVenue().isBlank()) {
                match.setVenue(request.getVenue());
            }
            return matchRepository.save(match);
        }

        // 创建新比赛档案
        String matchCode = request.getMatchCode();
        if (matchCode == null || matchCode.isBlank()) {
            String timeStr = request.getMatchTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            matchCode = String.format("%s_%s_%s_%s_%s",
                    league.getCode(), season.getName(), request.getHomeTeam().trim(), request.getAwayTeam().trim(), timeStr);
        }

        FootballMatch newMatch = FootballMatch.builder()
                .matchCode(matchCode)
                .leagueId(league.getId())
                .leagueCode(league.getCode())
                .leagueName(league.getName())
                .seasonId(season.getId())
                .seasonName(season.getName())
                .roundName(request.getRoundName())
                .homeTeam(request.getHomeTeam().trim())
                .awayTeam(request.getAwayTeam().trim())
                .matchTime(request.getMatchTime())
                .venue(request.getVenue())
                .status(MatchStatusEnum.SCHEDULED)
                .isSettled(0)
                .build();

        return matchRepository.save(newMatch);
    }

    private void saveOrUpdateAiPrediction(FootballMatch match, SingleAiPredictionDTO predDTO) {
        String modelCode = predDTO.getModelCode().trim().toUpperCase();

        AiModel aiModel = aiModelRepository.findByModelCode(modelCode)
                .orElseGet(() -> {
                    log.info("未找到AI模型 [{}], 自动创建记录", modelCode);
                    return aiModelRepository.save(AiModel.builder()
                            .modelCode(modelCode)
                            .modelName(modelCode)
                            .status(1)
                            .build());
                });

        MatchPrediction prediction = predictionRepository.findByMatchIdAndAiModelCode(match.getId(), modelCode)
                .orElseGet(() -> MatchPrediction.builder()
                        .matchId(match.getId())
                        .aiModelId(aiModel.getId())
                        .aiModelCode(modelCode)
                        .settleStatus(SettleStatusEnum.PENDING)
                        .build());

        // 处理预测胜负多选项 (优先 resultItems 结构，含赛果及各自赔率，支持不败双选)
        if (predDTO.getResultItems() != null && !predDTO.getResultItems().isEmpty()) {
            List<MatchPrediction.ResultItem> items = predDTO.getResultItems().stream()
                    .map(dto -> new MatchPrediction.ResultItem(dto.getResult(), dto.getOdds() != null ? dto.getOdds() : BigDecimal.valueOf(1.85)))
                    .collect(Collectors.toList());
            prediction.setResultItemsList(items);
        } else if (predDTO.getPredictedResults() != null && !predDTO.getPredictedResults().isEmpty()) {
            BigDecimal defOdds = predDTO.getResultOdds() != null ? predDTO.getResultOdds() : BigDecimal.valueOf(1.85);
            List<MatchPrediction.ResultItem> items = predDTO.getPredictedResults().stream()
                    .map(r -> new MatchPrediction.ResultItem(r, defOdds))
                    .collect(Collectors.toList());
            prediction.setResultItemsList(items);
        } else if (predDTO.getPredictedResult() != null) {
            prediction.setPredictedResult(predDTO.getPredictedResult());
            prediction.setResultOdds(predDTO.getResultOdds() != null ? predDTO.getResultOdds() :
                    (predDTO.getPredictedOdds() != null ? predDTO.getPredictedOdds() : BigDecimal.valueOf(1.85)));
            prediction.setResultItemsList(List.of(new MatchPrediction.ResultItem(prediction.getPredictedResult(), prediction.getResultOdds())));
        }

        // 处理预测比分 (优先 scoreItems 结构，含比分及各自赔率)
        if (predDTO.getScoreItems() != null && !predDTO.getScoreItems().isEmpty()) {
            List<MatchPrediction.ScoreItem> items = predDTO.getScoreItems().stream()
                    .map(dto -> new MatchPrediction.ScoreItem(dto.getScore(), dto.getOdds() != null ? dto.getOdds() : BigDecimal.valueOf(7.00)))
                    .collect(Collectors.toList());
            prediction.setScoreItemsList(items);
        } else if (predDTO.getPredictedScores() != null && !predDTO.getPredictedScores().isEmpty()) {
            prediction.setPredictedScoresList(predDTO.getPredictedScores());
        } else if (predDTO.getPredictedHomeScore() != null && predDTO.getPredictedAwayScore() != null) {
            prediction.setPredictedScoresList(List.of(predDTO.getPredictedHomeScore() + "-" + predDTO.getPredictedAwayScore()));
        }

        // 处理预测总进球数 (优先 goalItems 结构，含进球数及各自赔率)
        if (predDTO.getGoalItems() != null && !predDTO.getGoalItems().isEmpty()) {
            List<MatchPrediction.GoalItem> items = predDTO.getGoalItems().stream()
                    .map(dto -> new MatchPrediction.GoalItem(dto.getGoals(), dto.getOdds() != null ? dto.getOdds() : BigDecimal.valueOf(3.50)))
                    .collect(Collectors.toList());
            prediction.setGoalItemsList(items);
        } else if (predDTO.getPredictedGoals() != null && !predDTO.getPredictedGoals().isEmpty()) {
            prediction.setPredictedGoalsList(predDTO.getPredictedGoals());
        }

        prediction.setPredictedHomeScore(predDTO.getPredictedHomeScore());
        prediction.setPredictedAwayScore(predDTO.getPredictedAwayScore());
        prediction.setConfidence(predDTO.getConfidence());
        prediction.setAnalysisSummary(predDTO.getAnalysisSummary());
        prediction.setDetailedAnalysis(predDTO.getDetailedAnalysis());
        prediction.setPredictedOdds(predDTO.getPredictedOdds());

        // 计算初始选项总数与初始利润 (如 不败2+比分3+进球3=8项，初始利润 -8.00)
        int initialOptCount = prediction.getResultItemsList().size() + prediction.getScoreItemsList().size() + prediction.getGoalItemsList().size();
        if (initialOptCount == 0) initialOptCount = 1;
        prediction.setOptionsCount(initialOptCount);
        prediction.setInitialProfit(BigDecimal.valueOf(initialOptCount).setScale(2, java.math.RoundingMode.HALF_UP).negate());
        prediction.setStakeAmount(BigDecimal.valueOf(initialOptCount).setScale(2, java.math.RoundingMode.HALF_UP));

        // 如果比赛已经结算，则同步核对与财务收益计算
        if (match.getIsSettled() != null && match.getIsSettled() == 1 && match.getResult() != null) {
            BigDecimal matchStake = BigDecimal.ZERO;
            BigDecimal matchReturn = BigDecimal.ZERO;

            // 胜负多选
            List<MatchPrediction.ResultItem> resultItems = prediction.getResultItemsList();
            BigDecimal resultStake = BigDecimal.valueOf(Math.max(resultItems.size(), 1));
            matchStake = matchStake.add(resultStake);

            boolean isWon = false;
            BigDecimal resultReturn = BigDecimal.ZERO;
            for (MatchPrediction.ResultItem item : resultItems) {
                if (item.getResult() == match.getResult()) {
                    isWon = true;
                    BigDecimal odds = item.getOdds() != null ? item.getOdds() : BigDecimal.valueOf(1.85);
                    resultReturn = resultReturn.add(odds);
                }
            }
            prediction.setIsResultCorrect(isWon ? 1 : 0);
            prediction.setSettleStatus(isWon ? SettleStatusEnum.WON : SettleStatusEnum.LOST);
            prediction.setResultReturn(resultReturn);
            matchReturn = matchReturn.add(resultReturn);

            boolean isScoreHit = false;
            String hitScore = null;
            boolean isGoalsHit = false;
            Integer hitGoals = null;

            if (match.getHomeScore() != null && match.getAwayScore() != null) {
                String actualScoreStr = match.getHomeScore() + "-" + match.getAwayScore();
                int actualTotalGoals = match.getHomeScore() + match.getAwayScore();

                // 比分
                List<MatchPrediction.ScoreItem> scoreItems = prediction.getScoreItemsList();
                matchStake = matchStake.add(BigDecimal.valueOf(scoreItems.size()));
                BigDecimal scoreReturn = BigDecimal.ZERO;
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

                // 进球数
                List<MatchPrediction.GoalItem> goalItems = prediction.getGoalItemsList();
                matchStake = matchStake.add(BigDecimal.valueOf(goalItems.size()));
                BigDecimal goalsReturn = BigDecimal.ZERO;
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
            }

            int totalOptionsCount = prediction.getResultItemsList().size() + prediction.getScoreItemsList().size() + prediction.getGoalItemsList().size();
            if (totalOptionsCount == 0) totalOptionsCount = 1;
            prediction.setOptionsCount(totalOptionsCount);
            prediction.setInitialProfit(BigDecimal.valueOf(totalOptionsCount).setScale(2, java.math.RoundingMode.HALF_UP).negate());
            prediction.setStakeAmount(matchStake.setScale(2, java.math.RoundingMode.HALF_UP));
            prediction.setReturnAmount(matchReturn.setScale(2, java.math.RoundingMode.HALF_UP));
            prediction.setNetProfit(matchReturn.subtract(matchStake).setScale(2, java.math.RoundingMode.HALF_UP));

            // 重要性与等级评定 (比分最重要 > 进球数次之 > 胜负最后)
            if (isScoreHit) {
                prediction.setHitImportance(1);
                if (isGoalsHit && isWon) {
                    prediction.setHitTier("PERFECT_HIT");
                    prediction.setHitTierDesc("🌟 全中大满贯");
                } else if (isGoalsHit) {
                    prediction.setHitTier("SCORE_GOALS_HIT");
                    prediction.setHitTierDesc("🎯 比分+进球双中");
                } else if (isWon) {
                    prediction.setHitTier("SCORE_RESULT_HIT");
                    prediction.setHitTierDesc("🎯 比分+胜负命中");
                } else {
                    prediction.setHitTier("SCORE_HIT");
                    prediction.setHitTierDesc("🎯 精准比分");
                }
            } else if (isGoalsHit) {
                prediction.setHitImportance(2);
                if (isWon) {
                    prediction.setHitTier("GOALS_RESULT_HIT");
                    prediction.setHitTierDesc("⚽ 进球数+胜负命中");
                } else {
                    prediction.setHitTier("GOALS_HIT");
                    prediction.setHitTierDesc("⚽ 进球数命中");
                }
            } else if (isWon) {
                prediction.setHitImportance(3);
                prediction.setHitTier("RESULT_HIT");
                prediction.setHitTierDesc("🚩 胜负命中");
            } else {
                prediction.setHitImportance(0);
                prediction.setHitTier("MISSED");
                prediction.setHitTierDesc("⚫ 未命中");
            }

            prediction.setSettledAt(LocalDateTime.now());
        }

        predictionRepository.save(prediction);
    }

    public MatchDetailVO buildMatchDetailVO(FootballMatch match) {
        List<MatchPrediction> predictions = predictionRepository.findByMatchId(match.getId());
        List<MatchPredictionVO> predVOList = predictions.stream().map(p -> {
            String aiName = aiModelRepository.findByModelCode(p.getAiModelCode())
                    .map(AiModel::getModelName)
                    .orElse(p.getAiModelCode());

            List<MatchPrediction.ResultItem> resultItems = p.getResultItemsList();
            List<MatchPrediction.ScoreItem> scoreItems = p.getScoreItemsList();
            List<MatchPrediction.GoalItem> goalItems = p.getGoalItemsList();

            List<String> scoresList = scoreItems.stream().map(MatchPrediction.ScoreItem::getScore).collect(Collectors.toList());
            List<Integer> goalsList = goalItems.stream().map(MatchPrediction.GoalItem::getGoals).collect(Collectors.toList());

            String predScoreStr = !scoresList.isEmpty()
                    ? String.join(" / ", scoresList)
                    : (p.getPredictedHomeScore() != null && p.getPredictedAwayScore() != null
                        ? p.getPredictedHomeScore() + "-" + p.getPredictedAwayScore()
                        : null);

            return MatchPredictionVO.builder()
                    .id(p.getId())
                    .aiModelCode(p.getAiModelCode())
                    .aiModelName(aiName)
                    .predictedResult(p.getPredictedResult())
                    .predictedResultDesc(p.getPredictedResult() != null ? p.getPredictedResult().getDescription() : null)
                    .resultOdds(p.getResultOdds())
                    .resultItems(resultItems)
                    .scoreItems(scoreItems)
                    .goalItems(goalItems)
                    .predictedScores(scoresList)
                    .predictedGoals(goalsList)
                    .predictedHomeScore(p.getPredictedHomeScore())
                    .predictedAwayScore(p.getPredictedAwayScore())
                    .predictedScoreStr(predScoreStr)
                    .confidence(p.getConfidence())
                    .analysisSummary(p.getAnalysisSummary())
                    .detailedAnalysis(p.getDetailedAnalysis())
                    .predictedOdds(p.getPredictedOdds())
                    .settleStatus(p.getSettleStatus())
                    .settleStatusDesc(p.getSettleStatus() != null ? p.getSettleStatus().getDescription() : null)
                    .isResultCorrect(p.getIsResultCorrect())
                    .isScoreCorrect(p.getIsScoreCorrect())
                    .hitScore(p.getHitScore())
                    .isGoalsCorrect(p.getIsGoalsCorrect())
                    .hitGoals(p.getHitGoals())
                    .hitImportance(p.getHitImportance())
                    .hitTier(p.getHitTier())
                    .hitTierDesc(p.getHitTierDesc())
                    .optionsCount(p.getOptionsCount())
                    .initialProfit(p.getInitialProfit())
                    .stakeAmount(p.getStakeAmount())
                    .returnAmount(p.getReturnAmount())
                    .netProfit(p.getNetProfit())
                    .resultReturn(p.getResultReturn())
                    .scoreReturn(p.getScoreReturn())
                    .goalsReturn(p.getGoalsReturn())
                    .settledAt(p.getSettledAt())
                    .build();
        }).collect(Collectors.toList());

        String scoreStr = (match.getHomeScore() != null && match.getAwayScore() != null)
                ? match.getHomeScore() + "-" + match.getAwayScore()
                : null;

        return MatchDetailVO.builder()
                .id(match.getId())
                .matchCode(match.getMatchCode())
                .leagueId(match.getLeagueId())
                .leagueCode(match.getLeagueCode())
                .leagueName(match.getLeagueName())
                .seasonId(match.getSeasonId())
                .seasonName(match.getSeasonName())
                .roundName(match.getRoundName())
                .homeTeam(match.getHomeTeam())
                .awayTeam(match.getAwayTeam())
                .matchTime(match.getMatchTime())
                .venue(match.getVenue())
                .homeScore(match.getHomeScore())
                .awayScore(match.getAwayScore())
                .scoreStr(scoreStr)
                .halfHomeScore(match.getHalfHomeScore())
                .halfAwayScore(match.getHalfAwayScore())
                .result(match.getResult())
                .resultDesc(match.getResult() != null ? match.getResult().getDescription() : null)
                .status(match.getStatus())
                .statusDesc(match.getStatus() != null ? match.getStatus().getDescription() : null)
                .isSettled(match.getIsSettled())
                .settledAt(match.getSettledAt())
                .predictions(predVOList)
                .build();
    }
}
