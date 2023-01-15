package com.promise8.wwwbe.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "user", schema = "www", catalog = "")
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private long userId;
    @Basic
    @Column(name = "device_id")
    private String deviceId;
    @Basic
    @Column(name = "user_name")
    private String userName;
    @Basic
    @Column(name = "uuid")
    private UUID uuid = UUID.randomUUID();

//    @JsonIgnore
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<MeetingEntity> meetingEntityList = new ArrayList<>();
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<MeetingUserEntity> meetingUserEntityList = new ArrayList<>();
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<PlaceVoteEntity> placeVoteEntityList = new ArrayList<>();

}
