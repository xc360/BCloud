package com.xc.basic.web.rest;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONObject;
import com.xc.api.basic.bean.PasswordBean;
import com.xc.api.basic.bean.UpdateMailBean;
import com.xc.api.basic.bean.UpdatePhoneBean;
import com.xc.api.basic.dto.*;
import com.xc.api.basic.enums.AuthorityType;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.basic.bean.QueryAuthorityBean;
import com.xc.basic.config.Constants;
import com.xc.basic.entity.*;
import com.xc.basic.enums.TreeDictType;
import com.xc.basic.service.*;
import com.xc.basic.task.BasicTask;
import com.xc.core.annotation.Authority;
import com.xc.core.bean.QueryBean;
import com.xc.core.bean.SignBean;
import com.xc.core.aspect.AuthorityHandle;
import com.xc.core.aspect.BasicConstants;
import com.xc.core.enums.CoreFailCode;
import com.xc.core.enums.EffectStatus;
import com.xc.core.enums.Whether;
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
 * <p>需要登录权限接口，我调用的开放接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，我调用的开放接口"})
@RestController
public class MyAppRest {
    @Autowired
    private BasicConstants basicConstants;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;
    @Autowired
    private AppService appService;
    @Autowired
    private TreeDictService treeDictService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private DictService dictService;
    @Autowired
    private UserService userService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private Constants constants;
    @Autowired
    private BasicService basicService;
    @Autowired
    private BasicTask basicTask;

    @ApiOperation(value = "安装配置")
    @GetMapping("/install")
    public void install(@ModelAttribute SignBean signBean, @RequestParam Map<String, String> paramMap) {
        signBean.setAuthorityCode(BasicRestCode.install.getCode());
        signBean.getSignDataObj(basicConstants.getAppSecret(), constants.getSignValidTime(), signBean.getAuthorityCode());
        String refreshAuthority = paramMap.get("refreshAuthority");
        String refreshAuthorize = paramMap.get("refreshAuthorize");
        String testData = paramMap.get("testData");
        // 刷新应用TOKEN的权限
        AppEntity appEntity = appService.verifyAppSecret(basicConstants.getAppId(), basicConstants.getAppSecret());
        if (Whether.YES.getValue().equals(refreshAuthority)) {
            basicAuthorizeService.updateAppTokenAuthority(appEntity);
            AuthorityHandle.clearToken();
        }
        // 刷新用户授权
        if (Whether.YES.getValue().equals(refreshAuthorize)) {
            basicAuthorizeService.updateAppUserAuthorize(appEntity);
        }
        // 测试数据关联
        if (Whether.YES.getValue().equals(testData)) {
            basicService.testData();
        }
    }

    @ApiOperation(value = "任务执行")
    @GetMapping("/task")
    public void install(@ModelAttribute SignBean signBean) {
        if (signBean.getAppId() == null) {
            throw CoreFailCode.SIGN_ERROR.getOperateException();
        }
        signBean.setAuthorityCode(BasicRestCode.task.getCode());
        JSONObject jsonObject = signBean.getSignDataObj(basicConstants.getAppSecret(), constants.getSignValidTime(), signBean.getAuthorityCode());
        String taskCode = jsonObject.getStr("taskCode");
        ReflectUtil.invoke(basicTask, taskCode);
    }


    @ApiOperation(value = "获取用户组集合")
    @GetMapping("/my_app/user/group_list")
    @Authority
    public List<GroupDto> getMyAppUserGroupList(TokenModel tokenModel) {
        AppEntity appEntity = appService.verifyAppSecret(basicConstants.getAppId(), basicConstants.getAppSecret());
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setAppId(appEntity.getId());
        groupEntity.setStatus(EffectStatus.VALID.getStatus());
        return groupService.getUserGroupList(tokenModel.getUserId(), new QueryBean(), groupEntity);
    }

    @ApiOperation(value = "获取区域集合", notes = "根据父级code获取区域集合，不传code获取顶级区域")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "区域标识,为null查询全部", name = "parentNode", paramType = "query"),
    })
    @GetMapping("/my_app/area_list")
    @Authority
    public List<TreeDictDto> getMyAppAreaList(@RequestParam(required = false) String firstCode) {
        AppEntity appEntity = appService.verifyAppSecret(basicConstants.getAppId(), basicConstants.getAppSecret());
        return treeDictService.getTreeDictList(appEntity.getId(), TreeDictType.AREA.getType(), firstCode);
    }

    @ApiOperation(value = "获取区域节点集合", notes = "获取区域节点集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "区域标识", name = "code", paramType = "query"),
    })
    @Authority
    @GetMapping("/my_app/area_node_list")
    public List<TreeDictDto> getMyAppAreaNodeList(@RequestParam String code) {
        AppEntity appEntity = appService.verifyAppSecret(basicConstants.getAppId(), basicConstants.getAppSecret());
        return treeDictService.getTreeNodeList(appEntity.getId(), TreeDictType.AREA.getType(), code);
    }

    @ApiOperation(value = "获取菜单集合")
    @GetMapping("/my_app/menu_list")
    @Authority
    public List<AuthorityDto> getMyAppMenuList() {
        AppEntity appEntity = appService.verifyAppSecret(basicConstants.getAppId(), basicConstants.getAppSecret());
        QueryAuthorityBean queryAuthorityBean = new QueryAuthorityBean();
        queryAuthorityBean.setType(AuthorityType.MENU.getType());
        queryAuthorityBean.setStatus(EffectStatus.VALID.getStatus());
        QueryBean queryBean = new QueryBean();
        queryBean.setSortField("seq");
        queryBean.setSortRule("ASC");
        List<AuthorityEntity> authorityEntities = authorityService.getAppAuthorityList(appEntity.getId(), queryBean, queryAuthorityBean);
        return ObjectUtils.convertList(authorityEntities, AuthorityDto::new);
    }

    @ApiOperation(value = "获取字典集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "字典类型,为null查询全部", name = "type", paramType = "query"),
    })
    @GetMapping("/my_app/dict_list")
    @Authority
    public List<DictDto> getMyAppDictList(@RequestParam(required = false) String type) {
        AppEntity appEntity = appService.verifyAppSecret(basicConstants.getAppId(), basicConstants.getAppSecret());
        DictEntity dictEntity = new DictEntity();
        dictEntity.setAppId(appEntity.getId());
        dictEntity.setType(type);
        dictEntity.setStatus(EffectStatus.VALID.getStatus());
        return dictService.getDictList(new QueryBean("seq", "ASC"), dictEntity);
    }

    @ApiOperation(value = "退出登录", notes = "删除当前我的应用的token信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true)
    })
    @DeleteMapping("/my_app/token")
    @Authority
    public void deleteMyAppToken(TokenModel tokenModel) {
        basicAuthorizeService.deleteToken(tokenModel.getAccessToken());
    }

    @ApiOperation(value = "获取用户", notes = "获取我的应用下的用户")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true)
    })
    @GetMapping("/my_app/user")
    @Authority
    public UserDto getMyAppUser(TokenModel tokenModel) {
        UserEntity userEntity = userService.getById(tokenModel.getUserId());
        UserDto userDto = ObjectUtils.convert(new UserDto(), userEntity);
        if (userEntity.getPassword() != null) {
            userDto.setPasswordExist(true);
        } else {
            userDto.setPasswordExist(false);
        }
        return userDto;
    }


    @ApiOperation(value = "修改密码", notes = "修改我的应用下的用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
    })
    @PutMapping("/my_app/user/password")
    @Authority
    public UserDto updateMyAppUserPassword(TokenModel tokenModel, @RequestBody PasswordBean passwordBean) {
        return ObjectUtils.convert(new UserDto(), userService.updateUserPassword(tokenModel.getUserId(), passwordBean));
    }

    @ApiOperation(value = "修改用户邮箱")
    @PutMapping("/my_app/user/email")
    @Authority
    public UserDto updateMyAppUserMail(TokenModel tokenModel, @RequestBody UpdateMailBean updateMailBean) {
        return ObjectUtils.convert(new UserDto(), userService.updateOpenUserMail(tokenModel.getUserId(), updateMailBean));
    }

    @ApiOperation(value = "修改用户手机号")
    @PutMapping("/my_app/user/phone")
    @Authority
    public UserDto updateMyAppUserPhone(TokenModel tokenModel, @RequestBody UpdatePhoneBean updatePhoneBean) {
        return ObjectUtils.convert(new UserDto(), userService.updateOpenUserPhone(tokenModel.getUserId(), updatePhoneBean));
    }

    @ApiOperation(value = "发送用户验证码")
    @PostMapping("/my_app/user_captcha/{messageCode}/{accountType}")
    @Authority
    public void createMyAppUserCaptcha(TokenModel tokenModel, @PathVariable String messageCode, @PathVariable String accountType) {
        AppEntity appEntity = appService.verifyAppSecret(basicConstants.getAppId(), basicConstants.getAppSecret());
        captchaService.createUserCaptcha(appEntity, tokenModel.getUserId(), messageCode, accountType);
    }

    @ApiOperation(value = "获取用户验证码信息")
    @GetMapping("/my_app/user_captcha/{messageCode}")
    @Authority
    public void getMyAppUserCaptcha(TokenModel tokenModel, @PathVariable String messageCode,
                                    @RequestParam String captcha, @RequestParam String isDelete) {
        captchaService.getUserCaptcha(tokenModel.getUserId(), messageCode, captcha, isDelete);
    }

    @ApiOperation(value = "注销账户", notes = "删除用户")
    @DeleteMapping("/my_app/user")
    @Authority
    public UserDto deleteMyAppUser(TokenModel tokenModel, @RequestParam String captcha, @RequestParam String accountType) {
        UserEntity userEntity = userService.deleteUser(tokenModel.getUserId(), captcha, accountType);
        basicAuthorizeService.deleteToken(tokenModel.getAccessToken());
        return ObjectUtils.convert(new UserDto(), userEntity);
    }

    @ApiOperation(value = "生成签名")
    @GetMapping("/my_app/upload_file/sign")
    @Authority
    public Map<String, Object> getUploadFileSign(TokenModel tokenModel) {
        return basicAuthorizeService.getUploadFileSign(tokenModel.getUserId());
    }
}
