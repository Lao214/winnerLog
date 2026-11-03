package com.example.winnerLogBackend.repository;

import com.example.winnerLogBackend.entity.League;
import com.example.winnerLogBackend.enums.LeagueCategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    Optional<League> findByCode(String code);
    Optional<League> findByNameOrShortName(String name, String shortName);
    List<League> findByStatusOrderBySortOrderAsc(Integer status);
    List<League> findByCategoryAndStatusOrderBySortOrderAsc(LeagueCategoryEnum category, Integer status);
}
