package com.promise8.wwwbe.model.v1.entity;

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
public class PlaceVoteEntityV1 extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "place_vote_id")
    private Long placeVoteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetingUserId")
    private MeetingUserEntityV1 meetingUserEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_place_id")
    private MeetingPlaceEntityV1 meetingPlaceEntity;
}
