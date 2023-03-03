package com.promise8.wwwbe.v1.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.promise8.wwwbe.v1.model.dto.PromiseTime;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@Entity(name = "meeting_user_timetable")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meeting_user_timetable", schema = "www", catalog = "")
public class MeetingUserTimetableEntityV1 extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "meeting_user_timetable_id")
    private Long meetingUserTimetableId;
    @Basic
    @Column(name = "promise_date")
    private LocalDate promiseDate;
    @Basic
    @Column(name = "promise_time")
    @Enumerated(EnumType.STRING)
    private PromiseTime promiseTime;
    @Basic
    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_user_id")
    private MeetingUserEntityV1 meetingUserEntity;
}
