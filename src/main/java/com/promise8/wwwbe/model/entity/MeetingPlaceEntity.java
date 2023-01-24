package com.promise8.wwwbe.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "meeting_place", schema = "www", catalog = "")
public class MeetingPlaceEntity extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "meeting_place_id")
    private long meetingPlaceId;
    @Basic
    @Column(name = "promise_place")
    private String promisePlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_user_id")
    private MeetingUserEntity meetingUserEntity;

    public MeetingPlaceEntity(String promisePlace, MeetingUserEntity meetingUserEntity) {
        this.promisePlace = promisePlace;
        this.meetingUserEntity = meetingUserEntity;
    }
}
