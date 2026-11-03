package com.example.winnerLogBackend.enums;

public enum ScopeTypeEnum {
    OVERALL("全局总榜"),
    SEASON("赛季总榜"),
    LEAGUE_SEASON("联赛赛季榜"),
    LEAGUE("联赛历史总榜");

    private final String description;

    ScopeTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
