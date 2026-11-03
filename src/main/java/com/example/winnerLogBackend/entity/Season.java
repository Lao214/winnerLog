package com.example.winnerLogBackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "season", uniqueConstraints = {
        @UniqueConstraint(name = "uk_league_season", columnNames = {"league_id", "name"})
}, indexes = {
        @Index(name = "idx_season_league_code", columnList = "league_code"),
        @Index(name = "idx_season_name", columnList = "name")
})
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "league_id", nullable = false)
    private Long leagueId;

    @Column(name = "league_code", nullable = false, length = 50)
    private String leagueCode;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_current", nullable = false)
    private Integer isCurrent = 1;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Season() {}

    public Season(Long id, Long leagueId, String leagueCode, String name, LocalDate startDate, LocalDate endDate, Integer isCurrent, Integer status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.leagueId = leagueId;
        this.leagueCode = leagueCode;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCurrent = isCurrent != null ? isCurrent : 1;
        this.status = status != null ? status : 1;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SeasonBuilder builder() {
        return new SeasonBuilder();
    }

    public static class SeasonBuilder {
        private Long id;
        private Long leagueId;
        private String leagueCode;
        private String name;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer isCurrent = 1;
        private Integer status = 1;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public SeasonBuilder id(Long id) { this.id = id; return this; }
        public SeasonBuilder leagueId(Long leagueId) { this.leagueId = leagueId; return this; }
        public SeasonBuilder leagueCode(String leagueCode) { this.leagueCode = leagueCode; return this; }
        public SeasonBuilder name(String name) { this.name = name; return this; }
        public SeasonBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public SeasonBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public SeasonBuilder isCurrent(Integer isCurrent) { this.isCurrent = isCurrent; return this; }
        public SeasonBuilder status(Integer status) { this.status = status; return this; }
        public SeasonBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SeasonBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Season build() {
            return new Season(id, leagueId, leagueCode, name, startDate, endDate, isCurrent, status, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getLeagueId() { return leagueId; }
    public void setLeagueId(Long leagueId) { this.leagueId = leagueId; }
    public String getLeagueCode() { return leagueCode; }
    public void setLeagueCode(String leagueCode) { this.leagueCode = leagueCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Integer getIsCurrent() { return isCurrent; }
    public void setIsCurrent(Integer isCurrent) { this.isCurrent = isCurrent; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
