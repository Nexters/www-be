package com.promise8.wwwbe.model.http;

import lombok.Getter;

@Getter
public enum BaseErrorCode {

    SUCCESS(0, "Success"),
    SERVER_ERROR(1000, "Server Error"),
    FCM_SEND_ERROR(2000, "FCM Push Error"),
    ALREADY_PARTICIPATED_MEETING(3001, "Already Participated Meeting"),
    NOT_EXIST_MEETING(4000, "Not Exist Meeting"),
    ALREADY_VOTING_MEETING(5000, "Already Voting Meeting"),
    INVALID_REQUEST(9000, "Invalid Request");

    BaseErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
}
