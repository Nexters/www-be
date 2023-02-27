package com.promise8.wwwbe.repository;

import com.github.javafaker.Faker;
import com.promise8.wwwbe.model.dto.PromiseTime;
import com.promise8.wwwbe.model.entity.*;
import com.promise8.wwwbe.service.LinkService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
@ActiveProfiles("alpha")
public class DataSample {
    static Faker fakerKo = new Faker(new Locale("ko"));
    static Faker fakerEng = new Faker();
    private final String[] meetingNameList = {"프로미스8 최종발표 회의", "넥나잇은 언제하나요", "넥스터즈 23기 신입 환영회", "프로미스8 기획 회의", "일단 모여", "송별회", "프로미스8 중간발표 회의", "안드로이드 스터디 1회차", "iOS 스터디 2회차", "SpringBoot 스터디 3회차"};
    private final int MEETING_CODE_LENGTH = 6;
    @Autowired
    LinkService linkService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    MeetingUserRepository meetingUserRepository;
    @Autowired
    MeetingUserTimetableRepository meetingUserTimetableRepository;
    @Autowired
    MeetingPlaceRepository meetingPlaceRepository;
    @Autowired
    PlaceVoteRepository placeVoteRepository;
    UserEntity user = new UserEntity();
    MeetingEntity meeting = new MeetingEntity();
    MeetingUserEntity meetingUser = new MeetingUserEntity();
    MeetingUserTimetableEntity meetingUserTimetable = new MeetingUserTimetableEntity();
    MeetingPlaceEntity meetingPlace = new MeetingPlaceEntity();
    PlaceVoteEntity placeVote = new PlaceVoteEntity();
    Random random = new Random();
    List<String> codeList = new ArrayList<>();
    List<Long> meetingPlaceIdList = new ArrayList<>();

//    @Test
//    void saveDataList() {
//        int tc = 10;
//        while (tc-- > 0) {
//            String userName = fakerKo.name().fullName().replace(" ", "");
//            setCreator(userName);
//            setWaitingMeeting(tc);
//            setMeetingUser(userName);
//            setMeetingUserTimetable();
//            setMeetingPlace();
//            setPlaceVote();
//            initId();
//        }
//
//        for (String code : codeList) {
//            System.out.println(code);
//        }
//    }

    private void initId() {
        user.setUserId(null);
        meeting.setMeetingId(null);
        meetingUser.setMeetingUserId(null);
        meetingUserTimetable.setMeetingUserTimetableId(null);
        meetingPlace.setMeetingPlaceId(null);
        placeVote.setPlaceVoteId(null);
    }

    private void setCreator(String userName) {
        user.setFcmToken(fakerEng.random().hex(163));
        user.setUserName(userName);
        user.setDeviceId(fakerEng.random().hex(16));
        user.setIsAlarmOn(false);
        userRepository.save(user);
    }

    private void setWaitingMeeting(int tc) {
        long cnt = random.nextInt(19) + 2;
        String meetingCode = getMeetingCode();
        codeList.add(meetingCode);
        meeting.setConditionCount(cnt);
        meeting.setStartDate(LocalDate.now());
        meeting.setEndDate(LocalDate.now().plusDays(13L));
        meeting.setMeetingCode(meetingCode);
        meeting.setMeetingName(meetingNameList[tc]);
        meeting.setMeetingStatus(MeetingStatus.WAITING);
        meeting.setShortLink(linkService.createLink(meetingCode).getShortLink());
        meeting.setCreator(user);
        meetingRepository.save(meeting);
    }

    private String getMeetingCode() {
        String code = RandomStringUtils.random(MEETING_CODE_LENGTH, true, false);
        while (true) {
            String existMeetingCode = meetingRepository.isExistMeetingCode(code);
            if (existMeetingCode == null) {
                return code;
            }
        }
    }

    private void setMeetingUser(String userName) {
        meetingUser.setMeetingEntity(meeting);
        meetingUser.setUserEntity(user);
        meetingUser.setMeetingUserName(userName);
        meetingUserRepository.save(meetingUser);
    }

    private void setMeetingUserTimetable() {
        LocalDate promiseDate = null;
        PromiseTime promiseTime = null;

        HashSet<String> existPromiseTime = new HashSet<>();
        int pickCnt = 20;
        while (pickCnt-- > 0) {
            while (true) {
                promiseDate = LocalDate.now().plusDays(random.nextInt(14));
                promiseTime = PromiseTime.values()[random.nextInt(4)];
                String dateTime = promiseDate.toString() + promiseTime;
                if (!existPromiseTime.contains(dateTime)) {
                    existPromiseTime.add(dateTime);
                    break;
                }
            }
            meetingUserTimetable.setMeetingUserEntity(meetingUser);
            meetingUserTimetable.setIsConfirmed(false);
            meetingUserTimetable.setPromiseDate(promiseDate);
            meetingUserTimetable.setPromiseTime(promiseTime);
            meetingUserTimetableRepository.save(meetingUserTimetable);
            meetingUserTimetable.setMeetingUserTimetableId(null);
        }
    }

    private void setMeetingPlace() {
        HashSet<String> existPlace = new HashSet<>();
        String place = null;
        int pickCnt = 3;
        while (pickCnt-- > 0) {
            while (true) {
                place = fakerKo.address().streetName().replace(" ", "");
                if (!existPlace.contains(place)) {
                    existPlace.add(place);
                    break;
                }
            }
            meetingPlace.setPromisePlace(place);
            meetingPlace.setMeetingUserEntity(meetingUser);
            meetingPlace.setIsConfirmed(false);
            meetingPlaceRepository.save(meetingPlace);
            meetingPlaceIdList.add(meetingPlace.getMeetingPlaceId());
            meetingPlace.setMeetingPlaceId(null);
        }
    }

    private void setPlaceVote() {
        int pickCnt = random.nextInt(3);
        for (int i = 0; i < 3; i++) {
            if (i == pickCnt) continue;

            placeVote.setMeetingUserEntity(meetingUser);
            placeVote.setMeetingPlaceEntity(meetingPlaceRepository.findById(meetingPlaceIdList.get(i)).get());
            placeVoteRepository.save(placeVote);
            placeVote.setPlaceVoteId(null);
        }

        meetingPlaceIdList.clear();
    }
}
