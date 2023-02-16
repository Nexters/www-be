package com.promise8.wwwbe.model.mobile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PushMessage {
    private ContentType contentType;
    private Long contentId;
    private String text;

    public enum ContentType {
        MEETING;
    }
}
