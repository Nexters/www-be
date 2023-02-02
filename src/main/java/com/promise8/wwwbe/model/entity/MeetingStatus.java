package com.promise8.wwwbe.model.entity;

import java.util.Arrays;

public enum MeetingStatus {
    WAITING("waiting"), // 투표 시작 전 (생성 시점)
    VOTING("voting"), // 투표 중 (방 최소 조건 인원 달성시)
    VOTED("voted"), // 투표 완료 (방장 액션에 의해)
    CONFIRMED("confirmed"), // 약속 확정 (방자 액션에 의해)
    DONE("done"); // 약속 종료 (약속 확정 날짜 지난 경우)

    private String name;

    MeetingStatus(String name) {
        this.name = name;
    }

    public static MeetingStatus of(String meetingStatusStr) {
        if (meetingStatusStr == null) {
            throw new IllegalArgumentException("not valid argument. " + meetingStatusStr);
        }

        return Arrays.asList(MeetingStatus.values()).stream()
                .filter(actionType -> actionType.name.equals(meetingStatusStr))
                .findFirst()
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("not valid argument. " + meetingStatusStr);
                });
    }

}
