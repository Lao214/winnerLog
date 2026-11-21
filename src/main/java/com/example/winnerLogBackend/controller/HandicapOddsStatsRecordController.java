package com.example.winnerLogBackend.controller;

import com.example.winnerLogBackend.common.ApiResponse;
import com.example.winnerLogBackend.dto.request.HandicapOddsStatsRecordRequest;
import com.example.winnerLogBackend.dto.response.HandicapOddsStatsSummaryVO;
import com.example.winnerLogBackend.entity.HandicapOddsStatsRecord;
import com.example.winnerLogBackend.service.HandicapOddsStatsRecordService;
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

@Tag(name = "10. 让球方获胜赔率六区间胜平负与穿盘率大数据统计接口", description = "统计让球方获胜赔率六区间（<=1.4, 1.41-1.6, 1.61-1.8, 1.81-2, 2.01-2.2, >=2.21）的胜平负数及穿盘率，细分主让球与客让球")
@RestController
@RequestMapping("/api/handicap-odds-stats")
public class HandicapOddsStatsRecordController {

    private final HandicapOddsStatsRecordService service;

    public HandicapOddsStatsRecordController(HandicapOddsStatsRecordService service) {
        this.service = service;
    }

    @Operation(summary = "查询让球赔率战绩记录列表", description = "支持按起止日期范围筛选记录列表")
    @GetMapping("/list")
    public ApiResponse<List<HandicapOddsStatsRecord>> list(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<HandicapOddsStatsRecord> list = service.getAllRecords(startDate, endDate);
        return ApiResponse.success(list);
    }

    @Operation(summary = "分页查询让球赔率战绩", description = "支持按起止日期分页检索")
    @GetMapping("/page")
    public ApiResponse<Page<HandicapOddsStatsRecord>> page(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        int pageIndex = Math.max(0, page - 1);
        Page<HandicapOddsStatsRecord> recordPage = service.getRecordsPage(startDate, endDate,
                PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "recordDate")));
        return ApiResponse.success(recordPage);
    }

    @Operation(summary = "获取让球赔率六区间大数据宏观全盘汇总", description = "聚合累计总场数、各区间胜平负数、赢球穿盘率、主客让差异与AI策略研判")
    @GetMapping("/summary")
    public ApiResponse<HandicapOddsStatsSummaryVO> summary(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        HandicapOddsStatsSummaryVO summary = service.getMacroSummary(startDate, endDate);
        return ApiResponse.success(summary);
    }

    @Operation(summary = "根据ID获取让球赔率战绩详情")
    @GetMapping("/{id}")
    public ApiResponse<HandicapOddsStatsRecord> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getRecordById(id));
    }

    @Operation(summary = "根据日期获取让球赔率战绩详情")
    @GetMapping("/by-date")
    public ApiResponse<HandicapOddsStatsRecord> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate) {
        return ApiResponse.success(service.getRecordByDate(recordDate));
    }

    @Operation(summary = "新增让球赔率战绩记录")
    @PostMapping
    public ApiResponse<HandicapOddsStatsRecord> create(@Valid @RequestBody HandicapOddsStatsRecordRequest request) {
        HandicapOddsStatsRecord created = service.createRecord(request);
        return ApiResponse.success("新增让球赔率战绩记录成功", created);
    }

    @Operation(summary = "更新让球赔率战绩记录")
    @PutMapping("/{id}")
    public ApiResponse<HandicapOddsStatsRecord> update(
            @PathVariable Long id,
            @Valid @RequestBody HandicapOddsStatsRecordRequest request) {
        HandicapOddsStatsRecord updated = service.updateRecord(id, request);
        return ApiResponse.success("更新让球赔率战绩记录成功", updated);
    }

    @Operation(summary = "删除让球赔率战绩记录")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.deleteRecord(id);
        return ApiResponse.success("删除让球赔率战绩记录成功", null);
    }
}
