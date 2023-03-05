package com.promise8.wwwbe.model.v1.dto.res;

import com.promise8.wwwbe.model.v1.dto.PromiseTime;
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
