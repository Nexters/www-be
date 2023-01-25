package com.promise8.wwwbe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingCreateResDto {
    private String meetingCode;
    private String meetingLink;
}