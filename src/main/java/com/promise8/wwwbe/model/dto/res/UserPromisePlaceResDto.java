package com.promise8.wwwbe.model.dto.res;

import com.promise8.wwwbe.model.dto.UserInfoDto;
import com.promise8.wwwbe.model.entity.MeetingPlaceEntity;
import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import com.promise8.wwwbe.model.entity.PlaceVoteEntity;
import com.promise8.wwwbe.service.ThumbnailHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@ApiModel(value = "UserPromisePlace", description = "유저들이 선택한 장소")
public class UserPromisePlaceResDto {
    @ApiModelProperty(value = "placeId", required = true, notes = "장소 id")
    private Long placeId;
    @ApiModelProperty(value = "userName", required = true, notes = "해당 장소를 선택한 유저")
    private String userName;
    @ApiModelProperty(value = "userCharacter", notes = "유저 캐릭터")
    private ThumbnailHelper.CharacterType userCharacter;
    @ApiModelProperty(value = "promisePlace", required = true, notes = "해당 장소를 선택한 유저")
    private String promisePlace;
    @ApiModelProperty(value = "userInfoList", required = true, notes = "해당 장소를 선택한 유저 list")
    private List<UserInfoDto> userInfoList;
    @ApiModelProperty(value = "votedUserCount", required = true, notes = "해당 장소를 투표한 사람 수")
    private int votedUserCount;

    public static UserPromisePlaceResDto of(MeetingPlaceEntity meetingPlaceEntity, Long userId) {
        MeetingUserEntity meetingUserEntity = meetingPlaceEntity.getMeetingUserEntity();

        List<UserInfoDto> userInfoList = new ArrayList<>();
        for (PlaceVoteEntity placeVoteEntity : meetingPlaceEntity.getPlaceVoteEntityList()) {
            userInfoList.add(
                    new UserInfoDto(
                            placeVoteEntity.getMeetingUserEntity().getMeetingUserName(),
                            ThumbnailHelper.getCharacter(
                                    placeVoteEntity.getMeetingUserEntity().getUserEntity().getUserId(),
                                    placeVoteEntity.getMeetingUserEntity().getUserEntity().getUserId().equals(userId)
                            )
                    )
            );
        }

        return UserPromisePlaceResDto.builder()
                .placeId(meetingPlaceEntity.getMeetingPlaceId())
                .userName(meetingUserEntity.getMeetingUserName())
                .userCharacter(ThumbnailHelper.getCharacter(
                        meetingUserEntity.getUserEntity().getUserId(),
                        meetingUserEntity.getUserEntity().getUserId().equals(meetingUserEntity.getMeetingEntity().getCreator().getUserId())
                ))
                .promisePlace(meetingPlaceEntity.getPromisePlace())
                .userInfoList(userInfoList)
                .votedUserCount(userInfoList.size())
                .build();
    }
}
