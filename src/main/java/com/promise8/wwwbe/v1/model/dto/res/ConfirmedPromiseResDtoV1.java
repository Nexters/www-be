package com.promise8.wwwbe.v1.model.dto.res;

import com.promise8.wwwbe.v1.model.dto.PromiseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class ConfirmedPromiseResDtoV1 {
    private LocalDate promiseDate = null;
    private PromiseTime promiseTime = null;
    private String promisePlace = null;
    private Integer votingUserCount = 0;
    private String hostName = null;
}