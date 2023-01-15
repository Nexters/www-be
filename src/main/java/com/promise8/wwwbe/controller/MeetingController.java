package com.promise8.wwwbe.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    /**
     * 입력 받을 정보 : 방 이름, 예상인원, 일정 범위, 투표 종료일
     * @return
     */
    @PostMapping
    public String createMeeting() {
        return "meeting";
    }

    @GetMapping("/{meetingId}")
    public String getMeeting(@PathVariable("meetingId") long meetingId) {
        return "meetingId";
    }

    @PostMapping("/{meetingId}/timetable")
    public String createTimetable() {
        return "createTimetable";
    }

    @PostMapping("/{meetingId}/places")
    public String createPlace() {
        return "createPlace";
    }
}
