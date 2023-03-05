package com.promise8.wwwbe.config.security;

import com.promise8.wwwbe.repository.UserRepository;
import com.promise8.wwwbe.v1.model.dto.req.LoginReqDtoV1;
import com.promise8.wwwbe.v1.model.entity.UserEntityV1;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeviceIdAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LoginReqDtoV1 loginReqDto = (LoginReqDtoV1) authentication.getCredentials();
        Optional<UserEntityV1> userEntityOptional = userRepository.findByDeviceId(loginReqDto.getDeviceId());

        if (userEntityOptional.isPresent()) {
            UserEntityV1 user = userEntityOptional.get();
            user.setFcmToken(loginReqDto.getFcmToken());
            user = userRepository.save(user);

            return new UsernamePasswordAuthenticationToken(UserPrincipal.create(user), user.getDeviceId(), new ArrayList<>());
        } else {
            UserEntityV1 user = UserEntityV1.builder()
                    .userName(loginReqDto.getUserName())
                    .deviceId(loginReqDto.getDeviceId())
                    .fcmToken(loginReqDto.getFcmToken())
                    .isAlarmOn(true)
                    .build();
            UserEntityV1 newUser = userRepository.save(user);
            return new UsernamePasswordAuthenticationToken(UserPrincipal.create(newUser), newUser.getDeviceId(), new ArrayList<>());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
