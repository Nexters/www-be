package com.promise8.wwwbe.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("alpha")
class PlaceVoteRepositoryTest {

    @Autowired
    PlaceVoteRepository placeVoteRepository;
    @Test
    void getVotedUserCount() {
        long votedUserCount = placeVoteRepository.getVotedUserCount(1L);
        System.out.println(votedUserCount);
    }
}