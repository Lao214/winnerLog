package com.example.winnerLogBackend.service;

import com.example.winnerLogBackend.dto.request.DailyWaterComboRecordRequest;
import com.example.winnerLogBackend.dto.response.DailyWaterComboSummaryVO;
import com.example.winnerLogBackend.entity.DailyWaterComboRecord;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface DailyWaterComboRecordService {

    DailyWaterComboRecord saveOrUpdate(DailyWaterComboRecordRequest request);

    DailyWaterComboRecord getById(Long id);

    void deleteById(Long id);

    List<DailyWaterComboRecord> queryList(LocalDate startDate, LocalDate endDate);

    Page<DailyWaterComboRecord> queryPage(LocalDate startDate, LocalDate endDate, int page, int size);

    DailyWaterComboSummaryVO getSummaryStats(LocalDate startDate, LocalDate endDate);
}
