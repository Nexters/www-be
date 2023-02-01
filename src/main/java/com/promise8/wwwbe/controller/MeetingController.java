package com.promise8.wwwbe.controller;

import com.promise8.wwwbe.config.security.UserPrincipal;
import com.promise8.wwwbe.model.dto.ActionType;
import com.promise8.wwwbe.model.dto.MeetingCreateReqDto;
import com.promise8.wwwbe.model.dto.MeetingCreateResDto;
import com.promise8.wwwbe.model.dto.MeetingGetRes;
import com.promise8.wwwbe.model.http.BaseResponse;
import com.promise8.wwwbe.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    /**
     * 방 생성
     *
     * 입력 받을 정보 : 방 이름, 예상인원, 일정 범위, 투표 종료일
     * @param meetingCreateReqDto
     * @return
     */
    @PostMapping
    public BaseResponse<MeetingCreateResDto> createMeeting(@RequestBody MeetingCreateReqDto meetingCreateReqDto) {
        return BaseResponse.ok(meetingService.createMeeting(meetingCreateReqDto));
    }

    /**
     * 약속방 메인 view 전용 API
     *
     * @param userPrincipal
     * @return
     */
    @GetMapping
    public BaseResponse<List<MeetingGetRes>> getMeetingList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return BaseResponse.ok(meetingService.getMeetingByDeviceId(userPrincipal.getDeviceId()));
    }

    /**
     *
     * TODO: 요청자가 방장인지 여부 응답 필요
     * 약속방 정보 조회
     *
     * @param meetingId
     * @return
     */
    @GetMapping("/{meetingId}")
    public BaseResponse<MeetingGetRes> getMeetingById(@PathVariable("meetingId") long meetingId) {
        return BaseResponse.ok(meetingService.getMeetingById(meetingId));
    }

    /**
     * 링크공유를 통해 약속방 입장
     *
     * @param meetingId
     * @return
     */
    @PostMapping("/{meetingId}")
    public BaseResponse<Void> joinMeeting(@PathVariable("meetingId") long meetingId) {
        return BaseResponse.ok();
    }

    /**
     * code를 통해 약속방 입장
     *
     * @param meetingCode
     * @return
     */
    @PostMapping("/code/{meetingCode}")
    public BaseResponse<MeetingGetRes> getMeetingByCode(@PathVariable("meetingCode") String meetingCode) {
        return BaseResponse.ok(meetingService.getMeetingByCode(meetingCode));
    }


    /**
     * 투표하기
     *
     * @param userPrincipal
     * @param meetingId
     * @return
     */
    @PostMapping("/meetings/{meetingId}/votes")
    public BaseResponse<Void> createPlaceVote(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("meetingId") long meetingId) {
        return BaseResponse.ok();
    }
    /**
     * 약속방에 액션수행 (방장만 수행 가능)
     *
     * @param userPrincipal
     * @param meetingId
     * @param actionType
     * @return
     */
    @PutMapping("/meetings/{meetingId}/actions/{actionType}")
    @PreAuthorize("@meetingAuthorizer.isCreator(#userPrincipal, #meetingId)")
    public BaseResponse<Void> createMeetingAction(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("meetingId") long meetingId,
            @PathVariable("actionType") ActionType actionType
    ) {
        meetingService.putMeetingStatus(meetingId, actionType);
        return BaseResponse.ok();
    }
}
