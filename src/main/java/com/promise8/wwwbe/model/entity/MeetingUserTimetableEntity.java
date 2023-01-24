package com.promise8.wwwbe.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "meeting_user_timetable", schema = "www", catalog = "")
public class MeetingUserTimetableEntity extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "meeting_user_timetable_id")
    private long meetingUserTimetableId;
    @Basic
    @Column(name = "promise_date")
    private LocalDateTime promiseDate;
    @Basic
    @Column(name = "promise_time")
    private String promiseTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_user_id")
    private MeetingUserEntity meetingUserEntity;

    public MeetingUserTimetableEntity(LocalDateTime promiseDate, String promiseTime, MeetingUserEntity meetingUserEntity) {
        this.promiseDate = promiseDate;
        this.promiseTime = promiseTime;
        this.meetingUserEntity = meetingUserEntity;
    }
}
