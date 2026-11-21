package com.example.winnerLogBackend.controller;

import com.example.winnerLogBackend.common.ApiResponse;
import com.example.winnerLogBackend.dto.request.HandicapMatchRecordRequest;
import com.example.winnerLogBackend.entity.HandicapMatchRecord;
import com.example.winnerLogBackend.service.HandicapMatchRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "11. 单场比赛精细化让球与开奖明细接口", description = "支持录入每场比赛的胜赔（分主客）、开奖结果（结果及结果赔、让球赔、半全场、总进球）、是否穿盘，供前端按0.05/0.1/0.15/0.2等步幅动态切分聚合")
@RestController
@RequestMapping("/api/handicap-match-record")
public class HandicapMatchRecordController {

    private final HandicapMatchRecordService service;

    public HandicapMatchRecordController(HandicapMatchRecordService service) {
        this.service = service;
    }

    @Operation(summary = "查询单场比赛明细列表", description = "支持按日期范围、让球方（HOME/AWAY）、赛制类型（LEAGUE/CUP/FRIENDLY）、联赛名称筛选")
    @GetMapping("/list")
    public ApiResponse<List<HandicapMatchRecord>> list(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "让球方 (HOME/AWAY/ALL)")
            @RequestParam(required = false) String handicapSide,
            @Parameter(description = "赛制类型 (LEAGUE/CUP/FRIENDLY/ALL)")
            @RequestParam(required = false) String matchSystem,
            @Parameter(description = "联赛名称")
            @RequestParam(required = false) String leagueName) {
        List<HandicapMatchRecord> list = service.getAllMatches(startDate, endDate, handicapSide, matchSystem, leagueName);
        return ApiResponse.success(list);
    }

    @Operation(summary = "分页查询单场比赛明细")
    @GetMapping("/page")
    public ApiResponse<Page<HandicapMatchRecord>> page(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "让球方 (HOME/AWAY/ALL)")
            @RequestParam(required = false) String handicapSide,
            @Parameter(description = "赛制类型 (LEAGUE/CUP/FRIENDLY/ALL)")
            @RequestParam(required = false) String matchSystem,
            @Parameter(description = "联赛名称")
            @RequestParam(required = false) String leagueName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int size) {
        int pageIndex = Math.max(0, page - 1);
        Page<HandicapMatchRecord> p = service.getMatchesPage(startDate, endDate, handicapSide, matchSystem, leagueName,
                PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "matchDate").and(Sort.by(Sort.Direction.DESC, "id"))));
        return ApiResponse.success(p);
    }


    @Operation(summary = "获取单场比赛详情")
    @GetMapping("/{id}")
    public ApiResponse<HandicapMatchRecord> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getMatchById(id));
    }

    @Operation(summary = "新增单场比赛记录")
    @PostMapping
    public ApiResponse<HandicapMatchRecord> create(@Valid @RequestBody HandicapMatchRecordRequest request) {
        return ApiResponse.success("比赛记录新增成功", service.createMatch(request));
    }

    @Operation(summary = "批量录入多场比赛记录")
    @PostMapping("/batch")
    public ApiResponse<List<HandicapMatchRecord>> batchCreate(@Valid @RequestBody List<HandicapMatchRecordRequest> requests) {
        return ApiResponse.success("批量录入成功，共录入 " + requests.size() + " 场比赛", service.batchCreateMatches(requests));
    }

    @Operation(summary = "修改单场比赛记录")
    @PutMapping("/{id}")
    public ApiResponse<HandicapMatchRecord> update(@PathVariable Long id, @Valid @RequestBody HandicapMatchRecordRequest request) {
        return ApiResponse.success("比赛记录更新成功", service.updateMatch(id, request));
    }

    @Operation(summary = "删除单场比赛记录")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.deleteMatch(id);
        return ApiResponse.success("比赛记录删除成功", null);
    }

    @Operation(summary = "批量删除比赛记录")
    @PostMapping("/batch-delete")
    public ApiResponse<Void> batchDelete(@RequestBody List<Long> ids) {
        service.batchDeleteMatches(ids);
        return ApiResponse.success("批量删除成功", null);
    }

    @Operation(summary = "单场比赛胜平负概率与期望ROI智能测算", description = "输入赛制类型、让球方、胜平负赔率及步幅容差，从库内检索相似历史样本并计算打出概率、ROI及穿盘策略")
    @PostMapping("/simulate")
    public ApiResponse<com.example.winnerLogBackend.dto.response.HandicapMatchSimulationVO> simulate(
            @Valid @RequestBody com.example.winnerLogBackend.dto.request.HandicapMatchSimulateRequest request) {
        return ApiResponse.success("测算成功", service.simulateMatch(request));
    }
}
