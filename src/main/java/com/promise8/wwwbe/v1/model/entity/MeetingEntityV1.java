package com.promise8.wwwbe.v1.model.entity;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity(name = "meeting")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meeting", schema = "www", catalog = "")
public class MeetingEntityV1 extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "meeting_id")
    private Long meetingId;
    @Basic
    @Column(name = "meeting_name")
    private String meetingName;
    @Basic
    @Column(name = "condition_count")
    private Long conditionCount;
    @Basic
    @Column(name = "start_date")
    private LocalDate startDate;
    @Basic
    @Column(name = "end_date")
    private LocalDate endDate;
    @Basic
    @Column(name = "meeting_code", columnDefinition = "VARCHAR(255) COLLATE utf8_bin")
    private String meetingCode;
    @Basic
    @Column(name = "short_link")
    private String shortLink;
    @Basic
    @Column(name = "meeting_status")
    @Enumerated(EnumType.STRING)
    private MeetingStatusV1 meetingStatus = MeetingStatusV1.WAITING;
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name = "hostId")
    private UserEntityV1 creator;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "meetingEntity", cascade = CascadeType.ALL)
    private List<MeetingUserEntityV1> meetingUserEntityList = new ArrayList<>();

    private LocalDateTime voteFinishDateTime;

    public MeetingUserEntityV1 addMeetingUser(MeetingUserEntityV1 meetingUserEntity) {
        this.meetingUserEntityList.add(meetingUserEntity);
        meetingUserEntity.setMeetingEntity(this);
        return meetingUserEntity;
    }
}
