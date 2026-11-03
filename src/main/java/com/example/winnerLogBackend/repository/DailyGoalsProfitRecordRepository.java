package com.example.winnerLogBackend.repository;

import com.example.winnerLogBackend.entity.DailyGoalsProfitRecord;
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
public interface DailyGoalsProfitRecordRepository extends JpaRepository<DailyGoalsProfitRecord, Long>, JpaSpecificationExecutor<DailyGoalsProfitRecord> {

    Optional<DailyGoalsProfitRecord> findByMatchDate(LocalDate matchDate);

    boolean existsByMatchDate(LocalDate matchDate);

    boolean existsByMatchDateAndIdNot(LocalDate matchDate, Long id);

    List<DailyGoalsProfitRecord> findAllByOrderByMatchDateDesc();

    List<DailyGoalsProfitRecord> findByMatchDateBetweenOrderByMatchDateAsc(LocalDate startDate, LocalDate endDate);

    Page<DailyGoalsProfitRecord> findByMatchDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("SELECT r FROM DailyGoalsProfitRecord r ORDER BY r.matchDate ASC")
    List<DailyGoalsProfitRecord> findAllOrderByMatchDateAsc();
}
