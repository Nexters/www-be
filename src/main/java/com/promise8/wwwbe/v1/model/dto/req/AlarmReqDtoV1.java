package com.promise8.wwwbe.v1.model.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AlarmRequest", description = "알림 설정 on off를 위한 request")
public class AlarmReqDtoV1 {
    @ApiModelProperty(value = "isAlarmOn", required = true, notes = "알림 수신 여부")
    private boolean isAlarmOn;
}