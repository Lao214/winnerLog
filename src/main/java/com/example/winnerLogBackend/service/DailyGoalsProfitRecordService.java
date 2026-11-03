package com.example.winnerLogBackend.service;

import com.example.winnerLogBackend.dto.request.DailyGoalsProfitRecordRequest;
import com.example.winnerLogBackend.dto.response.DailyGoalsProfitSummaryVO;
import com.example.winnerLogBackend.entity.DailyGoalsProfitRecord;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface DailyGoalsProfitRecordService {

    DailyGoalsProfitRecord saveOrUpdate(DailyGoalsProfitRecordRequest request);

    DailyGoalsProfitRecord getById(Long id);

    void deleteById(Long id);

    List<DailyGoalsProfitRecord> queryList(LocalDate startDate, LocalDate endDate);

    Page<DailyGoalsProfitRecord> queryPage(LocalDate startDate, LocalDate endDate, int page, int size);

    DailyGoalsProfitSummaryVO getSummaryStats(LocalDate startDate, LocalDate endDate);
}
