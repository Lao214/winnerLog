package com.example.winnerLogBackend.service.impl;

import com.example.winnerLogBackend.common.BusinessException;
import com.example.winnerLogBackend.dto.request.HandicapOddsStatsRecordRequest;
import com.example.winnerLogBackend.dto.response.HandicapOddsStatsSummaryVO;
import com.example.winnerLogBackend.dto.response.OddsIntervalItemVO;
import com.example.winnerLogBackend.entity.HandicapOddsStatsRecord;
import com.example.winnerLogBackend.repository.HandicapOddsStatsRecordRepository;
import com.example.winnerLogBackend.service.HandicapOddsStatsRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HandicapOddsStatsRecordServiceImpl implements HandicapOddsStatsRecordService {

    private final HandicapOddsStatsRecordRepository repository;

    public HandicapOddsStatsRecordServiceImpl(HandicapOddsStatsRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<HandicapOddsStatsRecord> getAllRecords(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return repository.findByRecordDateBetweenOrderByRecordDateAsc(startDate, endDate);
        }
        return repository.findAll(Sort.by(Sort.Direction.DESC, "recordDate"));
    }

    @Override
    public Page<HandicapOddsStatsRecord> getRecordsPage(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        if (startDate != null && endDate != null) {
            return repository.findByRecordDateBetween(startDate, endDate, pageable);
        }
        return repository.findAll(pageable);
    }

    @Override
    public HandicapOddsStatsRecord getRecordById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("未找到ID为 " + id + " 的让球赔率战绩记录"));
    }

    @Override
    public HandicapOddsStatsRecord getRecordByDate(LocalDate recordDate) {
        return repository.findByRecordDate(recordDate)
                .orElseThrow(() -> new BusinessException("未找到日期为 " + recordDate + " 的让球赔率战绩记录"));
    }

    @Override
    @Transactional
    public HandicapOddsStatsRecord createRecord(HandicapOddsStatsRecordRequest request) {
        if (repository.existsByRecordDate(request.getRecordDate())) {
            throw new BusinessException("日期 " + request.getRecordDate() + " 的让球赔率记录已存在，请勿重复新增");
        }
        validateIntervalCounts(request);

        HandicapOddsStatsRecord record = new HandicapOddsStatsRecord();
        mapRequestToEntity(request, record);
        record.calculateMetrics();
        return repository.save(record);
    }

    @Override
    @Transactional
    public HandicapOddsStatsRecord updateRecord(Long id, HandicapOddsStatsRecordRequest request) {
        HandicapOddsStatsRecord record = repository.findById(id)
                .orElseThrow(() -> new BusinessException("未找到ID为 " + id + " 的让球赔率战绩记录"));

        if (repository.existsByRecordDateAndIdNot(request.getRecordDate(), id)) {
            throw new BusinessException("日期 " + request.getRecordDate() + " 的让球赔率记录已存在，无法重复更新");
        }
        validateIntervalCounts(request);

        mapRequestToEntity(request, record);
        record.calculateMetrics();
        return repository.save(record);
    }

    @Override
    @Transactional
    public void deleteRecord(Long id) {
        if (!repository.existsById(id)) {
            throw new BusinessException("未找到ID为 " + id + " 的让球赔率战绩记录");
        }
        repository.deleteById(id);
    }

    @Override
    public HandicapOddsStatsSummaryVO getMacroSummary(LocalDate startDate, LocalDate endDate) {
        List<HandicapOddsStatsRecord> records = (startDate != null && endDate != null)
                ? repository.findByRecordDateBetweenOrderByRecordDateAsc(startDate, endDate)
                : repository.findAll(Sort.by(Sort.Direction.ASC, "recordDate"));

        HandicapOddsStatsSummaryVO summary = new HandicapOddsStatsSummaryVO();
        if (records == null || records.isEmpty()) {
            return summary;
        }

        summary.setTotalRecords(records.size());

        // 累加器初始化
        int totalMatches = 0;
        int totalWin = 0;
        int totalDraw = 0;
        int totalLoss = 0;
        int totalBreak = 0;

        int homeMatches = 0;
        int homeWin = 0;
        int homeBreak = 0;

        int awayMatches = 0;
        int awayWin = 0;
        int awayBreak = 0;

        // 6 区间累加 (主让)
        int[] hWin = new int[6];
        int[] hDraw = new int[6];
        int[] hLoss = new int[6];
        int[] hBreak = new int[6];

        // 6 区间累加 (客让)
        int[] aWin = new int[6];
        int[] aDraw = new int[6];
        int[] aLoss = new int[6];
        int[] aBreak = new int[6];

        for (HandicapOddsStatsRecord r : records) {
            totalMatches += r.getTotalMatches();
            totalWin += r.getTotalWinCount();
            totalDraw += r.getTotalDrawCount();
            totalLoss += r.getTotalLossCount();
            totalBreak += r.getTotalBreakCount();

            homeMatches += r.getHomeTotalMatches();
            homeWin += r.getHomeTotalWin();
            homeBreak += r.getHomeTotalBreak();

            awayMatches += r.getAwayTotalMatches();
            awayWin += r.getAwayTotalWin();
            awayBreak += r.getAwayTotalBreak();

            // 主让累加
            hWin[0] += r.getHomeR1Win(); hDraw[0] += r.getHomeR1Draw(); hLoss[0] += r.getHomeR1Loss(); hBreak[0] += r.getHomeR1Break();
            hWin[1] += r.getHomeR2Win(); hDraw[1] += r.getHomeR2Draw(); hLoss[1] += r.getHomeR2Loss(); hBreak[1] += r.getHomeR2Break();
            hWin[2] += r.getHomeR3Win(); hDraw[2] += r.getHomeR3Draw(); hLoss[2] += r.getHomeR3Loss(); hBreak[2] += r.getHomeR3Break();
            hWin[3] += r.getHomeR4Win(); hDraw[3] += r.getHomeR4Draw(); hLoss[3] += r.getHomeR4Loss(); hBreak[3] += r.getHomeR4Break();
            hWin[4] += r.getHomeR5Win(); hDraw[4] += r.getHomeR5Draw(); hLoss[4] += r.getHomeR5Loss(); hBreak[4] += r.getHomeR5Break();
            hWin[5] += r.getHomeR6Win(); hDraw[5] += r.getHomeR6Draw(); hLoss[5] += r.getHomeR6Loss(); hBreak[5] += r.getHomeR6Break();

            // 客让累加
            aWin[0] += r.getAwayR1Win(); aDraw[0] += r.getAwayR1Draw(); aLoss[0] += r.getAwayR1Loss(); aBreak[0] += r.getAwayR1Break();
            aWin[1] += r.getAwayR2Win(); aDraw[1] += r.getAwayR2Draw(); aLoss[1] += r.getAwayR2Loss(); aBreak[1] += r.getAwayR2Break();
            aWin[2] += r.getAwayR3Win(); aDraw[2] += r.getAwayR3Draw(); aLoss[2] += r.getAwayR3Loss(); aBreak[2] += r.getAwayR3Break();
            aWin[3] += r.getAwayR4Win(); aDraw[3] += r.getAwayR4Draw(); aLoss[3] += r.getAwayR4Loss(); aBreak[3] += r.getAwayR4Break();
            aWin[4] += r.getAwayR5Win(); aDraw[4] += r.getAwayR5Draw(); aLoss[4] += r.getAwayR5Loss(); aBreak[4] += r.getAwayR5Break();
            aWin[5] += r.getAwayR6Win(); aDraw[5] += r.getAwayR6Draw(); aLoss[5] += r.getAwayR6Loss(); aBreak[5] += r.getAwayR6Break();
        }

        summary.setTotalMatches(totalMatches);
        summary.setTotalWinCount(totalWin);
        summary.setTotalDrawCount(totalDraw);
        summary.setTotalLossCount(totalLoss);
        summary.setTotalBreakCount(totalBreak);
        summary.setTotalUnbreakCount(Math.max(0, totalWin - totalBreak));

        if (totalMatches > 0) {
            summary.setOverallWinRate(BigDecimal.valueOf(totalWin * 100.0 / totalMatches).setScale(2, RoundingMode.HALF_UP));
            summary.setOverallDrawRate(BigDecimal.valueOf(totalDraw * 100.0 / totalMatches).setScale(2, RoundingMode.HALF_UP));
            summary.setOverallLossRate(BigDecimal.valueOf(totalLoss * 100.0 / totalMatches).setScale(2, RoundingMode.HALF_UP));
            summary.setOverallBreakRate(BigDecimal.valueOf(totalBreak * 100.0 / totalMatches).setScale(2, RoundingMode.HALF_UP));
        }
        if (totalWin > 0) {
            summary.setOverallBreakRateOnWin(BigDecimal.valueOf(totalBreak * 100.0 / totalWin).setScale(2, RoundingMode.HALF_UP));
        }

        // 主让总体
        summary.setHomeTotalMatches(homeMatches);
        summary.setHomeTotalWin(homeWin);
        summary.setHomeTotalBreak(homeBreak);
        if (homeMatches > 0) {
            summary.setHomeWinRate(BigDecimal.valueOf(homeWin * 100.0 / homeMatches).setScale(2, RoundingMode.HALF_UP));
            summary.setHomeBreakRate(BigDecimal.valueOf(homeBreak * 100.0 / homeMatches).setScale(2, RoundingMode.HALF_UP));
        }
        if (homeWin > 0) {
            summary.setHomeBreakRateOnWin(BigDecimal.valueOf(homeBreak * 100.0 / homeWin).setScale(2, RoundingMode.HALF_UP));
        }

        // 客让总体
        summary.setAwayTotalMatches(awayMatches);
        summary.setAwayTotalWin(awayWin);
        summary.setAwayTotalBreak(awayBreak);
        if (awayMatches > 0) {
            summary.setAwayWinRate(BigDecimal.valueOf(awayWin * 100.0 / awayMatches).setScale(2, RoundingMode.HALF_UP));
            summary.setAwayBreakRate(BigDecimal.valueOf(awayBreak * 100.0 / awayMatches).setScale(2, RoundingMode.HALF_UP));
        }
        if (awayWin > 0) {
            summary.setAwayBreakRateOnWin(BigDecimal.valueOf(awayBreak * 100.0 / awayWin).setScale(2, RoundingMode.HALF_UP));
        }

        // 构造六区间明细
        String[] codes = {"R1", "R2", "R3", "R4", "R5", "R6"};
        String[] names = {"1.40及以下", "1.41-1.60", "1.61-1.80", "1.81-2.00", "2.01-2.20", "2.21以上"};

        List<OddsIntervalItemVO> homeItems = new ArrayList<>();
        List<OddsIntervalItemVO> awayItems = new ArrayList<>();
        List<OddsIntervalItemVO> totalItems = new ArrayList<>();

        double maxBreakRate = -1.0;
        double minBreakRate = 101.0;
        String bestInterval = "暂无数据";
        String lowestInterval = "暂无数据";

        for (int i = 0; i < 6; i++) {
            OddsIntervalItemVO hItem = new OddsIntervalItemVO(codes[i], names[i], "HOME", "主让球",
                    hWin[i], hDraw[i], hLoss[i], hBreak[i]);
            OddsIntervalItemVO aItem = new OddsIntervalItemVO(codes[i], names[i], "AWAY", "客让球",
                    aWin[i], aDraw[i], aLoss[i], aBreak[i]);
            OddsIntervalItemVO tItem = new OddsIntervalItemVO(codes[i], names[i], "TOTAL", "全盘汇总",
                    hWin[i] + aWin[i], hDraw[i] + aDraw[i], hLoss[i] + aLoss[i], hBreak[i] + aBreak[i]);

            homeItems.add(hItem);
            awayItems.add(aItem);
            totalItems.add(tItem);

            // 比较最高/最低穿盘率区间 (主让和客让分别看)
            if (hItem.getTotalMatches() > 0) {
                double rate = hItem.getOverallBreakRate().doubleValue();
                if (rate > maxBreakRate) {
                    maxBreakRate = rate;
                    bestInterval = "主让 " + codes[i] + "(" + names[i] + ")";
                }
                if (rate < minBreakRate) {
                    minBreakRate = rate;
                    lowestInterval = "主让 " + codes[i] + "(" + names[i] + ")";
                }
            }
            if (aItem.getTotalMatches() > 0) {
                double rate = aItem.getOverallBreakRate().doubleValue();
                if (rate > maxBreakRate) {
                    maxBreakRate = rate;
                    bestInterval = "客让 " + codes[i] + "(" + names[i] + ")";
                }
                if (rate < minBreakRate) {
                    minBreakRate = rate;
                    lowestInterval = "客让 " + codes[i] + "(" + names[i] + ")";
                }
            }
        }

        summary.setHomeIntervals(homeItems);
        summary.setAwayIntervals(awayItems);
        summary.setTotalIntervals(totalItems);

        summary.setBestBreakInterval(bestInterval);
        summary.setBestBreakRate(maxBreakRate >= 0 ? BigDecimal.valueOf(maxBreakRate).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        summary.setLowestBreakInterval(lowestInterval);
        summary.setLowestBreakRate(minBreakRate <= 100 ? BigDecimal.valueOf(minBreakRate).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);

        // 主客倾斜与策略
        if (summary.getHomeBreakRate().compareTo(summary.getAwayBreakRate().add(BigDecimal.valueOf(10.0))) > 0) {
            summary.setDominantSide("HOME_DOMINANT");
            summary.setAdviceSummary("历史大数据显示：主让球穿盘率大幅领先客让球 (" + summary.getHomeBreakRate() + "% vs " + summary.getAwayBreakRate() + "%)，主场深盘打穿概率极强，客让浅盘需警惕受让翻车。");
        } else if (summary.getAwayBreakRate().compareTo(summary.getHomeBreakRate().add(BigDecimal.valueOf(10.0))) > 0) {
            summary.setDominantSide("AWAY_DOMINANT");
            summary.setAdviceSummary("历史大数据显示：客让球穿盘率突出 (" + summary.getAwayBreakRate() + "% vs " + summary.getHomeBreakRate() + "%)，强队客战具备强劲统治力。");
        } else {
            summary.setDominantSide("BALANCED");
            summary.setAdviceSummary("历史大数据显示：主客让球穿盘态势较为均衡，建议核心结合赔率区间分化（如1.40以下强势穿盘与1.80以上浅盘走水）制定投注组合。");
        }

        return summary;
    }

    private void validateIntervalCounts(HandicapOddsStatsRecordRequest req) {
        // 校验：穿盘数不得大于胜场数
        checkBreakNotGreaterThanWin("主让 R1(<=1.40)", req.getHomeR1Win(), req.getHomeR1Break());
        checkBreakNotGreaterThanWin("主让 R2(1.41-1.60)", req.getHomeR2Win(), req.getHomeR2Break());
        checkBreakNotGreaterThanWin("主让 R3(1.61-1.80)", req.getHomeR3Win(), req.getHomeR3Break());
        checkBreakNotGreaterThanWin("主让 R4(1.81-2.00)", req.getHomeR4Win(), req.getHomeR4Break());
        checkBreakNotGreaterThanWin("主让 R5(2.01-2.20)", req.getHomeR5Win(), req.getHomeR5Break());
        checkBreakNotGreaterThanWin("主让 R6(>=2.21)", req.getHomeR6Win(), req.getHomeR6Break());

        checkBreakNotGreaterThanWin("客让 R1(<=1.40)", req.getAwayR1Win(), req.getAwayR1Break());
        checkBreakNotGreaterThanWin("客让 R2(1.41-1.60)", req.getAwayR2Win(), req.getAwayR2Break());
        checkBreakNotGreaterThanWin("客让 R3(1.61-1.80)", req.getAwayR3Win(), req.getAwayR3Break());
        checkBreakNotGreaterThanWin("客让 R4(1.81-2.00)", req.getAwayR4Win(), req.getAwayR4Break());
        checkBreakNotGreaterThanWin("客让 R5(2.01-2.20)", req.getAwayR5Win(), req.getAwayR5Break());
        checkBreakNotGreaterThanWin("客让 R6(>=2.21)", req.getAwayR6Win(), req.getAwayR6Break());
    }

    private void checkBreakNotGreaterThanWin(String intervalName, Integer win, Integer breakCnt) {
        int w = win != null ? win : 0;
        int b = breakCnt != null ? breakCnt : 0;
        if (b > w) {
            throw new BusinessException(intervalName + " 的穿盘数 (" + b + ") 不能大于胜场数 (" + w + ")，因为只有在获胜的前提下才能打穿盘口");
        }
    }

    private void mapRequestToEntity(HandicapOddsStatsRecordRequest req, HandicapOddsStatsRecord entity) {
        entity.setRecordDate(req.getRecordDate());
        entity.setBatchTitle(req.getBatchTitle());
        entity.setRemarks(req.getRemarks());

        entity.setHomeR1Win(req.getHomeR1Win() != null ? req.getHomeR1Win() : 0);
        entity.setHomeR1Draw(req.getHomeR1Draw() != null ? req.getHomeR1Draw() : 0);
        entity.setHomeR1Loss(req.getHomeR1Loss() != null ? req.getHomeR1Loss() : 0);
        entity.setHomeR1Break(req.getHomeR1Break() != null ? req.getHomeR1Break() : 0);

        entity.setHomeR2Win(req.getHomeR2Win() != null ? req.getHomeR2Win() : 0);
        entity.setHomeR2Draw(req.getHomeR2Draw() != null ? req.getHomeR2Draw() : 0);
        entity.setHomeR2Loss(req.getHomeR2Loss() != null ? req.getHomeR2Loss() : 0);
        entity.setHomeR2Break(req.getHomeR2Break() != null ? req.getHomeR2Break() : 0);

        entity.setHomeR3Win(req.getHomeR3Win() != null ? req.getHomeR3Win() : 0);
        entity.setHomeR3Draw(req.getHomeR3Draw() != null ? req.getHomeR3Draw() : 0);
        entity.setHomeR3Loss(req.getHomeR3Loss() != null ? req.getHomeR3Loss() : 0);
        entity.setHomeR3Break(req.getHomeR3Break() != null ? req.getHomeR3Break() : 0);

        entity.setHomeR4Win(req.getHomeR4Win() != null ? req.getHomeR4Win() : 0);
        entity.setHomeR4Draw(req.getHomeR4Draw() != null ? req.getHomeR4Draw() : 0);
        entity.setHomeR4Loss(req.getHomeR4Loss() != null ? req.getHomeR4Loss() : 0);
        entity.setHomeR4Break(req.getHomeR4Break() != null ? req.getHomeR4Break() : 0);

        entity.setHomeR5Win(req.getHomeR5Win() != null ? req.getHomeR5Win() : 0);
        entity.setHomeR5Draw(req.getHomeR5Draw() != null ? req.getHomeR5Draw() : 0);
        entity.setHomeR5Loss(req.getHomeR5Loss() != null ? req.getHomeR5Loss() : 0);
        entity.setHomeR5Break(req.getHomeR5Break() != null ? req.getHomeR5Break() : 0);

        entity.setHomeR6Win(req.getHomeR6Win() != null ? req.getHomeR6Win() : 0);
        entity.setHomeR6Draw(req.getHomeR6Draw() != null ? req.getHomeR6Draw() : 0);
        entity.setHomeR6Loss(req.getHomeR6Loss() != null ? req.getHomeR6Loss() : 0);
        entity.setHomeR6Break(req.getHomeR6Break() != null ? req.getHomeR6Break() : 0);

        entity.setAwayR1Win(req.getAwayR1Win() != null ? req.getAwayR1Win() : 0);
        entity.setAwayR1Draw(req.getAwayR1Draw() != null ? req.getAwayR1Draw() : 0);
        entity.setAwayR1Loss(req.getAwayR1Loss() != null ? req.getAwayR1Loss() : 0);
        entity.setAwayR1Break(req.getAwayR1Break() != null ? req.getAwayR1Break() : 0);

        entity.setAwayR2Win(req.getAwayR2Win() != null ? req.getAwayR2Win() : 0);
        entity.setAwayR2Draw(req.getAwayR2Draw() != null ? req.getAwayR2Draw() : 0);
        entity.setAwayR2Loss(req.getAwayR2Loss() != null ? req.getAwayR2Loss() : 0);
        entity.setAwayR2Break(req.getAwayR2Break() != null ? req.getAwayR2Break() : 0);

        entity.setAwayR3Win(req.getAwayR3Win() != null ? req.getAwayR3Win() : 0);
        entity.setAwayR3Draw(req.getAwayR3Draw() != null ? req.getAwayR3Draw() : 0);
        entity.setAwayR3Loss(req.getAwayR3Loss() != null ? req.getAwayR3Loss() : 0);
        entity.setAwayR3Break(req.getAwayR3Break() != null ? req.getAwayR3Break() : 0);

        entity.setAwayR4Win(req.getAwayR4Win() != null ? req.getAwayR4Win() : 0);
        entity.setAwayR4Draw(req.getAwayR4Draw() != null ? req.getAwayR4Draw() : 0);
        entity.setAwayR4Loss(req.getAwayR4Loss() != null ? req.getAwayR4Loss() : 0);
        entity.setAwayR4Break(req.getAwayR4Break() != null ? req.getAwayR4Break() : 0);

        entity.setAwayR5Win(req.getAwayR5Win() != null ? req.getAwayR5Win() : 0);
        entity.setAwayR5Draw(req.getAwayR5Draw() != null ? req.getAwayR5Draw() : 0);
        entity.setAwayR5Loss(req.getAwayR5Loss() != null ? req.getAwayR5Loss() : 0);
        entity.setAwayR5Break(req.getAwayR5Break() != null ? req.getAwayR5Break() : 0);

        entity.setAwayR6Win(req.getAwayR6Win() != null ? req.getAwayR6Win() : 0);
        entity.setAwayR6Draw(req.getAwayR6Draw() != null ? req.getAwayR6Draw() : 0);
        entity.setAwayR6Loss(req.getAwayR6Loss() != null ? req.getAwayR6Loss() : 0);
        entity.setAwayR6Break(req.getAwayR6Break() != null ? req.getAwayR6Break() : 0);
    }
}
