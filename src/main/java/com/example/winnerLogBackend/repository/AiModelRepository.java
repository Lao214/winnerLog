package com.example.winnerLogBackend.repository;

import com.example.winnerLogBackend.entity.AiModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiModelRepository extends JpaRepository<AiModel, Long> {
    Optional<AiModel> findByModelCode(String modelCode);
    List<AiModel> findByStatus(Integer status);
}
