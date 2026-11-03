package com.example.winnerLogBackend.controller;

import com.example.winnerLogBackend.common.ApiResponse;
import com.example.winnerLogBackend.dto.request.DailyWaterComboRecordRequest;
import com.example.winnerLogBackend.dto.response.DailyWaterComboSummaryVO;
import com.example.winnerLogBackend.entity.DailyWaterComboRecord;
import com.example.winnerLogBackend.service.DailyWaterComboRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "9. 每日水位规律串子战绩与大数据分析接口", description = "手动记录每日比赛场数、打票成本、最终利润，实时呈现大数据ROI、回款与走势分析")
@RestController
@RequestMapping("/api/water-combo-record")
public class DailyWaterComboRecordController {

    private final DailyWaterComboRecordService dailyWaterComboRecordService;

    public DailyWaterComboRecordController(DailyWaterComboRecordService dailyWaterComboRecordService) {
        this.dailyWaterComboRecordService = dailyWaterComboRecordService;
    }

    @Operation(summary = "查询每日水位串子战绩列表", description = "支持按起止日期范围筛选记录列表，默认按日期倒序展示")
    @GetMapping
    public ApiResponse<List<DailyWaterComboRecord>> list(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DailyWaterComboRecord> list = dailyWaterComboRecordService.queryList(startDate, endDate);
        return ApiResponse.success(list);
    }

    @Operation(summary = "分页查询每日水位串子战绩", description = "支持按起止日期分页检索水位串关战绩数据")
    @GetMapping("/page")
    public ApiResponse<Page<DailyWaterComboRecord>> page(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DailyWaterComboRecord> recordPage = dailyWaterComboRecordService.queryPage(startDate, endDate, page, size);
        return ApiResponse.success(recordPage);
    }

    @Operation(summary = "获取水位串子战绩大数据宏观汇总指标", description = "聚合累计总场数、总打票成本、总净利润、综合ROI、红单胜率、单日极值等指标")
    @GetMapping("/summary")
    public ApiResponse<DailyWaterComboSummaryVO> summary(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        DailyWaterComboSummaryVO summary = dailyWaterComboRecordService.getSummaryStats(startDate, endDate);
        return ApiResponse.success(summary);
    }

    @Operation(summary = "获取单日水位串子战绩详情")
    @GetMapping("/{id}")
    public ApiResponse<DailyWaterComboRecord> getById(@PathVariable("id") Long id) {
        DailyWaterComboRecord record = dailyWaterComboRecordService.getById(id);
        return ApiResponse.success(record);
    }

    @Operation(summary = "录入每日水位串子战绩")
    @PostMapping
    public ApiResponse<DailyWaterComboRecord> create(@Valid @RequestBody DailyWaterComboRecordRequest request) {
        request.setId(null);
        DailyWaterComboRecord record = dailyWaterComboRecordService.saveOrUpdate(request);
        return ApiResponse.success("每日水位串子战绩录入成功", record);
    }

    @Operation(summary = "修改每日水位串子战绩")
    @PutMapping("/{id}")
    public ApiResponse<DailyWaterComboRecord> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody DailyWaterComboRecordRequest request) {
        request.setId(id);
        DailyWaterComboRecord record = dailyWaterComboRecordService.saveOrUpdate(request);
        return ApiResponse.success("每日水位串子战绩更新成功", record);
    }

    @Operation(summary = "删除每日水位串子战绩记录")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        dailyWaterComboRecordService.deleteById(id);
        return ApiResponse.success("记录删除成功", null);
    }
}
