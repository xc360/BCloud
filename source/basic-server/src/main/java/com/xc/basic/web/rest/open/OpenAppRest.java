package com.xc.basic.web.rest.open;

import com.xc.api.basic.dto.AppDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.SignBean;
import com.xc.api.file.utils.FileUrlUtils;
import com.xc.basic.config.Constants;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>开放接口，应用</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"开放接口，应用"})
@RestController
public class OpenAppRest {
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;
    @Autowired
    private Constants constants;

    @ApiOperation(value = "获取应用基础信息", notes = "获取应用基础信息")
    @GetMapping("/open/app")
    public AppDto getOpenApp(@ModelAttribute SignBean signBean) {
        signBean.setAuthorityCode(BasicRestCode.getOpenApp.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        appEntity.setLogoUrl(FileUrlUtils.templateTurnUrl(appEntity.getLogoUrl()));
        return ObjectUtils.convert(new AppDto(), appEntity);
    }

    @ApiOperation(value = "刷新应用TOKEN的权限", notes = "刷新应用TOKEN的权限")
    @PutMapping("/open/app/token_authority")
    public AppDto updateOpenAppTokenAuthority(@ModelAttribute SignBean signBean) {
        signBean.setAuthorityCode(BasicRestCode.updateOpenAppTokenAuthority.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        basicAuthorizeService.updateAppTokenAuthority(appEntity);
        return ObjectUtils.convert(new AppDto(), appEntity);
    }

    @ApiOperation(value = "刷新用户授权", notes = "刷新用户授权")
    @PutMapping("/open/app/user_authorize")
    public AppDto updateOpenAppUserAuthorize(@ModelAttribute SignBean signBean) {
        signBean.setAuthorityCode(BasicRestCode.updateOpenAppUserAuthorize.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        basicAuthorizeService.updateAppUserAuthorize(appEntity);
        return ObjectUtils.convert(new AppDto(), appEntity);
    }
}
