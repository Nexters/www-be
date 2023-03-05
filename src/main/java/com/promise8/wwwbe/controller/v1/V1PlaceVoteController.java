package com.promise8.wwwbe.controller.v1;

import com.promise8.wwwbe.config.security.UserPrincipal;
import com.promise8.wwwbe.service.PlaceVoteService;
import com.promise8.wwwbe.model.v1.dto.res.PromisePlaceResDtoWrapperV1;
import com.promise8.wwwbe.model.v1.http.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
@Api(value = "PlaceVoteController", tags = "PlaceVoteController", description = "약속방 내 유저들이 투표한 장소를 제공하는 api")
public class V1PlaceVoteController {
    private final PlaceVoteService placeVoteService;

    @ApiOperation(value = "유저들이 투표한 장소 리스트 조회", notes = "유저들이 선택한 장소를 선택한 유저 수 기준 내림차순으로 정렬된 데이터와 본인이 선택한 장소 리스트를 받는다.")
    @ApiResponses({
            @ApiResponse(code = 0, message = "조회 성공"),
            @ApiResponse(code = 403, message = "접근 거부"),
            @ApiResponse(code = 500, message = "서버 에러 발생"),
            @ApiResponse(code = 1000, message = "서버 에러 발생"),
            @ApiResponse(code = 4000, message = "존재하지 않는 약속방"),
            @ApiResponse(code = 4001, message = "존재하지 않는 유저"),
            @ApiResponse(code = 4002, message = "해당 약속방에 참여하지 않은 유저")
    })
    @GetMapping("/{meetingId}")
    public BaseResponse<PromisePlaceResDtoWrapperV1> getPlaceVoteList(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("meetingId") Long meetingId) {
        return BaseResponse.ok(placeVoteService.getMeetingPlaceList(userPrincipal.getId(), meetingId));
    }
}
