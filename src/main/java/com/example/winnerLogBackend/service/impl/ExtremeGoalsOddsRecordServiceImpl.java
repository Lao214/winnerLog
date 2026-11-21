package com.example.winnerLogBackend.service.impl;

import com.example.winnerLogBackend.common.BusinessException;
import com.example.winnerLogBackend.dto.request.ExtremeGoalsOddsRecordRequest;
import com.example.winnerLogBackend.dto.request.ExtremeGoalsSimulateRequest;
import com.example.winnerLogBackend.dto.response.ExtremeGoalsSimulateVO;
import com.example.winnerLogBackend.dto.response.ExtremeGoalsStatsSummaryVO;
import com.example.winnerLogBackend.entity.ExtremeGoalsOddsRecord;
import com.example.winnerLogBackend.repository.ExtremeGoalsOddsRecordRepository;
import com.example.winnerLogBackend.service.ExtremeGoalsOddsRecordService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExtremeGoalsOddsRecordServiceImpl implements ExtremeGoalsOddsRecordService {

    private final ExtremeGoalsOddsRecordRepository repository;

    public ExtremeGoalsOddsRecordServiceImpl(ExtremeGoalsOddsRecordRepository repository) {
        this.repository = repository;
    }

    private Specification<ExtremeGoalsOddsRecord> buildSpecification(LocalDate startDate, LocalDate endDate, String leagueName, String matchSystem) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (startDate != null && endDate != null) {
                predicates.add(cb.between(root.get("matchDate"), startDate, endDate));
            } else if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("matchDate"), startDate));
            } else if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("matchDate"), endDate));
            }

            if (leagueName != null && !leagueName.trim().isEmpty() && !"ALL".equalsIgnoreCase(leagueName)) {
                predicates.add(cb.like(root.get("leagueName"), "%" + leagueName.trim() + "%"));
            }

            if (matchSystem != null && !matchSystem.trim().isEmpty() && !"ALL".equalsIgnoreCase(matchSystem)) {
                predicates.add(cb.equal(root.get("matchSystem"), matchSystem.toUpperCase().trim()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public List<ExtremeGoalsOddsRecord> getAllRecords(LocalDate startDate, LocalDate endDate, String leagueName, String matchSystem) {
        Specification<ExtremeGoalsOddsRecord> spec = buildSpecification(startDate, endDate, leagueName, matchSystem);
        Sort sort = Sort.by(Sort.Direction.DESC, "matchDate").and(Sort.by(Sort.Direction.DESC, "id"));
        return repository.findAll(spec, sort);
    }

    @Override
    public Page<ExtremeGoalsOddsRecord> getRecordsPage(LocalDate startDate, LocalDate endDate, String leagueName, String matchSystem, Pageable pageable) {
        Specification<ExtremeGoalsOddsRecord> spec = buildSpecification(startDate, endDate, leagueName, matchSystem);
        return repository.findAll(spec, pageable);
    }

    @Override
    public ExtremeGoalsOddsRecord getRecordById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("未找到ID为 " + id + " 的进球赔率记录"));
    }

    @Override
    @Transactional
    public ExtremeGoalsOddsRecord createRecord(ExtremeGoalsOddsRecordRequest request) {
        ExtremeGoalsOddsRecord record = new ExtremeGoalsOddsRecord();
        copyProperties(request, record);
        record.autoCalculateDerivedFields();
        return repository.save(record);
    }

    @Override
    @Transactional
    public List<ExtremeGoalsOddsRecord> batchCreateRecords(List<ExtremeGoalsOddsRecordRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new BusinessException("批量录入数据不能为空");
        }
        List<ExtremeGoalsOddsRecord> list = new ArrayList<>();
        for (ExtremeGoalsOddsRecordRequest req : requests) {
            ExtremeGoalsOddsRecord record = new ExtremeGoalsOddsRecord();
            copyProperties(req, record);
            record.autoCalculateDerivedFields();
            list.add(record);
        }
        return repository.saveAll(list);
    }

    @Override
    @Transactional
    public ExtremeGoalsOddsRecord updateRecord(Long id, ExtremeGoalsOddsRecordRequest request) {
        ExtremeGoalsOddsRecord record = repository.findById(id)
                .orElseThrow(() -> new BusinessException("未找到待修改的进球赔率记录(ID: " + id + ")"));
        copyProperties(request, record);
        record.autoCalculateDerivedFields();
        return repository.save(record);
    }

    @Override
    @Transactional
    public void deleteRecord(Long id) {
        if (!repository.existsById(id)) {
            throw new BusinessException("待删除记录不存在(ID: " + id + ")");
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void batchDeleteRecords(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            repository.deleteAllById(ids);
        }
    }

    @Override
    public List<String> getDistinctLeagueNames() {
        return repository.findDistinctLeagueNames();
    }

    @Override
    @Transactional(readOnly = true)
    public ExtremeGoalsStatsSummaryVO getStatsSummary(LocalDate startDate, LocalDate endDate, String leagueName, String matchSystem) {
        Specification<ExtremeGoalsOddsRecord> spec = buildSpecification(startDate, endDate, leagueName, matchSystem);
        List<ExtremeGoalsOddsRecord> records = repository.findAll(spec);

        ExtremeGoalsStatsSummaryVO vo = new ExtremeGoalsStatsSummaryVO();
        int total = records.size();
        vo.setTotalMatches(total);

        if (total == 0) {
            vo.setAvgZeroGoalsOdds(BigDecimal.ZERO);
            vo.setAvgSevenGoalsOdds(BigDecimal.ZERO);
            vo.setAvgTotalGoals(BigDecimal.ZERO);
            vo.setAvgOddsRatio(BigDecimal.ZERO);
            vo.setZeroOneCount(0);
            vo.setZeroOneRate(BigDecimal.ZERO);
            vo.setTwoThreeCount(0);
            vo.setTwoThreeRate(BigDecimal.ZERO);
            vo.setFourSixCount(0);
            vo.setFourSixRate(BigDecimal.ZERO);
            vo.setSevenPlusCount(0);
            vo.setSevenPlusRate(BigDecimal.ZERO);
            vo.setGoalsDistribution(Collections.emptyMap());
            vo.setZeroOddsTiers(Collections.emptyList());
            vo.setSevenOddsTiers(Collections.emptyList());
            vo.setRatioTiers(Collections.emptyList());
            vo.setLeagueRankings(Collections.emptyList());
            return vo;
        }

        BigDecimal n = new BigDecimal(total);
        BigDecimal sumZeroOdds = BigDecimal.ZERO;
        BigDecimal sumSevenOdds = BigDecimal.ZERO;
        BigDecimal sumTotalGoals = BigDecimal.ZERO;
        BigDecimal sumRatio = BigDecimal.ZERO;

        int zeroOne = 0;
        int twoThree = 0;
        int fourSix = 0;
        int sevenPlus = 0;

        Map<String, Integer> goalsMap = new LinkedHashMap<>();
        for (int i = 0; i <= 6; i++) {
            goalsMap.put(i + "球", 0);
        }
        goalsMap.put("7+球", 0);

        for (ExtremeGoalsOddsRecord r : records) {
            if (r.getZeroGoalsOdds() != null) sumZeroOdds = sumZeroOdds.add(r.getZeroGoalsOdds());
            if (r.getSevenGoalsOdds() != null) sumSevenOdds = sumSevenOdds.add(r.getSevenGoalsOdds());
            if (r.getTotalGoals() != null) {
                sumTotalGoals = sumTotalGoals.add(new BigDecimal(r.getTotalGoals()));
                int g = r.getTotalGoals();
                if (g <= 1) zeroOne++;
                else if (g <= 3) twoThree++;
                else if (g <= 6) fourSix++;
                else sevenPlus++;

                String gKey = g >= 7 ? "7+球" : g + "球";
                goalsMap.put(gKey, goalsMap.getOrDefault(gKey, 0) + 1);
            }
            if (r.getOddsRatio() != null) {
                sumRatio = sumRatio.add(r.getOddsRatio());
            } else if (r.getZeroGoalsOdds() != null && r.getSevenGoalsOdds() != null && r.getZeroGoalsOdds().compareTo(BigDecimal.ZERO) > 0) {
                sumRatio = sumRatio.add(r.getSevenGoalsOdds().divide(r.getZeroGoalsOdds(), 2, RoundingMode.HALF_UP));
            }
        }

        vo.setAvgZeroGoalsOdds(sumZeroOdds.divide(n, 2, RoundingMode.HALF_UP));
        vo.setAvgSevenGoalsOdds(sumSevenOdds.divide(n, 2, RoundingMode.HALF_UP));
        vo.setAvgTotalGoals(sumTotalGoals.divide(n, 2, RoundingMode.HALF_UP));
        vo.setAvgOddsRatio(sumRatio.divide(n, 2, RoundingMode.HALF_UP));

        vo.setZeroOneCount(zeroOne);
        vo.setZeroOneRate(new BigDecimal(zeroOne).multiply(new BigDecimal(100)).divide(n, 2, RoundingMode.HALF_UP));

        vo.setTwoThreeCount(twoThree);
        vo.setTwoThreeRate(new BigDecimal(twoThree).multiply(new BigDecimal(100)).divide(n, 2, RoundingMode.HALF_UP));

        vo.setFourSixCount(fourSix);
        vo.setFourSixRate(new BigDecimal(fourSix).multiply(new BigDecimal(100)).divide(n, 2, RoundingMode.HALF_UP));

        vo.setSevenPlusCount(sevenPlus);
        vo.setSevenPlusRate(new BigDecimal(sevenPlus).multiply(new BigDecimal(100)).divide(n, 2, RoundingMode.HALF_UP));

        vo.setGoalsDistribution(goalsMap);

        // 1. 0球赔率分档分析
        vo.setZeroOddsTiers(buildTierAnalysis(records, total, true));

        // 2. 7球赔率分档分析
        vo.setSevenOddsTiers(buildTierAnalysis(records, total, false));

        // 3. 赔率比值 (7+/0) 分档分析
        vo.setRatioTiers(buildRatioTiers(records, total));

        // 4. 联赛特征排行榜
        vo.setLeagueRankings(buildLeagueRankings(records));

        return vo;
    }

    private List<ExtremeGoalsStatsSummaryVO.TierAnalysisItemVO> buildTierAnalysis(List<ExtremeGoalsOddsRecord> records, int totalMatches, boolean isZeroOdds) {
        List<ExtremeGoalsStatsSummaryVO.TierAnalysisItemVO> tiers = new ArrayList<>();

        if (isZeroOdds) {
            tiers.add(createTierItem("极度防守 (< 9.00)", 0.0, 8.99, records, totalMatches, true));
            tiers.add(createTierItem("常规偏小 (9.00-11.00)", 9.00, 11.00, records, totalMatches, true));
            tiers.add(createTierItem("均势主流 (11.01-13.00)", 11.01, 13.00, records, totalMatches, true));
            tiers.add(createTierItem("常规偏大 (13.01-15.00)", 13.01, 15.00, records, totalMatches, true));
            tiers.add(createTierItem("极度开放 (> 15.00)", 15.01, 999.0, records, totalMatches, true));
        } else {
            tiers.add(createTierItem("极度防大 (< 22.00)", 0.0, 21.99, records, totalMatches, false));
            tiers.add(createTierItem("敏感活跃 (22.00-28.00)", 22.00, 28.00, records, totalMatches, false));
            tiers.add(createTierItem("均势主流 (28.01-35.00)", 28.01, 35.00, records, totalMatches, false));
            tiers.add(createTierItem("偏小稳健 (35.01-45.00)", 35.01, 45.00, records, totalMatches, false));
            tiers.add(createTierItem("极低大球 (> 45.00)", 45.01, 999.0, records, totalMatches, false));
        }

        return tiers;
    }

    private ExtremeGoalsStatsSummaryVO.TierAnalysisItemVO createTierItem(String name, double min, double max, List<ExtremeGoalsOddsRecord> all, int totalMatches, boolean isZeroOdds) {
        ExtremeGoalsStatsSummaryVO.TierAnalysisItemVO item = new ExtremeGoalsStatsSummaryVO.TierAnalysisItemVO();
        item.setTierName(name);
        item.setTierRange(min + " ~ " + max);

        List<ExtremeGoalsOddsRecord> matched = all.stream().filter(r -> {
            BigDecimal val = isZeroOdds ? r.getZeroGoalsOdds() : r.getSevenGoalsOdds();
            if (val == null) return false;
            double v = val.doubleValue();
            return v >= min && v <= max;
        }).collect(Collectors.toList());

        int count = matched.size();
        item.setMatchCount(count);
        if (totalMatches > 0) {
            item.setCountRate(new BigDecimal(count).multiply(new BigDecimal(100)).divide(new BigDecimal(totalMatches), 2, RoundingMode.HALF_UP));
        } else {
            item.setCountRate(BigDecimal.ZERO);
        }

        if (count == 0) {
            item.setAvgGoals(BigDecimal.ZERO);
            item.setZeroOneRate(BigDecimal.ZERO);
            item.setTwoThreeRate(BigDecimal.ZERO);
            item.setFourPlusRate(BigDecimal.ZERO);
            item.setDominantGoals("--");
            return item;
        }

        int sumGoals = 0;
        int zo = 0;
        int tt = 0;
        int fp = 0;
        Map<Integer, Integer> freq = new HashMap<>();

        for (ExtremeGoalsOddsRecord r : matched) {
            int g = r.getTotalGoals() != null ? r.getTotalGoals() : 0;
            sumGoals += g;
            if (g <= 1) zo++;
            else if (g <= 3) tt++;
            else fp++;

            freq.put(g, freq.getOrDefault(g, 0) + 1);
        }

        BigDecimal c = new BigDecimal(count);
        item.setAvgGoals(new BigDecimal(sumGoals).divide(c, 2, RoundingMode.HALF_UP));
        item.setZeroOneRate(new BigDecimal(zo).multiply(new BigDecimal(100)).divide(c, 2, RoundingMode.HALF_UP));
        item.setTwoThreeRate(new BigDecimal(tt).multiply(new BigDecimal(100)).divide(c, 2, RoundingMode.HALF_UP));
        item.setFourPlusRate(new BigDecimal(fp).multiply(new BigDecimal(100)).divide(c, 2, RoundingMode.HALF_UP));

        int dominant = freq.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(2);
        item.setDominantGoals(dominant >= 7 ? "7+球" : dominant + "球");

        return item;
    }

    private List<ExtremeGoalsStatsSummaryVO.TierAnalysisItemVO> buildRatioTiers(List<ExtremeGoalsOddsRecord> records, int totalMatches) {
        List<ExtremeGoalsStatsSummaryVO.TierAnalysisItemVO> list = new ArrayList<>();

        list.add(createRatioItem("极低比值 (< 2.50 防大球)", 0.0, 2.49, records, totalMatches));
        list.add(createRatioItem("均衡比值 (2.50-3.50 中规中矩)", 2.50, 3.50, records, totalMatches));
        list.add(createRatioItem("极高比值 (> 3.50 防小球)", 3.51, 999.0, records, totalMatches));

        return list;
    }

    private ExtremeGoalsStatsSummaryVO.TierAnalysisItemVO createRatioItem(String name, double min, double max, List<ExtremeGoalsOddsRecord> all, int totalMatches) {
        ExtremeGoalsStatsSummaryVO.TierAnalysisItemVO item = new ExtremeGoalsStatsSummaryVO.TierAnalysisItemVO();
        item.setTierName(name);
        item.setTierRange(min + " ~ " + max);

        List<ExtremeGoalsOddsRecord> matched = all.stream().filter(r -> {
            BigDecimal val = r.getOddsRatio();
            if (val == null && r.getZeroGoalsOdds() != null && r.getSevenGoalsOdds() != null && r.getZeroGoalsOdds().compareTo(BigDecimal.ZERO) > 0) {
                val = r.getSevenGoalsOdds().divide(r.getZeroGoalsOdds(), 2, RoundingMode.HALF_UP);
            }
            if (val == null) return false;
            double v = val.doubleValue();
            return v >= min && v <= max;
        }).collect(Collectors.toList());

        int count = matched.size();
        item.setMatchCount(count);
        item.setCountRate(totalMatches > 0 ? new BigDecimal(count).multiply(new BigDecimal(100)).divide(new BigDecimal(totalMatches), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);

        if (count == 0) {
            item.setAvgGoals(BigDecimal.ZERO);
            item.setZeroOneRate(BigDecimal.ZERO);
            item.setTwoThreeRate(BigDecimal.ZERO);
            item.setFourPlusRate(BigDecimal.ZERO);
            item.setDominantGoals("--");
            return item;
        }

        int sumGoals = 0;
        int zo = 0;
        int tt = 0;
        int fp = 0;
        Map<Integer, Integer> freq = new HashMap<>();

        for (ExtremeGoalsOddsRecord r : matched) {
            int g = r.getTotalGoals() != null ? r.getTotalGoals() : 0;
            sumGoals += g;
            if (g <= 1) zo++;
            else if (g <= 3) tt++;
            else fp++;

            freq.put(g, freq.getOrDefault(g, 0) + 1);
        }

        BigDecimal c = new BigDecimal(count);
        item.setAvgGoals(new BigDecimal(sumGoals).divide(c, 2, RoundingMode.HALF_UP));
        item.setZeroOneRate(new BigDecimal(zo).multiply(new BigDecimal(100)).divide(c, 2, RoundingMode.HALF_UP));
        item.setTwoThreeRate(new BigDecimal(tt).multiply(new BigDecimal(100)).divide(c, 2, RoundingMode.HALF_UP));
        item.setFourPlusRate(new BigDecimal(fp).multiply(new BigDecimal(100)).divide(c, 2, RoundingMode.HALF_UP));

        int dominant = freq.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(2);
        item.setDominantGoals(dominant >= 7 ? "7+球" : dominant + "球");

        return item;
    }

    private List<ExtremeGoalsStatsSummaryVO.LeagueGoalFeatureVO> buildLeagueRankings(List<ExtremeGoalsOddsRecord> records) {
        Map<String, List<ExtremeGoalsOddsRecord>> group = records.stream()
                .filter(r -> r.getLeagueName() != null && !r.getLeagueName().trim().isEmpty())
                .collect(Collectors.groupingBy(r -> r.getLeagueName().trim()));

        List<ExtremeGoalsStatsSummaryVO.LeagueGoalFeatureVO> list = new ArrayList<>();

        for (Map.Entry<String, List<ExtremeGoalsOddsRecord>> entry : group.entrySet()) {
            String league = entry.getKey();
            List<ExtremeGoalsOddsRecord> sub = entry.getValue();
            int count = sub.size();
            if (count == 0) continue;

            BigDecimal c = new BigDecimal(count);
            BigDecimal sumZero = BigDecimal.ZERO;
            BigDecimal sumSeven = BigDecimal.ZERO;
            int sumGoals = 0;
            int zo = 0;
            int tt = 0;
            int fp = 0;

            for (ExtremeGoalsOddsRecord r : sub) {
                if (r.getZeroGoalsOdds() != null) sumZero = sumZero.add(r.getZeroGoalsOdds());
                if (r.getSevenGoalsOdds() != null) sumSeven = sumSeven.add(r.getSevenGoalsOdds());
                int g = r.getTotalGoals() != null ? r.getTotalGoals() : 0;
                sumGoals += g;
                if (g <= 1) zo++;
                else if (g <= 3) tt++;
                else fp++;
            }

            ExtremeGoalsStatsSummaryVO.LeagueGoalFeatureVO vo = new ExtremeGoalsStatsSummaryVO.LeagueGoalFeatureVO();
            vo.setLeagueName(league);
            vo.setMatchCount(count);
            vo.setAvgZeroOdds(sumZero.divide(c, 2, RoundingMode.HALF_UP));
            vo.setAvgSevenOdds(sumSeven.divide(c, 2, RoundingMode.HALF_UP));
            BigDecimal avgG = new BigDecimal(sumGoals).divide(c, 2, RoundingMode.HALF_UP);
            vo.setAvgGoals(avgG);
            vo.setZeroOneRate(new BigDecimal(zo).multiply(new BigDecimal(100)).divide(c, 2, RoundingMode.HALF_UP));
            vo.setTwoThreeRate(new BigDecimal(tt).multiply(new BigDecimal(100)).divide(c, 2, RoundingMode.HALF_UP));
            vo.setFourPlusRate(new BigDecimal(fp).multiply(new BigDecimal(100)).divide(c, 2, RoundingMode.HALF_UP));

            if (avgG.compareTo(new BigDecimal("3.00")) >= 0) {
                vo.setStyleTag("🔥 大球天堂");
            } else if (avgG.compareTo(new BigDecimal("2.20")) <= 0) {
                vo.setStyleTag("🛡️ 小球温床");
            } else if (vo.getTwoThreeRate().compareTo(new BigDecimal("55.00")) >= 0) {
                vo.setStyleTag("🎯 2-3球黄金区");
            } else {
                vo.setStyleTag("⚖️ 均势平稳");
            }

            list.add(vo);
        }

        // 按场次降序，再按进球均值降序
        list.sort(Comparator.comparing(ExtremeGoalsStatsSummaryVO.LeagueGoalFeatureVO::getMatchCount).reversed()
                .thenComparing(ExtremeGoalsStatsSummaryVO.LeagueGoalFeatureVO::getAvgGoals, Comparator.reverseOrder()));

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public ExtremeGoalsSimulateVO simulate(ExtremeGoalsSimulateRequest request) {
        if (request == null) {
            throw new BusinessException("测算参数不能为空");
        }

        BigDecimal zeroOdds = request.getZeroGoalsOdds() != null ? request.getZeroGoalsOdds() : new BigDecimal("9.50");
        BigDecimal sevenOdds = request.getSevenGoalsOdds() != null ? request.getSevenGoalsOdds() : new BigDecimal("28.00");
        BigDecimal zeroTol = request.getZeroTolerance() != null ? request.getZeroTolerance() : new BigDecimal("1.00");
        BigDecimal sevenTol = request.getSevenTolerance() != null ? request.getSevenTolerance() : new BigDecimal("4.00");

        BigDecimal minZero = zeroOdds.subtract(zeroTol).setScale(2, RoundingMode.HALF_UP);
        BigDecimal maxZero = zeroOdds.add(zeroTol).setScale(2, RoundingMode.HALF_UP);

        BigDecimal minSeven = sevenOdds.subtract(sevenTol).setScale(2, RoundingMode.HALF_UP);
        BigDecimal maxSeven = sevenOdds.add(sevenTol).setScale(2, RoundingMode.HALF_UP);

        String leagueName = request.getLeagueName();
        String matchSystem = request.getMatchSystem();

        Specification<ExtremeGoalsOddsRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.between(root.get("zeroGoalsOdds"), minZero, maxZero));
            predicates.add(cb.between(root.get("sevenGoalsOdds"), minSeven, maxSeven));

            if (leagueName != null && !leagueName.trim().isEmpty() && !"ALL".equalsIgnoreCase(leagueName)) {
                predicates.add(cb.like(root.get("leagueName"), "%" + leagueName.trim() + "%"));
            }

            if (matchSystem != null && !matchSystem.trim().isEmpty() && !"ALL".equalsIgnoreCase(matchSystem)) {
                predicates.add(cb.equal(root.get("matchSystem"), matchSystem.toUpperCase().trim()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<ExtremeGoalsOddsRecord> matched = repository.findAll(spec, Sort.by(Sort.Direction.DESC, "matchDate").and(Sort.by(Sort.Direction.DESC, "id")));

        ExtremeGoalsSimulateVO vo = new ExtremeGoalsSimulateVO();
        vo.setLeagueName((leagueName != null && !leagueName.trim().isEmpty()) ? leagueName : "ALL");
        vo.setMatchSystem((matchSystem != null && !matchSystem.trim().isEmpty()) ? matchSystem : "ALL");
        vo.setZeroOddsRangeDesc(String.format("%.2f ~ %.2f (基准 %.2f ± %.2f)", minZero, maxZero, zeroOdds, zeroTol));
        vo.setSevenOddsRangeDesc(String.format("%.2f ~ %.2f (基准 %.2f ± %.2f)", minSeven, maxSeven, sevenOdds, sevenTol));

        int count = matched.size();
        vo.setMatchedCount(count);

        if (count == 0) {
            vo.setAvgGoalsInSample(BigDecimal.ZERO);
            vo.setPredZeroOneRate(BigDecimal.ZERO);
            vo.setPredTwoThreeRate(BigDecimal.ZERO);
            vo.setPredFourPlusRate(BigDecimal.ZERO);
            vo.setTopGoalsRanking(Collections.emptyList());
            vo.setGoalsCountMap(Collections.emptyMap());
            vo.setAdvice("库内在 0球赔率 " + minZero + "~" + maxZero + " 且 7+赔率 " + minSeven + "~" + maxSeven + " 区间暂无同类样本。建议适当放宽 0球或7球容差步长，或继续补充历史数据录入。");
            vo.setRiskLevel("HIGH");
            vo.setMatchedSamples(Collections.emptyList());
            return vo;
        }

        BigDecimal c = new BigDecimal(count);
        int sumGoals = 0;
        int zo = 0;
        int tt = 0;
        int fp = 0;
        Map<Integer, Integer> freq = new HashMap<>();

        for (ExtremeGoalsOddsRecord r : matched) {
            int g = r.getTotalGoals() != null ? r.getTotalGoals() : 0;
            sumGoals += g;
            if (g <= 1) zo++;
            else if (g <= 3) tt++;
            else fp++;

            freq.put(g, freq.getOrDefault(g, 0) + 1);
        }

        vo.setAvgGoalsInSample(new BigDecimal(sumGoals).divide(c, 2, RoundingMode.HALF_UP));
        vo.setPredZeroOneRate(new BigDecimal(zo).multiply(new BigDecimal(100)).divide(c, 2, RoundingMode.HALF_UP));
        vo.setPredTwoThreeRate(new BigDecimal(tt).multiply(new BigDecimal(100)).divide(c, 2, RoundingMode.HALF_UP));
        vo.setPredFourPlusRate(new BigDecimal(fp).multiply(new BigDecimal(100)).divide(c, 2, RoundingMode.HALF_UP));

        // Top 3 进球数排行
        List<ExtremeGoalsSimulateVO.GoalRankItemVO> topList = freq.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(3)
                .map(e -> {
                    ExtremeGoalsSimulateVO.GoalRankItemVO item = new ExtremeGoalsSimulateVO.GoalRankItemVO();
                    int g = e.getKey();
                    item.setGoalValue(g);
                    item.setGoalText(g >= 7 ? "7+球" : g + "球");
                    item.setCount(e.getValue());
                    item.setRate(new BigDecimal(e.getValue()).multiply(new BigDecimal(100)).divide(c, 2, RoundingMode.HALF_UP));
                    return item;
                }).collect(Collectors.toList());

        vo.setTopGoalsRanking(topList);

        Map<String, Integer> gCountMap = new LinkedHashMap<>();
        for (int i = 0; i <= 6; i++) {
            gCountMap.put(i + "球", freq.getOrDefault(i, 0));
        }
        gCountMap.put("7+球", freq.getOrDefault(7, 0));
        vo.setGoalsCountMap(gCountMap);

        // 评定置信度
        if (count >= 12) vo.setRiskLevel("LOW");
        else if (count >= 5) vo.setRiskLevel("MEDIUM");
        else vo.setRiskLevel("HIGH");

        // 智能决策建议
        StringBuilder sb = new StringBuilder();
        if (!topList.isEmpty()) {
            ExtremeGoalsSimulateVO.GoalRankItemVO top1 = topList.get(0);
            sb.append("📊 【大数据推荐】历史同类两极赔率样本中，打出最频繁的是【").append(top1.getGoalText())
              .append("】（打出概率 ").append(top1.getRate()).append("%");
            if (topList.size() >= 2) {
                ExtremeGoalsSimulateVO.GoalRankItemVO top2 = topList.get(1);
                sb.append("，次高频为【").append(top2.getGoalText()).append("】").append(top2.getRate()).append("%");
            }
            sb.append("）。");
        }

        if (vo.getPredTwoThreeRate().compareTo(new BigDecimal("50.00")) >= 0) {
            sb.append(" 2-3球黄金中球概率达 ").append(vo.getPredTwoThreeRate()).append("%，建议策略防守重心压在 2球与3球。");
        } else if (vo.getPredFourPlusRate().compareTo(new BigDecimal("35.00")) >= 0) {
            sb.append(" 4球及以上大球概率达 ").append(vo.getPredFourPlusRate()).append("%，7+赔率被有效防范，提示大开大合对攻态势。");
        } else if (vo.getPredZeroOneRate().compareTo(new BigDecimal("35.00")) >= 0) {
            sb.append(" 0-1球小球打出率达 ").append(vo.getPredZeroOneRate()).append("%，0球赔率低位压制，警惕互交白卷或1-0胶着沉闷。");
        }

        if (count < 6) {
            sb.append(" （注：当前匹配样本仅 ").append(count).append(" 场，建议适当放宽 0球/7球 赔率容差增强置信度）。");
        }

        vo.setAdvice(sb.toString());

        // 匹配历史比赛列表快照 (前 20 场)
        List<ExtremeGoalsSimulateVO.MatchedMatchSampleVO> sampleVOs = matched.stream().limit(20).map(r -> {
            ExtremeGoalsSimulateVO.MatchedMatchSampleVO s = new ExtremeGoalsSimulateVO.MatchedMatchSampleVO();
            s.setId(r.getId());
            s.setMatchDate(r.getMatchDate() != null ? r.getMatchDate().toString() : "");
            s.setLeagueName(r.getLeagueName());
            s.setMatchSystem(r.getMatchSystem());
            s.setHomeTeam(r.getHomeTeam());
            s.setAwayTeam(r.getAwayTeam());
            s.setZeroGoalsOdds(r.getZeroGoalsOdds());
            s.setSevenGoalsOdds(r.getSevenGoalsOdds());
            s.setTotalGoals(r.getTotalGoals());
            s.setFullScore(r.getFullScore());
            return s;
        }).collect(Collectors.toList());

        vo.setMatchedSamples(sampleVOs);

        return vo;
    }

    private void copyProperties(ExtremeGoalsOddsRecordRequest src, ExtremeGoalsOddsRecord target) {
        target.setMatchDate(src.getMatchDate());
        target.setBatchTitle(src.getBatchTitle());
        target.setLeagueName(src.getLeagueName());
        target.setMatchSystem(src.getMatchSystem() != null ? src.getMatchSystem().toUpperCase().trim() : "LEAGUE");
        target.setHomeTeam(src.getHomeTeam());
        target.setAwayTeam(src.getAwayTeam());
        target.setZeroGoalsOdds(src.getZeroGoalsOdds());
        target.setSevenGoalsOdds(src.getSevenGoalsOdds());
        target.setTotalGoals(src.getTotalGoals());
        target.setFullScore(src.getFullScore());
        target.setRemarks(src.getRemarks());
    }
}
