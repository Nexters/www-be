package com.promise8.wwwbe.model.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPromiseTimeReqDto {
    private LocalDate promiseDate;
    private PromiseTime promiseTime;
}
