package com.example.winnerLogBackend.controller;

import com.example.winnerLogBackend.common.ApiResponse;
import com.example.winnerLogBackend.dto.request.MatchQueryRequest;
import com.example.winnerLogBackend.dto.request.SettleMatchRequest;
import com.example.winnerLogBackend.dto.response.MatchDetailVO;
import com.example.winnerLogBackend.entity.FootballMatch;
import com.example.winnerLogBackend.service.FootballMatchService;
import com.example.winnerLogBackend.service.MatchSettlementService;
import com.example.winnerLogBackend.service.PredictionImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "2. 足球比赛与结算接口", description = "比赛档案管理、赛果录入、红黑单核对与结算")
@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchSettlementService matchSettlementService;
    private final FootballMatchService footballMatchService;
    private final PredictionImportService predictionImportService;

    public MatchController(MatchSettlementService matchSettlementService,
                           FootballMatchService footballMatchService,
                           PredictionImportService predictionImportService) {
        this.matchSettlementService = matchSettlementService;
        this.footballMatchService = footballMatchService;
        this.predictionImportService = predictionImportService;
    }

    @Operation(summary = "录入比赛最终结果并结算", description = "录入比赛完场比分后，自动核对所有AI模型的预测结果（红单/黑单/比分命中），并实时更新各大排行榜与连红记录。")
    @PostMapping("/settle")
    public ApiResponse<MatchDetailVO> settleMatch(@Valid @RequestBody SettleMatchRequest request) {
        FootballMatch match = matchSettlementService.settleMatch(request);
        MatchDetailVO detail = predictionImportService.buildMatchDetailVO(match);
        return ApiResponse.success("比赛结算成功，AI战绩与排行榜已同步更新", detail);
    }

    @Operation(summary = "多条件分页查询比赛列表", description = "支持按联赛(五大联赛/欧冠等)、赛季、状态、球队名称关键字、时间范围分页检索比赛，并返回各AI的预测数据与赛果对比。")
    @GetMapping
    public ApiResponse<Page<MatchDetailVO>> queryMatches(MatchQueryRequest request) {
        Page<MatchDetailVO> page = footballMatchService.queryMatches(request);
        return ApiResponse.success(page);
    }

    @Operation(summary = "根据比赛ID查询单场详情", description = "获取某场比赛的完整档案、比分结果以及所有AI模型（GPT、千问等）的详细分析报告。")
    @GetMapping("/{id}")
    public ApiResponse<MatchDetailVO> getMatchDetail(@PathVariable("id") Long id) {
        MatchDetailVO detail = footballMatchService.getMatchDetailById(id);
        return ApiResponse.success(detail);
    }
}
