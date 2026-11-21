package com.example.winnerLogBackend.service;

import com.example.winnerLogBackend.dto.request.ExtremeGoalsOddsRecordRequest;
import com.example.winnerLogBackend.dto.request.ExtremeGoalsSimulateRequest;
import com.example.winnerLogBackend.dto.response.ExtremeGoalsSimulateVO;
import com.example.winnerLogBackend.dto.response.ExtremeGoalsStatsSummaryVO;
import com.example.winnerLogBackend.entity.ExtremeGoalsOddsRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ExtremeGoalsOddsRecordService {

    List<ExtremeGoalsOddsRecord> getAllRecords(LocalDate startDate, LocalDate endDate, String leagueName, String matchSystem);

    Page<ExtremeGoalsOddsRecord> getRecordsPage(LocalDate startDate, LocalDate endDate, String leagueName, String matchSystem, Pageable pageable);

    ExtremeGoalsOddsRecord getRecordById(Long id);

    ExtremeGoalsOddsRecord createRecord(ExtremeGoalsOddsRecordRequest request);

    List<ExtremeGoalsOddsRecord> batchCreateRecords(List<ExtremeGoalsOddsRecordRequest> requests);

    ExtremeGoalsOddsRecord updateRecord(Long id, ExtremeGoalsOddsRecordRequest request);

    void deleteRecord(Long id);

    void batchDeleteRecords(List<Long> ids);

    ExtremeGoalsStatsSummaryVO getStatsSummary(LocalDate startDate, LocalDate endDate, String leagueName, String matchSystem);

    ExtremeGoalsSimulateVO simulate(ExtremeGoalsSimulateRequest request);

    List<String> getDistinctLeagueNames();
}
