package com.promise8.wwwbe.controller.v1;

import com.promise8.wwwbe.model.v1.dto.PlatformTypeV1;
import com.promise8.wwwbe.model.v1.http.BaseResponse;
import com.promise8.wwwbe.service.AppVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/versions")
@RequiredArgsConstructor
@Api(value = "AppVersionController", tags = "AppVersionController", description = "사용중인 앱의 최신 버전을 제공하는 API")
public class V1AppVersionController {
    private final AppVersionService appVersionService;
    @GetMapping("/{platformType}")
    @ApiOperation(value = "사용중인 앱의 최신 버전 조회", notes = "앱 실행 시 현재 사용중인 앱의 최신 버전을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 0, message = "조회 성공"),
            @ApiResponse(code = 500, message = "서버 에러 발생"),
            @ApiResponse(code = 1000, message = "서버 에러 발생")
    })
    public BaseResponse<String> getCurrentAppVersion(@PathVariable("platformType") PlatformTypeV1 platformType) {
        return BaseResponse.ok(appVersionService.getCurrentAppVersion(platformType));
    }
}
