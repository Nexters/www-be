package com.promise8.wwwbe.model.entity;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
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
    private LocalDate startDate;
    @Basic
    @Column(name = "end_date")
    private LocalDate endDate;
    @Basic
    @Column(name = "meeting_code")
    private String meetingCode;
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name = "hostId")
    private UserEntity userEntity;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "meetingEntity", cascade = CascadeType.ALL)
    private List<MeetingUserEntity> meetingUserEntityList = new ArrayList<>();
}
