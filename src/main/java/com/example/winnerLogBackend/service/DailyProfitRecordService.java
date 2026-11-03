package com.example.winnerLogBackend.service;

import com.example.winnerLogBackend.dto.request.DailyProfitRecordRequest;
import com.example.winnerLogBackend.dto.response.DailyProfitSummaryVO;
import com.example.winnerLogBackend.entity.DailyProfitRecord;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface DailyProfitRecordService {

    DailyProfitRecord saveOrUpdate(DailyProfitRecordRequest request);

    DailyProfitRecord getById(Long id);

    void deleteById(Long id);

    List<DailyProfitRecord> queryList(LocalDate startDate, LocalDate endDate);

    Page<DailyProfitRecord> queryPage(LocalDate startDate, LocalDate endDate, int page, int size);

    DailyProfitSummaryVO getSummaryStats(LocalDate startDate, LocalDate endDate);
}
