package com.xc.basic.web.rest.open.user;

import com.xc.api.basic.dto.VersionDto;
import com.xc.api.basic.bean.UserSignBean;
import com.xc.api.basic.dto.UserSignDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.api.basic.dto.UserVersionDto;
import com.xc.basic.entity.UserEntity;
import com.xc.basic.entity.VersionEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.VersionService;
import com.xc.api.file.utils.FileUrlUtils;
import com.xc.core.aspect.BasicConstants;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>开放接口，用户的应用版本</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"开放接口，用户的应用版本"})
@RestController
public class OpenUserVersionRest {

    @Autowired
    private BasicAuthorizeService basicAuthorizeService;
    @Autowired
    private VersionService versionService;
    @Autowired
    private AppService appService;
    @Autowired
    private BasicConstants basicConstants;

    @ApiOperation(value = "获取开放的用户的应用的版本集合", notes = "获取公开版本分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header"),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "query", required = true),
    })
    @GetMapping("/open/user/app/{appId}/version_list")
    public List<UserVersionDto> getOpenUserAppVersionList(@ModelAttribute UserSignBean userSignBean, @PathVariable String appId) {
        userSignBean.setAuthorityCode(BasicRestCode.getOpenUserAppVersionList.getCode());
        userSignBean.setMyAppId(basicConstants.getAppId());
        UserEntity userEntity = basicAuthorizeService.verifyUserSign(userSignBean, new UserSignDto<>());
        appService.verifyAppIsOpen(appId, userEntity.getId());
        // 查询分页
        VersionEntity versionEntity = new VersionEntity();
        versionEntity.setAppId(appId);
        List<UserVersionDto> versionList = versionService.getOvertVersionList(versionEntity);
        for (UserVersionDto userVersionDto : versionList) {
            userVersionDto.setPackageUrl(FileUrlUtils.templateTurnUrl(userVersionDto.getPackageUrl()));
        }
        return versionList;
    }


    @ApiOperation(value = "获取开放的用户的应用的版本信息", notes = "获取开放的用户的应用的版本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header"),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "query", required = true),
            @ApiImplicitParam(value = "版本号", name = "version", paramType = "query"),
    })
    @GetMapping("/open/user/app/{appId}/version")
    public UserVersionDto getOpenUserAppVersion(@ModelAttribute UserSignBean userSignBean, @PathVariable String appId,
                                                @RequestParam String type, @RequestParam(required = false) String version) {
        userSignBean.setAuthorityCode(BasicRestCode.getOpenUserAppVersion.getCode());
        userSignBean.setMyAppId(basicConstants.getAppId());
        UserEntity userEntity = basicAuthorizeService.verifyUserSign(userSignBean, new UserSignDto<>());
        appService.verifyAppIsOpen(appId, userEntity.getId());
        // 查询版本信息
        VersionDto versionDto = versionService.getNewestVersion(appId, type, version);
        if (versionDto == null) {
            throw FailCode.APP_VERSION_DOC_NOT_EXIST.getOperateException();
        }
        return ObjectUtils.convert(new UserVersionDto(), versionDto);
    }
}
