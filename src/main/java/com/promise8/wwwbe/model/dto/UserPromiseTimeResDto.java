package com.promise8.wwwbe.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
public class UserPromiseTimeResDto {
    private LocalDate promiseDate;
    private PromiseTime promiseTime;
    private PromiseDayOfWeek promiseDayOfWeek;
    private List<String> userNameList;
}
