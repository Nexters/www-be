package com.promise8.wwwbe.v1.model.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DynamicLinkResDtoV1 {
    private String previewLink;
    private String shortLink;
}
