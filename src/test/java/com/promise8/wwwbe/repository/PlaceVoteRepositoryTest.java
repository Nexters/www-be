package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.v1.repository.PlaceVoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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