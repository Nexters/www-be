package com.promise8.wwwbe.config.converter;

import com.promise8.wwwbe.model.v1.dto.PlatformTypeV1;
import org.springframework.core.convert.converter.Converter;

public class PlatformTypeConverter implements Converter<String, PlatformTypeV1> {
    @Override
    public PlatformTypeV1 convert(String source) {
        return PlatformTypeV1.of(source);
    }
}
