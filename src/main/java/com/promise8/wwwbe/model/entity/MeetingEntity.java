package com.promise8.wwwbe.model.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "meeting", schema = "www", catalog = "")
public class MeetingEntity {
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
    @Column(name = "vote_end_date")
    private LocalDateTime voteEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

//    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
//    private List<MeetingUserEntity> meetingUserEntityList = new ArrayList<>();
}
