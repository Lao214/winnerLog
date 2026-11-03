package com.example.winnerLogBackend.service.impl;

import com.example.winnerLogBackend.common.BusinessException;
import com.example.winnerLogBackend.dto.request.DailyWaterComboRecordRequest;
import com.example.winnerLogBackend.dto.response.DailyWaterComboSummaryVO;
import com.example.winnerLogBackend.entity.DailyWaterComboRecord;
import com.example.winnerLogBackend.repository.DailyWaterComboRecordRepository;
import com.example.winnerLogBackend.service.DailyWaterComboRecordService;
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
public class DailyWaterComboRecordServiceImpl implements DailyWaterComboRecordService {

    private final DailyWaterComboRecordRepository repository;

    public DailyWaterComboRecordServiceImpl(DailyWaterComboRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public DailyWaterComboRecord saveOrUpdate(DailyWaterComboRecordRequest request) {
        DailyWaterComboRecord record;
        if (request.getId() != null) {
            record = repository.findById(request.getId())
                    .orElseThrow(() -> new BusinessException("未找到ID为 " + request.getId() + " 的水位串子战绩记录"));
            if (repository.existsByRecordDateAndIdNot(request.getRecordDate(), request.getId())) {
                throw new BusinessException("日期 " + request.getRecordDate() + " 的水位串子战绩已存在，无法重复更新至该日期");
            }
        } else {
            if (repository.existsByRecordDate(request.getRecordDate())) {
                throw new BusinessException("日期 " + request.getRecordDate() + " 的水位串子战绩已存在，请勿重复新增");
            }
            record = new DailyWaterComboRecord();
        }

        record.setRecordDate(request.getRecordDate());
        record.setMatchCount(request.getMatchCount());
        record.setTicketCost(request.getTicketCost());
        record.setFinalProfit(request.getFinalProfit());
        record.setLongComboCost(request.getLongComboCost() != null ? request.getLongComboCost() : BigDecimal.ZERO);
        record.setLongComboProfit(request.getLongComboProfit() != null ? request.getLongComboProfit() : BigDecimal.ZERO);
        if (request.getComboType() != null && !request.getComboType().trim().isEmpty()) {
            record.setComboType(request.getComboType().trim());
        }
        record.setRemarks(request.getRemarks());

        // 计算所有衍生指标 (总回款、ROI、场均、盈亏状态评级)
        record.calculateMetrics();

        return repository.save(record);
    }

    @Override
    public DailyWaterComboRecord getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("未找到该水位串子战绩记录"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new BusinessException("水位串子战绩记录不存在或已被删除");
        }
        repository.deleteById(id);
    }

    @Override
    public List<DailyWaterComboRecord> queryList(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return repository.findByRecordDateBetweenOrderByRecordDateAsc(startDate, endDate);
        }
        return repository.findAllByOrderByRecordDateDesc();
    }

    @Override
    public Page<DailyWaterComboRecord> queryPage(LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "recordDate"));
        if (startDate != null && endDate != null) {
            return repository.findByRecordDateBetween(startDate, endDate, pageable);
        }
        return repository.findAll(pageable);
    }

    @Override
    public DailyWaterComboSummaryVO getSummaryStats(LocalDate startDate, LocalDate endDate) {
        List<DailyWaterComboRecord> records;
        if (startDate != null && endDate != null) {
            records = repository.findByRecordDateBetweenOrderByRecordDateAsc(startDate, endDate);
        } else {
            records = repository.findAllOrderByRecordDateAsc();
        }

        DailyWaterComboSummaryVO summary = new DailyWaterComboSummaryVO();
        if (records == null || records.isEmpty()) {
            return summary;
        }

        summary.setTotalDays(records.size());

        int totalMatches = 0;
        BigDecimal totalRegularCost = BigDecimal.ZERO;
        BigDecimal totalRegularProfit = BigDecimal.ZERO;

        BigDecimal totalLongComboCost = BigDecimal.ZERO;
        BigDecimal totalLongComboProfit = BigDecimal.ZERO;
        int longComboWinDays = 0;

        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalProfit = BigDecimal.ZERO;
        BigDecimal totalReturn = BigDecimal.ZERO;

        int profitDays = 0;
        int lossDays = 0;
        int breakevenDays = 0;

        BigDecimal maxProfit = null;
        BigDecimal maxLoss = null;

        for (DailyWaterComboRecord r : records) {
            int matches = r.getMatchCount() != null ? r.getMatchCount() : 0;
            totalMatches += matches;

            BigDecimal regularCost = r.getTicketCost() != null ? r.getTicketCost() : BigDecimal.ZERO;
            BigDecimal regularProfit = r.getFinalProfit() != null ? r.getFinalProfit() : BigDecimal.ZERO;
            totalRegularCost = totalRegularCost.add(regularCost);
            totalRegularProfit = totalRegularProfit.add(regularProfit);

            BigDecimal lcCost = r.getLongComboCost() != null ? r.getLongComboCost() : BigDecimal.ZERO;
            BigDecimal lcProfit = r.getLongComboProfit() != null ? r.getLongComboProfit() : BigDecimal.ZERO;
            totalLongComboCost = totalLongComboCost.add(lcCost);
            totalLongComboProfit = totalLongComboProfit.add(lcProfit);
            if (lcProfit.compareTo(BigDecimal.ZERO) > 0) {
                longComboWinDays++;
            }

            BigDecimal dayCost = r.getTotalCost() != null ? r.getTotalCost() : regularCost.add(lcCost);
            BigDecimal dayProfit = r.getTotalProfit() != null ? r.getTotalProfit() : regularProfit.add(lcProfit);
            BigDecimal dayReturn = r.getTotalReturn() != null ? r.getTotalReturn() : dayCost.add(dayProfit);

            totalCost = totalCost.add(dayCost);
            totalProfit = totalProfit.add(dayProfit);
            totalReturn = totalReturn.add(dayReturn);

            if (dayProfit.compareTo(BigDecimal.ZERO) > 0) {
                profitDays++;
            } else if (dayProfit.compareTo(BigDecimal.ZERO) < 0) {
                lossDays++;
            } else {
                breakevenDays++;
            }

            if (maxProfit == null || dayProfit.compareTo(maxProfit) > 0) {
                maxProfit = dayProfit;
            }
            if (maxLoss == null || dayProfit.compareTo(maxLoss) < 0) {
                maxLoss = dayProfit;
            }
        }

        summary.setTotalMatches(totalMatches);

        // 常规串细分
        summary.setTotalRegularCost(totalRegularCost);
        summary.setTotalRegularProfit(totalRegularProfit);
        if (totalRegularCost.compareTo(BigDecimal.ZERO) > 0) {
            summary.setRegularRoi(totalRegularProfit.multiply(BigDecimal.valueOf(100)).divide(totalRegularCost, 2, RoundingMode.HALF_UP));
        }

        // 长串细分
        summary.setTotalLongComboCost(totalLongComboCost);
        summary.setTotalLongComboProfit(totalLongComboProfit);
        summary.setLongComboWinDays(longComboWinDays);
        if (totalLongComboCost.compareTo(BigDecimal.ZERO) > 0) {
            summary.setLongComboRoi(totalLongComboProfit.multiply(BigDecimal.valueOf(100)).divide(totalLongComboCost, 2, RoundingMode.HALF_UP));
        }

        // 综合统计
        summary.setTotalCost(totalCost);
        summary.setTotalProfit(totalProfit);
        summary.setTotalReturn(totalReturn);

        summary.setProfitableDays(profitDays);
        summary.setLossDays(lossDays);
        summary.setBreakevenDays(breakevenDays);

        summary.setMaxSingleDayProfit(maxProfit != null ? maxProfit : BigDecimal.ZERO);
        summary.setMaxSingleDayLoss(maxLoss != null ? maxLoss : BigDecimal.ZERO);

        int totalDays = records.size();
        BigDecimal daysDec = BigDecimal.valueOf(totalDays);

        // 胜率 (盈利日占比)
        summary.setWinDaysRate(BigDecimal.valueOf(profitDays).multiply(BigDecimal.valueOf(100)).divide(daysDec, 2, RoundingMode.HALF_UP));

        // 综合 ROI
        if (totalCost.compareTo(BigDecimal.ZERO) > 0) {
            summary.setOverallRoi(totalProfit.multiply(BigDecimal.valueOf(100)).divide(totalCost, 2, RoundingMode.HALF_UP));
        }

        // 日均指标
        summary.setAvgDailyCost(totalCost.divide(daysDec, 2, RoundingMode.HALF_UP));
        summary.setAvgDailyProfit(totalProfit.divide(daysDec, 2, RoundingMode.HALF_UP));

        // 场均指标
        if (totalMatches > 0) {
            BigDecimal matchesDec = BigDecimal.valueOf(totalMatches);
            summary.setAvgMatchCost(totalCost.divide(matchesDec, 2, RoundingMode.HALF_UP));
            summary.setAvgMatchProfit(totalProfit.divide(matchesDec, 2, RoundingMode.HALF_UP));
        }

        // 大数据收益与风控综合评估 (含长串贡献)
        String longComboInsight = "";
        if (totalLongComboCost.compareTo(BigDecimal.ZERO) > 0) {
            if (totalLongComboProfit.compareTo(BigDecimal.ZERO) > 0) {
                longComboInsight = "，长串斩获净利¥" + totalLongComboProfit + " (ROI " + summary.getLongComboRoi() + "%) 爆发力极佳！";
            } else {
                longComboInsight = "，长串目前处于回撤积累期 (净利¥" + totalLongComboProfit + ")，建议控制长串单次本金。";
            }
        }

        if (summary.getOverallRoi().compareTo(BigDecimal.valueOf(50)) >= 0) {
            summary.setRiskEvaluation("🔥 超高爆发型实战策略 (综合ROI超50%，胜率" + summary.getWinDaysRate() + "%)" + longComboInsight);
        } else if (summary.getOverallRoi().compareTo(BigDecimal.valueOf(20)) >= 0) {
            summary.setRiskEvaluation("✨ 稳健复利型实战策略 (综合ROI在20%~50%，正收益显著)" + longComboInsight);
        } else if (summary.getOverallRoi().compareTo(BigDecimal.ZERO) >= 0) {
            summary.setRiskEvaluation("🛡️ 保本微利防御型策略 (资金安全边际高)" + longComboInsight);
        } else {
            summary.setRiskEvaluation("⚠️ 出现短期回撤波动 (建议适度控制单日主串与长串打票成本)");
        }

        return summary;
    }
}
