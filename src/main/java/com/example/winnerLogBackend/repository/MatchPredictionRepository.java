package com.example.winnerLogBackend.repository;

import com.example.winnerLogBackend.entity.MatchPrediction;
import com.example.winnerLogBackend.enums.SettleStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchPredictionRepository extends JpaRepository<MatchPrediction, Long> {
    Optional<MatchPrediction> findByMatchIdAndAiModelCode(Long matchId, String aiModelCode);
    List<MatchPrediction> findByMatchId(Long matchId);
    List<MatchPrediction> findByMatchIdIn(List<Long> matchIds);
    List<MatchPrediction> findByAiModelCodeAndSettleStatus(String aiModelCode, SettleStatusEnum settleStatus);

    @Query("SELECT p FROM MatchPrediction p JOIN FootballMatch m ON p.matchId = m.id " +
           "WHERE p.aiModelCode = :aiModelCode AND p.settleStatus != 'PENDING' " +
           "ORDER BY m.matchTime DESC")
    List<MatchPrediction> findRecentSettledPredictionsByAiModel(@Param("aiModelCode") String aiModelCode);

    @Query("SELECT p FROM MatchPrediction p JOIN FootballMatch m ON p.matchId = m.id " +
           "WHERE p.aiModelCode = :aiModelCode AND m.leagueCode = :leagueCode AND m.seasonName = :seasonName AND p.settleStatus != 'PENDING' " +
           "ORDER BY m.matchTime DESC")
    List<MatchPrediction> findRecentSettledPredictionsByLeagueAndSeason(@Param("aiModelCode") String aiModelCode,
                                                                      @Param("leagueCode") String leagueCode,
                                                                      @Param("seasonName") String seasonName);

    @Query("SELECT p FROM MatchPrediction p JOIN FootballMatch m ON p.matchId = m.id " +
           "WHERE p.aiModelCode = :aiModelCode AND m.seasonName = :seasonName AND p.settleStatus != 'PENDING' " +
           "ORDER BY m.matchTime DESC")
    List<MatchPrediction> findRecentSettledPredictionsBySeason(@Param("aiModelCode") String aiModelCode,
                                                             @Param("seasonName") String seasonName);

    @Query("SELECT p FROM MatchPrediction p JOIN FootballMatch m ON p.matchId = m.id " +
           "WHERE p.aiModelCode = :aiModelCode AND m.leagueCode = :leagueCode AND p.settleStatus != 'PENDING' " +
           "ORDER BY m.matchTime DESC")
    List<MatchPrediction> findRecentSettledPredictionsByLeague(@Param("aiModelCode") String aiModelCode,
                                                             @Param("leagueCode") String leagueCode);
}
