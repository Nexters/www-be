package com.promise8.wwwbe.model.dto.res;

import com.promise8.wwwbe.model.entity.MeetingPlaceEntity;
import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import com.promise8.wwwbe.service.ThumbnailHelper;
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
    @ApiModelProperty(value = "userName", required = true, notes = "해당 장소를 선택한 유저")
    private String userName;
    @ApiModelProperty(value = "userCharacter", notes = "유저 캐릭터")
    private ThumbnailHelper.CharacterType userCharacter;
    @ApiModelProperty(value = "promisePlace", required = true, notes = "해당 장소를 선택한 유저")
    private String promisePlace;

    public static UserPromisePlaceResDto of(MeetingPlaceEntity meetingPlaceEntity) {
        MeetingUserEntity meetingUserEntity = meetingPlaceEntity.getMeetingUserEntity();
        return UserPromisePlaceResDto.builder()
                .userName(meetingUserEntity.getMeetingUserName())
                .userCharacter(ThumbnailHelper.getCharacter(
                        meetingUserEntity.getUserEntity().getUserId(),
                        meetingUserEntity.getUserEntity().getUserId() == meetingUserEntity.getMeetingEntity().getCreator().getUserId()
                ))
                .promisePlace(meetingPlaceEntity.getPromisePlace())
                .build();
    }
}
