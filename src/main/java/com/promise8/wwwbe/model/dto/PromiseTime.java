package com.promise8.wwwbe.model.dto;

import lombok.Getter;

@Getter
public enum PromiseTime {
    MORNING("MORNING", 0),
    LUNCH("LUNCH", 1),
    DINNER("DINNER", 2),
    NIGHT("NIGHT", 3);

    private String time;
    private int priority;

    PromiseTime(String time, int priority) {
        this.time = time;
        this.priority = priority;
    }
}
