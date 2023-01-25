package com.promise8.wwwbe.model.exception;

import com.promise8.wwwbe.model.http.BaseErrorCode;
import lombok.Getter;

@Getter
public class BizException extends RuntimeException {
    Object message;
    BaseErrorCode code = BaseErrorCode.SERVER_ERROR;

    public BizException(BaseErrorCode code, Object msg) {
        super(code.getMessage());
        this.code = code;
        this.message = msg;
    }

    public BizException(BaseErrorCode code) {
        super(code.getMessage());
        this.code = code;
        this.message = null;
    }

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message);
        this.initCause(cause);
        this.setStackTrace(cause.getStackTrace());
    }

    public BizException(Throwable cause) {
        super(cause.getMessage());
        if (cause instanceof BizException) {
            this.code = ((BizException)cause).getCode();
            this.message = cause.getMessage();
        }

        this.initCause(cause);
        this.setStackTrace(cause.getStackTrace());
    }

    public BizException(BaseErrorCode code, Throwable cause) {
        this(code, cause, null);
    }

    public BizException(BaseErrorCode code, Throwable cause, Object msg) {
        super(cause);
        this.code = code;
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return this.message == null ? super.getMessage() : super.getMessage() + " (" + this.message + ")";
    }
}