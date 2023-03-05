package com.promise8.wwwbe.model.v1.dto.res;

import com.promise8.wwwbe.service.ThumbnailHelper;
import com.promise8.wwwbe.model.v1.entity.MeetingPlaceEntityV1;
import com.promise8.wwwbe.model.v1.entity.MeetingUserEntityV1;
import com.promise8.wwwbe.model.v1.entity.PlaceVoteEntityV1;
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
public class UserPromisePlaceResDtoV1 {
    @ApiModelProperty(value = "placeId", required = true, notes = "장소 id")
    private Long placeId;
    @ApiModelProperty(value = "userName", required = true, notes = "해당 장소를 선택한 유저")
    private String userName;
    @ApiModelProperty(value = "userCharacter", notes = "유저 캐릭터")
    private ThumbnailHelper.CharacterType userCharacter;
    @ApiModelProperty(value = "promisePlace", required = true, notes = "해당 장소를 선택한 유저")
    private String promisePlace;
    @ApiModelProperty(value = "userInfoList", required = true, notes = "해당 장소를 선택한 유저 list")
    private List<UserInfoDtoV1> userInfoList;
    @ApiModelProperty(value = "votedUserCount", required = true, notes = "해당 장소를 투표한 사람 수")
    private int votedUserCount;

    public static UserPromisePlaceResDtoV1 of(MeetingPlaceEntityV1 meetingPlaceEntity, Long userId) {
        MeetingUserEntityV1 meetingUserEntity = meetingPlaceEntity.getMeetingUserEntity();

        List<UserInfoDtoV1> userInfoList = new ArrayList<>();
        for (PlaceVoteEntityV1 placeVoteEntity : meetingPlaceEntity.getPlaceVoteEntityList()) {
            userInfoList.add(
                    new UserInfoDtoV1(
                            placeVoteEntity.getMeetingUserEntity().getMeetingUserName(),
                            ThumbnailHelper.getCharacter(
                                    placeVoteEntity.getMeetingUserEntity().getUserEntity().getUserId(),
                                    placeVoteEntity.getMeetingUserEntity().getUserEntity().getUserId().equals(userId)
                            )
                    )
            );
        }

        return UserPromisePlaceResDtoV1.builder()
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
