package com.example.demo.web.overt;

import com.xc.api.basic.BasicApi;
import com.xc.api.basic.dto.AppDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.annotation.Authority;
import com.xc.core.aspect.AuthorityHandle;
import com.xc.core.aspect.BasicConstants;
import com.xc.core.bean.SignBean;
import com.xc.core.dto.TokenDto;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>我的授权,调用开放接口,无需权限</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"我的授权,调用开放接口,无需权限"})
@RestController
public class OvertMyAppRest {
    @Autowired
    private BasicConstants basicConstants;
    @Autowired
    private BasicApi basicApi;

    @ApiOperation(value = "获取token")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "授权code", name = "code", paramType = "query", required = true),
    })
    @GetMapping("/my_app/token")
    public TokenDto getMyAppToken(@RequestParam(required = false) String code, @RequestParam(required = false) String accessToken) {
        SignBean signBean = new SignBean(basicConstants.getAppId(), basicConstants.getAppSecret(), BasicRestCode.getOpenToken.getCode());
        TokenDto tokenDto = basicApi.getOpenToken(signBean, code, accessToken);
        AuthorityHandle.setToken(tokenDto.getAccessToken(), ObjectUtils.convert(new TokenModel(), tokenDto));
        return tokenDto;
    }

    @GetMapping("/home")
    @Authority(verifyAuthority = false)
    public AppDto home() {

        SignBean signBean = new SignBean(basicConstants.getAppId(), basicConstants.getAppSecret(), BasicRestCode.getOpenApp.getCode());
        return basicApi.getOpenApp(signBean);
    }
}