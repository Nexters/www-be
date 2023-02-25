package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.service.ThumbnailHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoDto {
    private String joinedUserName;
    private ThumbnailHelper.CharacterType characterType;
}
