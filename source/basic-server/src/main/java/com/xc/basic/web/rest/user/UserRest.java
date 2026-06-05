package com.xc.basic.web.rest.user;

import com.xc.api.basic.dto.UserDto;
import com.xc.api.file.utils.FileUrlUtils;
import com.xc.basic.bean.AppUserBean;
import com.xc.basic.bean.GroupRoleBean;
import com.xc.basic.dto.AccessSecretDto;
import com.xc.basic.dto.BasicUserDto;
import com.xc.basic.dto.GroupRoleDto;
import com.xc.basic.entity.UserEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.UserService;
import com.xc.core.annotation.Authority;
import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.Md5Utils;
import com.xc.tool.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>需要登录权限接口，用户</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，用户"})
@RestController
public class UserRest {

    @Autowired
    private UserService userService;
    @Autowired
    private AppService appService;

    @ApiOperation(value = "用户分页", notes = "应用的用户分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/user_page/{current}")
    @Authority
    public PagingDto<UserDto> getAppUserPage(TokenModel tokenModel, @PathVariable Integer current, @PathVariable String appId,
                                             @ModelAttribute PagingBean pagingBean, @ModelAttribute AppUserBean appUserBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppUserPage");
        return userService.getAppUserPage(current, appId, pagingBean, appUserBean);
    }

    @ApiOperation(value = "获取用户", notes = "获取应用的用户")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @GetMapping("/app/{appId}/user/{userId}")
    @Authority
    public BasicUserDto getAppUser(TokenModel tokenModel, @PathVariable String appId, @PathVariable String userId) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppUser");
        BasicUserDto basicUserDto = userService.getAppUser(appId, userId);
        basicUserDto.setPortrait(FileUrlUtils.templateTurnUrl(basicUserDto.getPortrait()));
        return basicUserDto;
    }

    @ApiOperation(value = "获取用户的组和角色", notes = "获取应用下用户的组和角色")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "用户主键", name = "userId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/user/{userId}/group_role")
    @Authority
    public GroupRoleDto getUserGroupRole(TokenModel tokenModel, @PathVariable String appId,
                                         @PathVariable String userId) {
        appService.verifyUserHaveApp(appId, tokenModel, "getUserGroupRole");
        return userService.getUserGroupRole(appId, userId);
    }

    @ApiOperation(value = "创建用户的用户，组，角色关联", notes = "创建应用的用户，组，角色关联")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "用户主键", name = "userId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/user/{userId}/group_role")
    @Authority
    public GroupRoleDto updateUserGroupRole(TokenModel tokenModel, @PathVariable String appId,
                                            @PathVariable String userId, @RequestBody GroupRoleBean groupRoleBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "updateUserGroupRole");
        return userService.updateUserGroupRole(appId, userId, groupRoleBean);
    }

    @ApiOperation(value = "获取当前用户访问秘钥", notes = "获取当前用户访问秘钥")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
    })
    @GetMapping("/current_user/access_secret")
    @Authority
    public AccessSecretDto getCurrentUserAccessSecret(TokenModel tokenModel) {
        UserEntity userEntity = userService.getById(tokenModel.getUserId());
        AccessSecretDto accessSecretDto = new AccessSecretDto();
        accessSecretDto.setAccessSecret(userEntity.getAccessSecret());
        return accessSecretDto;
    }

    @ApiOperation(value = "更新当前用户访问秘钥", notes = "更新当前用户访问秘钥")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
    })
    @PutMapping("/current_user/access_secret")
    @Authority
    public AccessSecretDto updateCurrentUserAccessSecret(TokenModel tokenModel) {
        UserEntity userEntity = userService.getById(tokenModel.getUserId());
        userEntity.setAccessSecret(Md5Utils.getSaltMd5(StringUtils.random(10)));
        if (!userService.updateById(userEntity)) {
            throw FailCode.USER_UPDATE_FAIL.getOperateException();
        }
        AccessSecretDto accessSecretDto = new AccessSecretDto();
        accessSecretDto.setAccessSecret(userEntity.getAccessSecret());
        return accessSecretDto;
    }
}
