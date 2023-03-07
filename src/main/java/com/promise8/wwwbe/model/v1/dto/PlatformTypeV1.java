package com.promise8.wwwbe.model.v1.dto;

import com.promise8.wwwbe.model.v1.exception.BizException;
import com.promise8.wwwbe.model.v1.http.BaseErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PlatformTypeV1 {
    ANDROID("android"),
    IOS("ios");

    private String name;

    public static PlatformTypeV1 of(String platformTypeName) {
        if (platformTypeName == null) {
            throw new IllegalArgumentException("not valid argument. " + platformTypeName);
        }

        return Arrays.stream(PlatformTypeV1.values())
                .filter(platform -> platform.name.equals(platformTypeName))
                .findFirst()
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("not valid argument. " + platformTypeName);
                });
    }

    PlatformTypeV1(String name) {
        this.name = name;
    }
}
