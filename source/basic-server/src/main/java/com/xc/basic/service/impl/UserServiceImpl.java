package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.xc.api.basic.bean.*;
import com.xc.api.basic.dto.UserDto;
import com.xc.basic.bean.AppUserBean;
import com.xc.basic.bean.GroupRoleBean;
import com.xc.basic.dto.BasicUserDto;
import com.xc.basic.dto.GroupRoleDto;
import com.xc.basic.entity.*;
import com.xc.basic.enums.AccountType;
import com.xc.basic.enums.FailCode;
import com.xc.basic.enums.MessageCode;
import com.xc.basic.mapper.*;
import com.xc.basic.service.*;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.model.RelationModel;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.Md5Utils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>用户接口实现类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAuthorizeMapper userAuthorizeMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserGroupMapper userGroupMapper;
    @Autowired
    private GroupService groupService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AppService appService;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private DeletedUserMapper deletedUserMapper;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @Override
    public PagingDto<UserDto> getAppUserPage(Integer current, String id, PagingBean pagingBean, AppUserBean appUserBean) {
        // 查用户关联
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        userAuthorizeEntity.setAppId(id);
        userAuthorizeEntity.setRemark(appUserBean.getRemark());
        QueryWrapper<UserAuthorizeEntity> queryWrapper = ServiceUtils.queryData(pagingBean, userAuthorizeEntity);
        // 查用户
        if (appUserBean.getAccount() != null || appUserBean.getPhone() != null || appUserBean.getEmail() != null) {
            UserEntity userEntity = ObjectUtils.convert(new UserEntity(), appUserBean);
            List<UserEntity> userEntities = this.list(new LambdaQueryWrapper<>(userEntity));
            List<String> userIds = userEntities.stream().map(UserEntity::getId).collect(Collectors.toList());
            if (userIds.size() == 0) {
                return new PagingDto<>(0L, new ArrayList<>());
            }
            queryWrapper.lambda().in(UserAuthorizeEntity::getUserId, userIds);
        }
        // 查角色
        if (appUserBean.getRoleId() != null) {
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setRoleId(appUserBean.getRoleId());
            List<UserRoleEntity> userRoleEntities = userRoleMapper.selectList(new QueryWrapper<>(userRoleEntity));
            List<String> roleUserIds = userRoleEntities.stream().map(UserRoleEntity::getUserId).collect(Collectors.toList());
            if (roleUserIds.size() == 0) {
                return new PagingDto<>(0L, new ArrayList<>());
            }
            queryWrapper.lambda().in(UserAuthorizeEntity::getUserId, roleUserIds);
        }
        // 查用户组
        if (appUserBean.getGroupId() != null) {
            UserGroupEntity userGroupEntity = new UserGroupEntity();
            userGroupEntity.setGroupId(appUserBean.getGroupId());
            List<UserGroupEntity> userGroupEntities = userGroupMapper.selectList(new QueryWrapper<>(userGroupEntity));
            List<String> groupUserIds = userGroupEntities.stream().map(UserGroupEntity::getUserId).collect(Collectors.toList());
            if (groupUserIds.size() == 0) {
                return new PagingDto<>(0L, new ArrayList<>());
            }
            queryWrapper.lambda().in(UserAuthorizeEntity::getUserId, groupUserIds);
        }
        IPage<UserAuthorizeEntity> iPage = userAuthorizeMapper.selectPage(new Page<>(current, pagingBean.getSize()), queryWrapper);
        // 查用户
        List<String> resultUserIds = iPage.getRecords().stream().map(UserAuthorizeEntity::getUserId).collect(Collectors.toList());
        if (resultUserIds.size() == 0) {
            return new PagingDto<>(0L, new ArrayList<>());
        }
        LambdaQueryWrapper<UserEntity> userQueryWrapper = Wrappers.lambdaQuery();
        userQueryWrapper.in(UserEntity::getId, resultUserIds);
        List<UserEntity> userEntities = this.list(userQueryWrapper);
        // 处理数据
        List<UserDto> userList = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            UserDto userDto = ObjectUtils.convert(new UserDto(), userEntity);
            for (UserAuthorizeEntity userAuthorize : iPage.getRecords()) {
                if (userEntity.getId().equals(userAuthorize.getUserId())) {
                    userDto.setAuthorizeTime(userAuthorize.getAuthorizeTime());
                    userDto.setRemark(userAuthorize.getRemark());
                }
            }
            userList.add(userDto);
        }
        return new PagingDto<>(iPage.getTotal(), userList);
    }

    @Override
    public BasicUserDto getAppUser(String appId, String userId) {
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        BasicUserDto basicUserDto = new BasicUserDto();
        userAuthorizeEntity.setAppId(appId);
        List<UserAuthorizeEntity> entities = userAuthorizeMapper.selectList(new QueryWrapper<>(userAuthorizeEntity));
        List<String> ids = entities.stream().map(UserAuthorizeEntity::getUserId).collect(Collectors.toList());
        if (ids.size() > 0) {
            UserEntity user = new UserEntity();
            user.setId(userId);
            QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>(user);
            queryWrapper.lambda().in(UserEntity::getId, ids);
            UserEntity userEntity = this.getOne(queryWrapper);
            if (userEntity != null) {
                UserInfoEntity userInfo = new UserInfoEntity();
                userInfo.setUserId(userId);
                ObjectUtils.convert(basicUserDto, userEntity);
                UserInfoEntity userInfoEntity = userInfoMapper.selectOne(new QueryWrapper<>(userInfo));
                if (userInfoEntity != null) {
                    ObjectUtils.convert(basicUserDto, userInfoEntity);
                }
                basicUserDto.setId(userEntity.getId());
            }
        }
        return basicUserDto;
    }


    @Override
    @Transactional
    public UserEntity updateUserPassword(String userId, PasswordBean passwordBean) {
        UserEntity userEntity = userMapper.selectById(userId);
        if (userEntity.getPassword() != null) {
            if (!Md5Utils.verifySaltMd5(passwordBean.getPassword(), userEntity.getPassword())) {
                throw FailCode.USER_PASSWORD_UPDATE_FAIL.getOperateException();
            }
        }
        userEntity.setPassword(Md5Utils.getSaltMd5(passwordBean.getNewPassword()));
        if (!this.updateById(userEntity)) {
            throw FailCode.USER_UPDATE_FAIL.getOperateException();
        }
        // 删除用户所有在线token
        basicAuthorizeService.deleteUserToken(userId);
        return userEntity;
    }


    @Override
    public UserEntity updateOpenUserMail(String userId, UpdateMailBean updateMailBean) {
        if (updateMailBean.getEmail() == null) {
            throw FailCode.USER_MAIL_NOT_NULL.getOperateException();
        }
        // 查询用户
        UserEntity userEntity = userMapper.selectById(userId);
        // 验证验证码
        String key = MessageCode.EMAIL_UPDATE.getCode() + updateMailBean.getEmail();
        captchaService.verifyCaptcha(key, updateMailBean.getCaptcha());
        // 验证确认验证码
        if (userEntity.getPhone() != null || userEntity.getEmail() != null) {
            String confirmKey = MessageCode.EMAIL_UPDATE_EMAIL.getCode() + userEntity.getId();
            if (AccountType.PHONE.getType().equals(updateMailBean.getConfirmAccountType())) {
                confirmKey = MessageCode.PHONE_UPDATE_EMAIL.getCode() + userEntity.getId();
            }
            if (!captchaService.verifyCaptcha(confirmKey, updateMailBean.getConfirmCaptcha(), false)) {
                throw FailCode.CONFIRM_CAPTCHA_ERROR.getOperateException();
            }
        }
        // 修改邮箱
        userEntity.setEmail(updateMailBean.getEmail());
        try {
            if (!this.updateById(userEntity)) {
                throw FailCode.USER_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.USER_MAIL_REPEAT.getOperateException();
        }
        return userEntity;
    }

    @Override
    public UserEntity updateOpenUserPhone(String userId, UpdatePhoneBean updatePhoneBean) {
        if (updatePhoneBean.getPhone() == null) {
            throw FailCode.USER_PHONE_NOT_NULL.getOperateException();
        }
        // 查询用户
        UserEntity userEntity = userMapper.selectById(userId);
        // 验证验证码
        String key = MessageCode.PHONE_UPDATE.getCode() + updatePhoneBean.getPhone();
        captchaService.verifyCaptcha(key, updatePhoneBean.getCaptcha());
        // 验证确认验证码
        if (userEntity.getPhone() != null || userEntity.getEmail() != null) {
            String confirmKey = MessageCode.EMAIL_UPDATE_PHONE.getCode() + userEntity.getId();
            if (AccountType.PHONE.getType().equals(updatePhoneBean.getConfirmAccountType())) {
                confirmKey = MessageCode.PHONE_UPDATE_PHONE.getCode() + userEntity.getId();
            }
            if (!captchaService.verifyCaptcha(confirmKey, updatePhoneBean.getConfirmCaptcha(), false)) {
                throw FailCode.CONFIRM_CAPTCHA_ERROR.getOperateException();
            }
        }
        // 修改手机号
        userEntity.setPhone(updatePhoneBean.getPhone());
        try {
            if (!this.updateById(userEntity)) {
                throw FailCode.USER_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.USER_PHONE_REPEAT.getOperateException();
        }
        return userEntity;
    }

    @Override
    public UserEntity getUserByEmailOrPhone(String account) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>(new UserEntity());
        queryWrapper.lambda().nested(wrapper -> wrapper
                .eq(UserEntity::getEmail, account).or()
                .eq(UserEntity::getPhone, account));
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public List<UserDto> getAppUserList(String appId, List<String> userIds) {
        List<String> ids = appService.getAuthorizeAppUserIds(appId, userIds);
        if (ids.size() > 0) {
            QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>(new UserEntity());
            queryWrapper.lambda().in(UserEntity::getId, ids);
            List<UserEntity> userEntities = userMapper.selectList(queryWrapper);
            return ObjectUtils.convertList(userEntities, UserDto::new);
        }
        return new ArrayList<>();
    }

    @Override
    public GroupRoleDto getUserGroupRole(String appId, String userId) {
        // 修改基础信息
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        userAuthorizeEntity.setAppId(appId);
        userAuthorizeEntity.setUserId(userId);
        UserAuthorizeEntity userAuthorize = userAuthorizeMapper.selectOne(new QueryWrapper<>(userAuthorizeEntity));
        if (userAuthorize == null) {
            throw FailCode.USER_AUTHORIZE_NOT_EXIST.getOperateException();
        }
        //查询用户组
        List<GroupEntity> groupEntities = groupService.getAppUserValidGroupList(appId, userId);
        List<String> groupIds = new ArrayList<>();
        for (GroupEntity groupEntity : groupEntities) {
            groupIds.add(groupEntity.getId());
        }
        // 查询角色
        List<RoleEntity> roleEntities = roleService.getAppUserRoleList(appId, userId);
        List<String> roleIds = new ArrayList<>();
        for (RoleEntity roleEntity : roleEntities) {
            roleIds.add(roleEntity.getId());
        }
        GroupRoleDto groupRoleDto = new GroupRoleDto();
        groupRoleDto.setRoleIds(roleIds);
        groupRoleDto.setGroupIds(groupIds);
        groupRoleDto.setRemark(userAuthorize.getRemark());
        return groupRoleDto;
    }

    @Override
    @Transactional
    public GroupRoleDto updateUserGroupRole(String appId, String userId, GroupRoleBean groupRoleBean) {
        // 修改基础信息
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        userAuthorizeEntity.setAppId(appId);
        userAuthorizeEntity.setUserId(userId);
        UserAuthorizeEntity userAuthorize = userAuthorizeMapper.selectOne(new QueryWrapper<>(userAuthorizeEntity));
        if (userAuthorize == null) {
            throw FailCode.USER_AUTHORIZE_NOT_EXIST.getOperateException();
        }
        userAuthorize.setRemark(groupRoleBean.getRemark());
        userAuthorizeMapper.updateById(userAuthorize);
        // 查询需要删除的用户组和需要创建的用户组
        List<GroupEntity> groupEntities = groupService.getAppUserValidGroupList(appId, userId);
        List<String> oldGroupIds = groupEntities.stream().map(GroupEntity::getId).collect(Collectors.toList());
        RelationModel groupRelation = ServiceUtils.relationHandle(Arrays.asList(groupRoleBean.getGroupIds()), oldGroupIds);
        // 删除用户组关联
        if (groupRelation.getDeleteIds().size() > 0) {
            UserGroupEntity deleteGroupEntity = new UserGroupEntity();
            deleteGroupEntity.setUserId(userId);
            QueryWrapper<UserGroupEntity> queryWrapper = new QueryWrapper<>(deleteGroupEntity);
            queryWrapper.lambda().in(UserGroupEntity::getGroupId, groupRelation.getDeleteIds());
            userGroupMapper.delete(queryWrapper);
        }
        // 创建用户组关联
        for (String groupId : groupRelation.getCreateIds()) {
            UserGroupEntity userGroup = new UserGroupEntity();
            userGroup.setGroupId(groupId);
            userGroup.setUserId(userId);
            if (!SqlHelper.retBool(userGroupMapper.insert(userGroup))) {
                throw FailCode.GROUP_AND_USER_RELATION_CREATE_FAIL.getOperateException();
            }
        }

        // 查询需要删除的角色和需要创建的角色
        List<RoleEntity> roleEntities = roleService.getAppUserRoleList(appId, userId);
        List<String> oldRoleIds = roleEntities.stream().map(RoleEntity::getId).collect(Collectors.toList());
        RelationModel roleRelation = ServiceUtils.relationHandle(Arrays.asList(groupRoleBean.getRoleIds()), oldRoleIds);
        // 删除用户角色关联
        if (roleRelation.getDeleteIds().size() > 0) {
            UserRoleEntity deleteUserRole = new UserRoleEntity();
            deleteUserRole.setUserId(userId);
            QueryWrapper<UserRoleEntity> queryWrapper = new QueryWrapper<>(deleteUserRole);
            queryWrapper.lambda().in(UserRoleEntity::getRoleId, roleRelation.getDeleteIds());
            userRoleMapper.delete(queryWrapper);
        }
        // 创建用户角色关联
        for (String roleId : roleRelation.getCreateIds()) {
            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            if (!SqlHelper.retBool(userRoleMapper.insert(userRole))) {
                throw FailCode.USER_AND_ROLE_RELATION_CREATE_FAIL.getOperateException();
            }
        }
        return ObjectUtils.convert(new GroupRoleDto(), groupRoleBean);
    }

    @Override
    public UserEntity getUserByAccount(String account) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>(new UserEntity());
        queryWrapper.lambda().nested(wrapper -> wrapper.eq(UserEntity::getAccount, account).or()
                .eq(UserEntity::getEmail, account).or().eq(UserEntity::getPhone, account).or());
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional
    public UserEntity deleteUser(String userId, String captcha, String accountType) {
        UserEntity userEntity = userMapper.selectById(userId);
        if (userEntity == null) {
            throw FailCode.USER_ID_ERROR.getOperateException();
        }
        // 验证验证码
        String key = MessageCode.EMAIL_UNSUBSCRIBE.getCode() + userEntity.getId();
        if (AccountType.PHONE.getType().equals(accountType)) {
            key = MessageCode.PHONE_UNSUBSCRIBE.getCode() + userEntity.getId();
        }
        captchaService.verifyCaptcha(key, captcha);
        // 复制用户
        DeletedUserEntity entity = ObjectUtils.convert(new DeletedUserEntity(), userEntity);
        entity.setId(null);
        entity.setUserId(userEntity.getId());
        entity.setDeleteTime(new Date());
        if (!SqlHelper.retBool(deletedUserMapper.insert(entity))) {
            throw FailCode.DELETED_USER_CREATE_FAIL.getOperateException();
        }
        // 删除用户
        if (!this.removeById(userId)) {
            throw FailCode.USER_DELETE_FAIL.getOperateException();
        }
        return userEntity;
    }

    @Override
    public List<String> getUserIdsByRoleId(String roleId) {
        if (roleId != null) {
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setRoleId(roleId);
            List<UserRoleEntity> userRoleEntities = userRoleMapper.selectList(new QueryWrapper<>(userRoleEntity));
            return userRoleEntities.stream().map(UserRoleEntity::getUserId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public UserEntity getUserByAccessId(String accessId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setAccessId(accessId);
        return userMapper.selectOne(new QueryWrapper<>(userEntity));
    }

    @Override
    public void createUserBindRole(String appId, String userId, UserRoleBean userRoleBean) {
        // 判断用是否授权了应用
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        userAuthorizeEntity.setAppId(appId);
        userAuthorizeEntity.setUserId(userId);
        UserAuthorizeEntity userAuthorize = userAuthorizeMapper.selectOne(new QueryWrapper<>(userAuthorizeEntity));
        if (userAuthorize == null) {
            throw FailCode.USER_AUTHORIZE_NOT_EXIST.getOperateException();
        }
        // 查询角色
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setAppId(appId);
        roleEntity.setCode(userRoleBean.getRoleCode());
        RoleEntity entity = roleService.getOne(new QueryWrapper<>(roleEntity));
        if (entity == null) {
            throw FailCode.ROLE_CODE_ERROR.getOperateException();
        }
        // 创建用户角色关联
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUserId(userId);
        userRoleEntity.setRoleId(entity.getId());
        UserRoleEntity userRole = userRoleMapper.selectOne(new QueryWrapper<>(userRoleEntity));
        if (userRole == null) {
            userRole = new UserRoleEntity();
            userRole.setUserId(userId);
            userRole.setRoleId(entity.getId());
            if (!SqlHelper.retBool(userRoleMapper.insert(userRole))) {
                throw FailCode.USER_AND_ROLE_RELATION_CREATE_FAIL.getOperateException();
            }
        }
    }


    @Override
    public void deleteUserBindRole(String appId, String userId, String roleCode) {
        // 判断用是否授权了应用
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        userAuthorizeEntity.setAppId(appId);
        userAuthorizeEntity.setUserId(userId);
        UserAuthorizeEntity userAuthorize = userAuthorizeMapper.selectOne(new QueryWrapper<>(userAuthorizeEntity));
        if (userAuthorize == null) {
            throw FailCode.USER_AUTHORIZE_NOT_EXIST.getOperateException();
        }
        // 查询角色
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setAppId(appId);
        roleEntity.setCode(roleCode);
        RoleEntity entity = roleService.getOne(new QueryWrapper<>(roleEntity));
        if (entity == null) {
            throw FailCode.ROLE_CODE_ERROR.getOperateException();
        }
        // 删除用户角色关联
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUserId(userId);
        userRole.setRoleId(entity.getId());
        userRoleMapper.delete(new QueryWrapper<>(userRole));
    }

    @Override
    public void createUserBindGroup(String appId, String userId, UserGroupBean userGroupBean) {
        // 判断用是否授权了应用
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        userAuthorizeEntity.setAppId(appId);
        userAuthorizeEntity.setUserId(userId);
        UserAuthorizeEntity userAuthorize = userAuthorizeMapper.selectOne(new QueryWrapper<>(userAuthorizeEntity));
        if (userAuthorize == null) {
            throw FailCode.USER_AUTHORIZE_NOT_EXIST.getOperateException();
        }
        // 查询组
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setAppId(appId);
        groupEntity.setCode(userGroupBean.getGroupCode());
        GroupEntity entity = groupService.getOne(new QueryWrapper<>(groupEntity));
        if (entity == null) {
            throw FailCode.GROUP_CODE_ERROR.getOperateException();
        }
        // 创建用户组关联
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupEntity.setUserId(userId);
        userGroupEntity.setGroupId(entity.getId());
        UserGroupEntity userGroup = userGroupMapper.selectOne(new QueryWrapper<>(userGroupEntity));
        if (userGroup == null) {
            userGroup = new UserGroupEntity();
            userGroup.setUserId(userId);
            userGroup.setGroupId(entity.getId());
            if (!SqlHelper.retBool(userGroupMapper.insert(userGroup))) {
                throw FailCode.GROUP_AND_USER_RELATION_CREATE_FAIL.getOperateException();
            }
        }
    }

    @Override
    public void deleteUserBindGroup(String appId, String userId, String groupCode) {
        // 判断用是否授权了应用
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        userAuthorizeEntity.setAppId(appId);
        userAuthorizeEntity.setUserId(userId);
        UserAuthorizeEntity userAuthorize = userAuthorizeMapper.selectOne(new QueryWrapper<>(userAuthorizeEntity));
        if (userAuthorize == null) {
            throw FailCode.USER_AUTHORIZE_NOT_EXIST.getOperateException();
        }
        // 查询组
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setCode(groupCode);
        groupEntity.setAppId(appId);
        GroupEntity entity = groupService.getOne(new QueryWrapper<>(groupEntity));
        if (entity == null) {
            throw FailCode.GROUP_CODE_ERROR.getOperateException();
        }
        // 删除用户组关联
        UserGroupEntity userGroup = new UserGroupEntity();
        userGroup.setUserId(userId);
        userGroup.setGroupId(entity.getId());
        userGroupMapper.delete(new QueryWrapper<>(userGroup));
    }
}
