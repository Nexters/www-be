package com.promise8.wwwbe.v1.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity(name = "meeting_place")
@Builder
@ToString(of = {"meetingPlaceId", "promisePlace", "isConfirmed"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meeting_place", schema = "www", catalog = "")
public class MeetingPlaceEntityV1 extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "meeting_place_id")
    private Long meetingPlaceId;
    @Basic
    @Column(name = "promise_place")
    private String promisePlace;
    @Basic
    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_user_id")
    private MeetingUserEntityV1 meetingUserEntity;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "meetingPlaceEntity", cascade = CascadeType.ALL)
    private List<PlaceVoteEntityV1> placeVoteEntityList = new ArrayList<>();
}
