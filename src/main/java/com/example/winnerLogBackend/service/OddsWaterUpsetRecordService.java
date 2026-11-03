package com.example.winnerLogBackend.service;

import com.example.winnerLogBackend.dto.request.OddsWaterUpsetRecordRequest;
import com.example.winnerLogBackend.dto.response.OddsWaterUpsetSummaryVO;
import com.example.winnerLogBackend.entity.OddsWaterUpsetRecord;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface OddsWaterUpsetRecordService {

    OddsWaterUpsetRecord saveOrUpdate(OddsWaterUpsetRecordRequest request);

    OddsWaterUpsetRecord getById(Long id);

    void deleteById(Long id);

    List<OddsWaterUpsetRecord> queryList(LocalDate startDate, LocalDate endDate);

    Page<OddsWaterUpsetRecord> queryPage(LocalDate startDate, LocalDate endDate, int page, int size);

    OddsWaterUpsetSummaryVO getSummaryStats(LocalDate startDate, LocalDate endDate);
}
