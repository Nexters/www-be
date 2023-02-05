package com.promise8.wwwbe.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ConfirmedPromiseDto {
    private LocalDate promiseDate = null;
    private PromiseTime promiseTime = null;
    private String promisePlace = null;
    private Integer votingUserCount = 0;
    private String hostName = null;
}