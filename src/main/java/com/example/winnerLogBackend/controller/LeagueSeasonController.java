package com.example.winnerLogBackend.controller;

import com.example.winnerLogBackend.common.ApiResponse;
import com.example.winnerLogBackend.entity.AiModel;
import com.example.winnerLogBackend.entity.League;
import com.example.winnerLogBackend.entity.Season;
import com.example.winnerLogBackend.enums.LeagueCategoryEnum;
import com.example.winnerLogBackend.service.LeagueSeasonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "5. 基础字典与赛事管理接口", description = "获取系统支持的联赛列表、各联赛赛季、AI模型列表")
@RestController
@RequestMapping("/api")
public class LeagueSeasonController {

    private final LeagueSeasonService leagueSeasonService;

    public LeagueSeasonController(LeagueSeasonService leagueSeasonService) {
        this.leagueSeasonService = leagueSeasonService;
    }

    @Operation(summary = "获取所有联赛/赛事列表", description = "获取五大联赛（英超、西甲、意甲、德甲、法甲）、欧冠、世界杯、欧洲杯等赛事字典，支持按分类筛选。")
    @GetMapping("/leagues")
    public ApiResponse<List<League>> getLeagues(
            @Parameter(description = "赛事分类 (可选: BIG_FIVE_LEAGUE:五大联赛, CONTINENTAL_CUP:洲际杯赛, INTERNATIONAL_CUP:国际大赛)")
            @RequestParam(value = "category", required = false) LeagueCategoryEnum category) {
        if (category != null) {
            return ApiResponse.success(leagueSeasonService.getLeaguesByCategory(category));
        }
        return ApiResponse.success(leagueSeasonService.getAllActiveLeagues());
    }

    @Operation(summary = "获取指定联赛的赛季列表", description = "根据联赛编码(如PL)获取其历史及当前活跃赛季(如2024-2025, 2025-2026)。")
    @GetMapping("/seasons")
    public ApiResponse<List<Season>> getSeasons(
            @Parameter(description = "联赛编码 (如 PL, LA_LIGA, UCL, WORLD_CUP)", required = true, example = "PL")
            @RequestParam("leagueCode") String leagueCode) {
        return ApiResponse.success(leagueSeasonService.getSeasonsByLeagueCode(leagueCode));
    }

    @Operation(summary = "获取所有支持的AI模型列表", description = "获取系统当前支持的AI预测模型 (如 GPT, QWEN, DEEPSEEK, CLAUDE)。")
    @GetMapping("/ai-models")
    public ApiResponse<List<AiModel>> getAiModels() {
        return ApiResponse.success(leagueSeasonService.getAllActiveAiModels());
    }
}
