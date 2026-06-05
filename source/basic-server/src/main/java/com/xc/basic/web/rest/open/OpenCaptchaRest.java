package com.xc.basic.web.rest.open;

import com.xc.api.basic.bean.CaptchaBean;
import com.xc.api.basic.dto.CaptchaDto;
import com.xc.core.dto.ImageCaptchaDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.SignBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>开放接口，验证码</p>
 *
 * @author xc
 * @version v1.0.0
 */
@RestController
@Api(tags = {"开放接口，验证码"})
public class OpenCaptchaRest {

    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "发送用户验证码", notes = "开放接口，发送用户验证码")
    @PostMapping("/open/user_captcha/{messageCode}/{accountType}")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "消息标识", name = "messageCode", paramType = "path"),
            @ApiImplicitParam(value = "账号类型", name = "accountType", paramType = "path")
    })
    public void createOpenUserCaptcha(@ModelAttribute SignBean signBean, @PathVariable String messageCode, @PathVariable String accountType) {
        signBean.setAuthorityCode(BasicRestCode.createOpenUserCaptcha.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        captchaService.createUserCaptcha(appEntity, signBean.getUserId(), messageCode, accountType);
    }

    @ApiOperation(value = "获取用户验证码信息", notes = "开放接口，获取用户验证码信息，验证验证码")
    @GetMapping("/open/user_captcha/{messageCode}")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "消息标识", name = "messageCode", paramType = "path"),
            @ApiImplicitParam(value = "验证码", name = "captcha", paramType = "query"),
            @ApiImplicitParam(value = "是否删除验证码", name = "isDelete", paramType = "query"),
    })
    public void getOpenUserCaptcha(@ModelAttribute SignBean signBean, @PathVariable String messageCode,
                                   @RequestParam String captcha, @RequestParam String isDelete) {
        signBean.setAuthorityCode(BasicRestCode.getOpenUserCaptcha.getCode());
        basicAuthorizeService.verifySign(signBean);
        captchaService.getUserCaptcha(signBean.getUserId(), messageCode, captcha, isDelete);
    }


    @ApiOperation(value = "发送验证码", notes = "开放接口，发送验证码")
    @PostMapping("/open/captcha/{messageCode}/{accountType}")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "消息标识", name = "messageCode", paramType = "path"),
            @ApiImplicitParam(value = "账号类型", name = "accountType", paramType = "path")
    })
    public CaptchaDto createOpenCaptcha(@ModelAttribute SignBean signBean, @PathVariable String messageCode,
                                        @PathVariable String accountType, @RequestBody CaptchaBean captchaBean) {
        signBean.setAuthorityCode(BasicRestCode.createOpenCaptcha.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        return captchaService.createCaptcha(appEntity, messageCode, accountType, captchaBean);
    }

    @ApiOperation(value = "获取验证码信息", notes = "开放接口，获取验证码信息，验证验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "消息标识", name = "messageCode", paramType = "path"),
            @ApiImplicitParam(value = "账号", name = "account", paramType = "query"),
            @ApiImplicitParam(value = "验证码", name = "captcha", paramType = "query"),
            @ApiImplicitParam(value = "是否删除验证码", name = "isDelete", paramType = "query"),
    })
    @GetMapping("/open/captcha/{messageCode}")
    public void getOpenCaptcha(@ModelAttribute SignBean signBean, @PathVariable String messageCode,
                               @RequestParam String account, @RequestParam String captcha, @RequestParam String isDelete) {
        signBean.setAuthorityCode(BasicRestCode.getOpenCaptcha.getCode());
        basicAuthorizeService.verifySign(signBean);
        captchaService.getCaptcha(account, messageCode, captcha, isDelete);
    }

    @ApiOperation(value = "获取图片验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "验证码", name = "captcha", paramType = "query")
    })
    @GetMapping("/open/img_captcha")
    public ImageCaptchaDto getOpenImgCaptcha(@ModelAttribute SignBean signBean, @RequestParam(required = false) String captcha) {
        signBean.setAuthorityCode(BasicRestCode.getOpenImgCaptcha.getCode());
        basicAuthorizeService.verifySign(signBean);
        return captchaService.getImgCaptcha(captcha);
    }
}
