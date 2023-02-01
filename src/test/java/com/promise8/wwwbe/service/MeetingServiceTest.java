package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.ActionType;
import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingStatus;
import com.promise8.wwwbe.repository.MeetingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class MeetingServiceTest {

    @InjectMocks
    MeetingService meetingService;

    @Mock
    MeetingRepository meetingRepository;


    @Test
    @DisplayName("end vote시 meetingStatus 변경 확인")
    void putMeetingStatusWhenActionEndVote() {
        // given
        MeetingEntity meetingEntity = MeetingEntity.builder()
                .meetingId(1L)
                .meetingStatus(MeetingStatus.WAITING)
                .build();

        when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meetingEntity));

        // when
        meetingService.putMeetingStatus(1L, ActionType.END_VOTE);

        // then
        Assertions.assertEquals(MeetingStatus.VOTED, meetingEntity.getMeetingStatus());
    }

    @Test
    @DisplayName("end meeting시 meetingStatus 변경 확인")
    void putMeetingStatusWhenActionEndMeeting() {
        // given
        MeetingEntity meetingEntity = MeetingEntity.builder()
                .meetingId(1L)
                .meetingStatus(MeetingStatus.WAITING)
                .build();

        when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meetingEntity));

        // when
        meetingService.putMeetingStatus(1L, ActionType.END_MEETING);

        // then
        Assertions.assertEquals(MeetingStatus.CONFIRMED, meetingEntity.getMeetingStatus());
    }
}