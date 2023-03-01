package com.promise8.wwwbe.model.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserPromisePlaceListResDto {
    @ApiModelProperty(value = "userPromisePlaceList", notes = "유저들이 선택한 장소")
    private List<UserPromisePlaceResDto> userPromisePlaceList;

    @ApiModelProperty(value = "voteFinishedUserCount", notes = "투표 완료한 유저 수")
    private int voteFinishedUserCount;
}
