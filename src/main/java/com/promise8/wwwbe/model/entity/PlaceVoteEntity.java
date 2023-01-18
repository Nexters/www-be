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
@Table(name = "place_vote", schema = "www", catalog = "")
public class PlaceVoteEntity extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "place_vote_id")
    private long placeVoteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_place_id")
    private MeetingPlaceEntity meetingPlaceEntity;
}
