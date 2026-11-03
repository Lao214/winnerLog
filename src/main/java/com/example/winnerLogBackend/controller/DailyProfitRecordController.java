package com.example.winnerLogBackend.controller;

import com.example.winnerLogBackend.common.ApiResponse;
import com.example.winnerLogBackend.dto.request.DailyProfitRecordRequest;
import com.example.winnerLogBackend.dto.response.DailyProfitSummaryVO;
import com.example.winnerLogBackend.entity.DailyProfitRecord;
import com.example.winnerLogBackend.service.DailyProfitRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "6. 每日成本利润与大数据分析接口", description = "记录每日比赛投注成本、利润、穿盘表现及双串关对冲策略分析")
@RestController
@RequestMapping("/api/daily-profit")
public class DailyProfitRecordController {

    private final DailyProfitRecordService dailyProfitRecordService;

    public DailyProfitRecordController(DailyProfitRecordService dailyProfitRecordService) {
        this.dailyProfitRecordService = dailyProfitRecordService;
    }

    @Operation(summary = "查询每日成本利润记录列表", description = "支持按起止日期范围筛选记录列表，默认按日期倒序展示")
    @GetMapping
    public ApiResponse<List<DailyProfitRecord>> list(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DailyProfitRecord> list = dailyProfitRecordService.queryList(startDate, endDate);
        return ApiResponse.success(list);
    }

    @Operation(summary = "分页查询每日成本利润战绩", description = "支持按起止日期分页检索每日战绩数据")
    @GetMapping("/page")
    public ApiResponse<Page<DailyProfitRecord>> page(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DailyProfitRecord> recordPage = dailyProfitRecordService.queryPage(startDate, endDate, page, size);
        return ApiResponse.success(recordPage);
    }

    @Operation(summary = "获取大数据宏观汇总与策略收益指标", description = "聚合累计总成本、总利润、综合ROI、穿盘胜率、3M2S与2M3S策略对比等指标")
    @GetMapping("/summary")
    public ApiResponse<DailyProfitSummaryVO> summary(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        DailyProfitSummaryVO summary = dailyProfitRecordService.getSummaryStats(startDate, endDate);
        return ApiResponse.success(summary);
    }

    @Operation(summary = "获取单日成本利润记录详情")
    @GetMapping("/{id}")
    public ApiResponse<DailyProfitRecord> getById(@PathVariable("id") Long id) {
        DailyProfitRecord record = dailyProfitRecordService.getById(id);
        return ApiResponse.success(record);
    }

    @Operation(summary = "录入每日成本与利润战绩")
    @PostMapping
    public ApiResponse<DailyProfitRecord> create(@Valid @RequestBody DailyProfitRecordRequest request) {
        request.setId(null);
        DailyProfitRecord record = dailyProfitRecordService.saveOrUpdate(request);
        return ApiResponse.success("每日战绩录入成功", record);
    }

    @Operation(summary = "修改每日成本与利润战绩")
    @PutMapping("/{id}")
    public ApiResponse<DailyProfitRecord> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody DailyProfitRecordRequest request) {
        request.setId(id);
        DailyProfitRecord record = dailyProfitRecordService.saveOrUpdate(request);
        return ApiResponse.success("每日战绩更新成功", record);
    }

    @Operation(summary = "删除每日成本与利润战绩记录")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        dailyProfitRecordService.deleteById(id);
        return ApiResponse.success("记录删除成功", null);
    }
}
