package com.promise8.wwwbe.service;

import com.promise8.wwwbe.repository.UserRepository;
import com.promise8.wwwbe.model.v1.entity.UserEntityV1;
import com.promise8.wwwbe.model.v1.exception.BizException;
import com.promise8.wwwbe.model.v1.http.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void setAlarm(long userId, boolean isAlarmOn) {
        UserEntityV1 userEntity = userRepository.findById(userId).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_USER));
        userEntity.setIsAlarmOn(isAlarmOn);
    }
}
