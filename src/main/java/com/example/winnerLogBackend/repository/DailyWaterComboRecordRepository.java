package com.example.winnerLogBackend.repository;

import com.example.winnerLogBackend.entity.DailyWaterComboRecord;
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
public interface DailyWaterComboRecordRepository extends JpaRepository<DailyWaterComboRecord, Long>, JpaSpecificationExecutor<DailyWaterComboRecord> {

    Optional<DailyWaterComboRecord> findByRecordDate(LocalDate recordDate);

    boolean existsByRecordDate(LocalDate recordDate);

    boolean existsByRecordDateAndIdNot(LocalDate recordDate, Long id);

    List<DailyWaterComboRecord> findAllByOrderByRecordDateDesc();

    List<DailyWaterComboRecord> findByRecordDateBetweenOrderByRecordDateAsc(LocalDate startDate, LocalDate endDate);

    Page<DailyWaterComboRecord> findByRecordDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("SELECT r FROM DailyWaterComboRecord r ORDER BY r.recordDate ASC")
    List<DailyWaterComboRecord> findAllOrderByRecordDateAsc();
}
