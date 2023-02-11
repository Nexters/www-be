package com.promise8.wwwbe.model.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user", schema = "www", catalog = "", uniqueConstraints = {
        @UniqueConstraint(columnNames = "device_id")
})
public class UserEntity extends BaseTimeEntity {
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
    @Column(name = "fcm_token")
    private String fcmToken;
}
