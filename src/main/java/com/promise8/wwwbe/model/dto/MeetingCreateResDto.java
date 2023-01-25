package com.promise8.wwwbe.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MeetingCreateResDto {
    private String meetingCode;
    private String meetingLink;

    public static MeetingCreateResDto of(String meetingCode, String meetingLink) {
        return MeetingCreateResDto.builder()
                .meetingCode(meetingCode)
                .meetingLink(meetingLink)
                .build();
    }
}
