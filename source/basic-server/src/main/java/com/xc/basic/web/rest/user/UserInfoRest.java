package com.xc.basic.web.rest.user;

import com.xc.basic.bean.UserInfoBean;
import com.xc.basic.config.Constants;
import com.xc.basic.dto.UserInfoDto;
import com.xc.basic.entity.UserInfoEntity;
import com.xc.basic.service.UserInfoService;
import com.xc.core.annotation.Authority;
import com.xc.core.model.TokenModel;
import com.xc.api.file.utils.FileUrlUtils;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>需要登录权限接口，用户信息</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，用户信息"})
@RestController
public class UserInfoRest {
    @Autowired
    private Constants constants;
    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/current_user/info")
    @Authority
    public UserInfoDto getCurrentUserInfo(TokenModel tokenModel) {
        UserInfoEntity userInfoEntity = userInfoService.getUserInfoByUserId(tokenModel.getUserId());
        if (userInfoEntity == null) {
            return null;
        }
        userInfoEntity.setPortrait(FileUrlUtils.templateTurnUrl(userInfoEntity.getPortrait()));
        return ObjectUtils.convert(new UserInfoDto(), userInfoEntity);
    }

    @ApiOperation(value = "修改当前用户信息")
    @PutMapping("/current_user/info")
    @Authority
    public UserInfoDto updateCurrentUserInfo(TokenModel tokenModel, @RequestBody UserInfoBean userInfoBean) {
        return userInfoService.updateUserInfo(tokenModel.getUserId(), userInfoBean);
    }
}
