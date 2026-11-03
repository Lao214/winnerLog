package com.example.winnerLogBackend.controller;

import com.example.winnerLogBackend.common.ApiResponse;
import com.example.winnerLogBackend.dto.request.DailyGoalsProfitRecordRequest;
import com.example.winnerLogBackend.dto.response.DailyGoalsProfitSummaryVO;
import com.example.winnerLogBackend.entity.DailyGoalsProfitRecord;
import com.example.winnerLogBackend.service.DailyGoalsProfitRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "7. 每日总进球数成本利润与规律大数据接口", description = "记录每日比赛总进球数规律符合度、成本利润及双串关对冲策略分析")
@RestController
@RequestMapping("/api/daily-goals-profit")
public class DailyGoalsProfitRecordController {

    private final DailyGoalsProfitRecordService dailyGoalsProfitRecordService;

    public DailyGoalsProfitRecordController(DailyGoalsProfitRecordService dailyGoalsProfitRecordService) {
        this.dailyGoalsProfitRecordService = dailyGoalsProfitRecordService;
    }

    @Operation(summary = "查询每日总进球数战绩记录列表", description = "支持按起止日期范围筛选进球数记录，默认按比赛日倒序展示")
    @GetMapping
    public ApiResponse<List<DailyGoalsProfitRecord>> list(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DailyGoalsProfitRecord> list = dailyGoalsProfitRecordService.queryList(startDate, endDate);
        return ApiResponse.success(list);
    }

    @Operation(summary = "分页查询每日总进球数成本利润记录")
    @GetMapping("/page")
    public ApiResponse<Page<DailyGoalsProfitRecord>> page(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DailyGoalsProfitRecord> recordPage = dailyGoalsProfitRecordService.queryPage(startDate, endDate, page, size);
        return ApiResponse.success(recordPage);
    }

    @Operation(summary = "获取总进球数大数据宏观汇总与策略收益指标", description = "聚合累计总成本、总利润、综合ROI、规律符合率、3M2S与2M3S策略对比等")
    @GetMapping("/summary")
    public ApiResponse<DailyGoalsProfitSummaryVO> summary(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        DailyGoalsProfitSummaryVO summary = dailyGoalsProfitRecordService.getSummaryStats(startDate, endDate);
        return ApiResponse.success(summary);
    }

    @Operation(summary = "获取单日总进球数记录详情")
    @GetMapping("/{id}")
    public ApiResponse<DailyGoalsProfitRecord> getById(@PathVariable("id") Long id) {
        DailyGoalsProfitRecord record = dailyGoalsProfitRecordService.getById(id);
        return ApiResponse.success(record);
    }

    @Operation(summary = "录入每日总进球数成本与利润战绩")
    @PostMapping
    public ApiResponse<DailyGoalsProfitRecord> create(@Valid @RequestBody DailyGoalsProfitRecordRequest request) {
        request.setId(null);
        DailyGoalsProfitRecord record = dailyGoalsProfitRecordService.saveOrUpdate(request);
        return ApiResponse.success("总进球数战绩录入成功", record);
    }

    @Operation(summary = "修改每日总进球数成本与利润战绩")
    @PutMapping("/{id}")
    public ApiResponse<DailyGoalsProfitRecord> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody DailyGoalsProfitRecordRequest request) {
        request.setId(id);
        DailyGoalsProfitRecord record = dailyGoalsProfitRecordService.saveOrUpdate(request);
        return ApiResponse.success("总进球数战绩更新成功", record);
    }

    @Operation(summary = "删除每日总进球数成本与利润战绩记录")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        dailyGoalsProfitRecordService.deleteById(id);
        return ApiResponse.success("总进球数战绩记录删除成功", null);
    }
}
