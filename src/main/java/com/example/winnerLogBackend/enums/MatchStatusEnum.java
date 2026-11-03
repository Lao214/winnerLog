package com.example.winnerLogBackend.enums;

public enum MatchStatusEnum {
    SCHEDULED("未开赛"),
    IN_PROGRESS("进行中"),
    FINISHED("已完赛"),
    POSTPONED("已延期"),
    CANCELLED("已取消");

    private final String description;

    MatchStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
