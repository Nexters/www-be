package com.promise8.wwwbe.model.v1.dto.res;

import com.promise8.wwwbe.service.ThumbnailHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoDtoV1 {
    private String joinedUserName;
    private ThumbnailHelper.CharacterType characterType;
}
