package com.promise8.wwwbe.model.v2.dto.res;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@ApiModel(value = "MeetingDetail", description = "약속방 내의 필요한 정보를 모두 담는다.")
public class MeetingGetResDtoV2 {
    private Long meetingId;
    private String meetingName;
    private int minimumAlertMembers;
    private String currentUserName;
    private Boolean isHost;
    private int joinedCount;
    private int votingCount;
    private String meetingCode;
    private String shortLink;
}
