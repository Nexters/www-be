package com.promise8.wwwbe.v1.model.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "LoginRequest", description = "유저 등록을 위한 request")
public class LoginReqDto {
    @ApiModelProperty(value = "userName", required = true, notes = "유저 이름")
    private String userName;
    @ApiModelProperty(value = "deviceId", required = true, notes = "사용중인 기기의 id")
    private String deviceId;
    @ApiModelProperty(value = "fcmToken", required = true, notes = "fcmToken")
    private String fcmToken;
}
