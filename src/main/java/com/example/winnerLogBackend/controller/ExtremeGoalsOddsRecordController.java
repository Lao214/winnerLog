package com.example.winnerLogBackend.controller;

import com.example.winnerLogBackend.common.ApiResponse;
import com.example.winnerLogBackend.dto.request.ExtremeGoalsOddsRecordRequest;
import com.example.winnerLogBackend.dto.request.ExtremeGoalsSimulateRequest;
import com.example.winnerLogBackend.dto.response.ExtremeGoalsSimulateVO;
import com.example.winnerLogBackend.dto.response.ExtremeGoalsStatsSummaryVO;
import com.example.winnerLogBackend.entity.ExtremeGoalsOddsRecord;
import com.example.winnerLogBackend.service.ExtremeGoalsOddsRecordService;
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

@Tag(name = "12. 0球与7球两极进球赔率大数据分析接口", description = "支持按天录入主流联赛（日职、韩职、澳超、巴甲、葡超、荷甲、五大联赛、欧冠等）的0球赔率、7球+赔率与开奖总进球数，提供区间分档与大数据关联统计分析")
@RestController
@RequestMapping("/api/extreme-goals-odds")
public class ExtremeGoalsOddsRecordController {

    private final ExtremeGoalsOddsRecordService service;

    public ExtremeGoalsOddsRecordController(ExtremeGoalsOddsRecordService service) {
        this.service = service;
    }

    @Operation(summary = "查询两极进球赔率明细列表")
    @GetMapping("/list")
    public ApiResponse<List<ExtremeGoalsOddsRecord>> list(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "联赛名称 (支持模糊匹配或日职/韩职/荷甲等)")
            @RequestParam(required = false) String leagueName,
            @Parameter(description = "赛制类型 (LEAGUE/CUP/FRIENDLY/ALL)")
            @RequestParam(required = false) String matchSystem) {
        return ApiResponse.success(service.getAllRecords(startDate, endDate, leagueName, matchSystem));
    }

    @Operation(summary = "分页查询两极进球赔率明细")
    @GetMapping("/page")
    public ApiResponse<Page<ExtremeGoalsOddsRecord>> page(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "联赛名称")
            @RequestParam(required = false) String leagueName,
            @Parameter(description = "赛制类型 (LEAGUE/CUP/FRIENDLY/ALL)")
            @RequestParam(required = false) String matchSystem,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int size) {
        int pageIndex = Math.max(0, page - 1);
        Page<ExtremeGoalsOddsRecord> p = service.getRecordsPage(startDate, endDate, leagueName, matchSystem,
                PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "matchDate").and(Sort.by(Sort.Direction.DESC, "id"))));
        return ApiResponse.success(p);
    }

    @Operation(summary = "获取两极进球赔率大数据分析综合概况看板")
    @GetMapping("/summary")
    public ApiResponse<ExtremeGoalsStatsSummaryVO> summary(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "联赛名称")
            @RequestParam(required = false) String leagueName,
            @Parameter(description = "赛制类型 (LEAGUE/CUP/FRIENDLY/ALL)")
            @RequestParam(required = false) String matchSystem) {
        return ApiResponse.success(service.getStatsSummary(startDate, endDate, leagueName, matchSystem));
    }

    @Operation(summary = "获取已录入的所有不重复联赛名称列表")
    @GetMapping("/leagues")
    public ApiResponse<List<String>> leagues() {
        return ApiResponse.success(service.getDistinctLeagueNames());
    }

    @Operation(summary = "获取单条两极进球赔率记录详情")
    @GetMapping("/{id}")
    public ApiResponse<ExtremeGoalsOddsRecord> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getRecordById(id));
    }

    @Operation(summary = "新增单条两极进球赔率记录")
    @PostMapping
    public ApiResponse<ExtremeGoalsOddsRecord> create(@Valid @RequestBody ExtremeGoalsOddsRecordRequest request) {
        return ApiResponse.success("新增记录成功", service.createRecord(request));
    }

    @Operation(summary = "按天/按批次批量录入多条两极进球赔率记录")
    @PostMapping("/batch")
    public ApiResponse<List<ExtremeGoalsOddsRecord>> batchCreate(@Valid @RequestBody List<ExtremeGoalsOddsRecordRequest> requests) {
        return ApiResponse.success("批量录入成功，共录入 " + requests.size() + " 场比赛数据", service.batchCreateRecords(requests));
    }

    @Operation(summary = "修改两极进球赔率记录")
    @PutMapping("/{id}")
    public ApiResponse<ExtremeGoalsOddsRecord> update(@PathVariable Long id, @Valid @RequestBody ExtremeGoalsOddsRecordRequest request) {
        return ApiResponse.success("更新记录成功", service.updateRecord(id, request));
    }

    @Operation(summary = "删除两极进球赔率记录")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.deleteRecord(id);
        return ApiResponse.success("删除记录成功", null);
    }

    @Operation(summary = "批量删除两极进球赔率记录")
    @PostMapping("/batch-delete")
    public ApiResponse<Void> batchDelete(@RequestBody List<Long> ids) {
        service.batchDeleteRecords(ids);
        return ApiResponse.success("批量删除成功", null);
    }

    @Operation(summary = "基于0球赔率与7球赔率测算进球数概率分布与AI打单建议")
    @PostMapping("/simulate")
    public ApiResponse<ExtremeGoalsSimulateVO> simulate(@Valid @RequestBody ExtremeGoalsSimulateRequest request) {
        return ApiResponse.success("测算成功", service.simulate(request));
    }
}
