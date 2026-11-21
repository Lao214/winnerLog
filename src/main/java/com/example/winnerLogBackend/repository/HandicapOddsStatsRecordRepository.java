package com.example.winnerLogBackend.repository;

import com.example.winnerLogBackend.entity.HandicapOddsStatsRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HandicapOddsStatsRecordRepository extends JpaRepository<HandicapOddsStatsRecord, Long> {

    Optional<HandicapOddsStatsRecord> findByRecordDate(LocalDate recordDate);

    boolean existsByRecordDate(LocalDate recordDate);

    boolean existsByRecordDateAndIdNot(LocalDate recordDate, Long id);

    List<HandicapOddsStatsRecord> findByRecordDateBetweenOrderByRecordDateAsc(LocalDate startDate, LocalDate endDate);

    Page<HandicapOddsStatsRecord> findByRecordDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
