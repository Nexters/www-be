package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.entity.UserEntity;
import com.promise8.wwwbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void setAlarm(long userId, boolean isAlarmOn) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId : " + userId));
        userEntity.setIsAlarmOn(isAlarmOn);
    }
}
