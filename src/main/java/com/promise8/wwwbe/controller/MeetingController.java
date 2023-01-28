package com.promise8.wwwbe.controller;

import com.promise8.wwwbe.config.security.TokenProvider;
import com.promise8.wwwbe.model.dto.MeetingCreateReqDto;
import com.promise8.wwwbe.model.dto.MeetingCreateResDto;
import com.promise8.wwwbe.model.dto.MeetingGetRes;
import com.promise8.wwwbe.model.http.BaseResponse;
import com.promise8.wwwbe.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private static final int TOKEN_BEGIN_INDEX = 7;
    private final MeetingService meetingService;
    private final TokenProvider tokenProvider;

    /**
     * 입력 받을 정보 : 방 이름, 예상인원, 일정 범위, 투표 종료일
     *
     * @return
     */
    @PostMapping
    public BaseResponse<MeetingCreateResDto> createMeeting(@RequestBody MeetingCreateReqDto meetingCreateReqDto) {
        return BaseResponse.ok(meetingService.createMeeting(meetingCreateReqDto));
    }

    @GetMapping
    public BaseResponse<List<MeetingGetRes>> getMeetingList(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(TOKEN_BEGIN_INDEX);
        String deviceId = tokenProvider.getDeviceIdFromToken(token);

        return BaseResponse.ok(meetingService.getMeetingByDeviceId(deviceId));
    }

    @GetMapping("/{meetingId}")
    public BaseResponse<MeetingGetRes> getMeetingById(@PathVariable("meetingId") long meetingId) {
        return BaseResponse.ok(meetingService.getMeetingById(meetingId));
    }

    @GetMapping("/code/{meetingCode}")
    public BaseResponse<MeetingGetRes> getMeetingByCode(@PathVariable("meetingCode") String meetingCode) {
        return BaseResponse.ok(meetingService.getMeetingByCode(meetingCode));
    }

    @PostMapping("/{meetingId}/timetable")
    public BaseResponse<String> createTimetable(@PathVariable("meetingId") long meetingId) {
        return BaseResponse.ok("createTimetable");
    }

    @PostMapping("/{meetingId}/places")
    public BaseResponse<String> createPlace(@PathVariable("meetingId") long meetingId) {
        return BaseResponse.ok("createPlace");
    }
}
