package com.promise8.wwwbe.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meeting_user_timetable", schema = "www", catalog = "")
public class MeetingUserTimetableEntity extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "meeting_user_timetable_id")
    private long meetingUserTimetableId;
    @Basic
    @Column(name = "promise_date")
    private LocalDate promiseDate;
    @Basic
    @Column(name = "promise_time")
    private String promiseTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "meeting_user_id")
    private MeetingUserEntity meetingUserEntity;
}
