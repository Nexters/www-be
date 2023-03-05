package com.promise8.wwwbe.model.v1.mobile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushMessageV1 {
    private Long id;
    private ContentType contentType;
    private Long contentId;
    private String title;
    private String text;

    public enum ContentType {
        MEETING;
    }

    public PushMessageV1(ContentType contentType, Long contentId, String title, String text) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.title = title;
        this.text = text;
    }
}
