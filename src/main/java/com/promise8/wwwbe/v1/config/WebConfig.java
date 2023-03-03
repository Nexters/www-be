package com.promise8.wwwbe.v1.config;

import com.promise8.wwwbe.v1.controller.converter.MeetingStatusConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MeetingStatusConverter());
    }
}
