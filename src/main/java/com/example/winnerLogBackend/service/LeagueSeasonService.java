package com.example.winnerLogBackend.service;

import com.example.winnerLogBackend.entity.AiModel;
import com.example.winnerLogBackend.entity.League;
import com.example.winnerLogBackend.entity.Season;
import com.example.winnerLogBackend.enums.LeagueCategoryEnum;
import com.example.winnerLogBackend.repository.AiModelRepository;
import com.example.winnerLogBackend.repository.LeagueRepository;
import com.example.winnerLogBackend.repository.SeasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LeagueSeasonService {

    private static final Logger log = LoggerFactory.getLogger(LeagueSeasonService.class);

    private final LeagueRepository leagueRepository;
    private final SeasonRepository seasonRepository;
    private final AiModelRepository aiModelRepository;

    public LeagueSeasonService(LeagueRepository leagueRepository,
                               SeasonRepository seasonRepository,
                               AiModelRepository aiModelRepository) {
        this.leagueRepository = leagueRepository;
        this.seasonRepository = seasonRepository;
        this.aiModelRepository = aiModelRepository;
    }

    public List<League> getAllActiveLeagues() {
        return leagueRepository.findByStatusOrderBySortOrderAsc(1);
    }

    public List<League> getLeaguesByCategory(LeagueCategoryEnum category) {
        return leagueRepository.findByCategoryAndStatusOrderBySortOrderAsc(category, 1);
    }

    public Optional<League> getLeagueByCode(String code) {
        return leagueRepository.findByCode(code.toUpperCase());
    }

    public List<Season> getSeasonsByLeagueCode(String leagueCode) {
        return seasonRepository.findByLeagueCodeOrderByStartDateDesc(leagueCode.toUpperCase());
    }

    public Optional<Season> getCurrentSeason(String leagueCode) {
        return seasonRepository.findByLeagueCodeAndIsCurrent(leagueCode.toUpperCase(), 1);
    }

    public Optional<Season> getSeasonByLeagueAndName(String leagueCode, String seasonName) {
        return seasonRepository.findByLeagueCodeAndName(leagueCode.toUpperCase(), seasonName);
    }

    public List<AiModel> getAllActiveAiModels() {
        return aiModelRepository.findByStatus(1);
    }

    public Optional<AiModel> getAiModelByCode(String modelCode) {
        return aiModelRepository.findByModelCode(modelCode.toUpperCase());
    }

    @Transactional
    public League saveLeague(League league) {
        return leagueRepository.save(league);
    }

    @Transactional
    public Season saveSeason(Season season) {
        return seasonRepository.save(season);
    }

    @Transactional
    public AiModel saveAiModel(AiModel aiModel) {
        return aiModelRepository.save(aiModel);
    }
}
