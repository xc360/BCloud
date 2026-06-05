package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.basic.bean.RoleAuthorityBean;
import com.xc.basic.bean.RoleBean;
import com.xc.basic.dto.RoleAuthorityDto;
import com.xc.basic.dto.RoleDto;
import com.xc.basic.entity.RoleEntity;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;

import java.util.List;

/**
 * <p>角色服务接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface RoleService extends IService<RoleEntity> {

    /**
     * 获取角色分页数据
     *
     * @param current    当前页
     * @param pagingBean 分页信息
     * @param roleEntity 角色参数
     * @return 分页信息及角色数据
     */
    public PagingDto<RoleDto> getRolePage(Integer current, PagingBean pagingBean, RoleEntity roleEntity);

    /**
     * 创建应用角色
     *
     * @param authorityIds 权限id集合
     * @param roleEntity   角色参数
     * @return 角色信息
     */
    public RoleDto createRole(RoleEntity roleEntity, List<String> authorityIds);

    /**
     * 修改应用角色
     *
     * @param authorityIds 权限id集合
     * @param roleEntity   角色参数
     * @return 角色信息
     */
    public RoleDto updateRole(RoleEntity roleEntity, List<String> authorityIds);

    /**
     * 删除角色
     *
     * @param roleId 角色id
     */
    public void deleteRole(String roleId);

    /**
     * 查询应用的角色
     *
     * @param roleId 角色id
     * @return 角色信息
     */
    public RoleDto getRole(String roleId);

    /**
     * 获取信息集合
     *
     * @param queryBean  查询信息
     * @param roleEntity 查询参数
     * @return 字典集合
     */
    public List<RoleDto> getRoleList(QueryBean queryBean, RoleEntity roleEntity);

    /**
     * 获取应用下用户的角色集合
     *
     * @param appId  应用主键id
     * @param userId 用户id
     * @return 角色集合
     */
    public List<RoleEntity> getAppUserRoleList(String appId, String userId);

    /**
     * 角色批量添加
     *
     * @param appId     应用id
     * @param roleBeans 角色集合
     * @return 角色集合
     */
    public List<RoleDto> createRoleList(String appId, List<RoleBean> roleBeans);

    /**
     * 上下移动
     *
     * @param entity 栏目实体
     * @param isUp   是否向上
     */
    public boolean move(RoleEntity entity, boolean isUp);

    /**
     * 创建角色权限关联集合
     *
     * @param appId              应用主键
     * @param roleAuthorityBeans 角色权限关联集合
     * @return 角色权限关联集合
     */
    public List<RoleAuthorityDto> createAppRoleAuthorityRelationList(String appId, List<RoleAuthorityBean> roleAuthorityBeans);

    /**
     * 获取的角色权限关联集合
     *
     * @param appId 应用主键
     * @return 角色权限关联集合
     */
    public List<RoleAuthorityDto> getAppRoleAuthorityRelationList(String appId);
}
