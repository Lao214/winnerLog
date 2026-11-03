package com.example.winnerLogBackend.repository;

import com.example.winnerLogBackend.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    Optional<Season> findByLeagueIdAndName(Long leagueId, String name);
    Optional<Season> findByLeagueCodeAndName(String leagueCode, String name);
    List<Season> findByLeagueCodeOrderByStartDateDesc(String leagueCode);
    List<Season> findByIsCurrent(Integer isCurrent);
    Optional<Season> findByLeagueCodeAndIsCurrent(String leagueCode, Integer isCurrent);
}
