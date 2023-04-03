package com.promise8.wwwbe.model.v2.dto.res;

import com.promise8.wwwbe.model.v1.dto.PromiseDayOfWeek;
import com.promise8.wwwbe.model.v1.dto.PromiseTime;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class ConfirmedPromise {
    @ApiModelProperty(value = "confirmedDate", notes = "확정된 날짜")
    private LocalDate promiseDate;
    @ApiModelProperty(value = "confirmedDayOfWeek", notes = "요일")
    private PromiseDayOfWeek promiseDayOfWeek;
    @ApiModelProperty(value = "confirmedTime", notes = "확정된 시간대")
    private PromiseTime promiseTime;
    @ApiModelProperty(value = "confirmedPlace", notes = "확정된 장소")
    private String promisePlace;
}
