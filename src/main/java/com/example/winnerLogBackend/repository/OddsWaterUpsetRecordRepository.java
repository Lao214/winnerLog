package com.example.winnerLogBackend.repository;

import com.example.winnerLogBackend.entity.OddsWaterUpsetRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OddsWaterUpsetRecordRepository extends JpaRepository<OddsWaterUpsetRecord, Long>, JpaSpecificationExecutor<OddsWaterUpsetRecord> {

    Optional<OddsWaterUpsetRecord> findByRecordDate(LocalDate recordDate);

    boolean existsByRecordDate(LocalDate recordDate);

    boolean existsByRecordDateAndIdNot(LocalDate recordDate, Long id);

    List<OddsWaterUpsetRecord> findAllByOrderByRecordDateDesc();

    List<OddsWaterUpsetRecord> findByRecordDateBetweenOrderByRecordDateAsc(LocalDate startDate, LocalDate endDate);

    Page<OddsWaterUpsetRecord> findByRecordDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("SELECT r FROM OddsWaterUpsetRecord r ORDER BY r.recordDate ASC")
    List<OddsWaterUpsetRecord> findAllOrderByRecordDateAsc();
}
