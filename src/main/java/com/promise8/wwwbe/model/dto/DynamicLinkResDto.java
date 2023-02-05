package com.promise8.wwwbe.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DynamicLinkResDto {
    private String previewLink;
    private String shortLink;
}
