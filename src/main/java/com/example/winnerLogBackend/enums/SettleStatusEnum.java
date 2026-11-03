package com.example.winnerLogBackend.enums;

public enum SettleStatusEnum {
    PENDING("待结算"),
    WON("红单(命中)"),
    LOST("黑单(未命中)"),
    VOID("走水/无效");

    private final String description;

    SettleStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
