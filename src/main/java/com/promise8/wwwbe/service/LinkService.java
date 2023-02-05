package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.DynamicLinkReqDto;
import com.promise8.wwwbe.model.dto.DynamicLinkResDto;
import com.promise8.wwwbe.model.dto.PlatformType;
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

    public DynamicLinkResDto createLink(PlatformType platformType, String endpointUrl) {
        String shortlinkApiUrl = createShortlinkApiUrl(platformType);

        DynamicLinkReqDto dynamicLinkReqDto = createDynamicLinkReq(platformType, endpointUrl);
        HttpEntity<DynamicLinkReqDto> dynamicLinkReq = createDynamicLinkReq(dynamicLinkReqDto);
        DynamicLinkResDto result = restTemplate.exchange(shortlinkApiUrl, HttpMethod.POST, dynamicLinkReq, DynamicLinkResDto.class).getBody();
        return result;
    }

    private HttpEntity<DynamicLinkReqDto> createDynamicLinkReq(DynamicLinkReqDto dynamicLinkReqDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<DynamicLinkReqDto> dynamicLinkReq = new HttpEntity<>(dynamicLinkReqDto, httpHeaders);
        return dynamicLinkReq;
    }

    private String createShortlinkApiUrl(PlatformType platformType) {
        String apiKey = PlatformType.ANDROID.equals(platformType) ? androidApiKey : iosApiKey;
        UriComponentsBuilder shortlinkApiUrlBuilder = UriComponentsBuilder.fromHttpUrl(FIREBASE_SHORTLINK_API_URL);
        shortlinkApiUrlBuilder.queryParam("key", apiKey);
        return shortlinkApiUrlBuilder.toUriString();
    }

    private DynamicLinkReqDto createDynamicLinkReq(PlatformType platformType, String endpointUrl) {
        UriComponentsBuilder dynamicLinkBuilder = UriComponentsBuilder.fromHttpUrl(DOMAIN_URL);
        dynamicLinkBuilder.queryParam("link", endpointUrl);

        if(PlatformType.ANDROID.equals(platformType)) {
            dynamicLinkBuilder.queryParam("apn", ANDROID_PACKAGE);
        } else {
            dynamicLinkBuilder.queryParam("ibi", IOS_PACKAGE);
        }
        DynamicLinkReqDto dynamicLinkReqDto = DynamicLinkReqDto.of(dynamicLinkBuilder.toUriString());
        return dynamicLinkReqDto;
    }
}
