package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.PlaceVoteEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserVoteResDto {
    private String userName;
    private String promisePlace;

    public static UserVoteResDto of(PlaceVoteEntity placeVoteEntity) {
        return UserVoteResDto.builder()
                .userName(placeVoteEntity.getUserEntity().getUserName())
                .promisePlace(placeVoteEntity.getMeetingPlaceEntity().getPromisePlace())
                .build();
    }
}
