package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.MeetingUserTimetableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class UserPromiseTimeResDto {
    private String userName;
    private LocalDate promiseDate;
    private PromiseTime promiseTime;

    public static UserPromiseTimeResDto of(
            MeetingUserTimetableEntity meetingUserTimetableEntity) {
        return UserPromiseTimeResDto.builder()
                .userName(meetingUserTimetableEntity.getMeetingUserEntity().getMeetingUserName())
                .promiseDate(meetingUserTimetableEntity.getPromiseDate())
                .promiseTime(meetingUserTimetableEntity.getPromiseTime())
                .build();
    }
}
