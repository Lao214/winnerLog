package com.example.winnerLogBackend.service;

import com.example.winnerLogBackend.dto.request.HandicapMatchRecordRequest;
import com.example.winnerLogBackend.entity.HandicapMatchRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface HandicapMatchRecordService {

    List<HandicapMatchRecord> getAllMatches(LocalDate startDate, LocalDate endDate, String handicapSide, String matchSystem, String leagueName);

    Page<HandicapMatchRecord> getMatchesPage(LocalDate startDate, LocalDate endDate, String handicapSide, String matchSystem, String leagueName, Pageable pageable);


    HandicapMatchRecord getMatchById(Long id);

    HandicapMatchRecord createMatch(HandicapMatchRecordRequest request);

    List<HandicapMatchRecord> batchCreateMatches(List<HandicapMatchRecordRequest> requests);

    HandicapMatchRecord updateMatch(Long id, HandicapMatchRecordRequest request);

    void deleteMatch(Long id);

    void batchDeleteMatches(List<Long> ids);

    com.example.winnerLogBackend.dto.response.HandicapMatchSimulationVO simulateMatch(com.example.winnerLogBackend.dto.request.HandicapMatchSimulateRequest request);
}
