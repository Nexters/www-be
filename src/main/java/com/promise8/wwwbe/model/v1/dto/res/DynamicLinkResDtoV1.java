package com.promise8.wwwbe.model.v1.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DynamicLinkResDtoV1 {
    private String previewLink;
    private String shortLink;
}
