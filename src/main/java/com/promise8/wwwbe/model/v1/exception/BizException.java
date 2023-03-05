package com.promise8.wwwbe.model.v1.exception;

import com.promise8.wwwbe.model.v1.http.BaseErrorCode;
import lombok.Getter;

@Getter
public class BizException extends RuntimeException {
    BaseErrorCode code = BaseErrorCode.SERVER_ERROR;

    public BizException(BaseErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public BizException(BaseErrorCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }
}
