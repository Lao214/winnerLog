package com.example.winnerLogBackend.service.impl;

import com.example.winnerLogBackend.common.BusinessException;
import com.example.winnerLogBackend.dto.request.OddsWaterUpsetRecordRequest;
import com.example.winnerLogBackend.dto.response.OddsWaterUpsetSummaryVO;
import com.example.winnerLogBackend.entity.OddsWaterUpsetRecord;
import com.example.winnerLogBackend.repository.OddsWaterUpsetRecordRepository;
import com.example.winnerLogBackend.service.OddsWaterUpsetRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class OddsWaterUpsetRecordServiceImpl implements OddsWaterUpsetRecordService {

    private final OddsWaterUpsetRecordRepository repository;

    public OddsWaterUpsetRecordServiceImpl(OddsWaterUpsetRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public OddsWaterUpsetRecord saveOrUpdate(OddsWaterUpsetRecordRequest request) {
        // 逻辑校验：各盘口翻车场数不得大于对应盘口总场数
        int highUpset = request.getHighWaterHomeUpsetCount() + request.getHighWaterAwayUpsetCount();
        if (highUpset > request.getHighWaterMatches()) {
            throw new BusinessException("高水主客场翻车场数之和 (" + highUpset + ") 不能大于高水总场数 (" + request.getHighWaterMatches() + ")");
        }
        int midUpset = request.getMidWaterHomeUpsetCount() + request.getMidWaterAwayUpsetCount();
        if (midUpset > request.getMidWaterMatches()) {
            throw new BusinessException("中水主客场翻车场数之和 (" + midUpset + ") 不能大于中水总场数 (" + request.getMidWaterMatches() + ")");
        }
        int lowUpset = request.getLowWaterHomeUpsetCount() + request.getLowWaterAwayUpsetCount();
        if (lowUpset > request.getLowWaterMatches()) {
            throw new BusinessException("低水主客场翻车场数之和 (" + lowUpset + ") 不能大于低水总场数 (" + request.getLowWaterMatches() + ")");
        }

        OddsWaterUpsetRecord record;
        if (request.getId() != null) {
            record = repository.findById(request.getId())
                    .orElseThrow(() -> new BusinessException("未找到ID为 " + request.getId() + " 的盘口水位翻车战绩记录"));
            if (repository.existsByRecordDateAndIdNot(request.getRecordDate(), request.getId())) {
                throw new BusinessException("日期 " + request.getRecordDate() + " 的盘口水位记录已存在，无法重复更新至该日期");
            }
        } else {
            if (repository.existsByRecordDate(request.getRecordDate())) {
                throw new BusinessException("日期 " + request.getRecordDate() + " 的盘口水位记录已存在，请勿重复新增");
            }
            record = new OddsWaterUpsetRecord();
        }

        record.setRecordDate(request.getRecordDate());
        record.setBatchTitle(request.getBatchTitle());
        record.setTotalMatches(request.getTotalMatches());

        record.setHighWaterMatches(request.getHighWaterMatches());
        record.setHighWaterHomeUpsetCount(request.getHighWaterHomeUpsetCount());
        record.setHighWaterAwayUpsetCount(request.getHighWaterAwayUpsetCount());

        record.setMidWaterMatches(request.getMidWaterMatches());
        record.setMidWaterHomeUpsetCount(request.getMidWaterHomeUpsetCount());
        record.setMidWaterAwayUpsetCount(request.getMidWaterAwayUpsetCount());

        record.setLowWaterMatches(request.getLowWaterMatches());
        record.setLowWaterHomeUpsetCount(request.getLowWaterHomeUpsetCount());
        record.setLowWaterAwayUpsetCount(request.getLowWaterAwayUpsetCount());

        record.setRemarks(request.getRemarks());

        // 计算所有大数据衍生指标
        record.calculateMetrics();

        return repository.save(record);
    }

    @Override
    public OddsWaterUpsetRecord getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("未找到该盘口水位翻车记录"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new BusinessException("盘口水位翻车记录不存在或已被删除");
        }
        repository.deleteById(id);
    }

    @Override
    public List<OddsWaterUpsetRecord> queryList(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return repository.findByRecordDateBetweenOrderByRecordDateAsc(startDate, endDate);
        }
        return repository.findAllByOrderByRecordDateDesc();
    }

    @Override
    public Page<OddsWaterUpsetRecord> queryPage(LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "recordDate"));
        if (startDate != null && endDate != null) {
            return repository.findByRecordDateBetween(startDate, endDate, pageable);
        }
        return repository.findAll(pageable);
    }

    @Override
    public OddsWaterUpsetSummaryVO getSummaryStats(LocalDate startDate, LocalDate endDate) {
        List<OddsWaterUpsetRecord> records;
        if (startDate != null && endDate != null) {
            records = repository.findByRecordDateBetweenOrderByRecordDateAsc(startDate, endDate);
        } else {
            records = repository.findAllOrderByRecordDateAsc();
        }

        OddsWaterUpsetSummaryVO summary = new OddsWaterUpsetSummaryVO();
        if (records == null || records.isEmpty()) {
            return summary;
        }

        summary.setTotalRecords(records.size());

        int totalMatches = 0;
        int totalUpset = 0;
        int totalNormal = 0;
        int totalHomeUpset = 0;
        int totalAwayUpset = 0;

        int totalHwMatches = 0;
        int totalHwHomeUpset = 0;
        int totalHwAwayUpset = 0;
        int totalHwUpset = 0;

        int totalMwMatches = 0;
        int totalMwHomeUpset = 0;
        int totalMwAwayUpset = 0;
        int totalMwUpset = 0;

        int totalLwMatches = 0;
        int totalLwHomeUpset = 0;
        int totalLwAwayUpset = 0;
        int totalLwUpset = 0;

        for (OddsWaterUpsetRecord r : records) {
            int matchCount = r.getTotalMatches() != null ? r.getTotalMatches() : 0;
            totalMatches += matchCount;

            int hwM = r.getHighWaterMatches() != null ? r.getHighWaterMatches() : 0;
            int hwH = r.getHighWaterHomeUpsetCount() != null ? r.getHighWaterHomeUpsetCount() : 0;
            int hwA = r.getHighWaterAwayUpsetCount() != null ? r.getHighWaterAwayUpsetCount() : 0;
            int hwU = r.getHighWaterUpsetCount() != null ? r.getHighWaterUpsetCount() : (hwH + hwA);
            totalHwMatches += hwM;
            totalHwHomeUpset += hwH;
            totalHwAwayUpset += hwA;
            totalHwUpset += hwU;

            int mwM = r.getMidWaterMatches() != null ? r.getMidWaterMatches() : 0;
            int mwH = r.getMidWaterHomeUpsetCount() != null ? r.getMidWaterHomeUpsetCount() : 0;
            int mwA = r.getMidWaterAwayUpsetCount() != null ? r.getMidWaterAwayUpsetCount() : 0;
            int mwU = r.getMidWaterUpsetCount() != null ? r.getMidWaterUpsetCount() : (mwH + mwA);
            totalMwMatches += mwM;
            totalMwHomeUpset += mwH;
            totalMwAwayUpset += mwA;
            totalMwUpset += mwU;

            int lwM = r.getLowWaterMatches() != null ? r.getLowWaterMatches() : 0;
            int lwH = r.getLowWaterHomeUpsetCount() != null ? r.getLowWaterHomeUpsetCount() : 0;
            int lwA = r.getLowWaterAwayUpsetCount() != null ? r.getLowWaterAwayUpsetCount() : 0;
            int lwU = r.getLowWaterUpsetCount() != null ? r.getLowWaterUpsetCount() : (lwH + lwA);
            totalLwMatches += lwM;
            totalLwHomeUpset += lwH;
            totalLwAwayUpset += lwA;
            totalLwUpset += lwU;

            totalHomeUpset += (hwH + mwH + lwH);
            totalAwayUpset += (hwA + mwA + lwA);
            totalUpset += (hwU + mwU + lwU);
        }

        totalNormal = Math.max(0, totalMatches - totalUpset);

        summary.setTotalMatches(totalMatches);
        summary.setTotalUpsetCount(totalUpset);
        summary.setTotalNormalCount(totalNormal);
        summary.setTotalHomeUpsetCount(totalHomeUpset);
        summary.setTotalAwayUpsetCount(totalAwayUpset);

        BigDecimal totalDec = totalMatches > 0 ? BigDecimal.valueOf(totalMatches) : BigDecimal.ONE;

        // 综合翻车率
        if (totalMatches > 0) {
            summary.setOverallUpsetRate(BigDecimal.valueOf(totalUpset).multiply(BigDecimal.valueOf(100)).divide(totalDec, 2, RoundingMode.HALF_UP));
            summary.setOverallHomeUpsetRate(BigDecimal.valueOf(totalHomeUpset).multiply(BigDecimal.valueOf(100)).divide(totalDec, 2, RoundingMode.HALF_UP));
            summary.setOverallAwayUpsetRate(BigDecimal.valueOf(totalAwayUpset).multiply(BigDecimal.valueOf(100)).divide(totalDec, 2, RoundingMode.HALF_UP));
        }

        if (totalUpset > 0) {
            BigDecimal upsetDec = BigDecimal.valueOf(totalUpset);
            summary.setHomeUpsetRatio(BigDecimal.valueOf(totalHomeUpset).multiply(BigDecimal.valueOf(100)).divide(upsetDec, 2, RoundingMode.HALF_UP));
            summary.setAwayUpsetRatio(BigDecimal.valueOf(totalAwayUpset).multiply(BigDecimal.valueOf(100)).divide(upsetDec, 2, RoundingMode.HALF_UP));
        }

        // 高水汇总
        summary.setTotalHighWaterMatches(totalHwMatches);
        summary.setTotalHighWaterUpsetCount(totalHwUpset);
        summary.setTotalHighWaterHomeUpsetCount(totalHwHomeUpset);
        summary.setTotalHighWaterAwayUpsetCount(totalHwAwayUpset);
        if (totalHwMatches > 0) {
            BigDecimal hwDec = BigDecimal.valueOf(totalHwMatches);
            summary.setAvgHighWaterUpsetRate(BigDecimal.valueOf(totalHwUpset).multiply(BigDecimal.valueOf(100)).divide(hwDec, 2, RoundingMode.HALF_UP));
            summary.setAvgHighWaterHomeUpsetRate(BigDecimal.valueOf(totalHwHomeUpset).multiply(BigDecimal.valueOf(100)).divide(hwDec, 2, RoundingMode.HALF_UP));
            summary.setAvgHighWaterAwayUpsetRate(BigDecimal.valueOf(totalHwAwayUpset).multiply(BigDecimal.valueOf(100)).divide(hwDec, 2, RoundingMode.HALF_UP));
        }
        if (totalMatches > 0) {
            summary.setHighWaterShareRate(BigDecimal.valueOf(totalHwMatches).multiply(BigDecimal.valueOf(100)).divide(totalDec, 2, RoundingMode.HALF_UP));
        }

        // 中水汇总
        summary.setTotalMidWaterMatches(totalMwMatches);
        summary.setTotalMidWaterUpsetCount(totalMwUpset);
        summary.setTotalMidWaterHomeUpsetCount(totalMwHomeUpset);
        summary.setTotalMidWaterAwayUpsetCount(totalMwAwayUpset);
        if (totalMwMatches > 0) {
            BigDecimal mwDec = BigDecimal.valueOf(totalMwMatches);
            summary.setAvgMidWaterUpsetRate(BigDecimal.valueOf(totalMwUpset).multiply(BigDecimal.valueOf(100)).divide(mwDec, 2, RoundingMode.HALF_UP));
            summary.setAvgMidWaterHomeUpsetRate(BigDecimal.valueOf(totalMwHomeUpset).multiply(BigDecimal.valueOf(100)).divide(mwDec, 2, RoundingMode.HALF_UP));
            summary.setAvgMidWaterAwayUpsetRate(BigDecimal.valueOf(totalMwAwayUpset).multiply(BigDecimal.valueOf(100)).divide(mwDec, 2, RoundingMode.HALF_UP));
        }
        if (totalMatches > 0) {
            summary.setMidWaterShareRate(BigDecimal.valueOf(totalMwMatches).multiply(BigDecimal.valueOf(100)).divide(totalDec, 2, RoundingMode.HALF_UP));
        }

        // 低水汇总
        summary.setTotalLowWaterMatches(totalLwMatches);
        summary.setTotalLowWaterUpsetCount(totalLwUpset);
        summary.setTotalLowWaterHomeUpsetCount(totalLwHomeUpset);
        summary.setTotalLowWaterAwayUpsetCount(totalLwAwayUpset);
        if (totalLwMatches > 0) {
            BigDecimal lwDec = BigDecimal.valueOf(totalLwMatches);
            summary.setAvgLowWaterUpsetRate(BigDecimal.valueOf(totalLwUpset).multiply(BigDecimal.valueOf(100)).divide(lwDec, 2, RoundingMode.HALF_UP));
            summary.setAvgLowWaterHomeUpsetRate(BigDecimal.valueOf(totalLwHomeUpset).multiply(BigDecimal.valueOf(100)).divide(lwDec, 2, RoundingMode.HALF_UP));
            summary.setAvgLowWaterAwayUpsetRate(BigDecimal.valueOf(totalLwAwayUpset).multiply(BigDecimal.valueOf(100)).divide(lwDec, 2, RoundingMode.HALF_UP));
        }
        if (totalMatches > 0) {
            summary.setLowWaterShareRate(BigDecimal.valueOf(totalLwMatches).multiply(BigDecimal.valueOf(100)).divide(totalDec, 2, RoundingMode.HALF_UP));
        }

        // 最高翻车率水位研判
        BigDecimal hwRate = summary.getAvgHighWaterUpsetRate();
        BigDecimal mwRate = summary.getAvgMidWaterUpsetRate();
        BigDecimal lwRate = summary.getAvgLowWaterUpsetRate();
        if (hwRate.compareTo(mwRate) >= 0 && hwRate.compareTo(lwRate) >= 0) {
            summary.setHighestRiskWaterLevel("高水盘口 (" + hwRate + "%)");
        } else if (mwRate.compareTo(hwRate) >= 0 && mwRate.compareTo(lwRate) >= 0) {
            summary.setHighestRiskWaterLevel("中水盘口 (" + mwRate + "%)");
        } else {
            summary.setHighestRiskWaterLevel("低水盘口 (" + lwRate + "% - 警惕大热必死)");
        }

        // 主客倾向研判
        if (totalHomeUpset > totalAwayUpset) {
            summary.setUpsetTendency("主场爆冷偏多 (" + summary.getHomeUpsetRatio() + "%)");
        } else if (totalAwayUpset > totalHomeUpset) {
            summary.setUpsetTendency("客场爆冷偏多 (" + summary.getAwayUpsetRatio() + "%)");
        } else {
            summary.setUpsetTendency("主客场爆冷均衡 (各50%)");
        }

        // 大数据研判建议
        if (summary.getOverallUpsetRate().compareTo(BigDecimal.valueOf(40)) >= 0) {
            summary.setRiskAssessmentSummary("总体翻车率达 " + summary.getOverallUpsetRate() + "%，冷门频发！建议高水防冷、低水慎选");
        } else if (summary.getOverallUpsetRate().compareTo(BigDecimal.valueOf(25)) >= 0) {
            summary.setRiskAssessmentSummary("总体翻车率 " + summary.getOverallUpsetRate() + "%，处于中度波动区间，可针对" + summary.getUpsetTendency() + "做对冲防御");
        } else {
            summary.setRiskAssessmentSummary("总体翻车率 " + summary.getOverallUpsetRate() + "%，正路打出概率高，适合稳健型策略");
        }

        return summary;
    }
}
