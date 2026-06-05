package com.xc.basic.web.rest.open;

import com.xc.api.basic.enums.BasicRestCode;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.core.bean.SignBean;
import com.xc.core.dto.TokenDto;
import com.xc.core.enums.CoreFailCode;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>开放接口，token</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"开放接口，token"})
@RestController
public class OpenTokenRest {
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "获取token信息", notes = "登录后使用code来换取token信息，获取的是当前应用的数据")
    @GetMapping("/open/token")
    public TokenDto getOpenToken(@ModelAttribute SignBean signBean,
                                 @RequestParam(required = false) String code,
                                 @RequestParam(required = false) String accessToken) {
        signBean.setAuthorityCode(BasicRestCode.getOpenToken.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        TokenModel tokenModel = basicAuthorizeService.getToken(appEntity, code, accessToken);
        return ObjectUtils.convert(new TokenDto(), tokenModel);
    }

    @ApiOperation(value = "删除token信息", notes = "删除服务器的token信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true)
    })
    @DeleteMapping("/open/token")
    public void deleteOpenToken(@ModelAttribute SignBean signBean, @RequestParam(required = false) String token,
                                @RequestParam(required = false) String tokenUserId) {
        signBean.setAuthorityCode(BasicRestCode.deleteOpenToken.getCode());
        basicAuthorizeService.verifySign(signBean);
        if (tokenUserId != null) {
            // 删除用户所有在线token
            basicAuthorizeService.deleteUserToken(tokenUserId);
        } else if (token != null) {
            basicAuthorizeService.deleteToken(token);
        } else {
            throw CoreFailCode.TOKEN_CANNOT_BE_EMPTY.getOperateException();
        }
    }

    @ApiOperation(value = "更新token信息", notes = "更新服token,并且获取新的token")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "刷新token", name = "refreshToken", paramType = "path", required = true),
    })
    @PutMapping("/open/token/{refreshToken}")
    public TokenDto updateOpenToken(@RequestBody SignBean signBean, @PathVariable String refreshToken) {
        signBean.setAuthorityCode(BasicRestCode.updateOpenToken.getCode());
        basicAuthorizeService.verifySign(signBean);
        TokenModel tokenModel = basicAuthorizeService.updateToken(refreshToken);
        return ObjectUtils.convert(new TokenDto(), tokenModel);
    }
}
