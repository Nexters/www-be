package com.promise8.wwwbe.model.dto;

public enum PromiseDayOfWeek {
    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SON("일");

    PromiseDayOfWeek(String day) {
        this.day = day;
    }

    private String day;

    @Override
    public String toString() {
        return day;
    }
}
