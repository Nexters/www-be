package com.promise8.wwwbe.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class PromiseDateAndTimeDto {
    private LocalDateTime promiseDate;
    private List<String> promiseTimeList;
}
