package com.xc.basic.web.rest.open;


import com.xc.api.basic.bean.ForgetBean;
import com.xc.api.basic.bean.RegisterBean;
import com.xc.api.basic.dto.LoginDto;
import com.xc.api.basic.dto.VerifyAccountDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.SignBean;
import com.xc.api.basic.bean.LoginBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.service.BasicAuthorizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>开放接口，登录相关接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"开放接口，登录相关接口"})
@RestController
public class OpenLoginRest {

    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "获取授权码")
    @PostMapping("/open/oauth/token")
    public LoginDto createOpenToken(@ModelAttribute SignBean signBean, @RequestBody LoginBean loginBean) {
        signBean.setAuthorityCode(BasicRestCode.createOpenToken.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        return basicAuthorizeService.login(appEntity, loginBean, true);
    }

    @ApiOperation(value = "注册,邮箱和手机号必填一个")
    @PostMapping("/open/register")
    public void createOpenRegister(@ModelAttribute SignBean signBean, @RequestBody RegisterBean registerBean) {
        signBean.setAuthorityCode(BasicRestCode.createOpenRegister.getCode());
        basicAuthorizeService.verifySign(signBean);
        basicAuthorizeService.register(registerBean);
    }

    @ApiOperation(value = "找回密码，可以通过手机号，微信code，邮箱找回")
    @PostMapping("/open/forget_password")
    public void createOpenForgetPassword(@ModelAttribute SignBean signBean, @RequestBody ForgetBean forgetBean) {
        signBean.setAuthorityCode(BasicRestCode.createOpenForgetPassword.getCode());
        basicAuthorizeService.verifySign(signBean);
        basicAuthorizeService.forgetPassword(forgetBean);
    }

    @ApiOperation(value = "验证账号")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "账号类型", name = "accountType", paramType = "path", required = true),
    })
    @GetMapping("/open/verify_account/{accountType}")
    public VerifyAccountDto getOpenVerifyAccount(@ModelAttribute SignBean signBean, @PathVariable String accountType, @RequestParam String account) {
        signBean.setAuthorityCode(BasicRestCode.getOpenVerifyAccount.getCode());
        basicAuthorizeService.verifySign(signBean);
        return basicAuthorizeService.verifyAccount(accountType, account);
    }
}
