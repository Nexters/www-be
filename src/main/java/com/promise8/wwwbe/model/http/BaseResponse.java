package com.promise8.wwwbe.model.http;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResponse<T> {
    private T object;
    private BaseErrorCode code;
    private String message;

    public static final BaseResponse ok(Object object) {
        return ok(object, null);
    }

    public static final BaseResponse ok(Object object, String message) {
        return new BaseResponse(object, BaseErrorCode.SUCCESS, message);
    }
}
