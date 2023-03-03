package com.promise8.wwwbe.v1.controller.converter;

import com.promise8.wwwbe.v1.model.entity.MeetingStatus;
import org.springframework.core.convert.converter.Converter;

public class MeetingStatusConverter implements Converter<String, MeetingStatus> {

    @Override
    public MeetingStatus convert(String source) {
        return MeetingStatus.of(source);
    }
}
