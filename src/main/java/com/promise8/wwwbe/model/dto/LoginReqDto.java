package com.promise8.wwwbe.model.dto;

import lombok.Data;

@Data
public class LoginReqDto {
    private String userName;
    private String deviceId;
    private String fcmToken;
}
