package com.promise8.wwwbe.controller;

import com.promise8.wwwbe.config.security.TokenProvider;
import com.promise8.wwwbe.config.security.UserPrincipal;
import com.promise8.wwwbe.model.dto.LoginReqDto;
import com.promise8.wwwbe.model.exception.BizException;
import com.promise8.wwwbe.model.http.BaseResponse;
import com.promise8.wwwbe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

        // TODO: advice 테스트용. 추후 변경 필요
        throw new BizException();
    }
}
