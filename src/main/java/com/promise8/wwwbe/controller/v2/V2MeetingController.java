package com.promise8.wwwbe.controller.v2;

import com.promise8.wwwbe.config.security.UserPrincipal;
import com.promise8.wwwbe.model.v1.dto.req.MeetingCreateReqDtoV1;
import com.promise8.wwwbe.model.v1.dto.res.MeetingGetResDtoV1;
import com.promise8.wwwbe.model.v1.http.BaseResponse;
import com.promise8.wwwbe.service.MeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/meetings")
@RequiredArgsConstructor
@Api(value = "MeetingController", tags = "MeetingController", description = "약속 방과 관련된 모든 api를 제공한다.")
public class V2MeetingController {
    private final MeetingService meetingService;

    @ApiOperation(value = "약속방 생성", notes = "약속방을 생성한다.")
    @ApiResponses({
            @ApiResponse(code = 0, message = "생성 완료"),
            @ApiResponse(code = 403, message = "접근 거부"),
            @ApiResponse(code = 500, message = "서버 에러 발생"),
            @ApiResponse(code = 1000, message = "서버 에러 발생")
    })
    @PostMapping
    public BaseResponse<MeetingGetResDtoV1> createMeeting(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody MeetingCreateReqDtoV1 meetingCreateReqDto) {

        return BaseResponse.ok();
    }
}
