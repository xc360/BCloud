package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.bean.PasswordBean;
import com.xc.api.basic.bean.UpdateMailBean;
import com.xc.api.basic.bean.UpdatePhoneBean;
import com.xc.api.basic.dto.UserDto;
import com.xc.api.basic.bean.UserGroupBean;
import com.xc.api.basic.bean.UserRoleBean;
import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.AppUserBean;
import com.xc.basic.bean.GroupRoleBean;
import com.xc.basic.dto.BasicUserDto;
import com.xc.basic.dto.GroupRoleDto;
import com.xc.basic.entity.UserEntity;

import java.util.List;

/**
 * <p>用户接口类</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface UserService extends IService<UserEntity> {


    /**
     * 用户信息分页数据
     *
     * @param current     当前页
     * @param pagingBean  分页信息
     * @param appUserBean 查询条件
     * @return 分页数据
     */
    public PagingDto<UserDto> getAppUserPage(Integer current, String id, PagingBean pagingBean, AppUserBean appUserBean);

    /**
     * 获取应用的用户
     *
     * @param appId  应用id
     * @param userId 用户userId
     * @return 基础用户信息
     */
    public BasicUserDto getAppUser(String appId, String userId);

    /**
     * 修改密码
     *
     * @param userId       用户userId
     * @param passwordBean 密码信息
     * @return 用户信息
     */
    public UserEntity updateUserPassword(String userId, PasswordBean passwordBean);

    /**
     * 修改邮箱
     *
     * @param userId         用户userId
     * @param updateMailBean 邮箱信息
     * @return 用户信息
     */
    public UserEntity updateOpenUserMail(String userId, UpdateMailBean updateMailBean);

    /**
     * 修改手机号
     *
     * @param userId          用户userId
     * @param updatePhoneBean 手机信息
     * @return 用户信息
     */
    public UserEntity updateOpenUserPhone(String userId, UpdatePhoneBean updatePhoneBean);

    /**
     * 根据账号获取用户信息
     *
     * @param account 账号
     * @return 用户信息
     */
    public UserEntity getUserByEmailOrPhone(String account);

    /**
     * 获取用户集合
     *
     * @param appId 应用主键
     * @return 用户集合
     */
    public List<UserDto> getAppUserList(String appId, List<String> userIds);

    /**
     * 获取用户的组和角色id集合
     *
     * @param appId  应用id
     * @param userId 用户角色
     * @return 用户组和角色id集合
     */
    public GroupRoleDto getUserGroupRole(String appId, String userId);

    /**
     * 创建用户组和角色
     *
     * @param userId        用户id
     * @param groupRoleBean 角色id和用户组id
     * @return 用户组和用户id集合
     */
    public GroupRoleDto updateUserGroupRole(String appId, String userId, GroupRoleBean groupRoleBean);

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public UserEntity getUserByAccount(String account);

    /**
     * 删除用户
     *
     * @return 用户基础信息
     */
    public UserEntity deleteUser(String userId, String captcha, String accountType);

    /**
     * 获取用户主键集合,根据角色
     *
     * @param roleId 角色主键
     * @return 用户主键集合
     */
    public List<String> getUserIdsByRoleId(String roleId);

    /**
     * 根据访问ID获取用户
     *
     * @param accessId 访问ID
     * @return 用户
     */
    public UserEntity getUserByAccessId(String accessId);

    /**
     * 创建用户绑定角色
     *
     * @param appId        应用主键
     * @param userId       用户主键
     * @param userRoleBean 用户角色信息
     */
    public void createUserBindRole(String appId, String userId, UserRoleBean userRoleBean);

    /**
     * 删除用户绑定角色
     *
     * @param appId    应用主键
     * @param userId   用户主键
     * @param roleCode 角色标识
     */
    public void deleteUserBindRole(String appId, String userId, String roleCode);

    /**
     * 创建用户绑定组
     *
     * @param appId         应用主键
     * @param userId        用户主键
     * @param userGroupBean 用户组信息
     */
    public void createUserBindGroup(String appId, String userId, UserGroupBean userGroupBean);

    /**
     * 删除用户绑定角色
     *
     * @param appId     应用主键
     * @param userId    用户主键
     * @param groupCode 组标识
     */
    public void deleteUserBindGroup(String appId, String userId, String groupCode);
}
