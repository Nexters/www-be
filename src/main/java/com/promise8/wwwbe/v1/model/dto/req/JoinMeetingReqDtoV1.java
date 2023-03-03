package com.promise8.wwwbe.v1.model.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "JoinMeetingRequest", description = "약속 방 입장을 위한 request")
public class JoinMeetingReqDtoV1 {
    @ApiModelProperty(value = "nickname", required = true, notes = "약속 방 내에서 사용할 이름(닉네임)")
    private String nickname;
    @ApiModelProperty(value = "userPromiseTimeList", required = true, notes = "선택한 희망 날짜, 시간 list")
    private List<UserPromiseTimeReqDtoV1> userPromiseTimeList = new ArrayList<>();

    @ApiModelProperty(value = "promisePlaceList", notes = "선택한 희망 장소 list")
    private List<String> promisePlaceList = new ArrayList<>();

}
