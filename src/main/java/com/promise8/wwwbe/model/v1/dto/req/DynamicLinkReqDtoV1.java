package com.promise8.wwwbe.model.v1.dto.req;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DynamicLinkReqDtoV1 {
    private String longDynamicLink;

    public static DynamicLinkReqDtoV1 of(String longDynamicLink) {
        return DynamicLinkReqDtoV1.builder()
                .longDynamicLink(longDynamicLink)
                .build();
    }
}
