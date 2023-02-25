package com.promise8.wwwbe.model.mobile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushMessage {
    private Long id;
    private ContentType contentType;
    private Long contentId;
    private String title;
    private String text;

    public enum ContentType {
        MEETING;
    }
}
