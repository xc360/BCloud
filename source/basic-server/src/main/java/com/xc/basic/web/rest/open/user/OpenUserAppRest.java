package com.xc.basic.web.rest.open.user;

import com.xc.api.basic.bean.UserSignBean;
import com.xc.api.basic.dto.UserSignDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.api.basic.dto.UserAppDto;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.UserEntity;
import com.xc.basic.service.AppService;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.api.file.utils.FileUrlUtils;
import com.xc.core.aspect.BasicConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>开放接口，用户的应用</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"开放接口，用户的应用"})
@RestController
public class OpenUserAppRest {
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;
    @Autowired
    private BasicConstants basicConstants;
    @Autowired
    private AppService appService;

    @ApiOperation(value = "获取用户的应用信息集合", notes = "获取用户的应用信息集合")
    @GetMapping("/open/user/app_list")
    public List<UserAppDto> getOpenUserAppList(@ModelAttribute UserSignBean userSignBean,
                                               @RequestParam(required = false) String versionType) {
        userSignBean.setAuthorityCode(BasicRestCode.getOpenUserAppList.getCode());
        userSignBean.setMyAppId(basicConstants.getAppId());
        UserEntity userEntity = basicAuthorizeService.verifyUserSign(userSignBean, new UserSignDto<>());
        List<UserAppDto> appList = appService.getAppListByUserId(userEntity.getId(), new AppEntity(), versionType);
        for (UserAppDto userAppDto : appList) {
            userAppDto.setLogoUrl(FileUrlUtils.templateTurnUrl(userAppDto.getLogoUrl()));
        }
        return appList;
    }
}
