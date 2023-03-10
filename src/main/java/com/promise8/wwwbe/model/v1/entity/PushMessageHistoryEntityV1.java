package com.promise8.wwwbe.model.v1.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "push_message_history")
@Table(name = "push_message_history")
public class PushMessageHistoryEntityV1 {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "push_message_history_id")
    private Long pushMessageHistoryId;
    private Long meetingId;
    private String title;

    private String text;

}
