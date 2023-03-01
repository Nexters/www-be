package com.promise8.wwwbe.model.http;

import lombok.Getter;

@Getter
public enum BaseErrorCode {

    SUCCESS(0, "Success"),
    SERVER_ERROR(1000, "Server Error"),
    FCM_SEND_ERROR(2000, "FCM Push Error"),
    ALREADY_PARTICIPATED_MEETING(3001, "Already Participated Meeting"),
    NOT_MEETING_STATUS_VOTING(3002, "Not Voting Status of Meeting"),
    NOT_EXIST_MEETING(4000, "Not Exist Meeting"),
    NOT_EXIST_USER(4001, "Not Exist User"),
    NOT_EXIST_MEETING_USER(4002, "Not Exist Meeting User"),
    NOT_EXIST_MEETING_PLACE(4003, "Not Exist Meeting Place"),
    NOT_EXIST_MEETING_TIMETABLE(4004, "Not Exist Meeting Timetable"),

    ALREADY_VOTING_MEETING(5000, "Already Voting Meeting"),
    INVALID_REQUEST(9000, "Invalid Request");

    BaseErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
}
