package com.promise8.wwwbe.v1.model.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Builder
@ApiModel(value = "UserAndMyVoteDto", description = "약속 방 내의 유저들과 본인의 투표 정보를 담는다.")
public class PromisePlaceResDtoWrapperV1 {
    @ApiModelProperty(value = "userVoteList", notes = "약속 방 내 유저들의 투표 내역")
    private List<Map.Entry<String, List<String>>> userVoteList;
    @ApiModelProperty(value = "myVoteList", notes = "약속 방 내 본인의 투표 내역")
    private List<String> myVoteList;

    @ApiModelProperty(value = "votedUserCount", notes = "현재 투표 참여 인원")
    private int votedUserCount;

    public static PromisePlaceResDtoWrapperV1 of(
            HashMap<String, List<String>> userVoteHashMap,
            List<String> myVoteList,
            int votedUserCount
    ) {
        List<Map.Entry<String, List<String>>> userVoteList = new ArrayList<>(userVoteHashMap.entrySet());
        userVoteList.sort((o1, o2) -> o2.getValue().size() - o1.getValue().size());

        return PromisePlaceResDtoWrapperV1.builder()
                .userVoteList(userVoteList)
                .myVoteList(myVoteList)
                .votedUserCount(votedUserCount)
                .build();
    }
}
