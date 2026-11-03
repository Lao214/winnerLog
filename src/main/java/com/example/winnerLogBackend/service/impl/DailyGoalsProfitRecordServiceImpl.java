package com.example.winnerLogBackend.service.impl;

import com.example.winnerLogBackend.common.BusinessException;
import com.example.winnerLogBackend.dto.request.DailyGoalsProfitRecordRequest;
import com.example.winnerLogBackend.dto.response.DailyGoalsProfitSummaryVO;
import com.example.winnerLogBackend.entity.DailyGoalsProfitRecord;
import com.example.winnerLogBackend.repository.DailyGoalsProfitRecordRepository;
import com.example.winnerLogBackend.service.DailyGoalsProfitRecordService;
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
public class DailyGoalsProfitRecordServiceImpl implements DailyGoalsProfitRecordService {

    private final DailyGoalsProfitRecordRepository repository;

    public DailyGoalsProfitRecordServiceImpl(DailyGoalsProfitRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public DailyGoalsProfitRecord saveOrUpdate(DailyGoalsProfitRecordRequest request) {
        DailyGoalsProfitRecord record;
        if (request.getId() != null) {
            record = repository.findById(request.getId())
                    .orElseThrow(() -> new BusinessException("未找到ID为 " + request.getId() + " 的进球数战绩记录"));
            if (repository.existsByMatchDateAndIdNot(request.getMatchDate(), request.getId())) {
                throw new BusinessException("比赛日 " + request.getMatchDate() + " 的进球数战绩记录已存在，无法更新为此日期");
            }
        } else {
            if (repository.existsByMatchDate(request.getMatchDate())) {
                throw new BusinessException("比赛日 " + request.getMatchDate() + " 的进球数战绩记录已存在，请勿重复新增");
            }
            record = new DailyGoalsProfitRecord();
        }

        record.setMatchDate(request.getMatchDate());
        record.setPatternFitCount(request.getPatternFitCount());
        record.setPatternSlightCount(request.getPatternSlightCount());
        record.setPatternMismatchCount(request.getPatternMismatchCount());
        record.setStrategy3m2sCost(request.getStrategy3m2sCost());
        record.setStrategy3m2sProfit(request.getStrategy3m2sProfit());
        record.setStrategy2m3sCost(request.getStrategy2m3sCost());
        record.setStrategy2m3sProfit(request.getStrategy2m3sProfit());
        record.setRemarks(request.getRemarks());

        // 计算所有大数据衍生指标
        record.calculateMetrics();

        return repository.save(record);
    }

    @Override
    public DailyGoalsProfitRecord getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("未找到该进球数战绩记录"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new BusinessException("进球数战绩记录不存在或已被删除");
        }
        repository.deleteById(id);
    }

    @Override
    public List<DailyGoalsProfitRecord> queryList(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return repository.findByMatchDateBetweenOrderByMatchDateAsc(startDate, endDate);
        }
        return repository.findAllByOrderByMatchDateDesc();
    }

    @Override
    public Page<DailyGoalsProfitRecord> queryPage(LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "matchDate"));
        if (startDate != null && endDate != null) {
            return repository.findByMatchDateBetween(startDate, endDate, pageable);
        }
        return repository.findAll(pageable);
    }

    @Override
    public DailyGoalsProfitSummaryVO getSummaryStats(LocalDate startDate, LocalDate endDate) {
        List<DailyGoalsProfitRecord> records;
        if (startDate != null && endDate != null) {
            records = repository.findByMatchDateBetweenOrderByMatchDateAsc(startDate, endDate);
        } else {
            records = repository.findAllOrderByMatchDateAsc();
        }

        DailyGoalsProfitSummaryVO summary = new DailyGoalsProfitSummaryVO();
        if (records == null || records.isEmpty()) {
            return summary;
        }

        summary.setTotalDays(records.size());

        int totalFit = 0;
        int totalSlight = 0;
        int totalMismatch = 0;

        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalProfit = BigDecimal.ZERO;
        BigDecimal totalReturn = BigDecimal.ZERO;

        BigDecimal cost3m2s = BigDecimal.ZERO;
        BigDecimal profit3m2s = BigDecimal.ZERO;

        BigDecimal cost2m3s = BigDecimal.ZERO;
        BigDecimal profit2m3s = BigDecimal.ZERO;

        int profitDays = 0;
        int lossDays = 0;
        int breakevenDays = 0;

        BigDecimal maxProfit = null;
        BigDecimal maxLoss = null;

        for (DailyGoalsProfitRecord r : records) {
            int fit = r.getPatternFitCount() != null ? r.getPatternFitCount() : 0;
            int slight = r.getPatternSlightCount() != null ? r.getPatternSlightCount() : 0;
            int mismatch = r.getPatternMismatchCount() != null ? r.getPatternMismatchCount() : 0;
            totalFit += fit;
            totalSlight += slight;
            totalMismatch += mismatch;

            BigDecimal c3 = r.getStrategy3m2sCost() != null ? r.getStrategy3m2sCost() : BigDecimal.ZERO;
            BigDecimal p3 = r.getStrategy3m2sProfit() != null ? r.getStrategy3m2sProfit() : BigDecimal.ZERO;
            cost3m2s = cost3m2s.add(c3);
            profit3m2s = profit3m2s.add(p3);

            BigDecimal c2 = r.getStrategy2m3sCost() != null ? r.getStrategy2m3sCost() : BigDecimal.ZERO;
            BigDecimal p2 = r.getStrategy2m3sProfit() != null ? r.getStrategy2m3sProfit() : BigDecimal.ZERO;
            cost2m3s = cost2m3s.add(c2);
            profit2m3s = profit2m3s.add(p2);

            BigDecimal dayCost = r.getTotalCost() != null ? r.getTotalCost() : c3.add(c2);
            BigDecimal dayProfit = r.getTotalProfit() != null ? r.getTotalProfit() : p3.add(p2);
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

        int totalMatches = totalFit + totalSlight + totalMismatch;
        summary.setTotalFitCount(totalFit);
        summary.setTotalSlightCount(totalSlight);
        summary.setTotalMismatchCount(totalMismatch);
        summary.setTotalMatches(totalMatches);

        if (totalMatches > 0) {
            BigDecimal totalDec = BigDecimal.valueOf(totalMatches);
            summary.setAvgFitRate(BigDecimal.valueOf(totalFit).multiply(BigDecimal.valueOf(100)).divide(totalDec, 2, RoundingMode.HALF_UP));
            summary.setAvgSlightFitRate(BigDecimal.valueOf(totalSlight).multiply(BigDecimal.valueOf(100)).divide(totalDec, 2, RoundingMode.HALF_UP));
            summary.setAvgMismatchRate(BigDecimal.valueOf(totalMismatch).multiply(BigDecimal.valueOf(100)).divide(totalDec, 2, RoundingMode.HALF_UP));
            summary.setAvgGeneralizedFitRate(BigDecimal.valueOf(totalFit + totalSlight).multiply(BigDecimal.valueOf(100)).divide(totalDec, 2, RoundingMode.HALF_UP));
        }

        summary.setTotalCost(totalCost);
        summary.setTotalProfit(totalProfit);
        summary.setTotalReturn(totalReturn);
        if (totalCost.compareTo(BigDecimal.ZERO) > 0) {
            summary.setOverallRoi(totalProfit.multiply(BigDecimal.valueOf(100)).divide(totalCost, 2, RoundingMode.HALF_UP));
        }

        // 3M2S
        summary.setTotal3m2sCost(cost3m2s);
        summary.setTotal3m2sProfit(profit3m2s);
        if (cost3m2s.compareTo(BigDecimal.ZERO) > 0) {
            summary.setRoi3m2s(profit3m2s.multiply(BigDecimal.valueOf(100)).divide(cost3m2s, 2, RoundingMode.HALF_UP));
        }

        // 2M3S
        summary.setTotal2m3sCost(cost2m3s);
        summary.setTotal2m3sProfit(profit2m3s);
        if (cost2m3s.compareTo(BigDecimal.ZERO) > 0) {
            summary.setRoi2m3s(profit2m3s.multiply(BigDecimal.valueOf(100)).divide(cost2m3s, 2, RoundingMode.HALF_UP));
        }

        // Best Strategy
        if (profit3m2s.compareTo(profit2m3s) > 0 && summary.getRoi3m2s().compareTo(summary.getRoi2m3s()) >= 0) {
            summary.setBestStrategy("主三串副二串 (3M2S 进攻高赔优势)");
        } else if (profit2m3s.compareTo(profit3m2s) > 0 && summary.getRoi2m3s().compareTo(summary.getRoi3m2s()) >= 0) {
            summary.setBestStrategy("主二串副三串 (2M3S 稳健兜底优势)");
        } else {
            summary.setBestStrategy("进球双策略均衡互补");
        }

        summary.setProfitableDays(profitDays);
        summary.setLossDays(lossDays);
        summary.setBreakevenDays(breakevenDays);
        if (records.size() > 0) {
            BigDecimal winRate = BigDecimal.valueOf(profitDays)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(records.size()), 2, RoundingMode.HALF_UP);
            summary.setWinDaysRate(winRate);
        }

        summary.setMaxSingleDayProfit(maxProfit != null ? maxProfit : BigDecimal.ZERO);
        summary.setMaxSingleDayLoss(maxLoss != null ? maxLoss : BigDecimal.ZERO);

        return summary;
    }
}
