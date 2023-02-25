package com.promise8.wwwbe.model.dto;

import lombok.Getter;

@Getter
public enum PromiseTime {
    MORNING("MORNING", "아침", 0),
    LUNCH("LUNCH", "점심", 1),
    DINNER("DINNER", "저녁", 2),
    NIGHT("NIGHT", "밤", 3);

    private String time;
    private String krName;
    private int priority;

    PromiseTime(String time, String krName, int priority) {
        this.time = time;
        this.krName = krName;
        this.priority = priority;
    }
}
