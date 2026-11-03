package com.example.winnerLogBackend.controller;

import com.example.winnerLogBackend.common.ApiResponse;
import com.example.winnerLogBackend.dto.request.OddsWaterUpsetRecordRequest;
import com.example.winnerLogBackend.dto.response.OddsWaterUpsetSummaryVO;
import com.example.winnerLogBackend.entity.OddsWaterUpsetRecord;
import com.example.winnerLogBackend.service.OddsWaterUpsetRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "8. 盘口水位冷门翻车大数据分析接口", description = "记录历史总场数、高中低水场数及各自主客场翻车场数，实时洞悉翻车率与冷门规律")
@RestController
@RequestMapping("/api/odds-water-record")
public class OddsWaterUpsetRecordController {

    private final OddsWaterUpsetRecordService oddsWaterUpsetRecordService;

    public OddsWaterUpsetRecordController(OddsWaterUpsetRecordService oddsWaterUpsetRecordService) {
        this.oddsWaterUpsetRecordService = oddsWaterUpsetRecordService;
    }

    @Operation(summary = "查询盘口水位翻车记录列表", description = "支持按起止日期范围筛选记录列表，默认按日期倒序展示")
    @GetMapping
    public ApiResponse<List<OddsWaterUpsetRecord>> list(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<OddsWaterUpsetRecord> list = oddsWaterUpsetRecordService.queryList(startDate, endDate);
        return ApiResponse.success(list);
    }

    @Operation(summary = "分页查询盘口水位翻车战绩", description = "支持按起止日期分页检索水位战绩数据")
    @GetMapping("/page")
    public ApiResponse<Page<OddsWaterUpsetRecord>> page(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<OddsWaterUpsetRecord> recordPage = oddsWaterUpsetRecordService.queryPage(startDate, endDate, page, size);
        return ApiResponse.success(recordPage);
    }

    @Operation(summary = "获取水位翻车大数据宏观汇总指标", description = "聚合累计历史总场数、高中低水翻车率、主客场翻车率、冷门偏向与风险研判等指标")
    @GetMapping("/summary")
    public ApiResponse<OddsWaterUpsetSummaryVO> summary(
            @Parameter(description = "开始日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        OddsWaterUpsetSummaryVO summary = oddsWaterUpsetRecordService.getSummaryStats(startDate, endDate);
        return ApiResponse.success(summary);
    }

    @Operation(summary = "获取单条水位翻车记录详情")
    @GetMapping("/{id}")
    public ApiResponse<OddsWaterUpsetRecord> getById(@PathVariable("id") Long id) {
        OddsWaterUpsetRecord record = oddsWaterUpsetRecordService.getById(id);
        return ApiResponse.success(record);
    }

    @Operation(summary = "录入盘口水位翻车记录")
    @PostMapping
    public ApiResponse<OddsWaterUpsetRecord> create(@Valid @RequestBody OddsWaterUpsetRecordRequest request) {
        request.setId(null);
        OddsWaterUpsetRecord record = oddsWaterUpsetRecordService.saveOrUpdate(request);
        return ApiResponse.success("水位翻车战绩录入成功", record);
    }

    @Operation(summary = "修改盘口水位翻车记录")
    @PutMapping("/{id}")
    public ApiResponse<OddsWaterUpsetRecord> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody OddsWaterUpsetRecordRequest request) {
        request.setId(id);
        OddsWaterUpsetRecord record = oddsWaterUpsetRecordService.saveOrUpdate(request);
        return ApiResponse.success("水位翻车战绩更新成功", record);
    }

    @Operation(summary = "删除盘口水位翻车记录")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        oddsWaterUpsetRecordService.deleteById(id);
        return ApiResponse.success("记录删除成功", null);
    }
}
