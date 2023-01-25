package com.promise8.wwwbe.model.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meeting_user", schema = "www", catalog = "")
public class MeetingUserEntity extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "meeting_user_id")
    private long meetingUserId;
    @Basic
    @Column(name = "meeting_user_name")
    private String meetingUserName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "meeting_id")
    private MeetingEntity meetingEntity;
}
