package com.promise8.wwwbe.v1.config.security;

import com.promise8.wwwbe.v1.model.entity.UserEntity;
import com.promise8.wwwbe.v1.model.exception.BizException;
import com.promise8.wwwbe.v1.model.http.BaseErrorCode;
import com.promise8.wwwbe.v1.repository.UserRepository;
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
        UserEntity user = userRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_USER));

        return UserPrincipal.create(user);
    }
}
