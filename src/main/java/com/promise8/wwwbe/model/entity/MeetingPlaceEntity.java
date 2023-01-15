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
@Table(name = "meeting_place", schema = "www", catalog = "")
public class MeetingPlaceEntity {
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

//    @JsonIgnore
//    @OneToMany(mappedBy = "meeting_place", cascade = CascadeType.ALL)
//    private List<PlaceVoteEntity> placeVoteEntityList = new ArrayList<>();
}
