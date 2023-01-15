package com.promise8.wwwbe.model.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "meeting_user_timetable", schema = "www", catalog = "")
public class MeetingUserTimetableEntity {
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
}
