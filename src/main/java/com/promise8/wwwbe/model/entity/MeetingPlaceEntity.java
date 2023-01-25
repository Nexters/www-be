package com.promise8.wwwbe.model.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meeting_place", schema = "www", catalog = "")
public class MeetingPlaceEntity extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "meeting_place_id")
    private long meetingPlaceId;
    @Basic
    @Column(name = "promise_place")
    private String promisePlace;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "meeting_user_id")
    private MeetingUserEntity meetingUserEntity;
}
