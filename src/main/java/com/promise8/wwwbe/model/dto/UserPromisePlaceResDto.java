package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.MeetingPlaceEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserPromisePlaceResDto {
    private String userName;
    private String promisePlace;

    public static UserPromisePlaceResDto of(MeetingPlaceEntity meetingPlaceEntity) {
        return UserPromisePlaceResDto.builder()
                .userName(meetingPlaceEntity.getMeetingUserEntity().getMeetingUserName())
                .promisePlace(meetingPlaceEntity.getPromisePlace())
                .build();
    }
}
