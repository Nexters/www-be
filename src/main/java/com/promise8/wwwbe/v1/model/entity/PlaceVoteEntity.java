package com.promise8.wwwbe.v1.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity(name = "place_vote")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "place_vote", schema = "www", catalog = "")
public class PlaceVoteEntity extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "place_vote_id")
    private Long placeVoteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetingUserId")
    private MeetingUserEntity meetingUserEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_place_id")
    private MeetingPlaceEntity meetingPlaceEntity;
}
