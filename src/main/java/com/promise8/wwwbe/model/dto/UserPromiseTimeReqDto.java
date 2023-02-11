package com.promise8.wwwbe.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class UserPromiseTimeReqDto {
    private LocalDate promiseDate;
    private PromiseTime promiseTime;
}
