package com.example.winnerLogBackend.service;

import com.example.winnerLogBackend.dto.request.HandicapOddsStatsRecordRequest;
import com.example.winnerLogBackend.dto.response.HandicapOddsStatsSummaryVO;
import com.example.winnerLogBackend.entity.HandicapOddsStatsRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface HandicapOddsStatsRecordService {

    List<HandicapOddsStatsRecord> getAllRecords(LocalDate startDate, LocalDate endDate);

    Page<HandicapOddsStatsRecord> getRecordsPage(LocalDate startDate, LocalDate endDate, Pageable pageable);

    HandicapOddsStatsRecord getRecordById(Long id);

    HandicapOddsStatsRecord getRecordByDate(LocalDate recordDate);

    HandicapOddsStatsRecord createRecord(HandicapOddsStatsRecordRequest request);

    HandicapOddsStatsRecord updateRecord(Long id, HandicapOddsStatsRecordRequest request);

    void deleteRecord(Long id);

    HandicapOddsStatsSummaryVO getMacroSummary(LocalDate startDate, LocalDate endDate);
}
