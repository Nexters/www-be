package com.promise8.wwwbe.v1.model.exception;

import com.promise8.wwwbe.v1.model.http.BaseErrorCode;
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
