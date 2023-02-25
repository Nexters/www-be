package com.promise8.wwwbe.model.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeetingConfirmDto {
    private long meetingPlaceId;
    private long meetingUserTimetableId;
}
