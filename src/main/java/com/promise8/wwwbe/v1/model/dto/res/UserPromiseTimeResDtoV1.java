package com.promise8.wwwbe.v1.model.dto.res;

import com.promise8.wwwbe.v1.model.dto.PromiseDayOfWeek;
import com.promise8.wwwbe.v1.model.dto.PromiseTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
@ApiModel(value = "UserPromiseTime", description = "유저들이 선택한 날짜, 시간대")
public class UserPromiseTimeResDtoV1 {
    @ApiModelProperty(value = "timetableId", required = true, notes = "시간 id")
    private Long timetableId;
    @ApiModelProperty(value = "promiseDate", required = true, notes = "날짜")
    private LocalDate promiseDate;
    @ApiModelProperty(value = "promiseTime", required = true, notes = "시간대")
    private PromiseTime promiseTime;
    @ApiModelProperty(value = "promiseDayOfWeek", required = true, notes = "요일")
    private PromiseDayOfWeek promiseDayOfWeek;
    @ApiModelProperty(value = "userInfoList", required = true, notes = "해당 날짜, 시간대를 선택한 유저 list")
    private List<UserInfoDtoV1> userInfoList;
}
