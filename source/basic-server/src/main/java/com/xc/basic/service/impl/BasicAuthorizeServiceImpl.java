package com.xc.basic.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.xc.api.basic.bean.*;
import com.xc.api.basic.dto.*;
import com.xc.api.basic.enums.AuthorityType;
import com.xc.api.file.bean.DiskSignBean;
import com.xc.api.file.config.FileConstants;
import com.xc.api.file.enums.FileRestCode;
import com.xc.basic.config.Constants;
import com.xc.basic.entity.*;
import com.xc.basic.enums.*;
import com.xc.basic.mapper.*;
import com.xc.basic.model.CodeModel;
import com.xc.basic.service.*;
import com.xc.core.aspect.AuthorityHandle;
import com.xc.core.bean.SignBean;
import com.xc.core.aspect.BasicConstants;
import com.xc.core.enums.AuditStatus;
import com.xc.core.enums.CoreFailCode;
import com.xc.core.enums.EffectStatus;
import com.xc.core.enums.Whether;
import com.xc.core.model.GroupModel;
import com.xc.core.model.TokenModel;
import com.xc.core.utils.RedisUtils;
import com.xc.tool.utils.JSONUtils;
import com.xc.tool.utils.Md5Utils;
import com.xc.tool.utils.ObjectUtils;
import com.xc.tool.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * <p>授权实现</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Slf4j
@Service
public class BasicAuthorizeServiceImpl implements BasicAuthorizeService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Constants constants;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private AppService appService;
    @Autowired
    private UserAuthorizeMapper userAuthorizeMapper;
    @Autowired
    private UserAuthorizeAuthorityMapper userAuthorizeAuthorityMapper;
    @Autowired
    private UserGroupMapper userGroupMapper;
    @Autowired
    private BasicConstants basicConstants;
    @Autowired
    private GroupService groupService;
    @Lazy
    @Autowired
    private UserService userService;
    @Autowired
    private GroupRoleMapper groupRoleMapper;
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private AppletService appletService;
    @Autowired
    private DeletedUserService deletedUserService;
    @Autowired
    private FileConstants fileConstants;
    @Autowired
    private AuthorizeService authorizeService;
    @Autowired
    private UserAuthorizeService userAuthorizeService;

    /**
     * 刷新权限线程池
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);


    @Override
    public LoginDto login(AppEntity appEntity, LoginBean loginBean, boolean openCaptcha) {
        if (loginBean.getAccount() == null) {
            throw FailCode.USER_ACCOUNT_NOT_NULL.getOperateException();
        }
        // 手机号登录
        if (AccountType.PHONE.getType().equals(loginBean.getAccountType()) ||
                AccountType.EMAIL.getType().equals(loginBean.getAccountType())) {
            // 验证验证码
            String key = MessageCode.EMAIL_LOGIN.getCode() + loginBean.getAccount();
            if (AccountType.PHONE.getType().equals(loginBean.getAccountType())) {
                key = MessageCode.PHONE_LOGIN.getCode() + loginBean.getAccount();
            }
            captchaService.verifyCaptcha(key, loginBean.getCaptcha());
            // 查询用户信息
            QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>(new UserEntity());
            if (AccountType.PHONE.getType().equals(loginBean.getAccountType())) {
                queryWrapper.lambda().eq(UserEntity::getPhone, loginBean.getAccount());
            }
            if (AccountType.EMAIL.getType().equals(loginBean.getAccountType())) {
                queryWrapper.lambda().eq(UserEntity::getEmail, loginBean.getAccount());
            }
            UserEntity userEntity = userMapper.selectOne(queryWrapper);
            if (userEntity == null) {
                // 判断账号是手机号还是邮箱
                UserEntity user = new UserEntity();
                String account = StringUtils.generateOnlyNum(constants.getAccountPrefix());
                user.setAccount(account);
                if (AccountType.PHONE.getType().equals(loginBean.getAccountType())) {
                    user.setPhone(loginBean.getAccount());
                } else {
                    user.setEmail(loginBean.getAccount());
                }
                // 验证账号是否注销过
                DeletedUserEntity deletedUserEntity = new DeletedUserEntity();
                deletedUserEntity.setPhone(user.getPhone());
                deletedUserEntity.setEmail(user.getEmail());
                List<DeletedUserEntity> deletedUserEntities = deletedUserService.list(new QueryWrapper<>(deletedUserEntity));
                if (deletedUserEntities.size() != 0) {
                    throw FailCode.ACCOUNT_LOGGED_OUT_UNAVAILABLE.getOperateException();
                }
                // 创建新账号
                user.setAccessId(StringUtils.generateOnlyNum(constants.getAccessIdPrefix()));
                user.setAccessSecret(Md5Utils.getSaltMd5(StringUtils.random(10)));
                if (!userService.save(user)) {
                    throw FailCode.USER_CREATE_FAIL.getOperateException();
                }
                // 创建应用用户权限关联
                return setLoginInfo(appEntity, loginBean, user);
            } else {
                // 创建应用用户权限关联
                createAppUserAuthority(appEntity, userEntity.getId(), loginBean.getAuthorityIds());
                return setLoginInfo(appEntity, loginBean, userEntity);
            }
        }
        // 密码登录
        if (AccountType.PASSWORD.getType().equals(loginBean.getAccountType())) {
            QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>(new UserEntity());
            queryWrapper.lambda().nested(wrapper -> wrapper
                    .eq(UserEntity::getAccount, loginBean.getAccount()).or()
                    .eq(UserEntity::getEmail, loginBean.getAccount()).or()
                    .eq(UserEntity::getPhone, loginBean.getAccount()).or());
            UserEntity user = userMapper.selectOne(queryWrapper);
            if (user == null) {
                throw FailCode.USER_ACCOUNT_ERROR.getOperateException();
            }
            if (user.getPassword() == null) {
                throw FailCode.YOU_NOT_SET_PASSWORD.getOperateException();
            }
            //是否需要验证码
            if (openCaptcha && user.getFailRecord() >= constants.getOpenVerifyCodeNum()) {
                if (loginBean.getCode() == null) {
                    throw FailCode.CAPTCHA_CODE_NOT_NULL.getOperateException();
                }
                // 验证验证码
                captchaService.verifyCaptcha(loginBean.getCode(), loginBean.getCaptcha());
            }
            //密码错误次数达上限
            if (user.getFailRecord() >= constants.getPasswordErrorsNum()) {
                if (userMapper.updateById(user) == 0) {
                    throw FailCode.USER_UPDATE_FAIL.getOperateException();
                }
                throw FailCode.USER_PASSWORD_ERROR_USER_LOCKED.getOperateException();
            }
            //验证密码
            if (!Md5Utils.verifySaltMd5(loginBean.getPassword(), user.getPassword())) {
                user.setFailRecord(user.getFailRecord() + 1);
                if (userMapper.updateById(user) == 0) {
                    throw FailCode.USER_UPDATE_FAIL.getOperateException();
                }
                throw FailCode.USER_PASSWORD_ERROR.getOperateException();
            }
            //重置密码错误次数
            if (user.getFailRecord() != 0) {
                user.setFailRecord(0);
                if (userMapper.updateById(user) == 0) {
                    throw FailCode.USER_UPDATE_FAIL.getOperateException();
                }
            }
            // 创建应用用户权限关联
            createAppUserAuthority(appEntity, user.getId(), loginBean.getAuthorityIds());
            return setLoginInfo(appEntity, loginBean, user);
        }
        // 账号类型错误
        throw FailCode.USER_ACCOUNT_TYPE_ERROR.getOperateException();
    }

    /**
     * 用户信息存入redis
     *
     * @param appEntity      应用信息
     * @param overtLoginBean 登录信息
     * @param user           用户信息
     * @return 登录需要的code
     */
    public LoginDto setLoginInfo(AppEntity appEntity, LoginBean overtLoginBean, UserEntity user) {
        String appId = appEntity.getAppId();
        // 创建应用用户权限关联
        createAppUserAuthority(appEntity, user.getId(), overtLoginBean.getAuthorityIds());
        // 返回数据
        String code = Md5Utils.getSaltMd5(UUID.randomUUID().toString());
        CodeModel codeModel = new CodeModel();
        codeModel.setUserEntity(user);
        codeModel.setCode(code);
        codeModel.setAppId(appId);
        RedisUtils.set(RedisPrefix.CODE.getKey() + code, JSONUtils.getStringByObject(codeModel), RedisTime.CODE_EXPIRY.getTime());
        LoginDto loginDto = new LoginDto();
        loginDto.setCode(code);
        return loginDto;
    }

    @Override
    public CodeModel getCode(String code) {
        // 获取code
        String key = RedisPrefix.CODE.getKey() + code;
        String json = RedisUtils.get(key);
        if (json == null) {
            throw FailCode.USER_AUTHORIZE_CODE_NOT_EXIST.getOperateException();
        }
        // 删除code
        RedisUtils.delete(key);
        // 获取code存储的信息
        return JSONUtils.getObjectByString(json, CodeModel.class);
    }

    @Override
    public TokenModel getToken(AppEntity appEntity, String code, String accessToken) {
        if (accessToken != null) {
            String accessKey = RedisPrefix.ACCESS.getKey() + accessToken;
            String json = RedisUtils.get(accessKey);
            TokenModel tokenModel = JSONUtils.getObjectByString(json, TokenModel.class);
            if (tokenModel == null) {
                throw CoreFailCode.TOKEN_EXPIRE.getOperateException();
            }
            if ((tokenModel.getUpdateTime().getTime() + tokenModel.getValidTime()) < new Date().getTime()) {
                throw CoreFailCode.TOKEN_EXPIRE.getOperateException();
            }
            return tokenModel;
        }
        CodeModel codeModel = this.getCode(code);
        if (!appEntity.getAppId().equals(codeModel.getAppId())) {
            throw FailCode.APP_ID_ERROR.getOperateException();
        }
        UserEntity userEntity = codeModel.getUserEntity();
        return getToken(appEntity, userEntity);
    }

    @Override
    public TokenModel getToken(AppEntity appEntity, UserEntity userEntity) {
        // 处理不能同时在线
        if (Whether.NO.getValue().equals(appEntity.getIsCoexist())) {
            deleteUserToken(appEntity.getAppId(), userEntity.getId());
        }
        // 关联基础角色
        RoleEntity role = new RoleEntity();
        role.setType(RoleType.BASIC_ROLE.getType());
        role.setAppId(appEntity.getId());
        userRelationRole(userEntity.getId(), role);
        // 封装token信息
        String refreshToken = Md5Utils.getSaltMd5(UUID.randomUUID().toString());
        TokenModel tokenModel = new TokenModel();
        tokenModel.setUserId(userEntity.getId());
        tokenModel.setUpdateTime(new Date());
        tokenModel.setValidTime(appEntity.getTokenValidTime());
        tokenModel.setAccessToken(Md5Utils.getSaltMd5(UUID.randomUUID().toString()));
        tokenModel.setRefreshToken(refreshToken);
        tokenModel.setAppId(appEntity.getAppId());
        tokenModel.setAccount(userEntity.getAccount());
        // 获取用户权限信息
        List<AuthorityEntity> authorityList = authorityService.getAppUserValidAuthorityList(appEntity.getId(), userEntity);
        List<String> authorityCodes = new ArrayList<>();
        for (AuthorityEntity authority : authorityList) {
            authorityCodes.add(authority.getCode());
        }
        tokenModel.setAuthorityCodes(authorityCodes);
        // 获取用户组权限信息
        tokenModel.setGroups(getGroupModelList(appEntity.getId(), userEntity.getId()));
        // token存入redis
        String accessKey = RedisPrefix.ACCESS.getKey() + tokenModel.getAccessToken();
        RedisUtils.set(accessKey, JSONUtils.getStringByObject(tokenModel), tokenModel.getValidTime());
        // 刷新token存入redis
        String refreshKey = RedisPrefix.REFRESH.getKey() + tokenModel.getRefreshToken();
        RedisUtils.set(refreshKey, JSONUtils.getStringByObject(tokenModel), appEntity.getRefreshTokenValidTime());
        return tokenModel;
    }

    @Override
    public void updateAppTokenAuthority(AppEntity appEntity) {
        executorService.execute(() -> {
            log.info("基础角色关联-开始！");
            // 关联基础角色
            RoleEntity role = new RoleEntity();
            role.setType(RoleType.BASIC_ROLE.getType());
            role.setAppId(appEntity.getId());
            QueryWrapper<RoleEntity> queryWrapper = new QueryWrapper<>(role);
            List<RoleEntity> roles = roleService.list(queryWrapper);
            if (roles.size() > 0) {
                // 查询应用拥有的用户授权
                UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
                userAuthorizeEntity.setAppId(appEntity.getId());
                List<UserAuthorizeEntity> userAuthorizeEntities = userAuthorizeService.list(new QueryWrapper<>(userAuthorizeEntity));
                for (UserAuthorizeEntity userAuthorize : userAuthorizeEntities) {
                    // 用户角色关联查询
                    List<String> roleIds = roles.stream().map(RoleEntity::getId).collect(Collectors.toList());
                    UserRoleEntity userRole = new UserRoleEntity();
                    userRole.setUserId(userAuthorize.getUserId());
                    QueryWrapper<UserRoleEntity> wrapper = new QueryWrapper<>(userRole);
                    wrapper.lambda().in(UserRoleEntity::getRoleId, roleIds);
                    List<UserRoleEntity> userRoleEntities = userRoleMapper.selectList(wrapper);
                    // 检测关联是否存在
                    for (RoleEntity roleEntity : roles) {
                        boolean bool = true;
                        for (UserRoleEntity userRoleEntity : userRoleEntities) {
                            if (userRoleEntity.getRoleId().equals(roleEntity.getId())) {
                                bool = false;
                                break;
                            }
                        }
                        if (bool) {
                            UserRoleEntity userRoleEntity = new UserRoleEntity();
                            userRoleEntity.setUserId(userAuthorize.getUserId());
                            userRoleEntity.setRoleId(roleEntity.getId());
                            if (!SqlHelper.retBool(userRoleMapper.insert(userRoleEntity))) {
                                throw FailCode.USER_AND_ROLE_RELATION_CREATE_FAIL.getOperateException();
                            }
                        }
                    }
                }
            }
            log.info("基础角色关联-结束！");
            log.info("token权限刷新-开始！");
            Set<String> refreshTokenSet = RedisUtils.getKes(RedisPrefix.REFRESH.getKey() + "*");
            for (String refreshTokenKey : refreshTokenSet) {
                String tokenJson = RedisUtils.get(refreshTokenKey);
                if (tokenJson != null) {
                    TokenModel tokenModel = JSONUtils.getObjectByString(tokenJson, TokenModel.class);
                    if (tokenModel.getAppId().equals(appEntity.getAppId())) {
                        tokenModel.setValidTime(appEntity.getTokenValidTime());
                        UserEntity userEntity = userService.getById(tokenModel.getUserId());
                        // 获取用户权限信息
                        List<AuthorityEntity> authorityList = authorityService.getAppUserValidAuthorityList(appEntity.getId(), userEntity);
                        List<String> authorityCodes = new ArrayList<>();
                        for (AuthorityEntity authority : authorityList) {
                            authorityCodes.add(authority.getCode());
                        }
                        tokenModel.setAuthorityCodes(authorityCodes);
                        // 获取用户组权限信息
                        tokenModel.setGroups(getGroupModelList(appEntity.getId(), userEntity.getId()));
                        // token存入redis
                        String accessKey = RedisPrefix.ACCESS.getKey() + tokenModel.getAccessToken();
                        RedisUtils.set(accessKey, JSONUtils.getStringByObject(tokenModel), tokenModel.getValidTime());
                        // 刷新token存入redis
                        String refreshKey = RedisPrefix.REFRESH.getKey() + tokenModel.getRefreshToken();
                        RedisUtils.set(refreshKey, JSONUtils.getStringByObject(tokenModel), appEntity.getRefreshTokenValidTime());
                    }
                }
            }
            log.info("token权限刷新-结束！");
        });
    }

    @Override
    public void updateAppUserAuthorize(AppEntity appEntity) {
        executorService.execute(() -> {
            if (Whether.NO.getValue().equals(appEntity.getShowUserAuthority())) {
                log.info("用户权限刷新-开始！");
                // 查询应用申请了那些权限
                AuthorizeEntity authorizeEntity = new AuthorizeEntity();
                authorizeEntity.setAppId(appEntity.getId());
                authorizeEntity.setAuditStatus(AuditStatus.REVIEWED.getStatus());
                List<AuthorizeEntity> authorizeEntities = authorizeService.list(new QueryWrapper<>(authorizeEntity));
                // 查询用户权限
                List<String> authorityIds = authorizeEntities.stream().map(AuthorizeEntity::getAuthorityId).collect(Collectors.toList());
                AuthorityEntity authorityEntity = new AuthorityEntity();
                authorityEntity.setType(AuthorityType.USER_INFO.getType());
                authorityEntity.setStatus(EffectStatus.VALID.getStatus());
                QueryWrapper<AuthorityEntity> queryWrapper = new QueryWrapper<>(authorityEntity);
                queryWrapper.lambda().in(AuthorityEntity::getId, authorityIds);
                List<AuthorityEntity> authorityEntities = authorityService.list(queryWrapper);
                // 查询应用拥有的用户授权
                UserAuthorizeEntity userAuthorize = new UserAuthorizeEntity();
                userAuthorize.setAppId(appEntity.getId());
                List<UserAuthorizeEntity> userAuthorizeEntities = userAuthorizeService.list(new QueryWrapper<>(userAuthorize));
                for (UserAuthorizeEntity userAuthorizeEntity : userAuthorizeEntities) {
                    // 删除用户授权权限
                    UserAuthorizeAuthorityEntity userAuthorizeAuthority = new UserAuthorizeAuthorityEntity();
                    userAuthorizeAuthority.setUserAuthorizeId(userAuthorizeEntity.getId());
                    userAuthorizeAuthorityMapper.delete(new QueryWrapper<>(userAuthorizeAuthority));
                    // 重建用户授权
                    for (AuthorityEntity authority : authorityEntities) {
                        UserAuthorizeAuthorityEntity userAuthorizeAuthorityEntity = new UserAuthorizeAuthorityEntity();
                        userAuthorizeAuthorityEntity.setUserAuthorizeId(userAuthorizeEntity.getId());
                        userAuthorizeAuthorityEntity.setAuthorityId(authority.getId());
                        try {
                            userAuthorizeAuthorityMapper.insert(userAuthorizeAuthorityEntity);
                        } catch (DuplicateKeyException e) {
                            throw FailCode.OPERATE_FREQUENTLY.getOperateException();
                        }
                    }
                }
                log.info("用户权限刷新-结束！");
            }
        });
    }


    @Override
    public void createAppUserAuthority(AppEntity appEntity, String userId, List<String> authorityIds) {
        // 创建应用用户关联
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        userAuthorizeEntity.setAppId(appEntity.getId());
        userAuthorizeEntity.setUserId(userId);
        UserAuthorizeEntity userAuthorize = userAuthorizeMapper.selectOne(new QueryWrapper<>(userAuthorizeEntity));
        String userAuthorizeId;
        if (userAuthorize != null) {
            userAuthorize.setAuthorizeTime(new Date());
            if (!SqlHelper.retBool(userAuthorizeMapper.updateById(userAuthorize))) {
                throw FailCode.USER_AUTHORIZE_UPDATE_FAIL.getOperateException();
            }
            userAuthorizeId = userAuthorize.getId();
        } else {
            userAuthorizeEntity.setAuthorizeTime(new Date());
            if (!SqlHelper.retBool(userAuthorizeMapper.insert(userAuthorizeEntity))) {
                throw FailCode.USER_AUTHORIZE_CREATE_FAIL.getOperateException();
            }
            userAuthorizeId = userAuthorizeEntity.getId();
        }
        // 删除用户授权权限
        UserAuthorizeAuthorityEntity userAuthorizeAuthority = new UserAuthorizeAuthorityEntity();
        userAuthorizeAuthority.setUserAuthorizeId(userAuthorizeId);
        userAuthorizeAuthorityMapper.delete(new QueryWrapper<>(userAuthorizeAuthority));
        // 创建用户授权权限
        if (Whether.YES.getValue().equals(appEntity.getShowUserAuthority())) {
            for (String authorityId : authorityIds) {
                UserAuthorizeAuthorityEntity userAuthorizeAuthorityEntity = new UserAuthorizeAuthorityEntity();
                userAuthorizeAuthorityEntity.setUserAuthorizeId(userAuthorizeId);
                userAuthorizeAuthorityEntity.setAuthorityId(authorityId);
                try {
                    userAuthorizeAuthorityMapper.insert(userAuthorizeAuthorityEntity);
                } catch (DuplicateKeyException e) {
                    throw FailCode.OPERATE_FREQUENTLY.getOperateException();
                }
            }
        } else {
            List<AuthorityEntity> authorityList = authorityService.getAppValidAuthorityList(appEntity.getId(), null, AuthorityType.USER_INFO.getType());
            for (AuthorityEntity authorityEntity : authorityList) {
                UserAuthorizeAuthorityEntity userAuthorizeAuthorityEntity = new UserAuthorizeAuthorityEntity();
                userAuthorizeAuthorityEntity.setUserAuthorizeId(userAuthorizeId);
                userAuthorizeAuthorityEntity.setAuthorityId(authorityEntity.getId());
                try {
                    userAuthorizeAuthorityMapper.insert(userAuthorizeAuthorityEntity);
                } catch (DuplicateKeyException e) {
                    throw FailCode.OPERATE_FREQUENTLY.getOperateException();
                }
            }
        }
    }

    private List<GroupModel> getGroupModelList(String appId, String userId) {
        List<GroupModel> groupModels = new ArrayList<>();
        // 获取用户的全部组
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupEntity.setUserId(userId);
        List<UserGroupEntity> userGroupEntities = userGroupMapper.selectList(new QueryWrapper<>(userGroupEntity));
        List<String> groupIds = userGroupEntities.stream().map(UserGroupEntity::getGroupId).collect(Collectors.toList());
        // 处理组数据
        if (groupIds.size() > 0) {
            // 查询用户拥有的组
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setAppId(appId);
            groupEntity.setStatus(EffectStatus.VALID.getStatus());
            QueryWrapper<GroupEntity> queryWrapper = new QueryWrapper<>(groupEntity);
            queryWrapper.lambda().in(GroupEntity::getId, groupIds);
            List<GroupEntity> groupEntities = groupService.list(queryWrapper);
            List<String> groupIdList = groupEntities.stream().map(GroupEntity::getId).collect(Collectors.toList());
            // 获取全部用户
            QueryWrapper<UserGroupEntity> userGroupWrapper = new QueryWrapper<>(new UserGroupEntity());
            userGroupWrapper.lambda().in(UserGroupEntity::getGroupId, groupIds);
            List<UserGroupEntity> userGroupList = userGroupMapper.selectList(userGroupWrapper);
            if (groupIdList.size() > 0) {
                // 查询用户组拥有的角色
                QueryWrapper<GroupRoleEntity> groupRoleWrapper = new QueryWrapper<>(new GroupRoleEntity());
                groupRoleWrapper.lambda().in(GroupRoleEntity::getGroupId, groupIdList);
                List<GroupRoleEntity> groupRoles = groupRoleMapper.selectList(groupRoleWrapper);
                List<String> roleIds = groupRoles.stream().map(GroupRoleEntity::getRoleId).collect(Collectors.toList());
                if (roleIds.size() > 0) {
                    // 查询用户组角色拥有的权限
                    List<RoleAuthorityEntity> roleAuthorityList = roleAuthorityMapper.selectList(new QueryWrapper<>(new RoleAuthorityEntity())
                            .lambda().in(RoleAuthorityEntity::getRoleId, roleIds));
                    List<String> authorityIds = roleAuthorityList.stream().map(RoleAuthorityEntity::getAuthorityId).collect(Collectors.toList());
                    if (authorityIds.size() > 0) {
                        // 查询用户组角色拥有的权限的详细信息
                        AuthorityEntity authorityEntity = new AuthorityEntity();
                        authorityEntity.setAppId(appId); //应用主键id
                        authorityEntity.setStatus(EffectStatus.VALID.getStatus());
                        QueryWrapper<AuthorityEntity> wrapper = new QueryWrapper<>(authorityEntity);
                        wrapper.lambda().in(AuthorityEntity::getId, authorityIds);
                        List<AuthorityEntity> authorityEntities = authorityService.list(wrapper);
                        // 循环处理数据
                        for (GroupEntity group : groupEntities) {
                            GroupModel groupModel = new GroupModel();
                            groupModel.setGroupId(group.getId());
                            groupModel.setCode(group.getCode());
                            groupModel.setAuthorityCodes(new ArrayList<>());
                            groupModel.setUserIds(new ArrayList<>());
                            for (GroupRoleEntity groupRoleEntity : groupRoles) {
                                for (RoleAuthorityEntity roleAuthorityEntity : roleAuthorityList) {
                                    for (AuthorityEntity authorityEntity1 : authorityEntities) {
                                        if (group.getId().equals(groupRoleEntity.getGroupId()) &&
                                                groupRoleEntity.getRoleId().equals(roleAuthorityEntity.getRoleId()) &&
                                                roleAuthorityEntity.getAuthorityId().equals(authorityEntity1.getId())) {
                                            groupModel.getAuthorityCodes().add(authorityEntity1.getCode());
                                        }
                                    }
                                }
                            }
                            for (UserGroupEntity userGroup : userGroupList) {
                                if (userGroup.getGroupId().equals(group.getId())) {
                                    groupModel.getUserIds().add(userGroup.getUserId());
                                }
                            }
                            groupModels.add(groupModel);
                        }
                    }
                }
            }
        }
        return groupModels;
    }

    @Override
    public TokenModel updateToken(String refreshToken) {
        // token存入redis
        String refreshKey = RedisPrefix.REFRESH.getKey() + refreshToken;
        String json = RedisUtils.get(refreshKey);
        if (json == null) {
            throw CoreFailCode.REFRESH_TOKEN_NOT_EXIST.getOperateException();
        }
        TokenModel tokenModel = JSONUtils.getObjectByString(json, TokenModel.class);
        // 查询就的token是否失效
        String oldTokenJson = RedisUtils.get(tokenModel.getAccessToken());
        if (oldTokenJson != null) {
            TokenModel oldTokenModel = JSONUtils.getObjectByString(oldTokenJson, TokenModel.class);
            if (oldTokenModel != null && (oldTokenModel.getUpdateTime().getTime() + oldTokenModel.getValidTime()) >= new Date().getTime()) {
                return oldTokenModel;
            }
        }
        AppEntity appEntity = appService.getValidAppByAppId(tokenModel.getAppId());
        // 删除原token
        String accessKey = RedisPrefix.ACCESS.getKey() + tokenModel.getAccessToken();
        RedisUtils.delete(accessKey);
        // 生成新的token
        String accessToken = Md5Utils.getSaltMd5(UUID.randomUUID().toString());
        tokenModel.setAccessToken(accessToken);
        tokenModel.setUpdateTime(new Date());
        // token存入redis
        String newAccessKey = RedisPrefix.ACCESS.getKey() + tokenModel.getAccessToken();
        RedisUtils.set(newAccessKey, JSONUtils.getStringByObject(tokenModel), tokenModel.getValidTime());
        // 刷新token存入redis
        RedisUtils.set(refreshKey, JSONUtils.getStringByObject(tokenModel), appEntity.getRefreshTokenValidTime());
        return tokenModel;
    }

    /**
     * 用户关联角色
     *
     * @param userId 用户id
     * @param role   角色条件
     */
    private void userRelationRole(String userId, RoleEntity role) {
        // 查询角色集合
        QueryWrapper<RoleEntity> queryWrapper = new QueryWrapper<>(role);
        List<RoleEntity> roles = roleService.list(queryWrapper);
        if (roles.size() > 0) {
            // 用户角色关联查询
            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setUserId(userId);
            QueryWrapper<UserRoleEntity> wrapper = new QueryWrapper<>(userRole);
            List<String> roleIds = roles.stream().map(RoleEntity::getId).collect(Collectors.toList());
            wrapper.lambda().in(UserRoleEntity::getRoleId, roleIds);
            List<UserRoleEntity> userRoleEntities = userRoleMapper.selectList(wrapper);
            // 检测关联是否存在
            for (RoleEntity roleEntity : roles) {
                boolean bool = true;
                for (UserRoleEntity userRoleEntity : userRoleEntities) {
                    if (userRoleEntity.getRoleId().equals(roleEntity.getId())) {
                        bool = false;
                        break;
                    }
                }
                if (bool) {
                    UserRoleEntity userRoleEntity = new UserRoleEntity();
                    userRoleEntity.setUserId(userId);
                    userRoleEntity.setRoleId(roleEntity.getId());
                    if (!SqlHelper.retBool(userRoleMapper.insert(userRoleEntity))) {
                        throw FailCode.USER_AND_ROLE_RELATION_CREATE_FAIL.getOperateException();
                    }
                }
            }
        }
    }

    @Override
    public void deleteUserToken(String appId, String userId) {
        List<TokenModel> tokenModels = AuthorityHandle.getUserTokenList(RedisPrefix.REFRESH.getKey() + "*", userId);
        for (TokenModel tokenModel : tokenModels) {
            if (appId.equals(tokenModel.getAppId())) {
                deleteToken(tokenModel);
            }
        }
    }

    @Override
    public void deleteUserToken(String userId) {
        List<TokenModel> tokenModels = AuthorityHandle.getUserTokenList(RedisPrefix.REFRESH.getKey() + "*", userId);
        for (TokenModel tokenModel : tokenModels) {
            deleteToken(tokenModel);
        }
    }

    @Override
    public void deleteToken(String accessToken) {
        String accessKey = RedisPrefix.ACCESS.getKey() + accessToken;
        String json = RedisUtils.get(accessKey);
        if (json != null) {
            TokenModel tokenModel = JSONUtils.getObjectByString(json, TokenModel.class);
            deleteToken(tokenModel);
        }
    }

    @Override
    public void deleteToken(TokenModel tokenModel) {
        // 删除token
        RedisUtils.delete(RedisPrefix.ACCESS.getKey() + tokenModel.getAccessToken());
        // 删除刷新token
        RedisUtils.delete(RedisPrefix.REFRESH.getKey() + tokenModel.getRefreshToken());
    }

    @Override
    public void register(RegisterBean registerBean) {
        if (registerBean.getAccount() == null) {
            throw FailCode.USER_ACCOUNT_NOT_NULL.getOperateException();
        }
        if (registerBean.getPassword() == null) {
            throw FailCode.USER_PASSWORD_NOT_NULL.getOperateException();
        }
        // 验证账户信息是否存在
        UserEntity user = new UserEntity();
        if (AccountType.PHONE.getType().equals(registerBean.getAccountType())) {
            user.setPhone(registerBean.getAccount());
        }
        if (AccountType.EMAIL.getType().equals(registerBean.getAccountType())) {
            user.setEmail(registerBean.getAccount());
        }
        UserEntity userEntity = userMapper.selectOne(new QueryWrapper<>(user));
        if (userEntity != null) {
            throw FailCode.USER_EXIST.getOperateException();
        }
        // 验证账号是否注销过
        DeletedUserEntity deletedUserEntity = new DeletedUserEntity();
        deletedUserEntity.setPhone(user.getPhone());
        deletedUserEntity.setEmail(user.getEmail());
        List<DeletedUserEntity> deletedUserEntities = deletedUserService.list(new QueryWrapper<>(deletedUserEntity));
        if (deletedUserEntities.size() != 0) {
            throw FailCode.ACCOUNT_LOGGED_OUT_UNAVAILABLE.getOperateException();
        }
        // 验证验证码
        String key = MessageCode.EMAIL_REGISTER.getCode() + registerBean.getAccount();
        if (AccountType.PHONE.getType().equals(registerBean.getAccountType())) {
            key = MessageCode.PHONE_REGISTER.getCode() + registerBean.getAccount();
        }
        captchaService.verifyCaptcha(key, registerBean.getCaptcha());
        // 添加用户数据
        ObjectUtils.convert(user, registerBean);
        user.setPassword(Md5Utils.getSaltMd5(registerBean.getPassword()));
        String account = StringUtils.generateOnlyNum(constants.getAccountPrefix());
        user.setAccount(account);
        user.setAccessId(StringUtils.generateOnlyNum(constants.getAccessIdPrefix()));
        user.setAccessSecret(Md5Utils.getSaltMd5(StringUtils.random(10)));
        if (!userService.save(user)) {
            throw FailCode.USER_CREATE_FAIL.getOperateException();
        }
    }

    @Override
    @Transactional
    public TokenModel createAppletToken(AppEntity appEntity, AppletBean appletBean) {
        AppletDto appletDto = appletService.getAppAppletByCode(appEntity.getId(), appletBean.getAppletType(), appletBean.getAppletId());
        if (appletDto != null) {
            // 验证账号是否注销过
            DeletedUserEntity deletedUserEntity = new DeletedUserEntity();
            deletedUserEntity.setUserId(appletDto.getUserId());
            List<DeletedUserEntity> deletedUserEntities = deletedUserService.list(new QueryWrapper<>(deletedUserEntity));
            if (deletedUserEntities.size() != 0) {
                throw FailCode.ACCOUNT_LOGGED_OUT_UNAVAILABLE.getOperateException();
            }
            // 创建应用用户权限关联
            UserEntity userEntity = userService.getById(appletDto.getUserId());
            createAppUserAuthority(appEntity, userEntity.getId(), appletBean.getAuthorityIds());
            return getToken(appEntity, userEntity);
        } else {
            // 手机号登录
            if (AccountType.PHONE.getType().equals(appletBean.getAccountType()) ||
                    AccountType.EMAIL.getType().equals(appletBean.getAccountType())) {
                // 验证验证码
                String key = MessageCode.EMAIL_LOGIN.getCode() + appletBean.getAccount();
                if (AccountType.PHONE.getType().equals(appletBean.getAccountType())) {
                    key = MessageCode.PHONE_LOGIN.getCode() + appletBean.getAccount();
                }
                captchaService.verifyCaptcha(key, appletBean.getCaptcha());
                // 查询用户信息
                UserEntity user = new UserEntity();
                if (AccountType.PHONE.getType().equals(appletBean.getAccountType())) {
                    user.setPhone(appletBean.getAccount());
                } else if (AccountType.EMAIL.getType().equals(appletBean.getAccountType())) {
                    user.setEmail(appletBean.getAccount());
                }
                UserEntity userEntity = userMapper.selectOne(new QueryWrapper<>(user));
                if (userEntity == null) {
                    // 验证账号是否注销过
                    DeletedUserEntity deletedUserEntity = new DeletedUserEntity();
                    deletedUserEntity.setPhone(user.getPhone());
                    deletedUserEntity.setEmail(user.getEmail());
                    List<DeletedUserEntity> deletedUserEntities = deletedUserService.list(new QueryWrapper<>(deletedUserEntity));
                    if (deletedUserEntities.size() != 0) {
                        throw FailCode.ACCOUNT_LOGGED_OUT_UNAVAILABLE.getOperateException();
                    }
                    // 创建新账号
                    String account = StringUtils.generateOnlyNum(constants.getAccountPrefix());
                    user.setAccount(account);
                    user.setAccessId(StringUtils.generateOnlyNum(constants.getAccessIdPrefix()));
                    user.setAccessSecret(Md5Utils.getSaltMd5(StringUtils.random(10)));
                    if (!userService.save(user)) {
                        throw FailCode.USER_CREATE_FAIL.getOperateException();
                    }
                    AppletEntity appletEntity = new AppletEntity();
                    appletEntity.setAppId(appEntity.getId());
                    appletEntity.setType(appletBean.getAppletType());
                    appletEntity.setAppletId(appletBean.getAppletId());
                    appletEntity.setUserId(user.getId());
                    if (!appletService.save(appletEntity)) {
                        throw FailCode.APPLET_CREATE_FAIL.getOperateException();
                    }
                    // 创建应用用户权限关联
                    createAppUserAuthority(appEntity, user.getId(), appletBean.getAuthorityIds());
                    return getToken(appEntity, user);
                } else {
                    AppletEntity appletEntity = new AppletEntity();
                    appletEntity.setAppId(appEntity.getId());
                    appletEntity.setType(appletBean.getAppletType());
                    appletEntity.setUserId(userEntity.getId());
                    AppletEntity applet = appletService.getOne(new QueryWrapper<>(appletEntity));
                    if (applet != null) {
                        applet.setUserId(userEntity.getId());
                        if (!appletService.updateById(applet)) {
                            throw FailCode.APPLET_UPDATE_FAIL.getOperateException();
                        }
                    } else {
                        appletEntity.setAppletId(appletBean.getAppletId());
                        if (!appletService.save(appletEntity)) {
                            throw FailCode.APPLET_CREATE_FAIL.getOperateException();
                        }
                    }
                    // 创建应用用户权限关联
                    createAppUserAuthority(appEntity, userEntity.getId(), appletBean.getAuthorityIds());
                    return getToken(appEntity, userEntity);
                }
            } else {
                // 账号类型错误
                throw FailCode.USER_ACCOUNT_TYPE_ERROR.getOperateException();
            }
        }
    }

    @Override
    public void forgetPassword(ForgetBean forgetBean) {
        if (forgetBean.getAccount() == null) {
            throw FailCode.USER_ACCOUNT_NOT_NULL.getOperateException();
        }
        // 密码不能为空
        if (forgetBean.getPassword() == null) {
            throw FailCode.USER_PASSWORD_NOT_NULL.getOperateException();
        }
        // 验证账户信息是否存在
        UserEntity user = new UserEntity();
        if (AccountType.EMAIL.getType().equals(forgetBean.getAccountType())) {
            user.setEmail(forgetBean.getAccount());
        } else if (AccountType.PHONE.getType().equals(forgetBean.getAccountType())) {
            user.setPhone(forgetBean.getAccount());
        } else {
            throw FailCode.USER_ACCOUNT_ERROR.getOperateException();
        }
        UserEntity userEntity = userMapper.selectOne(new QueryWrapper<>(user));
        if (userEntity == null) {
            throw FailCode.USER_ACCOUNT_ERROR.getOperateException();
        }
        // 验证验证码
        String key = MessageCode.EMAIL_FORGET_PASSWORD.getCode() + forgetBean.getAccount();
        if (AccountType.PHONE.getType().equals(forgetBean.getAccountType())) {
            key = MessageCode.PHONE_FORGET_PASSWORD.getCode() + forgetBean.getAccount();
        }
        captchaService.verifyCaptcha(key, forgetBean.getCaptcha());
        // 修改用户
        userEntity.setPassword(Md5Utils.getSaltMd5(forgetBean.getPassword()));
        userEntity.setFailRecord(0);
        if (!userService.updateById(userEntity)) {
            throw FailCode.USER_UPDATE_FAIL.getOperateException();
        }
    }

    @Override
    public VerifyAccountDto verifyAccount(String accountType, String account) {
        VerifyAccountDto verifyAccountDto = new VerifyAccountDto();
        if (AccountType.PHONE.getType().equals(accountType)) {
            UserEntity userEntity = new UserEntity();
            userEntity.setPhone(account);
            UserEntity user = userMapper.selectOne(new QueryWrapper<>(userEntity));
            if (user != null) {
                verifyAccountDto.setAccountCorrect(true);
            } else {
                verifyAccountDto.setAccountCorrect(false);
            }
        } else if (AccountType.EMAIL.getType().equals(accountType)) {
            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(account);
            UserEntity user = userMapper.selectOne(new QueryWrapper<>(userEntity));
            if (user != null) {
                verifyAccountDto.setAccountCorrect(true);
            } else {
                verifyAccountDto.setAccountCorrect(false);
            }
        } else if (AccountType.PASSWORD.getType().equals(accountType)) {
            QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>(new UserEntity());
            queryWrapper.lambda().nested(wrapper -> wrapper
                    .eq(UserEntity::getAccount, account).or()
                    .eq(UserEntity::getEmail, account).or()
                    .eq(UserEntity::getPhone, account));
            UserEntity userEntity = userMapper.selectOne(queryWrapper);
            if (userEntity == null) {
                verifyAccountDto.setAccountCorrect(false);
            } else {
                verifyAccountDto.setAccountCorrect(true);
                if (userEntity.getFailRecord() >= constants.getOpenVerifyCodeNum()) {
                    verifyAccountDto.setOpenCaptcha(true);
                } else {
                    verifyAccountDto.setOpenCaptcha(false);
                }
            }
        }
        return verifyAccountDto;
    }

    @Override
    public AppEntity verifySign(SignBean signBean) {
        signBean.setMyAppId(basicConstants.getAppId());
        return verifySign(signBean, null);
    }

    @Override
    public AppEntity verifySign(SignBean signBean, SignDto<JSONObject> signDto) {
        if (signBean.getMyAppId() == null) {
            throw FailCode.SIGN_MY_APP_ID_NOT_NULL.getOperateException();
        }
        if (signBean.getAppId() == null) {
            throw FailCode.SIGN_APP_ID_NOT_NULL.getOperateException();
        }
        AppEntity myApp = appService.getReviewedAppByAppId(signBean.getMyAppId());
        if (myApp == null) {
            throw FailCode.SIGN_MY_APP_ID_ERROR.getOperateException();
        }
        AppEntity appEntity = appService.getValidAppByAppId(signBean.getAppId());
        if (appEntity == null) {
            throw FailCode.SIGN_APP_ID_ERROR.getOperateException();
        }
        // 验证ip地址是否正确
        appService.verifyAppIp(signBean.getClientIp(), appEntity.getLocalIp());
        // 签名验证
        JSONObject jsonObject = signBean.getSignDataObj(appEntity.getAppSecret(), myApp.getSignValidTime(), signBean.getAuthorityCode());
        // 返回数据封装
        if (signDto != null) {
            signDto.setAppId(appEntity.getId());
            signDto.setSignData(jsonObject);
        }
        // 是否开启超级管理员权限
        UserEntity userEntity = userMapper.selectById(appEntity.getUserId());
        if (constants.getOpenInitialAdmin() && Whether.YES.getValue().equals(userEntity.getInitialAdmin())) {
            return appEntity;
        }
        // 权限验证
        verifyAppAuthority(appEntity.getId(), signBean, myApp);
        // 返回应用信息
        return appEntity;
    }

    @Override
    public UserEntity verifyUserSign(UserSignBean userSignBean, UserSignDto<JSONObject> userSignDto) {
        if (userSignBean.getMyAppId() == null) {
            throw FailCode.SIGN_MY_APP_ID_NOT_NULL.getOperateException();
        }
        if (userSignBean.getAccessId() == null) {
            throw FailCode.SIGN_ACCESS_ID_NOT_NULL.getOperateException();
        }
        // 查询接口提供者应用
        AppEntity myApp = appService.getValidAppByAppId(userSignBean.getMyAppId());
        if (myApp == null) {
            throw FailCode.SIGN_MY_APP_ID_ERROR.getOperateException();
        }
        // 查询用户
        UserEntity userEntity = userService.getUserByAccessId(userSignBean.getAccessId());
        if (userEntity == null) {
            throw FailCode.SIGN_ACCESS_ID_ERROR.getOperateException();
        }
        // 签名验证
        JSONObject jsonObject = userSignBean.getSignDataObj(userEntity.getAccessSecret(), myApp.getSignValidTime(), userSignBean.getAuthorityCode());
        // 封装返回数据
        if (userSignDto != null) {
            userSignDto.setUserId(userEntity.getId());
            userSignDto.setSignData(jsonObject);
        }
        // 返回用户信息
        return userEntity;
    }

    @Override
    public Map<String, Object> getUploadFileSign(String userId) {
        Map<String, Object> signMap = new TreeMap<>();
        String fixedPath = fileConstants.getFixedPath();
        signMap.put("fixedPath", fixedPath);
        signMap.put("groupCode", userId);
        DiskSignBean diskSignBean = new DiskSignBean(fileConstants.getDiskNo(), fileConstants.getDiskSecret(), FileRestCode.openUploadFile.getCode(), signMap);
        String uploadUrl = fileConstants.getFileUrl() + fileConstants.getUploadPath();
        signMap.put("uploadUrl", uploadUrl);
        signMap.putAll(ObjectUtils.convertMap(diskSignBean));
        return signMap;
    }

    /**
     * 验证应用权限/应用的用户权限
     *
     * @param appId    应用信息
     * @param signBean 签名数据
     */
    private void verifyAppAuthority(String appId, SignBean signBean, AppEntity myApp) {
        if (signBean.getUserId() != null) {
            // 验证应用是否有用户权限
            UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
            userAuthorizeEntity.setUserId(signBean.getUserId());
            userAuthorizeEntity.setAppId(appId);
            UserAuthorizeEntity userAuthorize = userAuthorizeMapper.selectOne(new QueryWrapper<>(userAuthorizeEntity));
            if (userAuthorize == null) {
                throw FailCode.USER_AUTHORIZE_NOT_EXIST.getOperateException();
            }
            UserAuthorizeAuthorityEntity userAuthorizeAuthority = new UserAuthorizeAuthorityEntity();
            userAuthorizeAuthority.setUserAuthorizeId(userAuthorize.getId());
            List<UserAuthorizeAuthorityEntity> userAuthorizeAuthorityEntities = userAuthorizeAuthorityMapper.selectList(new QueryWrapper<>(userAuthorizeAuthority));
            List<String> authorityIds = userAuthorizeAuthorityEntities.stream().map(UserAuthorizeAuthorityEntity::getAuthorityId).collect(Collectors.toList());
            // 判断是否验证权限
            List<AuthorityEntity> entities = authorityService.getAppValidAuthorityList(appId, myApp.getId(), AuthorityType.USER_INFO.getType());
            boolean bool = true;
            for (AuthorityEntity authorityEntity : entities) {
                if (authorityIds.contains(authorityEntity.getId()) && authorityEntity.getCode().equals(signBean.getAuthorityCode())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                throw FailCode.APP_NOT_USER_AUTHORITY_VISIT.getOperateException();
            }
        } else {
            // 验证应用是否有权限
            List<AuthorityEntity> entities = authorityService.getAppValidAuthorityList(appId, myApp.getId(), AuthorityType.APP.getType());
            boolean bool = true;
            for (AuthorityEntity authorityEntity : entities) {
                if (authorityEntity.getCode().equals(signBean.getAuthorityCode())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                throw FailCode.APP_NOT_AUTHORITY_VISIT.getOperateException();
            }
        }
    }
}
