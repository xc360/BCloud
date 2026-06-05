package com.xc.basic.web.controller;

import com.xc.api.basic.dto.VersionDto;
import com.xc.api.basic.enums.AuthorityType;
import com.xc.api.basic.enums.VersionType;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.AuthorityEntity;
import com.xc.basic.service.AppService;
import com.xc.basic.service.AuthorityService;
import com.xc.basic.service.IndexService;
import com.xc.basic.service.VersionService;
import com.xc.core.enums.Whether;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>无需权限接口，登录Controller</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"无需权限接口，登录Controller"})
@Controller
public class LoginController {

    @Autowired
    private IndexService indexService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private AppService appService;
    @Autowired
    private VersionService versionService;

    @ApiOperation(value = "单点登录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "应用ID", name = "appId", paramType = "query", required = true),
            @ApiImplicitParam(value = "客户端的状态值防止CSRF攻击", name = "state", paramType = "query", required = true),
            @ApiImplicitParam(value = "重定向地址", name = "redirectUri", paramType = "query", required = true)
    })
    @GetMapping("/oauth/login")
    public String oauthLogin(@RequestParam String appId, @RequestParam String state, @RequestParam String redirectUri, Model model) {
        AppEntity appEntity = indexService.setLoginModel(model, appId);
        // 查询用户信息权限
        if (Whether.YES.getValue().equals(appEntity.getShowUserAuthority())) {
            List<AuthorityEntity> authorityList = authorityService.getAppValidAuthorityList(appEntity.getId(), null, AuthorityType.USER_INFO.getType());
            model.addAttribute("authorityList", authorityList);
        }
        // 验证域名是否正确
        appService.verifyAppDomain(redirectUri, appEntity.getDomain());
        // 设置应用id
        model.addAttribute("appId", appId);
        // 设置从定向地址
        model.addAttribute("redirectUri", redirectUri);
        // 客户端的状态值防止CSRF攻击
        model.addAttribute("state", state);
        return "/basic/login/login";
    }


    @ApiOperation(value = "忘记密码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "应用ID", name = "appId", paramType = "query", required = true),
            @ApiImplicitParam(value = "客户端的状态值防止CSRF攻击", name = "state", paramType = "query", required = true),
            @ApiImplicitParam(value = "重定向地址", name = "redirectUri", paramType = "query", required = true)
    })
    @GetMapping("/forget")
    public String forget(Model model, @RequestParam String appId, @RequestParam String state, @RequestParam String redirectUri) {
        indexService.setLoginModel(model, appId);
        // 设置应用id
        model.addAttribute("appId", appId);
        // 设置从定向地址
        model.addAttribute("redirectUri", redirectUri);
        // 客户端的状态值防止CSRF攻击
        model.addAttribute("state", state);
        return "/basic/forget/forget";
    }

    @ApiOperation(value = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "应用ID", name = "appId", paramType = "query", required = true),
            @ApiImplicitParam(value = "客户端的状态值防止CSRF攻击", name = "state", paramType = "query", required = true),
            @ApiImplicitParam(value = "重定向地址", name = "redirectUri", paramType = "query", required = true)
    })
    @GetMapping("/register")
    public String register(Model model, @RequestParam String appId, @RequestParam String state, @RequestParam String redirectUri) {
        indexService.setLoginModel(model, appId);
        // 设置应用id
        model.addAttribute("appId", appId);
        // 设置从定向地址
        model.addAttribute("redirectUri", redirectUri);
        // 客户端的状态值防止CSRF攻击
        model.addAttribute("state", state);
        return "/basic/register/register";
    }

    @ApiOperation(value = "协议")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "协议类型", name = "type", paramType = "path"),
            @ApiImplicitParam(value = "协议版本", name = "version", paramType = "path"),
            @ApiImplicitParam(value = "应用ID", name = "appId", paramType = "query", required = true),
            @ApiImplicitParam(value = "客户端的状态值防止CSRF攻击", name = "state", paramType = "query", required = true),
            @ApiImplicitParam(value = "重定向地址", name = "redirectUri", paramType = "query", required = true)
    })
    @GetMapping(value = {"/agreement/{type}", "/privacy_agreement/{type}/{version}"})
    public String privacyAgreement(Model model, @PathVariable(required = false) String type, @PathVariable(required = false) String version,
                                   @RequestParam String appId, @RequestParam String state, @RequestParam String redirectUri) {
        AppEntity appEntity = indexService.setLoginModel(model, appId);
        // 设置应用id
        model.addAttribute("appId", appId);
        // 设置从定向地址
        model.addAttribute("redirectUri", redirectUri);
        // 客户端的状态值防止CSRF攻击
        model.addAttribute("state", state);
        // 版本处理
        VersionDto versionDto = versionService.getNewestVersion(appEntity.getId(), VersionType.WEBSITE.getType(), version);
        model.addAttribute("agreementTitle", "");
        if (versionDto != null) {
            if ("user".equals(type)) {
                model.addAttribute("agreementContent", versionDto.getUserAgreement());
                model.addAttribute("agreementTitle", appEntity.getAppName() + "用户协议");
            } else {
                model.addAttribute("agreementContent", versionDto.getPrivacyAgreement());
                model.addAttribute("agreementTitle", appEntity.getAppName() + "隐私协议");
            }
            model.addAttribute("appVersion", versionDto.getAppVersion());
        }
        // 返回的页面地址
        return "/basic/agreement/agreement";
    }
}
