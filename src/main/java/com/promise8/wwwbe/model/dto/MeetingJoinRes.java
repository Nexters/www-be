package com.promise8.wwwbe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeetingJoinRes {
    private long meetingId;
    private String meetingName;
}
