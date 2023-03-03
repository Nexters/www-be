package com.promise8.wwwbe.service;

import com.promise8.wwwbe.v1.model.dto.req.DynamicLinkReqDtoV1;
import com.promise8.wwwbe.v1.model.dto.res.DynamicLinkResDtoV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkService {
    private final RestTemplate restTemplate;

    @Value("${api-key.android}")
    private String androidApiKey;
    @Value("${api-key.ios}")
    private String iosApiKey;

    private static final String FIREBASE_SHORTLINK_API_URL = "https://firebasedynamiclinks.googleapis.com/v1/shortLinks";

    private static final String ANDROID_PACKAGE = "com.promiseeight.www";
    private static final String IOS_PACKAGE = "com.promise8.www";
    // TODO Fix link
    private static final String DOMAIN_URL = "https://whenwheres.page.link";
    private static final String ENDPOINT_URL = "https://whenwheres.com";

    public DynamicLinkResDtoV1 createLink(String meetingCode) {
        String shortlinkApiUrl = createShortlinkApiUrl();

        DynamicLinkReqDtoV1 dynamicLinkReqDto = createDynamicLinkReq(meetingCode);
        HttpEntity<DynamicLinkReqDtoV1> dynamicLinkReq = createDynamicLinkReq(dynamicLinkReqDto);
        DynamicLinkResDtoV1 result = restTemplate.exchange(shortlinkApiUrl, HttpMethod.POST, dynamicLinkReq, DynamicLinkResDtoV1.class).getBody();
        return result;
    }

    private HttpEntity<DynamicLinkReqDtoV1> createDynamicLinkReq(DynamicLinkReqDtoV1 dynamicLinkReqDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<DynamicLinkReqDtoV1> dynamicLinkReq = new HttpEntity<>(dynamicLinkReqDto, httpHeaders);
        return dynamicLinkReq;
    }

    private String createShortlinkApiUrl() {
        String apiKey = androidApiKey;
        UriComponentsBuilder shortlinkApiUrlBuilder = UriComponentsBuilder.fromHttpUrl(FIREBASE_SHORTLINK_API_URL);
        shortlinkApiUrlBuilder.queryParam("key", apiKey);
        return shortlinkApiUrlBuilder.toUriString();
    }

    private DynamicLinkReqDtoV1 createDynamicLinkReq(String meetingCode) {
        UriComponentsBuilder componentsBuilder = UriComponentsBuilder.fromHttpUrl(ENDPOINT_URL)
                .queryParam("meetingCode", meetingCode);

        UriComponentsBuilder dynamicLinkBuilder = UriComponentsBuilder.fromHttpUrl(DOMAIN_URL);
        dynamicLinkBuilder.queryParam("link", componentsBuilder.toUriString());
        dynamicLinkBuilder.queryParam("apn", ANDROID_PACKAGE);
        dynamicLinkBuilder.queryParam("ibi", IOS_PACKAGE);

        DynamicLinkReqDtoV1 dynamicLinkReqDto = DynamicLinkReqDtoV1.of(dynamicLinkBuilder.toUriString());
        return dynamicLinkReqDto;
    }
}
