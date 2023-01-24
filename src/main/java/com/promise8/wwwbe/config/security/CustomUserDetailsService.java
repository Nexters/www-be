package com.promise8.wwwbe.config.security;

import com.promise8.wwwbe.model.entity.UserEntity;
import com.promise8.wwwbe.repository.UserRepository;
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
                .orElseThrow(() -> new UsernameNotFoundException("User not found with deviceId : " + deviceId));

        return UserPrincipal.create(user);
    }
}
