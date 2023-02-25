package com.promise8.wwwbe.controller;

import com.promise8.wwwbe.config.security.UserPrincipal;
import com.promise8.wwwbe.model.dto.req.JoinMeetingReqDto;
import com.promise8.wwwbe.model.dto.req.MeetingCreateReqDto;
import com.promise8.wwwbe.model.dto.req.PlaceVoteReqDto;
import com.promise8.wwwbe.model.dto.res.MeetingCreateResDto;
import com.promise8.wwwbe.model.dto.res.MeetingGetResDto;
import com.promise8.wwwbe.model.dto.res.MeetingMainGetResDtoWrapper;
import com.promise8.wwwbe.model.entity.MeetingStatus;
import com.promise8.wwwbe.model.http.BaseResponse;
import com.promise8.wwwbe.service.MeetingService;
import com.promise8.wwwbe.service.PlaceVoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
@Api(value = "MeetingController", tags = "MeetingController", description = "약속 방과 관련된 모든 api를 제공한다.")
public class MeetingController {
    private final MeetingService meetingService;
    private final PlaceVoteService placeVoteService;

    /**
     * 약속방 메인 view 전용 API
     *
     * @param userPrincipal
     * @return
     */
    @ApiOperation(value = "참여중인 약속방 리스트 조회", notes = "사용자가 참여중인 모든 상태의 약속방 리스트를 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 0, message = "조회 성공"),
            @ApiResponse(code = 403, message = "접근 거부"),
            @ApiResponse(code = 500, message = "서버 에러 발생"),
            @ApiResponse(code = 1000, message = "서버 에러 발생")
    })
    @GetMapping
    public BaseResponse<MeetingMainGetResDtoWrapper> getMeetingList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return BaseResponse.ok(meetingService.getMeetingListByDeviceId(userPrincipal.getDeviceId()));
    }

    /**
     * 방 생성
     * <p>
     * 입력 받을 정보 : 방 이름, 예상인원, 일정 범위, 투표 종료일
     *
     * @param meetingCreateReqDto
     * @return
     */
    @ApiOperation(value = "약속방 생성", notes = "약속방을 생성한다.")
    @ApiResponses({
            @ApiResponse(code = 0, message = "생성 완료"),
            @ApiResponse(code = 403, message = "접근 거부"),
            @ApiResponse(code = 500, message = "서버 에러 발생"),
            @ApiResponse(code = 1000, message = "서버 에러 발생")
    })
    @PostMapping
    public BaseResponse<MeetingCreateResDto> createMeeting(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody MeetingCreateReqDto meetingCreateReqDto) {
        return BaseResponse.ok(meetingService.createMeeting(meetingCreateReqDto, userPrincipal.getDeviceId()));
    }


    /**
     * TODO: 요청자가 방장인지 여부 응답 필요
     * 약속방 정보 조회
     *
     * @param meetingId
     * @return
     */
    @ApiOperation(value = "약속방 조회 by meetingId", notes = "meetingId로 참여되어있는 약속방에 입장한다.\n방 내에서 필요한 정보를 모두 받는다.")
    @ApiResponses({
            @ApiResponse(code = 0, message = "입장 완료"),
            @ApiResponse(code = 403, message = "접근 거부"),
            @ApiResponse(code = 500, message = "서버 에러 발생"),
            @ApiResponse(code = 1000, message = "서버 에러 발생"),
            @ApiResponse(code = 4000, message = "존재하지 않는 약속방"),
            @ApiResponse(code = 4001, message = "존재하지 않는 유저"),
            @ApiResponse(code = 5000, message = "이미 투표가 시작된 약속방")
    })
    @GetMapping("/{meetingId}")
    public BaseResponse<MeetingGetResDto> getMeetingById(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("meetingId") long meetingId) {
        return BaseResponse.ok(meetingService.getMeetingById(meetingId, userPrincipal.getId()));
    }

    /**
     * TODO 미팅방 상태 확인
     * code를 통해 약속방 입장
     *
     * @param meetingCode
     * @return
     */
    @ApiOperation(value = "약속방 Code 확인 및 참여 요청", notes = "1. (방장만 해당) meetingCode로 참여되어있는 약속방에 입장하여 방 내에서 필요한 정보를 모두 받는다.\n2. (참여자만 해당) meetingCode로 약속방에 등록된 Code와 일치한지 확인하여 방 내에 언제, 어디서 정보를 입력하기 위한 정보를 받는다.")
    @ApiResponses({
            @ApiResponse(code = 0, message = "Code 일치"),
            @ApiResponse(code = 403, message = "접근 거부"),
            @ApiResponse(code = 500, message = "서버 에러 발생"),
            @ApiResponse(code = 1000, message = "서버 에러 발생"),
            @ApiResponse(code = 4000, message = "존재하지 않는 약속방"),
            @ApiResponse(code = 4001, message = "존재하지 않는 유저"),
            @ApiResponse(code = 5000, message = "이미 투표가 시작된 약속방")
    })
    @GetMapping("/code/{meetingCode}")
    public BaseResponse<MeetingGetResDto> getMeetingByCode(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("meetingCode") String meetingCode) {
        return BaseResponse.ok(meetingService.getMeetingByCode(meetingCode, userPrincipal.getId()));
    }

    /**
     * 링크공유를 통해 약속방 입장 (성찬)
     *
     * @param meetingId
     * @return
     */
    @ApiOperation(value = "약속방 참여", notes = "meetingId로 약속방에 참여를 요청한다.")
    @ApiResponses({
            @ApiResponse(code = 0, message = "참여 성공"),
            @ApiResponse(code = 403, message = "접근 거부"),
            @ApiResponse(code = 500, message = "서버 에러 발생"),
            @ApiResponse(code = 1000, message = "서버 에러 발생"),
            @ApiResponse(code = 3001, message = "이미 참여된 약속방"),
            @ApiResponse(code = 4000, message = "존재하지 않는 약속방"),
            @ApiResponse(code = 4001, message = "존재하지 않는 유저")
    })
    @PostMapping("/{meetingId}")
    public BaseResponse<Void> joinMeeting(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("meetingId") long meetingId,
            @RequestBody JoinMeetingReqDto joinMeetingReqDto
    ) {
        meetingService.joinMeetingAndGetMeetingUserId(userPrincipal.getId(), meetingId, joinMeetingReqDto);
        return BaseResponse.ok();
    }


    /**
     * 투표하기 (성찬)
     *
     * @param userPrincipal
     * @param meetingId
     * @return
     */
    @ApiOperation(value = "장소 투표", notes = "장소를 투표한다.")
    @ApiResponses({
            @ApiResponse(code = 0, message = "투표 완료"),
            @ApiResponse(code = 403, message = "접근 거부"),
            @ApiResponse(code = 500, message = "서버 에러 발생"),
            @ApiResponse(code = 1000, message = "서버 에러 발생"),
            @ApiResponse(code = 4000, message = "존재하지 않는 약속방"),
            @ApiResponse(code = 4001, message = "존재하지 않는 유저"),
            @ApiResponse(code = 4002, message = "해당 약속방에 참여하지 않은 유저")
    })
    @PostMapping("/{meetingId}/votes")
    public BaseResponse<Void> createPlaceVote(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("meetingId") long meetingId,
            @RequestBody PlaceVoteReqDto placeVoteReqDto
    ) {
        placeVoteService.vote(meetingId, userPrincipal.getId(), placeVoteReqDto);

        return BaseResponse.ok();
    }

    /**
     * 약속방에 액션수행 (방장만 수행 가능)
     *
     * @param userPrincipal
     * @param meetingId
     * @param meetingStatus
     * @return
     */
    @ApiOperation(value = "약속방 상태 변경", notes = "약속방의 상태를 변경한다.")
    @ApiResponses({
            @ApiResponse(code = 0, message = "변경 완료"),
            @ApiResponse(code = 403, message = "접근 거부"),
            @ApiResponse(code = 500, message = "서버 에러 발생"),
            @ApiResponse(code = 1000, message = "서버 에러 발생"),
            @ApiResponse(code = 4000, message = "존재하지 않는 약속방"),
            @ApiResponse(code = 9000, message = "잘못된 요청")
    })
    @PutMapping("/{meetingId}/meetingStatus/{meetingStatus}")
    @PreAuthorize("@meetingAuthorizer.isCreator(#userPrincipal, #meetingId)")
    public BaseResponse<Void> putMeetingAction(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("meetingId") long meetingId,
            @PathVariable("meetingStatus") MeetingStatus meetingStatus
    ) {
        meetingService.putMeetingStatus(meetingId, meetingStatus);
        return BaseResponse.ok();
    }
}
