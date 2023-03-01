package com.promise8.wwwbe.model.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserPromisePlaceListResDto {
    @ApiModelProperty(value = "UserPromisePlaceList", notes = "유저들이 선택한 장소")
    List<UserPromisePlaceResDto> UserPromisePlaceList;
}
