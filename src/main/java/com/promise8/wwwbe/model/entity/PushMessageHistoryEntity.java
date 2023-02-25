package com.promise8.wwwbe.model.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "push_message_history")
@Table(name = "push_message_history")
public class PushMessageHistoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "push_message_history_id")
    private long pushMessageHistoryId;
    private long meetingId;
    private String title;

    private String text;

}
