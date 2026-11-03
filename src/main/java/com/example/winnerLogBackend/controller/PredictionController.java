package com.example.winnerLogBackend.controller;

import com.example.winnerLogBackend.common.ApiResponse;
import com.example.winnerLogBackend.dto.request.BatchImportPredictionRequest;
import com.example.winnerLogBackend.dto.request.ImportPredictionRequest;
import com.example.winnerLogBackend.dto.response.MatchDetailVO;
import com.example.winnerLogBackend.service.PredictionImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "1. AI预测导入接口", description = "用于从GPT、通义千问等导入足球比赛预测数据，自动建档与结算")
@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    private final PredictionImportService predictionImportService;

    public PredictionController(PredictionImportService predictionImportService) {
        this.predictionImportService = predictionImportService;
    }

    @Operation(summary = "导入单场比赛预测", description = "支持同时传入GPT与千问等多模型预测。若比赛尚未存在则自动建档(未开赛)；若比赛已包含比分或完赛，将自动触发结算引擎。")
    @PostMapping("/import")
    public ApiResponse<MatchDetailVO> importSinglePrediction(@Valid @RequestBody ImportPredictionRequest request) {
        MatchDetailVO result = predictionImportService.importSinglePrediction(request);
        return ApiResponse.success("比赛预测导入成功", result);
    }

    @Operation(summary = "批量导入多场比赛预测", description = "一次性批量导入多场赛事的AI预测数据。")
    @PostMapping("/batch-import")
    public ApiResponse<List<MatchDetailVO>> batchImportPredictions(@Valid @RequestBody BatchImportPredictionRequest request) {
        List<MatchDetailVO> results = predictionImportService.batchImportPredictions(request);
        return ApiResponse.success("批量导入成功，共处理 " + results.size() + " 场比赛", results);
    }
}
