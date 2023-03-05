package com.promise8.wwwbe.model.v1.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "PlaceVoteRequest", description = "약속장소 투표를 위한 request")
public class PlaceVoteReqDtoV1 {
    @ApiModelProperty(value = "meetingPlaceIdList", required = true, notes = "투표한 장소의 id list")
    List<Long> meetingPlaceIdList;
}
