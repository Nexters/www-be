package com.promise8.wwwbe.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@ApiModel(value = "MeetingCreateResponse", description = "약속 방 생성 시 공유 코드, 링크를 발급 받는다.")
public class MeetingCreateResDto {
    @ApiModelProperty(value = "meetingCode", notes = "공유 코드")
    private String meetingCode;
    @ApiModelProperty(value = "meetingLink", notes = "공유 링크")
    private String meetingLink;

    public static MeetingCreateResDto of(String meetingCode, String meetingLink) {
        return MeetingCreateResDto.builder()
                .meetingCode(meetingCode)
                .meetingLink(meetingLink)
                .build();
    }
}
