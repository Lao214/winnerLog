package com.example.winnerLogBackend.controller;

import com.example.winnerLogBackend.common.ApiResponse;
import com.example.winnerLogBackend.dto.response.AiComparisonVO;
import com.example.winnerLogBackend.service.AiStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "4. AI模型对比分析接口", description = "GPT vs 千问 双模型头对头对决、分歧统计与五大联赛分项胜率对比")
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final AiStatisticsService aiStatisticsService;

    public StatisticsController(AiStatisticsService aiStatisticsService) {
        this.aiStatisticsService = aiStatisticsService;
    }

    @Operation(summary = "GPT 与 千问 双模型胜率与战绩对比", description = "提供两模型的胜率PK、共同预测一致率、观点分歧时的胜出统计、以及在五大联赛和欧冠等各大联赛的具体胜率拆分。")
    @GetMapping("/ai-comparison")
    public ApiResponse<AiComparisonVO> compareAiModels(
            @Parameter(description = "模型A编码 (默认GPT)", example = "GPT")
            @RequestParam(value = "modelA", defaultValue = "GPT", required = false) String modelA,
            @Parameter(description = "模型B编码 (默认QWEN)", example = "QWEN")
            @RequestParam(value = "modelB", defaultValue = "QWEN", required = false) String modelB,
            @Parameter(description = "赛季过滤 (可选，如 2024-2025)", example = "2024-2025")
            @RequestParam(value = "seasonName", required = false) String seasonName) {

        AiComparisonVO comparison = aiStatisticsService.compareTwoModels(modelA, modelB, seasonName);
        return ApiResponse.success(comparison);
    }
}
