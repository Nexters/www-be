package com.promise8.wwwbe.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meeting", schema = "www", catalog = "")
public class MeetingEntity extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "meeting_id")
    private long meetingId;
    @Basic
    @Column(name = "meeting_name")
    private String meetingName;
    @Basic
    @Column(name = "condition_count")
    private Long conditionCount;
    @Basic
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Basic
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Basic
    @Column(name = "meeting_code")
    private String meetingCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostId")
    private UserEntity userEntity;
}
