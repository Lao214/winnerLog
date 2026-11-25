package com.example.winnerLogBackend.repository;

import com.example.winnerLogBackend.entity.HandicapMatchRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HandicapMatchRecordRepository extends JpaRepository<HandicapMatchRecord, Long>, JpaSpecificationExecutor<HandicapMatchRecord> {

    List<HandicapMatchRecord> findAllByOrderByMatchDateDescIdDesc();

    List<HandicapMatchRecord> findByMatchDateBetweenOrderByMatchDateDescIdDesc(LocalDate startDate, LocalDate endDate);

    List<HandicapMatchRecord> findByHandicapSideOrderByMatchDateDescIdDesc(String handicapSide);

    Page<HandicapMatchRecord> findByMatchDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("SELECT DISTINCT r.leagueName FROM HandicapMatchRecord r WHERE r.leagueName IS NOT NULL AND TRIM(r.leagueName) != '' ORDER BY r.leagueName ASC")
    List<String> findDistinctLeagueNames();
}

