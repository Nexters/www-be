package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.MeetingPlaceEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@ApiModel(value = "UserPromisePlace", description = "유저들이 선택한 장소")
public class UserPromisePlaceResDto {
    @ApiModelProperty(value = "userName", notes = "해당 장소를 선택한 유저")
    private String userName;
    @ApiModelProperty(value = "promisePlace", notes = "해당 장소를 선택한 유저")
    private String promisePlace;

    public static UserPromisePlaceResDto of(MeetingPlaceEntity meetingPlaceEntity) {
        return UserPromisePlaceResDto.builder()
                .userName(meetingPlaceEntity.getMeetingUserEntity().getMeetingUserName())
                .promisePlace(meetingPlaceEntity.getPromisePlace())
                .build();
    }
}
