package com.promise8.wwwbe.config.converter;

import com.promise8.wwwbe.v1.model.entity.MeetingStatusV1;
import org.springframework.core.convert.converter.Converter;

public class MeetingStatusConverter implements Converter<String, MeetingStatusV1> {

    @Override
    public MeetingStatusV1 convert(String source) {
        return MeetingStatusV1.of(source);
    }
}
