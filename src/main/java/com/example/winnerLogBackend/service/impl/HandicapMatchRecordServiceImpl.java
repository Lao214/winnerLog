package com.example.winnerLogBackend.service.impl;

import com.example.winnerLogBackend.common.BusinessException;
import com.example.winnerLogBackend.dto.request.HandicapMatchRecordRequest;
import com.example.winnerLogBackend.entity.HandicapMatchRecord;
import com.example.winnerLogBackend.repository.HandicapMatchRecordRepository;
import com.example.winnerLogBackend.service.HandicapMatchRecordService;
import com.example.winnerLogBackend.dto.request.HandicapMatchSimulateRequest;
import com.example.winnerLogBackend.dto.response.HandicapMatchSimulationVO;
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
public class HandicapMatchRecordServiceImpl implements HandicapMatchRecordService {

    private final HandicapMatchRecordRepository repository;

    public HandicapMatchRecordServiceImpl(HandicapMatchRecordRepository repository) {
        this.repository = repository;
    }

    private Specification<HandicapMatchRecord> buildSpecification(LocalDate startDate, LocalDate endDate, String handicapSide, String matchSystem, String leagueName) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (startDate != null && endDate != null) {
                predicates.add(cb.between(root.get("matchDate"), startDate, endDate));
            } else if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("matchDate"), startDate));
            } else if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("matchDate"), endDate));
            }

            if (handicapSide != null && !handicapSide.trim().isEmpty() && !"ALL".equalsIgnoreCase(handicapSide)) {
                predicates.add(cb.equal(root.get("handicapSide"), handicapSide.toUpperCase().trim()));
            }

            if (matchSystem != null && !matchSystem.trim().isEmpty() && !"ALL".equalsIgnoreCase(matchSystem)) {
                predicates.add(cb.equal(root.get("matchSystem"), matchSystem.toUpperCase().trim()));
            }

            if (leagueName != null && !leagueName.trim().isEmpty()) {
                predicates.add(cb.like(root.get("leagueName"), "%" + leagueName.trim() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public List<HandicapMatchRecord> getAllMatches(LocalDate startDate, LocalDate endDate, String handicapSide, String matchSystem, String leagueName) {
        Specification<HandicapMatchRecord> spec = buildSpecification(startDate, endDate, handicapSide, matchSystem, leagueName);
        Sort sort = Sort.by(Sort.Direction.DESC, "matchDate").and(Sort.by(Sort.Direction.DESC, "id"));
        return repository.findAll(spec, sort);
    }

    @Override
    public Page<HandicapMatchRecord> getMatchesPage(LocalDate startDate, LocalDate endDate, String handicapSide, String matchSystem, String leagueName, Pageable pageable) {
        Specification<HandicapMatchRecord> spec = buildSpecification(startDate, endDate, handicapSide, matchSystem, leagueName);
        return repository.findAll(spec, pageable);
    }


    @Override
    public HandicapMatchRecord getMatchById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("未找到ID为 " + id + " 的比赛记录"));
    }

    @Override
    @Transactional
    public HandicapMatchRecord createMatch(HandicapMatchRecordRequest request) {
        HandicapMatchRecord record = new HandicapMatchRecord();
        copyProperties(request, record);
        record.autoCalculateDerivedFields();
        // 允许用户覆盖 autoCalculate 的某些手工字段
        overrideManualFields(request, record);
        return repository.save(record);
    }

    @Override
    @Transactional
    public List<HandicapMatchRecord> batchCreateMatches(List<HandicapMatchRecordRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new BusinessException("批量录入的比赛数据不能为空");
        }
        List<HandicapMatchRecord> list = new ArrayList<>();
        for (HandicapMatchRecordRequest req : requests) {
            HandicapMatchRecord record = new HandicapMatchRecord();
            copyProperties(req, record);
            record.autoCalculateDerivedFields();
            overrideManualFields(req, record);
            list.add(record);
        }
        return repository.saveAll(list);
    }

    @Override
    @Transactional
    public HandicapMatchRecord updateMatch(Long id, HandicapMatchRecordRequest request) {
        HandicapMatchRecord record = repository.findById(id)
                .orElseThrow(() -> new BusinessException("未找到待修改的比赛记录(ID: " + id + ")"));

        copyProperties(request, record);
        record.autoCalculateDerivedFields();
        overrideManualFields(request, record);
        return repository.save(record);
    }

    @Override
    @Transactional
    public void deleteMatch(Long id) {
        if (!repository.existsById(id)) {
            throw new BusinessException("待删除的比赛记录不存在(ID: " + id + ")");
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void batchDeleteMatches(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            repository.deleteAllById(ids);
        }
    }

    @Override
    public List<String> getDistinctLeagueNames() {
        return repository.findDistinctLeagueNames();
    }

    private void copyProperties(HandicapMatchRecordRequest src, HandicapMatchRecord target) {
        target.setMatchDate(src.getMatchDate());
        target.setBatchTitle(src.getBatchTitle());
        target.setLeagueName(src.getLeagueName());
        target.setMatchSystem(src.getMatchSystem() != null ? src.getMatchSystem().toUpperCase().trim() : "LEAGUE");
        target.setHomeTeam(src.getHomeTeam());
        target.setAwayTeam(src.getAwayTeam());
        target.setHandicapSide(src.getHandicapSide() != null ? src.getHandicapSide().toUpperCase().trim() : "HOME");
        target.setHandicapValue(src.getHandicapValue());
        target.setHomeWinOdds(src.getHomeWinOdds());
        target.setDrawOdds(src.getDrawOdds());
        target.setAwayWinOdds(src.getAwayWinOdds());
        target.setHandicapOdds(src.getHandicapOdds());
        target.setHalfScore(src.getHalfScore());
        target.setFullScore(src.getFullScore());
        target.setMatchResult(src.getMatchResult());
        target.setResultOdds(src.getResultOdds());
        target.setHandicapResult(src.getHandicapResult());
        target.setHandicapResultOdds(src.getHandicapResultOdds());
        target.setHalfFullResult(src.getHalfFullResult());
        target.setTotalGoals(src.getTotalGoals());
        if (src.getIsBreak() != null) {
            target.setIsBreak(src.getIsBreak());
        }
        target.setRemarks(src.getRemarks());
    }

    private void overrideManualFields(HandicapMatchRecordRequest src, HandicapMatchRecord target) {
        if (src.getMatchSystem() != null && !src.getMatchSystem().trim().isEmpty()) {
            target.setMatchSystem(src.getMatchSystem().toUpperCase().trim());
        }
        if (src.getIsBreak() != null) {
            target.setIsBreak(src.getIsBreak());
        }
        if (src.getResultOdds() != null) {
            target.setResultOdds(src.getResultOdds());
        }
        if (src.getHandicapResultOdds() != null) {
            target.setHandicapResultOdds(src.getHandicapResultOdds());
        }
        if (src.getTotalGoals() != null) {
            target.setTotalGoals(src.getTotalGoals());
        }
        if (src.getMatchResult() != null && !src.getMatchResult().trim().isEmpty()) {
            target.setMatchResult(src.getMatchResult().trim());
        }
        if (src.getHalfFullResult() != null && !src.getHalfFullResult().trim().isEmpty()) {
            target.setHalfFullResult(src.getHalfFullResult().trim());
        }
        if (src.getHandicapResult() != null && !src.getHandicapResult().trim().isEmpty()) {
            target.setHandicapResult(src.getHandicapResult().trim());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public HandicapMatchSimulationVO simulateMatch(HandicapMatchSimulateRequest request) {
        if (request == null) {
            throw new BusinessException("测算请求参数不能为空");
        }

        String handicapSide = (request.getHandicapSide() != null && !request.getHandicapSide().trim().isEmpty())
                ? request.getHandicapSide().toUpperCase().trim() : "HOME";
        String matchSystem = (request.getMatchSystem() != null && !request.getMatchSystem().trim().isEmpty())
                ? request.getMatchSystem().toUpperCase().trim() : "ALL";

        BigDecimal tolerance = request.getTolerance() != null ? request.getTolerance() : new BigDecimal("0.10");

        // 基准胜赔：如果是主让，取 homeWinOdds；如果是客让，取 awayWinOdds
        BigDecimal targetOdds;
        if ("HOME".equalsIgnoreCase(handicapSide)) {
            targetOdds = request.getHomeWinOdds();
            if (targetOdds == null) {
                targetOdds = new BigDecimal("1.50");
            }
        } else {
            targetOdds = request.getAwayWinOdds();
            if (targetOdds == null) {
                targetOdds = new BigDecimal("1.50");
            }
        }

        BigDecimal minOdds = targetOdds.subtract(tolerance).setScale(2, RoundingMode.HALF_UP);
        BigDecimal maxOdds = targetOdds.add(tolerance).setScale(2, RoundingMode.HALF_UP);

        // 构造动态查询
        Specification<HandicapMatchRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 匹配让球方
            if (!"ALL".equalsIgnoreCase(handicapSide)) {
                predicates.add(cb.equal(root.get("handicapSide"), handicapSide));
            }
            // 匹配赛制 (如果非 ALL)
            if (!"ALL".equalsIgnoreCase(matchSystem)) {
                predicates.add(cb.equal(root.get("matchSystem"), matchSystem));
            }
            // 匹配联赛 (如果非 ALL)
            String leagueName = request.getLeagueName();
            if (leagueName != null && !leagueName.trim().isEmpty() && !"ALL".equalsIgnoreCase(leagueName.trim())) {
                predicates.add(cb.equal(root.get("leagueName"), leagueName.trim()));
            }
            // 匹配赔率区间 [minOdds, maxOdds]
            predicates.add(cb.between(root.get("handicapOdds"), minOdds, maxOdds));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<HandicapMatchRecord> matchedRecords = repository.findAll(spec, Sort.by(Sort.Direction.DESC, "matchDate").and(Sort.by(Sort.Direction.DESC, "id")));

        HandicapMatchSimulationVO vo = new HandicapMatchSimulationVO();
        vo.setMatchSystem(matchSystem);
        vo.setMatchSystemDesc("LEAGUE".equals(matchSystem) ? "联赛" : ("CUP".equals(matchSystem) ? "杯赛/淘汰赛" : ("FRIENDLY".equals(matchSystem) ? "友谊赛" : "全赛制")));
        vo.setHandicapSide(handicapSide);
        vo.setHandicapSideDesc("HOME".equals(handicapSide) ? "主让" : "客让");
        vo.setTargetOdds(targetOdds);
        vo.setTolerance(tolerance);
        vo.setMinOdds(minOdds);
        vo.setMaxOdds(maxOdds);
        vo.setInputHomeWinOdds(request.getHomeWinOdds());
        vo.setInputDrawOdds(request.getDrawOdds());
        vo.setInputAwayWinOdds(request.getAwayWinOdds());
        vo.setInputHandicapHomeWinOdds(request.getHandicapHomeWinOdds());
        vo.setInputHandicapDrawOdds(request.getHandicapDrawOdds());
        vo.setInputHandicapAwayWinOdds(request.getHandicapAwayWinOdds());
        vo.setOddsRangeDesc(String.format("%.2f ~ %.2f (基准让球胜赔 %.2f ± %.2f)", minOdds, maxOdds, targetOdds, tolerance));

        int sampleCount = matchedRecords.size();
        vo.setSampleCount(sampleCount);

        if (sampleCount == 0) {
            vo.setHomeWinProb(BigDecimal.ZERO);
            vo.setDrawProb(BigDecimal.ZERO);
            vo.setAwayWinProb(BigDecimal.ZERO);
            vo.setBreakProb(BigDecimal.ZERO);
            vo.setHomeWinCount(0);
            vo.setDrawCount(0);
            vo.setAwayWinCount(0);
            vo.setBreakCount(0);
            vo.setHomeWinRoi(BigDecimal.ZERO);
            vo.setDrawRoi(BigDecimal.ZERO);
            vo.setAwayWinRoi(BigDecimal.ZERO);
            vo.setHandicapWinProb(BigDecimal.ZERO);
            vo.setHandicapDrawProb(BigDecimal.ZERO);
            vo.setHandicapLossProb(BigDecimal.ZERO);
            vo.setHandicapWinCount(0);
            vo.setHandicapDrawCount(0);
            vo.setHandicapLossCount(0);
            vo.setHandicapWinRoi(BigDecimal.ZERO);
            vo.setHandicapDrawRoi(BigDecimal.ZERO);
            vo.setHandicapLossRoi(BigDecimal.ZERO);
            vo.setBestChoice("NONE");
            vo.setBestChoiceName("暂无样本");
            vo.setMaxRoi(BigDecimal.ZERO);
            vo.setAdvice("库内当前在赔率 " + minOdds + " ~ " + maxOdds + " (" + vo.getMatchSystemDesc() + "·" + vo.getHandicapSideDesc() + ") 下暂无样本。建议调大容差步幅（如设为0.15或0.20），或录入历史比赛数据。");
            vo.setRiskLevel("HIGH");
            vo.setGoalsDistribution(Collections.emptyMap());
            vo.setAllRoiRankings(Collections.emptyList());
            vo.setHandicapRoiList(Collections.emptyList());
            vo.setGoalsRoiList(Collections.emptyList());
            vo.setHalfFullRoiList(Collections.emptyList());
            vo.setScoreRoiList(Collections.emptyList());
            return vo;
        }

        int homeWin = 0;
        int draw = 0;
        int awayWin = 0;
        int breakCount = 0;
        int hWin = 0;
        int hDraw = 0;
        int hLoss = 0;


        Map<String, Long> goalsMap = new HashMap<>();
        Map<String, Long> halfFullMap = new HashMap<>();

        for (HandicapMatchRecord r : matchedRecords) {
            String res = r.getMatchResult();
            if ("HOME_WIN".equalsIgnoreCase(res)) {
                homeWin++;
            } else if ("DRAW".equalsIgnoreCase(res)) {
                draw++;
            } else if ("AWAY_WIN".equalsIgnoreCase(res)) {
                awayWin++;
            }
            if (Boolean.TRUE.equals(r.getIsBreak())) {
                breakCount++;
            }

            // 统计让球结果 (WIN/DRAW/LOSS)
            String hr = r.getHandicapResult();
            if (hr == null || hr.trim().isEmpty()) {
                if (Boolean.TRUE.equals(r.getIsBreak())) {
                    hr = "WIN";
                } else if (r.getFullScore() != null && r.getFullScore().contains(":")) {
                    try {
                        String[] parts = r.getFullScore().split(":");
                        int hg = Integer.parseInt(parts[0].trim());
                        int ag = Integer.parseInt(parts[1].trim());
                        double hVal = r.getHandicapValue() != null ? r.getHandicapValue().doubleValue() : -1.0;
                        double diff = "HOME".equalsIgnoreCase(r.getHandicapSide()) ? (hg - ag) + hVal : (ag - hg) + hVal;
                        if (diff > 0.001) hr = "WIN";
                        else if (Math.abs(diff) <= 0.001) hr = "DRAW";
                        else hr = "LOSS";
                    } catch (Exception ignored) {}
                }
            }
            if ("WIN".equalsIgnoreCase(hr)) {
                hWin++;
            } else if ("DRAW".equalsIgnoreCase(hr)) {
                hDraw++;
            } else if ("LOSS".equalsIgnoreCase(hr)) {
                hLoss++;
            }

            if (r.getTotalGoals() != null) {
                String gKey = r.getTotalGoals() + "球";
                goalsMap.put(gKey, goalsMap.getOrDefault(gKey, 0L) + 1);
            }
            if (r.getHalfFullResult() != null && !r.getHalfFullResult().trim().isEmpty()) {
                String hfKey = r.getHalfFullResult().trim();
                halfFullMap.put(hfKey, halfFullMap.getOrDefault(hfKey, 0L) + 1);
            }
        }

        vo.setHomeWinCount(homeWin);
        vo.setDrawCount(draw);
        vo.setAwayWinCount(awayWin);
        vo.setBreakCount(breakCount);

        BigDecimal n = new BigDecimal(sampleCount);
        BigDecimal homeWinProb = new BigDecimal(homeWin).multiply(new BigDecimal(100)).divide(n, 2, RoundingMode.HALF_UP);
        BigDecimal drawProb = new BigDecimal(draw).multiply(new BigDecimal(100)).divide(n, 2, RoundingMode.HALF_UP);
        BigDecimal awayWinProb = new BigDecimal(awayWin).multiply(new BigDecimal(100)).divide(n, 2, RoundingMode.HALF_UP);
        BigDecimal breakProb = new BigDecimal(breakCount).multiply(new BigDecimal(100)).divide(n, 2, RoundingMode.HALF_UP);

        BigDecimal hWinProb = new BigDecimal(hWin).multiply(new BigDecimal(100)).divide(n, 2, RoundingMode.HALF_UP);
        BigDecimal hDrawProb = new BigDecimal(hDraw).multiply(new BigDecimal(100)).divide(n, 2, RoundingMode.HALF_UP);
        BigDecimal hLossProb = new BigDecimal(hLoss).multiply(new BigDecimal(100)).divide(n, 2, RoundingMode.HALF_UP);

        vo.setHomeWinProb(homeWinProb);
        vo.setDrawProb(drawProb);
        vo.setAwayWinProb(awayWinProb);
        vo.setBreakProb(breakProb);

        vo.setHandicapWinCount(hWin);
        vo.setHandicapDrawCount(hDraw);
        vo.setHandicapLossCount(hLoss);
        vo.setHandicapWinProb(hWinProb);
        vo.setHandicapDrawProb(hDrawProb);
        vo.setHandicapLossProb(hLossProb);

        // 计算胜平负期望 ROI: ROI = (P * odds - 1) * 100%
        BigDecimal homeRoi = BigDecimal.ZERO;
        if (request.getHomeWinOdds() != null && request.getHomeWinOdds().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal p = new BigDecimal(homeWin).divide(n, 4, RoundingMode.HALF_UP);
            homeRoi = p.multiply(request.getHomeWinOdds()).subtract(BigDecimal.ONE).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
        }
        vo.setHomeWinRoi(homeRoi);

        BigDecimal drawRoi = BigDecimal.ZERO;
        if (request.getDrawOdds() != null && request.getDrawOdds().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal p = new BigDecimal(draw).divide(n, 4, RoundingMode.HALF_UP);
            drawRoi = p.multiply(request.getDrawOdds()).subtract(BigDecimal.ONE).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
        }
        vo.setDrawRoi(drawRoi);

        BigDecimal awayRoi = BigDecimal.ZERO;
        if (request.getAwayWinOdds() != null && request.getAwayWinOdds().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal p = new BigDecimal(awayWin).divide(n, 4, RoundingMode.HALF_UP);
            awayRoi = p.multiply(request.getAwayWinOdds()).subtract(BigDecimal.ONE).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
        }
        vo.setAwayWinRoi(awayRoi);

        // 计算让球胜平负期望 ROI
        BigDecimal hWinRoi = BigDecimal.ZERO;
        if (request.getHandicapHomeWinOdds() != null && request.getHandicapHomeWinOdds().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal p = new BigDecimal(hWin).divide(n, 4, RoundingMode.HALF_UP);
            hWinRoi = p.multiply(request.getHandicapHomeWinOdds()).subtract(BigDecimal.ONE).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
        }
        vo.setHandicapWinRoi(hWinRoi);

        BigDecimal hDrawRoi = BigDecimal.ZERO;
        if (request.getHandicapDrawOdds() != null && request.getHandicapDrawOdds().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal p = new BigDecimal(hDraw).divide(n, 4, RoundingMode.HALF_UP);
            hDrawRoi = p.multiply(request.getHandicapDrawOdds()).subtract(BigDecimal.ONE).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
        }
        vo.setHandicapDrawRoi(hDrawRoi);

        BigDecimal hLossRoi = BigDecimal.ZERO;
        if (request.getHandicapAwayWinOdds() != null && request.getHandicapAwayWinOdds().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal p = new BigDecimal(hLoss).divide(n, 4, RoundingMode.HALF_UP);
            hLossRoi = p.multiply(request.getHandicapAwayWinOdds()).subtract(BigDecimal.ONE).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
        }
        vo.setHandicapLossRoi(hLossRoi);

        // 决策最优选项 (胜平负)
        String bestChoice = "HOME_WIN";
        String bestChoiceName = "主胜";
        BigDecimal maxRoi = homeRoi;

        if (drawRoi.compareTo(maxRoi) > 0) {
            maxRoi = drawRoi;
            bestChoice = "DRAW";
            bestChoiceName = "平局";
        }
        if (awayRoi.compareTo(maxRoi) > 0) {
            maxRoi = awayRoi;
            bestChoice = "AWAY_WIN";
            bestChoiceName = "客胜";
        }

        vo.setBestChoice(bestChoice);
        vo.setBestChoiceName(bestChoiceName);
        vo.setMaxRoi(maxRoi);

        // ==================== 多维度玩法 ROI 计算 (让球胜平负、进球数、半全场、比分) ====================
        List<HandicapMatchSimulationVO.RoiItemVO> allRoiRankings = new ArrayList<>();

        // 1. 胜平负项加入天梯
        if (request.getHomeWinOdds() != null && request.getHomeWinOdds().compareTo(BigDecimal.ZERO) > 0) {
            allRoiRankings.add(new HandicapMatchSimulationVO.RoiItemVO(
                    "RESULT", "胜平负", "HOME_WIN", "主胜",
                    request.getHomeWinOdds(), homeWin, sampleCount, homeWinProb, homeRoi, homeRoi.compareTo(BigDecimal.ZERO) > 0
            ));
        }
        if (request.getDrawOdds() != null && request.getDrawOdds().compareTo(BigDecimal.ZERO) > 0) {
            allRoiRankings.add(new HandicapMatchSimulationVO.RoiItemVO(
                    "RESULT", "胜平负", "DRAW", "平局",
                    request.getDrawOdds(), draw, sampleCount, drawProb, drawRoi, drawRoi.compareTo(BigDecimal.ZERO) > 0
            ));
        }
        if (request.getAwayWinOdds() != null && request.getAwayWinOdds().compareTo(BigDecimal.ZERO) > 0) {
            allRoiRankings.add(new HandicapMatchSimulationVO.RoiItemVO(
                    "RESULT", "胜平负", "AWAY_WIN", "客胜",
                    request.getAwayWinOdds(), awayWin, sampleCount, awayWinProb, awayRoi, awayRoi.compareTo(BigDecimal.ZERO) > 0
            ));
        }

        // 2. 让球胜平负项加入天梯
        List<HandicapMatchSimulationVO.RoiItemVO> handicapRoiList = new ArrayList<>();
        if (request.getHandicapHomeWinOdds() != null && request.getHandicapHomeWinOdds().compareTo(BigDecimal.ZERO) > 0) {
            HandicapMatchSimulationVO.RoiItemVO item = new HandicapMatchSimulationVO.RoiItemVO(
                    "HANDICAP", "让球胜平负", "HANDICAP_WIN", "让胜",
                    request.getHandicapHomeWinOdds(), hWin, sampleCount, hWinProb, hWinRoi, hWinRoi.compareTo(BigDecimal.ZERO) > 0
            );
            handicapRoiList.add(item);
            allRoiRankings.add(item);
        }
        if (request.getHandicapDrawOdds() != null && request.getHandicapDrawOdds().compareTo(BigDecimal.ZERO) > 0) {
            HandicapMatchSimulationVO.RoiItemVO item = new HandicapMatchSimulationVO.RoiItemVO(
                    "HANDICAP", "让球胜平负", "HANDICAP_DRAW", "让平",
                    request.getHandicapDrawOdds(), hDraw, sampleCount, hDrawProb, hDrawRoi, hDrawRoi.compareTo(BigDecimal.ZERO) > 0
            );
            handicapRoiList.add(item);
            allRoiRankings.add(item);
        }
        if (request.getHandicapAwayWinOdds() != null && request.getHandicapAwayWinOdds().compareTo(BigDecimal.ZERO) > 0) {
            HandicapMatchSimulationVO.RoiItemVO item = new HandicapMatchSimulationVO.RoiItemVO(
                    "HANDICAP", "让球胜平负", "HANDICAP_LOSS", "让负",
                    request.getHandicapAwayWinOdds(), hLoss, sampleCount, hLossProb, hLossRoi, hLossRoi.compareTo(BigDecimal.ZERO) > 0
            );
            handicapRoiList.add(item);
            allRoiRankings.add(item);
        }
        vo.setHandicapRoiList(handicapRoiList);


        // 2. 进球数 ROI 测算
        List<HandicapMatchSimulationVO.RoiItemVO> goalsRoiList = new ArrayList<>();
        if (request.getGoalsOdds() != null && !request.getGoalsOdds().isEmpty()) {
            for (Map.Entry<String, BigDecimal> entry : request.getGoalsOdds().entrySet()) {
                String gKey = entry.getKey() != null ? entry.getKey().trim() : "";
                BigDecimal odds = entry.getValue();
                if (gKey.isEmpty() || odds == null || odds.compareTo(BigDecimal.ZERO) <= 0) continue;

                int hit = 0;
                boolean isSevenPlus = gKey.startsWith("7") || gKey.contains("7+");
                int targetGoalNum = -1;
                if (!isSevenPlus) {
                    try {
                        targetGoalNum = Integer.parseInt(gKey.replace("球", "").trim());
                    } catch (NumberFormatException ignored) {}
                }

                for (HandicapMatchRecord r : matchedRecords) {
                    if (r.getTotalGoals() == null) continue;
                    if (isSevenPlus) {
                        if (r.getTotalGoals() >= 7) hit++;
                    } else if (targetGoalNum >= 0) {
                        if (r.getTotalGoals() == targetGoalNum) hit++;
                    }
                }

                BigDecimal prob = new BigDecimal(hit).multiply(new BigDecimal(100)).divide(n, 2, RoundingMode.HALF_UP);
                BigDecimal p = new BigDecimal(hit).divide(n, 4, RoundingMode.HALF_UP);
                BigDecimal roi = p.multiply(odds).subtract(BigDecimal.ONE).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
                String displayName = isSevenPlus ? "7+球" : (targetGoalNum >= 0 ? targetGoalNum + "球" : gKey);

                HandicapMatchSimulationVO.RoiItemVO item = new HandicapMatchSimulationVO.RoiItemVO(
                        "GOALS", "总进球", gKey, displayName, odds, hit, sampleCount, prob, roi, roi.compareTo(BigDecimal.ZERO) > 0
                );
                goalsRoiList.add(item);
                allRoiRankings.add(item);
            }
            goalsRoiList.sort((a, b) -> b.getRoi().compareTo(a.getRoi()));
        }
        vo.setGoalsRoiList(goalsRoiList);

        // 3. 半全场 ROI 测算
        List<HandicapMatchSimulationVO.RoiItemVO> halfFullRoiList = new ArrayList<>();
        if (request.getHalfFullOdds() != null && !request.getHalfFullOdds().isEmpty()) {
            for (Map.Entry<String, BigDecimal> entry : request.getHalfFullOdds().entrySet()) {
                String hfKey = entry.getKey() != null ? entry.getKey().trim() : "";
                BigDecimal odds = entry.getValue();
                if (hfKey.isEmpty() || odds == null || odds.compareTo(BigDecimal.ZERO) <= 0) continue;

                int hit = 0;
                for (HandicapMatchRecord r : matchedRecords) {
                    if (r.getHalfFullResult() != null && r.getHalfFullResult().trim().equals(hfKey)) {
                        hit++;
                    }
                }

                BigDecimal prob = new BigDecimal(hit).multiply(new BigDecimal(100)).divide(n, 2, RoundingMode.HALF_UP);
                BigDecimal p = new BigDecimal(hit).divide(n, 4, RoundingMode.HALF_UP);
                BigDecimal roi = p.multiply(odds).subtract(BigDecimal.ONE).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);

                HandicapMatchSimulationVO.RoiItemVO item = new HandicapMatchSimulationVO.RoiItemVO(
                        "HALF_FULL", "半全场", hfKey, hfKey, odds, hit, sampleCount, prob, roi, roi.compareTo(BigDecimal.ZERO) > 0
                );
                halfFullRoiList.add(item);
                allRoiRankings.add(item);
            }
            halfFullRoiList.sort((a, b) -> b.getRoi().compareTo(a.getRoi()));
        }
        vo.setHalfFullRoiList(halfFullRoiList);

        // 4. 比分 ROI 测算
        List<HandicapMatchSimulationVO.RoiItemVO> scoreRoiList = new ArrayList<>();
        if (request.getScoreOdds() != null && !request.getScoreOdds().isEmpty()) {
            for (Map.Entry<String, BigDecimal> entry : request.getScoreOdds().entrySet()) {
                String rawScore = entry.getKey() != null ? entry.getKey().trim() : "";
                BigDecimal odds = entry.getValue();
                if (rawScore.isEmpty() || odds == null || odds.compareTo(BigDecimal.ZERO) <= 0) continue;

                String normKey = rawScore.replace("-", ":").replace("：", ":");
                int hit = 0;
                for (HandicapMatchRecord r : matchedRecords) {
                    if (r.getFullScore() != null) {
                        String rNorm = r.getFullScore().trim().replace("-", ":").replace("：", ":");
                        if (rNorm.equals(normKey)) {
                            hit++;
                        }
                    }
                }

                BigDecimal prob = new BigDecimal(hit).multiply(new BigDecimal(100)).divide(n, 2, RoundingMode.HALF_UP);
                BigDecimal p = new BigDecimal(hit).divide(n, 4, RoundingMode.HALF_UP);
                BigDecimal roi = p.multiply(odds).subtract(BigDecimal.ONE).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);

                HandicapMatchSimulationVO.RoiItemVO item = new HandicapMatchSimulationVO.RoiItemVO(
                        "SCORE", "比分", rawScore, normKey, odds, hit, sampleCount, prob, roi, roi.compareTo(BigDecimal.ZERO) > 0
                );
                scoreRoiList.add(item);
                allRoiRankings.add(item);
            }
            scoreRoiList.sort((a, b) -> b.getRoi().compareTo(a.getRoi()));
        }
        vo.setScoreRoiList(scoreRoiList);

        // 综合排序全天梯榜单
        allRoiRankings.sort((a, b) -> b.getRoi().compareTo(a.getRoi()));
        vo.setAllRoiRankings(allRoiRankings);

        // 评定风险等级
        if (sampleCount >= 10) {
            vo.setRiskLevel("LOW");
        } else if (sampleCount >= 5) {
            vo.setRiskLevel("MEDIUM");
        } else {
            vo.setRiskLevel("HIGH");
        }

        // 生成策略建议
        StringBuilder sb = new StringBuilder();
        if (maxRoi.compareTo(BigDecimal.ZERO) > 0) {
            sb.append("🔥 【高价值发现】胜平负历史样本中【").append(bestChoiceName).append("】期望回报率(ROI)为 +")
              .append(maxRoi).append("%，存在超额正向数学期望（Value Bet）！");
        } else {
            sb.append("⚠️ 【负期望规避】胜平负三项期望回报率均为负，其中【")
              .append(bestChoiceName).append("】相对损耗最低 (ROI: ").append(maxRoi).append("%)。");
        }

        // 若进球/半全场/比分中发现了更高的正期望超额选项，额外提示
        Optional<HandicapMatchSimulationVO.RoiItemVO> topValueBet = allRoiRankings.stream()
                .filter(i -> Boolean.TRUE.equals(i.getIsValue()))
                .findFirst();
        if (topValueBet.isPresent()) {
            HandicapMatchSimulationVO.RoiItemVO tv = topValueBet.get();
            sb.append(" 🌟【跨玩法最佳价值点】：【").append(tv.getCategoryName()).append(" · ").append(tv.getOptionName())
              .append("】期望ROI达 +").append(tv.getRoi()).append("% (打出率 ").append(tv.getProbability()).append("%)！");
        }

        sb.append(" 历史样本开奖分布：主胜 ").append(homeWinProb).append("%，平局 ")
          .append(drawProb).append("%，客胜 ").append(awayWinProb).append("%。");
        if (breakProb.compareTo(new BigDecimal("50.00")) >= 0) {
            sb.append(" 穿盘率达 ").append(breakProb).append("%，让球方打穿能力显著，可优先参考让球胜盘口。");
        } else {
            sb.append(" 穿盘率为 ").append(breakProb).append("%，卡盘/冷门下盘风险偏高，让球玩法宜谨慎防平/防下。");
        }
        if (sampleCount < 6) {
            sb.append(" （提示：样本量仅 ").append(sampleCount).append(" 场，建议适当放宽步幅容差或补充历史数据增强置信度）。");
        }
        vo.setAdvice(sb.toString());

        // 进球与半全场 Top 排序
        vo.setGoalsDistribution(goalsMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(4)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)));

        vo.setHalfFullDistribution(halfFullMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(4)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)));

        // 样本比赛清单映射 (最多返回20场)
        List<HandicapMatchSimulationVO.MatchedSampleItemVO> sampleList = matchedRecords.stream().limit(20).map(r -> {
            HandicapMatchSimulationVO.MatchedSampleItemVO item = new HandicapMatchSimulationVO.MatchedSampleItemVO();
            item.setId(r.getId());
            item.setMatchDate(r.getMatchDate() != null ? r.getMatchDate().toString() : "");
            item.setLeagueName(r.getLeagueName());
            item.setHomeTeam(r.getHomeTeam());
            item.setAwayTeam(r.getAwayTeam());
            item.setFullScore(r.getFullScore());
            item.setHalfScore(r.getHalfScore());
            item.setMatchResult(r.getMatchResult());
            item.setMatchResultName("HOME_WIN".equals(r.getMatchResult()) ? "主胜" : ("DRAW".equals(r.getMatchResult()) ? "平局" : "客胜"));
            item.setResultOdds(r.getResultOdds());
            item.setHandicapOdds(r.getHandicapOdds());
            item.setIsBreak(r.getIsBreak());
            item.setHalfFullResult(r.getHalfFullResult());
            item.setTotalGoals(r.getTotalGoals());
            return item;
        }).collect(Collectors.toList());

        vo.setSampleMatches(sampleList);

        return vo;
    }
}

