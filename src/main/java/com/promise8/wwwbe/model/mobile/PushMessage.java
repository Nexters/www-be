package com.promise8.wwwbe.model.mobile;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PushMessage {
    private PushType pushType;
    private String body;
    private LocalDateTime createdAt;

    public enum PushType {
        DEFAULT,
        NOTI,
        EVENT
    }
}
