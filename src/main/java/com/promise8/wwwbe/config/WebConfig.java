package com.promise8.wwwbe.config;

import com.promise8.wwwbe.config.converter.MeetingStatusConverter;
import com.promise8.wwwbe.config.converter.PlatformTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MeetingStatusConverter());
        registry.addConverter(new PlatformTypeConverter());
    }
}
