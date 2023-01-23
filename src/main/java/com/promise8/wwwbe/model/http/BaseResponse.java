package com.promise8.wwwbe.model.http;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"errCode"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "message", "result"})
@ToString
public class BaseResponse<T> {
    @JsonIgnore
    private BaseErrorCode errCode = BaseErrorCode.SUCCESS;
    public Object result;

    public static BaseResponse ok() {
        return ok(null);
    }

    public static BaseResponse ok(Object result) {
        return writeAs(BaseErrorCode.SUCCESS, result);
    }

    public static BaseResponse error(BaseErrorCode errCode) {
        return error(errCode, null);
    }

    public static BaseResponse error(BaseErrorCode errCode, Object result) {
        return writeAs(errCode, result);
    }

    private static BaseResponse writeAs(BaseErrorCode errCode, Object result) {
        return new BaseResponse(errCode, result);
    }

    @JsonProperty("code")
    public int getCode() {
        return errCode.getCode();
    }

    @JsonProperty("message")
    public String getMessage() {
        return errCode.getMessage();
    }
}
