package com.example.winnerLogBackend.repository;

import com.example.winnerLogBackend.entity.FootballMatch;
import com.example.winnerLogBackend.enums.MatchStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FootballMatchRepository extends JpaRepository<FootballMatch, Long>, JpaSpecificationExecutor<FootballMatch> {
    Optional<FootballMatch> findByMatchCode(String matchCode);
    Optional<FootballMatch> findByHomeTeamAndAwayTeamAndMatchTime(String homeTeam, String awayTeam, LocalDateTime matchTime);
    List<FootballMatch> findByStatus(MatchStatusEnum status);
    List<FootballMatch> findByLeagueCodeAndSeasonName(String leagueCode, String seasonName);
    Page<FootballMatch> findByLeagueCodeAndSeasonName(String leagueCode, String seasonName, Pageable pageable);
}
