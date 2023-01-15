package com.promise8.wwwbe.model.http;

import lombok.Getter;

@Getter
public enum BaseErrorCode {

    SUCCESS(0),
    SERVER_ERROR(1000);

    BaseErrorCode(int value) {
        this.value = value;
    }
    private int value;
}
