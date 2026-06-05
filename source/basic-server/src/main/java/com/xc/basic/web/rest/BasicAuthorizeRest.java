package com.xc.basic.web.rest;

import cn.hutool.json.JSONObject;
import com.xc.basic.entity.AppEntity;
import com.xc.core.bean.SignBean;
import com.xc.api.basic.bean.UserSignBean;
import com.xc.api.basic.dto.SignDto;
import com.xc.api.basic.dto.UserSignDto;
import com.xc.basic.service.BasicAuthorizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>无需权限接口，授权验证接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"无需权限接口，授权验证接口"})
@RestController
public class BasicAuthorizeRest {
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "验证签名信息")
    @GetMapping("/verify_sign")
    public SignDto<JSONObject> verifySign(@ModelAttribute SignBean signBean) {
        SignDto<JSONObject> signDto = new SignDto<>();
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean, signDto);
        signDto.setAppId(appEntity.getAppId());
        return signDto;
    }

    @ApiOperation(value = "验证用户签名信息")
    @GetMapping("/verify_user_sign")
    public UserSignDto<JSONObject> verifyUserSign(@ModelAttribute UserSignBean userSignBean) {
        UserSignDto<JSONObject> userSignDto = new UserSignDto<>();
        basicAuthorizeService.verifyUserSign(userSignBean, userSignDto);
        return userSignDto;
    }
}
