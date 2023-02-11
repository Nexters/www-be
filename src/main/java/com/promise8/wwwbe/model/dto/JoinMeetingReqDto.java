package com.promise8.wwwbe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinMeetingReqDto {
    private String nickname;
    private List<UserPromiseTimeReqDto> userPromiseTimeList = new ArrayList<>();

    private List<String> promisePlaceList = new ArrayList<>();

}
