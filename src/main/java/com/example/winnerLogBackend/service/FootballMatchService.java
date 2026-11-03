package com.example.winnerLogBackend.service;

import com.example.winnerLogBackend.common.BusinessException;
import com.example.winnerLogBackend.dto.request.MatchQueryRequest;
import com.example.winnerLogBackend.dto.response.MatchDetailVO;
import com.example.winnerLogBackend.entity.FootballMatch;
import com.example.winnerLogBackend.repository.FootballMatchRepository;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FootballMatchService {

    private static final Logger log = LoggerFactory.getLogger(FootballMatchService.class);

    private final FootballMatchRepository matchRepository;
    private final PredictionImportService predictionImportService;

    public FootballMatchService(FootballMatchRepository matchRepository, PredictionImportService predictionImportService) {
        this.matchRepository = matchRepository;
        this.predictionImportService = predictionImportService;
    }

    /**
     * 多条件分页查询比赛列表 (附带各AI预测数据)
     */
    public Page<MatchDetailVO> queryMatches(MatchQueryRequest request) {
        int page = request.getPage() != null && request.getPage() >= 0 ? request.getPage() : 0;
        int size = request.getSize() != null && request.getSize() > 0 ? request.getSize() : 20;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "matchTime"));

        Specification<FootballMatch> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getLeagueCode() != null && !request.getLeagueCode().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("leagueCode"), request.getLeagueCode().trim().toUpperCase()));
            }

            if (request.getSeasonName() != null && !request.getSeasonName().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("seasonName"), request.getSeasonName().trim()));
            }

            if (request.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }

            if (request.getTeamName() != null && !request.getTeamName().isBlank()) {
                String teamPattern = "%" + request.getTeamName().trim() + "%";
                Predicate homeLike = criteriaBuilder.like(root.get("homeTeam"), teamPattern);
                Predicate awayLike = criteriaBuilder.like(root.get("awayTeam"), teamPattern);
                predicates.add(criteriaBuilder.or(homeLike, awayLike));
            }

            if (request.getStartTime() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("matchTime"), request.getStartTime()));
            }

            if (request.getEndTime() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("matchTime"), request.getEndTime()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<FootballMatch> matchPage = matchRepository.findAll(spec, pageable);
        return matchPage.map(predictionImportService::buildMatchDetailVO);
    }

    /**
     * 根据比赛ID查询详情
     */
    public MatchDetailVO getMatchDetailById(Long matchId) {
        FootballMatch match = matchRepository.findById(matchId)
                .orElseThrow(() -> new BusinessException(404, "未找到ID为 " + matchId + " 的比赛"));
        return predictionImportService.buildMatchDetailVO(match);
    }
}
