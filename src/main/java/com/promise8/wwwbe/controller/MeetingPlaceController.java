package com.promise8.wwwbe.controller;

import com.promise8.wwwbe.model.dto.res.UserPromisePlaceResDto;
import com.promise8.wwwbe.model.http.BaseResponse;
import com.promise8.wwwbe.service.MeetingPlaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
@Api(value = "MeetingPlaceController", tags = "MeetingPlaceController", description = "약속방 내 유저들이 선택한 장소를 제공하는 api")
public class MeetingPlaceController {
    private final MeetingPlaceService meetingPlaceService;

    @ApiOperation(value = "유저들이 선택한 장소 리스트 조회", notes = "유저들이 선택한 장소 리스트를 투표한 유저 수 기준 내림차순으로 정렬된 데이터를 받는다.")
    @ApiResponses({
            @ApiResponse(code = 0, message = "조회 성공"),
            @ApiResponse(code = 403, message = "접근 거부"),
            @ApiResponse(code = 500, message = "서버 에러 발생"),
            @ApiResponse(code = 1000, message = "서버 에러 발생"),
            @ApiResponse(code = 4000, message = "존재하지 않는 약속방")
    })
    @GetMapping("/{meetingId}")
    public BaseResponse<List<UserPromisePlaceResDto>> getPromisePlaceList(@PathVariable("meetingId") Long meetingId) {
        return BaseResponse.ok(meetingPlaceService.getMeetingPlaceList(meetingId));
    }
}
