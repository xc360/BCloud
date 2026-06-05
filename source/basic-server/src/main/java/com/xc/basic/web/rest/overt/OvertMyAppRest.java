package com.xc.basic.web.rest.overt;

import com.xc.api.basic.bean.CaptchaBean;
import com.xc.api.basic.bean.ForgetBean;
import com.xc.api.basic.bean.RegisterBean;
import com.xc.api.basic.dto.*;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.ImageCaptchaDto;
import com.xc.core.dto.TokenDto;
import com.xc.basic.bean.MyAppLoginBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.InfoEntity;
import com.xc.basic.service.AppService;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.CaptchaService;
import com.xc.basic.service.InfoService;
import com.xc.core.aspect.BasicConstants;
import com.xc.core.enums.EffectStatus;
import com.xc.api.basic.enums.InfoType;
import com.xc.core.model.ClientIpModel;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>无需权限接口，我调用的开放接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"无需权限接口，我调用的开放接口"})
@RestController
public class OvertMyAppRest {
    @Autowired
    private BasicConstants basicConstants;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;
    @Autowired
    private InfoService infoService;
    @Autowired
    private AppService appService;
    @Autowired
    private CaptchaService captchaService;

    @ApiOperation(value = "获取应用信息")
    @GetMapping("/my_app")
    public Map<String, Object> getMyApp() {
        // 查询app
        AppEntity appEntity = appService.verifyAppSecret(basicConstants.getAppId(), basicConstants.getAppSecret());
        AppDto appDto = ObjectUtils.convert(new AppDto(), appEntity);
        // 查询APP信息
        InfoEntity infoEntity = new InfoEntity();
        infoEntity.setAppId(appDto.getId());
        infoEntity.setType(InfoType.CLIENT_INFO.getType());
        infoEntity.setStatus(EffectStatus.VALID.getStatus());
        List<InfoDto> infoList = infoService.getInfoList(new QueryBean(), infoEntity);
        //数据转换
        Map<String, Object> objectMap = ObjectUtils.convertMap(appDto);
        for (InfoDto infoDto : infoList) {
            objectMap.put(infoDto.getKey(), infoDto.getValue());
        }
        return objectMap;
    }

    @ApiOperation(value = "获取token")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "授权code", name = "code", paramType = "query"),
            @ApiImplicitParam(value = "用户token", name = "accessToken", paramType = "query"),
    })
    @GetMapping("/my_app/token")
    public TokenDto getMyAppToken(@RequestParam(required = false) String code, @RequestParam(required = false) String accessToken) {
        AppEntity appEntity = appService.verifyAppSecret(basicConstants.getAppId(), basicConstants.getAppSecret());
        TokenModel tokenModel = basicAuthorizeService.getToken(appEntity, code, accessToken);
        return ObjectUtils.convert(new TokenDto(), tokenModel);
    }

    @ApiOperation(value = "刷新token信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "刷新token", name = "refreshToken", paramType = "path", required = true),
    })
    @PutMapping("/my_app/token/{refreshToken}")
    public TokenDto updateMyAppToken(@PathVariable String refreshToken) {
        TokenModel tokenModel = basicAuthorizeService.updateToken(refreshToken);
        return ObjectUtils.convert(new TokenDto(), tokenModel);
    }

    @ApiOperation(value = "发送验证码")
    @PostMapping("/my_app/captcha/{messageCode}/{accountType}")
    public CaptchaDto createMyAppCaptcha(ClientIpModel clientIpModel, @PathVariable String messageCode, @PathVariable String accountType,
                                         @RequestBody CaptchaBean captchaBean, @RequestParam(required = false) String appId) {
        AppEntity appEntity;
        if (appId != null) {
            appEntity = appService.getValidAppByAppId(appId);
        } else {
            appEntity = appService.verifyAppSecret(basicConstants.getAppId(), basicConstants.getAppSecret());
        }
        captchaBean.setClientIp(clientIpModel.getClientIp());
        return captchaService.createCaptcha(appEntity, messageCode, accountType, captchaBean);
    }

    @ApiOperation(value = "获取验证码信息")
    @GetMapping("/my_app/captcha/{messageCode}")
    public void getMyAppCaptcha(@PathVariable String messageCode, @RequestParam String account, @RequestParam String captcha, @RequestParam String isDelete) {
        captchaService.getCaptcha(account, messageCode, captcha, isDelete);
    }

    @ApiOperation(value = "获取授权码")
    @PostMapping("/my_app/oauth/token")
    public LoginDto createMyAppToken(@RequestBody MyAppLoginBean myAppLoginBean) {
        AppEntity appEntity = appService.getValidAppByAppId(myAppLoginBean.getAppId());
        return basicAuthorizeService.login(appEntity, myAppLoginBean, true);
    }

    @ApiOperation(value = "注册,邮箱和手机号必填一个")
    @PostMapping("/my_app/register")
    public void createMyAppRegister(@RequestBody RegisterBean registerBean) {
        basicAuthorizeService.register(registerBean);
    }

    @ApiOperation(value = "找回密码，可以通过手机号，微信code，邮箱找回")
    @PostMapping("/my_app/forget_password")
    public void createMyAppForgetPassword(@RequestBody ForgetBean forgetBean) {
        basicAuthorizeService.forgetPassword(forgetBean);
    }

    @ApiOperation(value = "验证账号")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "账号类型", name = "accountType", paramType = "path", required = true),
            @ApiImplicitParam(value = "账号", name = "account", paramType = "query", required = true),
    })
    @GetMapping("/my_app/verify_account/{accountType}")
    public VerifyAccountDto getMyAppVerifyAccount(@PathVariable String accountType, @RequestParam String account) {
        return basicAuthorizeService.verifyAccount(accountType, account);
    }

    @ApiOperation(value = "获取图片验证码")
    @GetMapping("/my_app/img_captcha")
    public ImageCaptchaDto getMyAppImgCaptcha() {
        return captchaService.getImgCaptcha(null);
    }
}
