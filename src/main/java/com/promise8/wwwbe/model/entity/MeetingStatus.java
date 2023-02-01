package com.promise8.wwwbe.model.entity;

public enum MeetingStatus {
    WAITING, // 투표 시작 전 (생성 시점)
    VOTING, // 투표 중 (방 최소 조건 인원 달성시)
    VOTED, // 투표 완료 (방장 액션에 의해)
    CONFIRMED, // 약속 확정 (방자 액션에 의해)
    DONE; // 약속 종료 (약속 확정 날짜 지난 경우)
}
