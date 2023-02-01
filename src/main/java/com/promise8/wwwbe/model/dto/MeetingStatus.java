package com.promise8.wwwbe.model.dto;

import lombok.Getter;

@Getter
public enum MeetingStatus {
    WAITING,
    VOTING,
    VOTED,
    CONFIRMED,
    DONE;
}
