package com.promise8.wwwbe.model.v1.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeetingConfirmDtoV1 {
    private long meetingPlaceId;
    private long meetingUserTimetableId;
}
