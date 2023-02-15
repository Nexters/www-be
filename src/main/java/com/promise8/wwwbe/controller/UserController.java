package com.promise8.wwwbe.controller;

import com.promise8.wwwbe.config.security.TokenProvider;
import com.promise8.wwwbe.config.security.UserPrincipal;
import com.promise8.wwwbe.model.dto.req.AlarmReqDto;
import com.promise8.wwwbe.model.dto.req.LoginReqDto;
import com.promise8.wwwbe.model.http.BaseResponse;
import com.promise8.wwwbe.service.UserService;
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
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    private final UserService userService;

    @PostMapping("/join")
    public BaseResponse<String> join(@RequestBody LoginReqDto loginReqDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(null, loginReqDto)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);

        return BaseResponse.ok(token);
    }

    @PostMapping("/alarm")
    public BaseResponse<Void> updateAlarm(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody AlarmReqDto alarmReqDto) {
        userService.setAlarm(userPrincipal.getId(), alarmReqDto.isAlarmOn());
        return BaseResponse.ok();
    }
}
