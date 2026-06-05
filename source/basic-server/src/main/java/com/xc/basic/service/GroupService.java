package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.GroupDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.GroupBean;
import com.xc.basic.bean.GroupRoleBean;
import com.xc.basic.dto.GroupRoleDto;
import com.xc.basic.entity.GroupEntity;

import java.util.List;

/**
 * <p>用户组服务接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface GroupService extends IService<GroupEntity> {

    /**
     * 获取用户组分页数据
     *
     * @param current     当前页
     * @param pagingBean  分页信息
     * @param groupEntity 用户组参数
     * @return 分页信息及用户组数据
     */
    public PagingDto<GroupDto> getGroupPage(Integer current, PagingBean pagingBean, GroupEntity groupEntity);

    /**
     * 创建组
     *
     * @param groupEntity 参数
     * @param roleIds     角色id集合
     * @return 创建后的数据
     */
    public GroupDto createGroup(GroupEntity groupEntity, List<String> roleIds);


    /**
     * 修改组
     *
     * @param groupEntity 参数
     * @param roleIds     角色id集合
     * @return 创建后的数据
     */
    public GroupDto updateGroup(GroupEntity groupEntity, List<String> roleIds);

    /**
     * 删除用户组
     *
     * @param groupId 组id
     */
    public void deleteGroup(String groupId);

    /**
     * 根据id查询组
     *
     * @param groupId 组id
     * @return 组信息
     */
    public GroupDto getGroupById(String groupId);


    /**
     * 获取应用的，用户的，有效组集合
     *
     * @param appId  应用主键id
     * @param userId 用户id
     * @return 组集合
     */
    public List<GroupEntity> getAppUserValidGroupList(String appId, String userId);

    /**
     * 获取用户组集合
     *
     * @param queryBean   查询信息
     * @param groupEntity 查询参数
     * @return 用户组集合
     */
    public List<GroupDto> getGroupList(QueryBean queryBean, GroupEntity groupEntity);

    /**
     * 获取用户的用户组集合
     * @param userId 用户主键
     * @param queryBean   查询信息
     * @param groupEntity 查询参数
     * @return 用户组集合
     */
    public List<GroupDto> getUserGroupList(String userId, QueryBean queryBean, GroupEntity groupEntity);

    /**
     * 用户组批量添加
     *
     * @param appId       应用id
     * @param groupBeans 用户组集合
     * @return 用户组集合
     */
    public List<GroupDto> createGroupList(String appId, List<GroupBean> groupBeans);

    /**
     * 创建用户组角色关联集合
     * @param appId               应用主键
     * @param groupRoleBeans 用户组栏目关联集合
     * @return 用户组栏目关联集合
     */
    public List<GroupRoleDto> createAppGroupRoleRelationList(String appId, List<GroupRoleBean> groupRoleBeans);

    /**
     * 获取的用户组栏目关联集合
     *
     * @param appId 应用主键
     * @return 用户组栏目关联集合
     */
    public List<GroupRoleDto> getAppGroupRoleRelationList(String appId);
}
