package com.example.winnerLogBackend.enums;

public enum LeagueCategoryEnum {
    BIG_FIVE_LEAGUE("五大联赛"),
    CONTINENTAL_CUP("洲际杯赛"),
    INTERNATIONAL_CUP("国际大赛"),
    DOMESTIC_LEAGUE("各国联赛"),
    OTHER("其他赛事");

    private final String description;

    LeagueCategoryEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
