package com.example.winnerLogBackend.controller;

import com.example.winnerLogBackend.common.ApiResponse;
import com.example.winnerLogBackend.dto.request.LeaderboardQueryRequest;
import com.example.winnerLogBackend.dto.response.AiLeaderboardVO;
import com.example.winnerLogBackend.service.LeaderboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "3. AI胜率排行榜接口", description = "按赛季榜、联赛赛季榜、联赛总榜、全局总榜查询各AI模型（GPT/千问等）胜率与排名")
@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @Operation(summary = "查询AI胜率排行榜", description = "支持按维度(OVERALL:全局总榜, SEASON:赛季总榜, LEAGUE_SEASON:联赛赛季榜, LEAGUE:联赛历史总榜)、赛季名称(如2024-2025)、联赛编码(如PL/LA_LIGA)进行排行，可按胜率、红单数、总场次、连红数、比分命中率排序。")
    @GetMapping
    public ApiResponse<List<AiLeaderboardVO>> getLeaderboard(LeaderboardQueryRequest request) {
        List<AiLeaderboardVO> leaderboard = leaderboardService.getLeaderboard(request);
        return ApiResponse.success(leaderboard);
    }
}
