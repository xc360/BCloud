package com.xc.basic.web.rest.open;

import com.xc.api.basic.bean.PasswordBean;
import com.xc.api.basic.bean.UpdateMailBean;
import com.xc.api.basic.bean.UpdatePhoneBean;
import com.xc.api.basic.bean.UserSignBean;
import com.xc.api.basic.dto.UserDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.api.file.enums.FileRestCode;
import com.xc.api.basic.bean.UserGroupBean;
import com.xc.api.basic.bean.UserRoleBean;
import com.xc.core.bean.SignBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.UserAuthorizeEntity;
import com.xc.basic.entity.UserEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.UserService;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>开放接口，用户</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"开放接口，用户"})
@RestController
public class OpenUserRest {
    @Autowired
    private AppService appService;
    @Autowired
    private UserService userService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "获取用户基础信息集合", notes = "获取用户基础信息集合，获取的是当前应用的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id集合", name = "userIds", paramType = "query", required = true),
    })
    @GetMapping("/open/user_list")
    public List<UserDto> getOpenUserList(@ModelAttribute SignBean signBean, @RequestParam(required = false) List<String> userIds) {
        signBean.setAuthorityCode(BasicRestCode.getOpenUserList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        return userService.getAppUserList(appEntity.getId(), userIds);
    }


    @ApiOperation(value = "获取用户基础信息", notes = "获取用户基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", paramType = "path", required = true)
    })
    @GetMapping("/open/user")
    public UserDto getOpenUser(@ModelAttribute SignBean signBean) {
        signBean.setAuthorityCode(BasicRestCode.getOpenUser.getCode());
        basicAuthorizeService.verifySign(signBean);
        UserEntity userEntity = userService.getById(signBean.getUserId());
        if (userEntity == null) {
            throw FailCode.USER_ID_ERROR.getOperateException();
        }
        UserDto userDto = ObjectUtils.convert(new UserDto(), userEntity);
        if (userEntity.getPassword() != null) {
            userDto.setPasswordExist(true);
        } else {
            userDto.setPasswordExist(false);
        }
        return userDto;
    }

    @ApiOperation(value = "修改用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", paramType = "path", required = true)
    })
    @PutMapping("/open/user/password")
    public UserDto updateOpenUserPassword(@ModelAttribute SignBean signBean, @RequestBody PasswordBean passwordBean) {
        signBean.setAuthorityCode(BasicRestCode.updateOpenUserPassword.getCode());
        basicAuthorizeService.verifySign(signBean);
        return ObjectUtils.convert(new UserDto(), userService.updateUserPassword(signBean.getUserId(), passwordBean));
    }

    @ApiOperation(value = "修改用户邮箱")
    @PutMapping("/open/user/email")
    public UserDto updateOpenUserMail(@ModelAttribute SignBean signBean, @RequestBody UpdateMailBean updateMailBean) {
        signBean.setAuthorityCode(BasicRestCode.updateOpenUserMail.getCode());
        basicAuthorizeService.verifySign(signBean);
        return ObjectUtils.convert(new UserDto(), userService.updateOpenUserMail(signBean.getUserId(), updateMailBean));
    }

    @ApiOperation(value = "修改用户手机号")
    @PutMapping("/open/user/phone")
    public UserDto updateOpenUserPhone(@ModelAttribute SignBean signBean, @RequestBody UpdatePhoneBean updatePhoneBean) {
        signBean.setAuthorityCode(BasicRestCode.updateOpenUserPhone.getCode());
        basicAuthorizeService.verifySign(signBean);
        return ObjectUtils.convert(new UserDto(), userService.updateOpenUserPhone(signBean.getUserId(), updatePhoneBean));
    }

    @ApiOperation(value = "根据账号获取用户基础信息", notes = "根据账号获取用户基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户账号", name = "account", paramType = "path", required = true)
    })
    @GetMapping("/open/user/account/{account}")
    public UserDto getOpenUserByAccount(@ModelAttribute SignBean signBean, @PathVariable String account) {
        signBean.setAuthorityCode(BasicRestCode.getOpenUserByAccount.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        UserEntity userEntity = userService.getUserByAccount(account);
        if (userEntity != null) {
            UserAuthorizeEntity userAuthorizeEntity = appService.getAppUser(appEntity.getId(), userEntity.getId());
            if (userAuthorizeEntity != null) {
                return ObjectUtils.convert(new UserDto(), userEntity);
            }
        }
        return null;
    }

    @ApiOperation(value = "注销账户", notes = "删除用户")
    @DeleteMapping("/open/user")
    public UserDto deleteOpenUser(@ModelAttribute SignBean signBean, @RequestParam String captcha, @RequestParam String accountType) {
        signBean.setAuthorityCode(BasicRestCode.deleteOpenUser.getCode());
        basicAuthorizeService.verifySign(signBean);
        UserEntity userEntity = userService.deleteUser(signBean.getUserId(), captcha, accountType);
        // 删除用户所有在线token
        basicAuthorizeService.deleteUserToken(signBean.getUserId());
        return ObjectUtils.convert(new UserDto(), userEntity);
    }

    @ApiOperation(value = "获取用户下载签名")
    @GetMapping("/open/user_download_sign")
    public UserSignBean getOpenUserDownloadSign(@ModelAttribute SignBean signBean) {
        signBean.setAuthorityCode(BasicRestCode.getOpenUserDownloadSign.getCode());
        basicAuthorizeService.verifySign(signBean);
        UserEntity userEntity = userService.getById(signBean.getUserId());
        if (userEntity == null) {
            throw FailCode.USER_ID_ERROR.getOperateException();
        }
        UserSignBean userSignBean = new UserSignBean(userEntity.getAccessId(), userEntity.getAccessSecret(), FileRestCode.downloadFile.getCode());
        try {
            userSignBean.setSignData(URLEncoder.encode(userSignBean.getSignData(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return userSignBean;
    }


    @ApiOperation(value = "创建用户绑定角色", notes = "创建用户绑定角色")
    @PostMapping("/open/user_bind_role")
    public void createOpenUserBindRole(@ModelAttribute SignBean signBean, @RequestBody UserRoleBean userRoleBean) {
        String userId = signBean.getUserId();
        signBean.setUserId(null);
        signBean.setAuthorityCode(BasicRestCode.createOpenUserBindRole.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        userService.createUserBindRole(appEntity.getId(), userId, userRoleBean);
    }


    @ApiOperation(value = "删除用户绑定角色", notes = "删除用户绑定角色")
    @DeleteMapping("/open/user_bind_role")
    public void deleteOpenUserBindRole(@ModelAttribute SignBean signBean, @RequestParam String roleCode) {
        String userId = signBean.getUserId();
        signBean.setUserId(null);
        signBean.setAuthorityCode(BasicRestCode.deleteOpenUserBindRole.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        userService.deleteUserBindRole(appEntity.getId(), userId, roleCode);
    }

    @ApiOperation(value = "创建用户绑定组", notes = "创建用户绑定组")
    @PostMapping("/open/user_bind_group")
    public void createOpenUserBindGroup(@ModelAttribute SignBean signBean, @RequestBody UserGroupBean userGroupBean) {
        String userId = signBean.getUserId();
        signBean.setUserId(null);
        signBean.setAuthorityCode(BasicRestCode.createOpenUserBindGroup.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        userService.createUserBindGroup(appEntity.getId(), userId, userGroupBean);
    }


    @ApiOperation(value = "删除用户绑定组", notes = "删除用户绑定组")
    @DeleteMapping("/open/user_bind_group")
    public void deleteOpenUserBindGroup(@ModelAttribute SignBean signBean, @RequestParam String groupCode) {
        String userId = signBean.getUserId();
        signBean.setUserId(null);
        signBean.setAuthorityCode(BasicRestCode.deleteOpenUserBindGroup.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        userService.deleteUserBindGroup(appEntity.getId(), userId, groupCode);
    }
}
