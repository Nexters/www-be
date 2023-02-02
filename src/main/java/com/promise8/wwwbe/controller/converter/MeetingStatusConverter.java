package com.promise8.wwwbe.controller.converter;

import com.promise8.wwwbe.model.entity.MeetingStatus;
import org.springframework.core.convert.converter.Converter;

public class MeetingStatusConverter implements Converter<String, MeetingStatus> {

    @Override
    public MeetingStatus convert(String source) {
        return MeetingStatus.of(source);
    }
}
