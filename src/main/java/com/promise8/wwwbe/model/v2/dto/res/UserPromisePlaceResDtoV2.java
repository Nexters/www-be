package com.promise8.wwwbe.model.v2.dto.res;

import com.promise8.wwwbe.service.ThumbnailHelper;
import com.promise8.wwwbe.model.v1.entity.MeetingPlaceEntityV1;
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
public class UserPromisePlaceResDtoV2 {
    @ApiModelProperty(value = "placeId", required = true, notes = "장소 id")
    private Long placeId;
    @ApiModelProperty(value = "promisePlace", required = true, notes = "해당 장소를 선택한 유저")
    private String promisePlace;
    @ApiModelProperty(value = "votedUserCount", required = true, notes = "해당 장소를 투표한 사람 수")
    private int votedUserCount;
    @ApiModelProperty(value = "userInfoList", required = true, notes = "해당 장소를 선택한 유저 list")
    private List<UserInfoDtoV2> userInfoList;

    public static UserPromisePlaceResDtoV2 of(MeetingPlaceEntityV1 meetingPlaceEntity, Long userId) {
        List<UserInfoDtoV2> userInfoList = new ArrayList<>();
        for (PlaceVoteEntityV1 placeVoteEntity : meetingPlaceEntity.getPlaceVoteEntityList()) {
            userInfoList.add(
                    new UserInfoDtoV2(
                            placeVoteEntity.getMeetingUserEntity().getMeetingUserName(),
                            ThumbnailHelper.getCharacter(
                                    placeVoteEntity.getMeetingUserEntity().getUserEntity().getUserId(),
                                    placeVoteEntity.getMeetingUserEntity().getUserEntity().getUserId().equals(userId)
                            )
                    )
            );
        }

        return UserPromisePlaceResDtoV2.builder()
                .placeId(meetingPlaceEntity.getMeetingPlaceId())
                .promisePlace(meetingPlaceEntity.getPromisePlace())
                .userInfoList(userInfoList)
                .votedUserCount(userInfoList.size())
                .build();
    }
}
