package com.promise8.wwwbe.controller;

import com.promise8.wwwbe.model.http.BaseResponse;
import com.promise8.wwwbe.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    
    /**
     * 입력 받을 정보 : 방 이름, 예상인원, 일정 범위, 투표 종료일
     * @return
     */
    @PostMapping
    public BaseResponse<String> createMeeting() {
        return BaseResponse.ok("meeting");
    }

    @GetMapping("/{meetingId}")
    public BaseResponse<String> getMeeting(@PathVariable("meetingId") long meetingId) {
        return BaseResponse.ok("meetingId");
    }

    @PostMapping("/{meetingId}/timetable")
    public BaseResponse<String> createTimetable() {
        return BaseResponse.ok("createTimetable");
    }

    @PostMapping("/{meetingId}/places")
    public BaseResponse<String> createPlace() {
        return BaseResponse.ok("createPlace");
    }
}
