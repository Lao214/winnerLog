package com.example.winnerLogBackend;

import com.example.winnerLogBackend.dto.request.ImportPredictionRequest;
import com.example.winnerLogBackend.dto.request.LeaderboardQueryRequest;
import com.example.winnerLogBackend.dto.request.SettleMatchRequest;
import com.example.winnerLogBackend.dto.request.SingleAiPredictionDTO;
import com.example.winnerLogBackend.dto.response.AiComparisonVO;
import com.example.winnerLogBackend.dto.response.AiLeaderboardVO;
import com.example.winnerLogBackend.dto.response.MatchDetailVO;
import com.example.winnerLogBackend.enums.MatchResultEnum;
import com.example.winnerLogBackend.enums.MatchStatusEnum;
import com.example.winnerLogBackend.enums.ScopeTypeEnum;
import com.example.winnerLogBackend.enums.SettleStatusEnum;
import com.example.winnerLogBackend.service.AiStatisticsService;
import com.example.winnerLogBackend.service.LeaderboardService;
import com.example.winnerLogBackend.service.MatchSettlementService;
import com.example.winnerLogBackend.service.PredictionImportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WinnerLogServiceTest {

    @Autowired
    private PredictionImportService predictionImportService;

    @Autowired
    private MatchSettlementService matchSettlementService;

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private AiStatisticsService aiStatisticsService;

    @Test
    @DisplayName("测试赔率输入、最多3个比分与进球数、结算及赚取倍数(净利润/ROI)统计全流程")
    public void testOddsAndProfitMultiplierWorkflow() {
        LocalDateTime matchTime1 = LocalDateTime.of(2025, 3, 15, 23, 0, 0);

        // 1. 导入英超 (阿森纳 VS 切尔西)，GPT与千问各自给出胜负赔率、最多3个比分赔率与3个总进球数赔率
        ImportPredictionRequest match1Req = ImportPredictionRequest.builder()
                .leagueCode("PL")
                .seasonName("2024-2025")
                .roundName("第28轮")
                .homeTeam("阿森纳")
                .awayTeam("切尔西")
                .matchTime(matchTime1)
                .venue("酋长球场")
                .predictions(List.of(
                        SingleAiPredictionDTO.builder()
                                .modelCode("GPT")
                                .predictedResult(MatchResultEnum.HOME_WIN)
                                .resultOdds(new BigDecimal("1.90"))
                                .scoreItems(List.of(
                                        new SingleAiPredictionDTO.ScoreItemDTO("2-1", new BigDecimal("8.50")),
                                        new SingleAiPredictionDTO.ScoreItemDTO("1-0", new BigDecimal("6.00")),
                                        new SingleAiPredictionDTO.ScoreItemDTO("2-0", new BigDecimal("7.00"))
                                ))
                                .goalItems(List.of(
                                        new SingleAiPredictionDTO.GoalItemDTO(2, new BigDecimal("3.20")),
                                        new SingleAiPredictionDTO.GoalItemDTO(3, new BigDecimal("3.80")),
                                        new SingleAiPredictionDTO.GoalItemDTO(4, new BigDecimal("5.50"))
                                ))
                                .confidence(new BigDecimal("82.50"))
                                .analysisSummary("阿森纳主场战绩强势，看好小胜")
                                .build(),
                        SingleAiPredictionDTO.builder()
                                .modelCode("QWEN")
                                .predictedResult(MatchResultEnum.HOME_WIN)
                                .resultOdds(new BigDecimal("1.90"))
                                .scoreItems(List.of(
                                        new SingleAiPredictionDTO.ScoreItemDTO("3-1", new BigDecimal("12.00")),
                                        new SingleAiPredictionDTO.ScoreItemDTO("3-0", new BigDecimal("10.00")),
                                        new SingleAiPredictionDTO.ScoreItemDTO("4-1", new BigDecimal("18.00"))
                                ))
                                .goalItems(List.of(
                                        new SingleAiPredictionDTO.GoalItemDTO(3, new BigDecimal("3.80")),
                                        new SingleAiPredictionDTO.GoalItemDTO(4, new BigDecimal("5.50")),
                                        new SingleAiPredictionDTO.GoalItemDTO(5, new BigDecimal("9.00"))
                                ))
                                .confidence(new BigDecimal("78.00"))
                                .analysisSummary("千问看好大比分主胜")
                                .build()
                ))
                .build();

        MatchDetailVO match1Detail = predictionImportService.importSinglePrediction(match1Req);
        assertNotNull(match1Detail);
        assertEquals(2, match1Detail.getPredictions().size());

        // 2. 比赛结束，录入最终比分 2-1 (主胜, 总进球=3)
        SettleMatchRequest settleReq1 = SettleMatchRequest.builder()
                .matchId(match1Detail.getId())
                .homeScore(2)
                .awayScore(1)
                .build();

        MatchDetailVO settledMatch1 = predictionImportService.buildMatchDetailVO(
                matchSettlementService.settleMatch(settleReq1)
        );

        settledMatch1.getPredictions().forEach(pred -> {
            if ("GPT".equals(pred.getAiModelCode())) {
                assertEquals(SettleStatusEnum.WON, pred.getSettleStatus(), "GPT胜平负正确");
                assertEquals(1, pred.getIsScoreCorrect(), "GPT在[2-1, 1-0, 2-0]中成功命中2-1");
                assertEquals("2-1", pred.getHitScore());
                assertEquals(1, pred.getIsGoalsCorrect(), "GPT在[2, 3, 4]球中成功命中3球");
                assertEquals(3, pred.getHitGoals());

                // 检验财务指标：投7注(胜负1+比分3+进球3)，返还 1.90+8.50+3.80 = 14.20，净赚 +7.20 倍
                assertEquals(new BigDecimal("7.00"), pred.getStakeAmount());
                assertEquals(new BigDecimal("14.20"), pred.getReturnAmount());
                assertEquals(new BigDecimal("7.20"), pred.getNetProfit());
            } else if ("QWEN".equals(pred.getAiModelCode())) {
                assertEquals(SettleStatusEnum.WON, pred.getSettleStatus(), "千问胜平负正确");
                assertEquals(0, pred.getIsScoreCorrect(), "千问在[3-1, 3-0, 4-1]中未命中比分");
                assertEquals(1, pred.getIsGoalsCorrect(), "千问在[3, 4, 5]球中成功命中3球");
                assertEquals(3, pred.getHitGoals());

                // 检验财务指标：投7注，返还 1.90+0+3.80 = 5.70，净赚 -1.30 倍 (亏1.3倍)
                assertEquals(new BigDecimal("7.00"), pred.getStakeAmount());
                assertEquals(new BigDecimal("5.70"), pred.getReturnAmount());
                assertEquals(new BigDecimal("-1.30"), pred.getNetProfit());
            }
        });

        // 3. 查询排行榜，验证盈利倍数统计
        LeaderboardQueryRequest seasonRankReq = LeaderboardQueryRequest.builder()
                .scopeType(ScopeTypeEnum.SEASON)
                .seasonName("2024-2025")
                .sortBy("netProfit")
                .sortOrder("desc")
                .build();

        List<AiLeaderboardVO> seasonRank = leaderboardService.getLeaderboard(seasonRankReq);
        assertEquals(2, seasonRank.size());

        AiLeaderboardVO top1 = seasonRank.get(0);
        assertEquals("GPT", top1.getAiModelCode(), "GPT净赚7.20倍排第一名");
        assertEquals(new BigDecimal("7.20"), top1.getNetProfit(), "GPT净赚+7.20倍");
        assertEquals(new BigDecimal("102.86"), top1.getRoiRate(), "GPT盈利率102.86%");

        AiLeaderboardVO top2 = seasonRank.get(1);
        assertEquals("QWEN", top2.getAiModelCode(), "千问净赚-1.30倍排第二名");
        assertEquals(new BigDecimal("-1.30"), top2.getNetProfit(), "千问净赚-1.30倍");
        assertEquals(new BigDecimal("-18.57"), top2.getRoiRate(), "千问盈利率-18.57%");

        // 4. 双模型对比检验
        AiComparisonVO comparison = aiStatisticsService.compareTwoModels("GPT", "QWEN", "2024-2025");
        assertNotNull(comparison);
        assertEquals(new BigDecimal("7.20"), comparison.getModelAStat().getNetProfit());
        assertEquals(new BigDecimal("-1.30"), comparison.getModelBStat().getNetProfit());

        System.out.println("====== 测试全部通过！赔率输入、比分进球多项预测与AI净赚倍数结算工作完全正常！ ======");
    }

    @Test
    @DisplayName("测试用户业务需求：初始利润为选项数(如不败+3比分+3进球=8项为-8)，中奖返还计算最终利润，按重要性分级(比分>进球数>胜负)")
    public void testUserScenarioWith8OptionsAndHitImportance() {
        LocalDateTime matchTime = LocalDateTime.of(2025, 4, 1, 20, 0, 0);

        // 构造4个AI模型分别覆盖4种命中重要性等级：
        // 1. GPT: 预测不败(主胜1.90+平局3.40=2项) + 3比分 + 3进球 = 8项 (初始利润-8)。命中比分2-1(6.50) + 命中进球3(3.50)，总返还赔率10.00 (假设胜负未中或只看返还10)，利润为 +2.00，重要性为 1 (比分最高)
        // 2. QWEN: 预测客胜(1项) + 3比分 + 3进球 = 7项 (初始利润-7)。比分未中，进球数命中3(3.80)，胜负未中。总返还 3.80，利润 -3.20，重要性为 2 (进球数次之)
        // 3. CLAUDE: 预测主胜(1项) + 3比分 + 3进球 = 7项 (初始利润-7)。比分进球全未中，胜负命中主胜(2.00)。总返还 2.00，利润 -5.00，重要性为 3 (胜负最后)
        // 4. DEEPSEEK: 预测客胜(1项) + 3比分 + 3进球 = 7项 (初始利润-7)。全部未中。总返还 0.00，利润 -7.00，重要性为 0 (未命中)
        ImportPredictionRequest req = ImportPredictionRequest.builder()
                .leagueCode("UCL")
                .seasonName("2024-2025")
                .roundName("1/4决赛")
                .homeTeam("皇家马德里")
                .awayTeam("拜仁慕尼黑")
                .matchTime(matchTime)
                .venue("伯纳乌球场")
                .predictions(List.of(
                        // 模型1: GPT - 8项 (不败2 + 比分3 + 进球3)，初始利润 -8
                        SingleAiPredictionDTO.builder()
                                .modelCode("GPT")
                                .resultItems(List.of(
                                        new SingleAiPredictionDTO.ResultItemDTO(MatchResultEnum.AWAY_WIN, new BigDecimal("3.20")), // 故意设为未中客胜+平局，验证比分6.50+进球3.50=10.00返还 -> 利润2
                                        new SingleAiPredictionDTO.ResultItemDTO(MatchResultEnum.DRAW, new BigDecimal("3.40"))
                                ))
                                .scoreItems(List.of(
                                        new SingleAiPredictionDTO.ScoreItemDTO("2-1", new BigDecimal("6.50")),
                                        new SingleAiPredictionDTO.ScoreItemDTO("1-1", new BigDecimal("7.00")),
                                        new SingleAiPredictionDTO.ScoreItemDTO("0-0", new BigDecimal("9.00"))
                                ))
                                .goalItems(List.of(
                                        new SingleAiPredictionDTO.GoalItemDTO(3, new BigDecimal("3.50")),
                                        new SingleAiPredictionDTO.GoalItemDTO(2, new BigDecimal("3.40")),
                                        new SingleAiPredictionDTO.GoalItemDTO(4, new BigDecimal("5.00"))
                                ))
                                .confidence(new BigDecimal("85.00"))
                                .analysisSummary("GPT预测防范平局与小球")
                                .build(),

                        // 模型2: QWEN - 进球数命中，比分未中，重要性 2
                        SingleAiPredictionDTO.builder()
                                .modelCode("QWEN")
                                .predictedResult(MatchResultEnum.AWAY_WIN)
                                .resultOdds(new BigDecimal("3.00"))
                                .scoreItems(List.of(
                                        new SingleAiPredictionDTO.ScoreItemDTO("0-3", new BigDecimal("25.00")),
                                        new SingleAiPredictionDTO.ScoreItemDTO("1-2", new BigDecimal("12.00")),
                                        new SingleAiPredictionDTO.ScoreItemDTO("0-2", new BigDecimal("15.00"))
                                ))
                                .goalItems(List.of(
                                        new SingleAiPredictionDTO.GoalItemDTO(3, new BigDecimal("3.80")),
                                        new SingleAiPredictionDTO.GoalItemDTO(5, new BigDecimal("9.00")),
                                        new SingleAiPredictionDTO.GoalItemDTO(6, new BigDecimal("18.00"))
                                ))
                                .confidence(new BigDecimal("70.00"))
                                .analysisSummary("千问看好拜仁客胜")
                                .build(),

                        // 模型3: CLAUDE - 仅胜负命中，重要性 3
                        SingleAiPredictionDTO.builder()
                                .modelCode("CLAUDE")
                                .predictedResult(MatchResultEnum.HOME_WIN)
                                .resultOdds(new BigDecimal("2.00"))
                                .scoreItems(List.of(
                                        new SingleAiPredictionDTO.ScoreItemDTO("3-0", new BigDecimal("18.00")),
                                        new SingleAiPredictionDTO.ScoreItemDTO("3-1", new BigDecimal("14.00")),
                                        new SingleAiPredictionDTO.ScoreItemDTO("4-0", new BigDecimal("30.00"))
                                ))
                                .goalItems(List.of(
                                        new SingleAiPredictionDTO.GoalItemDTO(4, new BigDecimal("5.20")),
                                        new SingleAiPredictionDTO.GoalItemDTO(5, new BigDecimal("8.50")),
                                        new SingleAiPredictionDTO.GoalItemDTO(6, new BigDecimal("16.00"))
                                ))
                                .confidence(new BigDecimal("75.00"))
                                .analysisSummary("Claude看好皇马大胜")
                                .build(),

                        // 模型4: DEEPSEEK - 全未中，重要性 0
                        SingleAiPredictionDTO.builder()
                                .modelCode("DEEPSEEK")
                                .predictedResult(MatchResultEnum.DRAW)
                                .resultOdds(new BigDecimal("3.50"))
                                .scoreItems(List.of(
                                        new SingleAiPredictionDTO.ScoreItemDTO("0-0", new BigDecimal("10.00")),
                                        new SingleAiPredictionDTO.ScoreItemDTO("1-1", new BigDecimal("7.00")),
                                        new SingleAiPredictionDTO.ScoreItemDTO("2-2", new BigDecimal("12.00"))
                                ))
                                .goalItems(List.of(
                                        new SingleAiPredictionDTO.GoalItemDTO(0, new BigDecimal("10.00")),
                                        new SingleAiPredictionDTO.GoalItemDTO(2, new BigDecimal("3.50")),
                                        new SingleAiPredictionDTO.GoalItemDTO(4, new BigDecimal("5.50"))
                                ))
                                .confidence(new BigDecimal("60.00"))
                                .analysisSummary("DeepSeek看好闷平")
                                .build()
                ))
                .build();

        MatchDetailVO detail = predictionImportService.importSinglePrediction(req);
        assertNotNull(detail);

        // 验证未结算时的初始选项数与初始利润
        detail.getPredictions().forEach(p -> {
            if ("GPT".equals(p.getAiModelCode())) {
                assertEquals(8, p.getOptionsCount(), "GPT有8个选项(2胜平负+3比分+3进球)");
                assertEquals(new BigDecimal("-8.00"), p.getInitialProfit(), "GPT初始利润为 -8.00");
                assertEquals(new BigDecimal("8.00"), p.getStakeAmount(), "GPT下注总数为 8.00");
            } else {
                assertEquals(7, p.getOptionsCount(), "其他模型有7个选项(1胜平负+3比分+3进球)");
                assertEquals(new BigDecimal("-7.00"), p.getInitialProfit(), "初始利润为 -7.00");
            }
        });

        // 结算比赛：比分 2-1 (主胜，总进球=3)
        SettleMatchRequest settleReq = SettleMatchRequest.builder()
                .matchId(detail.getId())
                .homeScore(2)
                .awayScore(1)
                .build();

        MatchDetailVO settled = predictionImportService.buildMatchDetailVO(
                matchSettlementService.settleMatch(settleReq)
        );

        // 校验4个模型的命中、利润与重要性评级
        settled.getPredictions().forEach(p -> {
            if ("GPT".equals(p.getAiModelCode())) {
                // 命中比分2-1(6.50) + 进球数3(3.50) = 10.00返还，选项数8(-8.00初始利润) -> 最终利润 = 10.00 - 8 = +2.00
                assertEquals(1, p.getIsScoreCorrect(), "比分命中");
                assertEquals("2-1", p.getHitScore());
                assertEquals(1, p.getIsGoalsCorrect(), "进球数命中");
                assertEquals(3, p.getHitGoals());
                assertEquals(8, p.getOptionsCount());
                assertEquals(new BigDecimal("10.00"), p.getReturnAmount(), "总获取赔率为 10.00");
                assertEquals(new BigDecimal("2.00"), p.getNetProfit(), "这场的利润为 +2.00 (10.00 - 8)");
                assertEquals(1, p.getHitImportance(), "命中比分是最重要的(等级1)");
                assertEquals("SCORE_GOALS_HIT", p.getHitTier());
            } else if ("QWEN".equals(p.getAiModelCode())) {
                // 进球数命中3(3.80)，比分未中，胜负未中 -> 返还 3.80 - 7 = 利润 -3.20，重要性 2
                assertEquals(0, p.getIsScoreCorrect());
                assertEquals(1, p.getIsGoalsCorrect());
                assertEquals(new BigDecimal("3.80"), p.getReturnAmount());
                assertEquals(new BigDecimal("-3.20"), p.getNetProfit());
                assertEquals(2, p.getHitImportance(), "进球数命中为次重要(等级2)");
                assertEquals("GOALS_HIT", p.getHitTier());
            } else if ("CLAUDE".equals(p.getAiModelCode())) {
                // 仅胜负命中(2.00) -> 返还 2.00 - 7 = 利润 -5.00，重要性 3
                assertEquals(1, p.getIsResultCorrect());
                assertEquals(0, p.getIsScoreCorrect());
                assertEquals(0, p.getIsGoalsCorrect());
                assertEquals(new BigDecimal("2.00"), p.getReturnAmount());
                assertEquals(new BigDecimal("-5.00"), p.getNetProfit());
                assertEquals(3, p.getHitImportance(), "仅胜负命中为基础等级(等级3)");
                assertEquals("RESULT_HIT", p.getHitTier());
            } else if ("DEEPSEEK".equals(p.getAiModelCode())) {
                // 全未中 -> 返还 0.00 - 7 = 利润 -7.00，重要性 0
                assertEquals(0, p.getIsResultCorrect());
                assertEquals(0, p.getIsScoreCorrect());
                assertEquals(0, p.getIsGoalsCorrect());
                assertEquals(new BigDecimal("0.00"), p.getReturnAmount());
                assertEquals(new BigDecimal("-7.00"), p.getNetProfit());
                assertEquals(0, p.getHitImportance(), "全未中为等级0");
                assertEquals("MISSED", p.getHitTier());
            }
        });

        System.out.println("====== 用户业务需求测试全部通过！8选项(-8初始利润)、返还10利润+2、重要性等级(比分>进球数>胜负)完全符合预期！ ======");
    }
}
