package com.promise8.wwwbe.config.security;

import com.promise8.wwwbe.model.dto.LoginReqDto;
import com.promise8.wwwbe.model.entity.UserEntity;
import com.promise8.wwwbe.repository.UserRepository;
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
        LoginReqDto loginReqDto = (LoginReqDto) authentication.getCredentials();
        Optional<UserEntity> userEntityOptional = userRepository.findByDeviceId(loginReqDto.getDeviceId());

        if (userEntityOptional.isPresent()) {
            UserEntity user = userEntityOptional.get();

            return new UsernamePasswordAuthenticationToken(UserPrincipal.create(user), user.getDeviceId(), new ArrayList<>());
        } else {
            UserEntity user = UserEntity.builder()
                    .userName(loginReqDto.getUserName())
                    .deviceId(loginReqDto.getDeviceId())
                    .fcmToken(loginReqDto.getFcmToken())
                    .build();
            UserEntity newUser = userRepository.save(user);
            return new UsernamePasswordAuthenticationToken(UserPrincipal.create(newUser), newUser.getDeviceId(), new ArrayList<>());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
