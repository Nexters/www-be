package com.promise8.wwwbe.config.security;

import com.promise8.wwwbe.repository.UserRepository;
import com.promise8.wwwbe.v1.model.entity.UserEntityV1;
import com.promise8.wwwbe.v1.model.exception.BizException;
import com.promise8.wwwbe.v1.model.http.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String deviceId) throws UsernameNotFoundException {
        UserEntityV1 user = userRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_USER));

        return UserPrincipal.create(user);
    }
}
