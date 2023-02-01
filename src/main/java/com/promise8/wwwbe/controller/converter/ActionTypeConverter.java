package com.promise8.wwwbe.controller.converter;

import com.promise8.wwwbe.model.dto.ActionType;
import org.springframework.core.convert.converter.Converter;

public class ActionTypeConverter implements Converter<String, ActionType> {

    @Override
    public ActionType convert(String source) {
        return ActionType.of(source);
    }
}
