package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingStatus;
import com.promise8.wwwbe.model.entity.UserEntity;
import com.promise8.wwwbe.model.exception.BizException;
import com.promise8.wwwbe.model.http.BaseErrorCode;
import com.promise8.wwwbe.repository.MeetingRepository;
import com.promise8.wwwbe.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class MeetingServiceTest {

    @InjectMocks
    MeetingService meetingService;

    @Mock
    MeetingRepository meetingRepository;

    @Mock
    UserRepository userRepository;


    @Test
    @DisplayName("meetingStatus 변경 확인")
    void putMeetingStatusWhenActionEndVote() {
        // given
        MeetingEntity meetingEntity = MeetingEntity.builder()
                .meetingId(1L)
                .meetingStatus(MeetingStatus.WAITING)
                .build();

        when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meetingEntity));

        // when
        meetingService.putMeetingStatus(1L, MeetingStatus.VOTED);

        // then
        Assertions.assertEquals(MeetingStatus.VOTED, meetingEntity.getMeetingStatus());
    }

    @Test
    @DisplayName("join meeting시 validation 확인")
    void joinMeetingTest() {
        // given
        MeetingEntity meetingEntity = MeetingEntity.builder()
                .meetingId(1L)
                .meetingStatus(MeetingStatus.DONE)
                .build();
        UserEntity userEntity = UserEntity.builder()
                .userId(1L)
                .build();
        when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meetingEntity));

        // when
        BizException bizException = Assertions.assertThrows(BizException.class, () -> {
            meetingService.joinMeeting(1L, 1L);
        });

        // then
        Assertions.assertEquals(BaseErrorCode.MEETING_VOTE_ALREADY_STARTED, bizException.getCode());
    }
}