package com.promise8.wwwbe.model.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class PlaceVoteReqDto {
    List<Long> meetingPlaceId;
}
