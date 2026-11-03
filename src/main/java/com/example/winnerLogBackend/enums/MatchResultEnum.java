package com.example.winnerLogBackend.enums;

public enum MatchResultEnum {
    HOME_WIN("主胜"),
    DRAW("平局"),
    AWAY_WIN("客胜");

    private final String description;

    MatchResultEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static MatchResultEnum calculateResult(Integer homeScore, Integer awayScore) {
        if (homeScore == null || awayScore == null) {
            return null;
        }
        if (homeScore > awayScore) {
            return HOME_WIN;
        } else if (homeScore < awayScore) {
            return AWAY_WIN;
        } else {
            return DRAW;
        }
    }
}
