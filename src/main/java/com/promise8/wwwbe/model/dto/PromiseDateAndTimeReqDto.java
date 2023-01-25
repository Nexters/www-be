package com.promise8.wwwbe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PromiseDateAndTimeReqDto {
    private LocalDate promiseDate;
    private List<PromiseTime> promiseTimeList;
}
