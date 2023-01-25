package com.promise8.wwwbe.model.http;

import lombok.Getter;

@Getter
public enum BaseErrorCode {

    SUCCESS(0, "Success"),
    SERVER_ERROR(1000, "Server Error"),
    FCM_SEND_ERROR(2000, "FCM Push Error");



    BaseErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

}