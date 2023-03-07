package com.promise8.wwwbe.model.v1.entity;

import com.promise8.wwwbe.model.v1.dto.PlatformTypeV1;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity(name = "app_version")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_version", schema = "www", catalog = "")
public class AppVersionEntityV1 extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "app_version_id")
    private Long appVersionId;
    @Basic
    @Column(name = "platform_type")
    @Enumerated(EnumType.STRING)
    private PlatformTypeV1 platformType;
    @Basic
    @Column(name = "version")
    private String version;
}
