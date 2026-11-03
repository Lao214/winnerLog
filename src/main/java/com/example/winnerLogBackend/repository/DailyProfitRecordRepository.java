package com.example.winnerLogBackend.repository;

import com.example.winnerLogBackend.entity.DailyProfitRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyProfitRecordRepository extends JpaRepository<DailyProfitRecord, Long>, JpaSpecificationExecutor<DailyProfitRecord> {

    Optional<DailyProfitRecord> findByMatchDate(LocalDate matchDate);

    boolean existsByMatchDate(LocalDate matchDate);

    boolean existsByMatchDateAndIdNot(LocalDate matchDate, Long id);

    List<DailyProfitRecord> findAllByOrderByMatchDateDesc();

    List<DailyProfitRecord> findByMatchDateBetweenOrderByMatchDateAsc(LocalDate startDate, LocalDate endDate);

    Page<DailyProfitRecord> findByMatchDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("SELECT r FROM DailyProfitRecord r ORDER BY r.matchDate ASC")
    List<DailyProfitRecord> findAllOrderByMatchDateAsc();
}
