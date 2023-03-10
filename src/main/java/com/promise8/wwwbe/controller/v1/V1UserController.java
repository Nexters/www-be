package com.promise8.wwwbe.controller.v1;

import com.promise8.wwwbe.config.security.TokenProvider;
import com.promise8.wwwbe.config.security.UserPrincipal;
import com.promise8.wwwbe.service.UserService;
import com.promise8.wwwbe.model.v1.dto.req.AlarmReqDtoV1;
import com.promise8.wwwbe.model.v1.dto.req.LoginReqDtoV1;
import com.promise8.wwwbe.model.v1.http.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Api(value = "UserController", tags = "UserController", description = "유저 정보를 입력, 알림 업데이트 기능을 제공한다.")
public class V1UserController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    private final UserService userService;

    @ApiOperation(value = "앱 로딩 시 유저 정보 확인, token 발급", notes = "앱 로딩 시 token을 발급 받는다.")
    @ApiResponses({
            @ApiResponse(code = 0, message = "조회 성공"),
            @ApiResponse(code = 1000, message = "서버 에러 발생")
    })
    @PostMapping("/join")
    public BaseResponse<String> join(@RequestBody LoginReqDtoV1 loginReqDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(null, loginReqDto)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);

        return BaseResponse.ok(token);
    }

    @ApiOperation(value = "알림 받기 on off", notes = "알림 받기를 허용(on) or 거부(off) 상태로 변경한다.")
    @ApiResponses({
            @ApiResponse(code = 0, message = "변경 완료"),
            @ApiResponse(code = 403, message = "접근 거부"),
            @ApiResponse(code = 500, message = "서버 에러 발생"),
            @ApiResponse(code = 1000, message = "서버 에러 발생"),
            @ApiResponse(code = 4001, message = "존재하지 않는 유저")
    })
    @PostMapping("/alarm")
    public BaseResponse<Void> updateAlarm(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody AlarmReqDtoV1 alarmReqDto) {
        userService.setAlarm(userPrincipal.getId(), alarmReqDto.isAlarmOn());
        return BaseResponse.ok();
    }
}
