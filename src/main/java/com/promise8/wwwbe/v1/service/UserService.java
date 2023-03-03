package com.promise8.wwwbe.v1.service;

import com.promise8.wwwbe.v1.model.entity.UserEntity;
import com.promise8.wwwbe.v1.model.exception.BizException;
import com.promise8.wwwbe.v1.model.http.BaseErrorCode;
import com.promise8.wwwbe.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void setAlarm(long userId, boolean isAlarmOn) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_USER));
        userEntity.setIsAlarmOn(isAlarmOn);
    }
}
