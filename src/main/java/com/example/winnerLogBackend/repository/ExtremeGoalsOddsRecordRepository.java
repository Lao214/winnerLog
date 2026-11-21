package com.example.winnerLogBackend.repository;

import com.example.winnerLogBackend.entity.ExtremeGoalsOddsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtremeGoalsOddsRecordRepository extends JpaRepository<ExtremeGoalsOddsRecord, Long>, JpaSpecificationExecutor<ExtremeGoalsOddsRecord> {

    @Query("SELECT DISTINCT r.leagueName FROM ExtremeGoalsOddsRecord r WHERE r.leagueName IS NOT NULL ORDER BY r.leagueName ASC")
    List<String> findDistinctLeagueNames();

    @Query("SELECT DISTINCT r.batchTitle FROM ExtremeGoalsOddsRecord r WHERE r.batchTitle IS NOT NULL ORDER BY r.batchTitle ASC")
    List<String> findDistinctBatchTitles();
}
