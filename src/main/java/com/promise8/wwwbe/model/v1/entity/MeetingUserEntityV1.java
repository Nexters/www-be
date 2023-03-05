package com.promise8.wwwbe.model.v1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity(name = "meeting_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meeting_user", schema = "www", catalog = "")
public class MeetingUserEntityV1 extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "meeting_user_id")
    private Long meetingUserId;
    @Basic
    @Column(name = "meeting_user_name")
    private String meetingUserName;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntityV1 userEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private MeetingEntityV1 meetingEntity;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "meetingUserEntity", cascade = CascadeType.ALL)
    private List<MeetingUserTimetableEntityV1> meetingUserTimetableEntityList = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "meetingUserEntity", cascade = CascadeType.ALL)
    private List<MeetingPlaceEntityV1> meetingPlaceEntityList = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "meetingUserEntity", cascade = CascadeType.ALL)
    private List<PlaceVoteEntityV1> placeVoteEntityList = new ArrayList<>();
}
