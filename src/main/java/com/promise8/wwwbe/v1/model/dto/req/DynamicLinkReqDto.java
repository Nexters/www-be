package com.promise8.wwwbe.v1.model.dto.req;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DynamicLinkReqDto {
    private String longDynamicLink;

    public static DynamicLinkReqDto of(String longDynamicLink) {
        return DynamicLinkReqDto.builder()
                .longDynamicLink(longDynamicLink)
                .build();
    }
}
