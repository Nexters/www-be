package com.promise8.wwwbe.v1.model.dto.req;

import com.promise8.wwwbe.v1.model.dto.PromiseTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "UserPromiseTimeRequest", description = "날짜와 해당 날짜의 시간대")
public class UserPromiseTimeReqDtoV1 {
    @ApiModelProperty(value = "promiseDate", required = true, notes = "날짜")
    private LocalDate promiseDate;
    @ApiModelProperty(value = "promiseTime", required = true, notes = "시간대")
    private PromiseTime promiseTime;
}
