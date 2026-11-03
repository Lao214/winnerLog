package com.example.winnerLogBackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_model", indexes = {
        @Index(name = "uk_ai_model_code", columnList = "model_code", unique = true)
})
public class AiModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_code", nullable = false, length = 50, unique = true)
    private String modelCode;

    @Column(name = "model_name", nullable = false, length = 100)
    private String modelName;

    @Column(name = "provider", length = 50)
    private String provider;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public AiModel() {}

    public AiModel(Long id, String modelCode, String modelName, String provider, String avatarUrl, String description, Integer status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.modelCode = modelCode;
        this.modelName = modelName;
        this.provider = provider;
        this.avatarUrl = avatarUrl;
        this.description = description;
        this.status = status != null ? status : 1;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AiModelBuilder builder() {
        return new AiModelBuilder();
    }

    public static class AiModelBuilder {
        private Long id;
        private String modelCode;
        private String modelName;
        private String provider;
        private String avatarUrl;
        private String description;
        private Integer status = 1;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public AiModelBuilder id(Long id) { this.id = id; return this; }
        public AiModelBuilder modelCode(String modelCode) { this.modelCode = modelCode; return this; }
        public AiModelBuilder modelName(String modelName) { this.modelName = modelName; return this; }
        public AiModelBuilder provider(String provider) { this.provider = provider; return this; }
        public AiModelBuilder avatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; return this; }
        public AiModelBuilder description(String description) { this.description = description; return this; }
        public AiModelBuilder status(Integer status) { this.status = status; return this; }
        public AiModelBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AiModelBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public AiModel build() {
            return new AiModel(id, modelCode, modelName, provider, avatarUrl, description, status, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getModelCode() { return modelCode; }
    public void setModelCode(String modelCode) { this.modelCode = modelCode; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
