package com.xc.basic.web.rest.open;

import com.xc.api.basic.bean.AppletBean;
import com.xc.api.basic.dto.AppletDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.SignBean;
import com.xc.core.dto.TokenDto;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.service.AppletService;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>开放接口，小程序</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"开放接口，小程序"})
@RestController
public class OpenAppletRest {

    @Autowired
    private BasicAuthorizeService basicAuthorizeService;
    @Autowired
    private AppletService appletService;

    @ApiOperation(value = "创建小程序token信息", notes = "开放接口，创建小程序token信息，小程序登录接口")
    @PostMapping("/open/applet/token")
    public TokenDto createOpenAppletToken(@ModelAttribute SignBean signBean, @RequestBody AppletBean appletBean) {
        signBean.setAuthorityCode(BasicRestCode.createOpenAppletToken.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        TokenModel tokenModel = basicAuthorizeService.createAppletToken(appEntity, appletBean);
        return ObjectUtils.convert(new TokenDto(), tokenModel);
    }

    @ApiOperation(value = "获取小程序信息", notes = "开放接口，获取小程序信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "小程序标识", name = "appletType", paramType = "path"),
            @ApiImplicitParam(value = "小程序id", name = "appletId", paramType = "path"),
    })
    @GetMapping("/open/applet/{appletType}/applet/{appletId}")
    public AppletDto getOpenAppletByApplet(@ModelAttribute SignBean signBean, @PathVariable String appletType, @PathVariable String appletId) {
        signBean.setAuthorityCode(BasicRestCode.getOpenAppletByApplet.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        return appletService.getAppAppletByCode(appEntity.getId(), appletType, appletId);
    }

    @ApiOperation(value = "获取用户的小程序信息", notes = "开放接口，获取用户的小程序信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "小程序标识", name = "appletType", paramType = "path"),
            @ApiImplicitParam(value = "用户主键", name = "userId", paramType = "path"),
    })
    @GetMapping("/open/applet/{appletType}/user/{userId}")
    public AppletDto getOpenAppletByUser(@ModelAttribute SignBean signBean, @PathVariable String appletType, @PathVariable String userId) {
        signBean.setAuthorityCode(BasicRestCode.getOpenAppletByUser.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        return appletService.getAppAppletByUser(appEntity.getId(), appletType, userId);
    }
}
