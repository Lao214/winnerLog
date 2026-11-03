package com.example.winnerLogBackend.repository;

import com.example.winnerLogBackend.entity.AiLeaderboardStat;
import com.example.winnerLogBackend.enums.ScopeTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiLeaderboardStatRepository extends JpaRepository<AiLeaderboardStat, Long> {

    Optional<AiLeaderboardStat> findByAiModelCodeAndScopeTypeAndLeagueCodeAndSeasonName(
            String aiModelCode, ScopeTypeEnum scopeType, String leagueCode, String seasonName);

    List<AiLeaderboardStat> findByScopeType(ScopeTypeEnum scopeType);

    List<AiLeaderboardStat> findByScopeTypeAndSeasonName(ScopeTypeEnum scopeType, String seasonName);

    List<AiLeaderboardStat> findByScopeTypeAndSeasonNameAndLeagueCode(
            ScopeTypeEnum scopeType, String seasonName, String leagueCode);

    List<AiLeaderboardStat> findByScopeTypeAndLeagueCode(ScopeTypeEnum scopeType, String leagueCode);

    List<AiLeaderboardStat> findByAiModelCode(String aiModelCode);
}
