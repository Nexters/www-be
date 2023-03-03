package com.promise8.wwwbe.v1.model.dto;

import com.promise8.wwwbe.v1.service.ThumbnailHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoDto {
    private String joinedUserName;
    private ThumbnailHelper.CharacterType characterType;
}
