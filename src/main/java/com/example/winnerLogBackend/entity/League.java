package com.example.winnerLogBackend.entity;

import com.example.winnerLogBackend.enums.LeagueCategoryEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "league", indexes = {
        @Index(name = "uk_league_code", columnList = "code", unique = true),
        @Index(name = "idx_league_category", columnList = "category")
})
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, length = 50, unique = true)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "short_name", nullable = false, length = 50)
    private String shortName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private LeagueCategoryEnum category;

    @Column(name = "country", length = 50)
    private String country;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public League() {}

    public League(Long id, String code, String name, String shortName, LeagueCategoryEnum category, String country, String logoUrl, Integer sortOrder, Integer status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.shortName = shortName;
        this.category = category;
        this.country = country;
        this.logoUrl = logoUrl;
        this.sortOrder = sortOrder != null ? sortOrder : 0;
        this.status = status != null ? status : 1;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static LeagueBuilder builder() {
        return new LeagueBuilder();
    }

    public static class LeagueBuilder {
        private Long id;
        private String code;
        private String name;
        private String shortName;
        private LeagueCategoryEnum category;
        private String country;
        private String logoUrl;
        private Integer sortOrder = 0;
        private Integer status = 1;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public LeagueBuilder id(Long id) { this.id = id; return this; }
        public LeagueBuilder code(String code) { this.code = code; return this; }
        public LeagueBuilder name(String name) { this.name = name; return this; }
        public LeagueBuilder shortName(String shortName) { this.shortName = shortName; return this; }
        public LeagueBuilder category(LeagueCategoryEnum category) { this.category = category; return this; }
        public LeagueBuilder country(String country) { this.country = country; return this; }
        public LeagueBuilder logoUrl(String logoUrl) { this.logoUrl = logoUrl; return this; }
        public LeagueBuilder sortOrder(Integer sortOrder) { this.sortOrder = sortOrder; return this; }
        public LeagueBuilder status(Integer status) { this.status = status; return this; }
        public LeagueBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public LeagueBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public League build() {
            return new League(id, code, name, shortName, category, country, logoUrl, sortOrder, status, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getShortName() { return shortName; }
    public void setShortName(String shortName) { this.shortName = shortName; }
    public LeagueCategoryEnum getCategory() { return category; }
    public void setCategory(LeagueCategoryEnum category) { this.category = category; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
