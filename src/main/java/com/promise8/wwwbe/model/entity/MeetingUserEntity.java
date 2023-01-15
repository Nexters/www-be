package com.promise8.wwwbe.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "meeting_user", schema = "www", catalog = "")
public class MeetingUserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "meeting_user_id")
    private long meetingUserId;
    @Basic
    @Column(name = "meeting_user_name")
    private String meetingUserName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private MeetingEntity meetingEntity;

//    @JsonIgnore
//    @OneToMany(mappedBy = "meeting_user", cascade = CascadeType.ALL)
//    private List<MeetingUserTimetableEntity> meetingUserTimetableEntityList = new ArrayList<>();
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "meeting_user", cascade = CascadeType.ALL)
//    private List<MeetingPlaceEntity> meetingPlaceEntityList = new ArrayList<>();
}
