package com.promise8.wwwbe.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "PromiseDateTimeRequest", description = "날짜와 해당 날짜의 시간대 list")
public class PromiseDateTimeReqDto {
    @ApiModelProperty(value = "promiseDate", required = true, notes = "날짜")
    private LocalDate promiseDate;
    @ApiModelProperty(value = "promiseTimeList", required = true, notes = "시간대 list")
    private List<PromiseTime> promiseTimeList;
}
