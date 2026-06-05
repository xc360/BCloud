package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.xc.api.basic.dto.GroupDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.GroupBean;
import com.xc.basic.bean.GroupRoleBean;
import com.xc.basic.dto.GroupRoleDto;
import com.xc.basic.entity.*;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.GroupMapper;
import com.xc.basic.mapper.GroupRoleMapper;
import com.xc.basic.mapper.UserGroupMapper;
import com.xc.basic.service.GroupService;
import com.xc.basic.service.RoleService;
import com.xc.core.enums.EffectStatus;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>用户组服务接口实现</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupEntity> implements GroupService {

    @Autowired
    private GroupRoleMapper groupRoleMapper;
    @Autowired
    private UserGroupMapper userGroupMapper;
    @Autowired
    private RoleService roleService;

    @Override
    public PagingDto<GroupDto> getGroupPage(Integer current, PagingBean pagingBean, GroupEntity groupEntity) {
        QueryWrapper<GroupEntity> queryWrapper = ServiceUtils.queryLike(groupEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<GroupEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), GroupDto::new));
    }

    @Override
    @Transactional
    public GroupDto createGroup(GroupEntity groupEntity, List<String> roleIds) {
        try {
            if (!this.save(groupEntity)) {
                throw FailCode.GROUP_CREATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.GROUP_CODE_REPEAT.getOperateException();
        }
        GroupDto groupDto = ObjectUtils.convert(new GroupDto(), groupEntity);
        groupDto.setRoleIds(createGroupRole(groupEntity.getId(), roleIds));
        return groupDto;
    }

    @Override
    @Transactional
    public GroupDto updateGroup(GroupEntity groupEntity, List<String> roleIds) {
        try {
            if (!this.updateById(groupEntity)) {
                throw FailCode.GROUP_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.GROUP_CODE_REPEAT.getOperateException();
        }
        GroupDto groupDto = ObjectUtils.convert(new GroupDto(), groupEntity);
        groupDto.setRoleIds(createGroupRole(groupEntity.getId(), roleIds));
        return groupDto;
    }

    @Override
    @Transactional
    public void deleteGroup(String groupId) {
        // 删除用户和组关联
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupEntity.setGroupId(groupId);
        userGroupMapper.delete(new QueryWrapper<>(userGroupEntity));
        // 删除组和角色关联
        GroupRoleEntity groupRoleEntity = new GroupRoleEntity();
        groupRoleEntity.setGroupId(groupId);
        groupRoleMapper.delete(new QueryWrapper<>(groupRoleEntity));
        // 删除组
        if (!this.removeById(groupId)) {
            throw FailCode.GROUP_DELETE_FAIL.getOperateException();
        }
    }

    @Override
    public GroupDto getGroupById(String groupId) {
        GroupEntity groupEntity = this.getById(groupId);
        GroupRoleEntity groupRoleEntity = new GroupRoleEntity();
        groupRoleEntity.setGroupId(groupEntity.getId());
        List<GroupRoleEntity> entities = groupRoleMapper.selectList(new QueryWrapper<>(groupRoleEntity));
        List<String> roleIds = new ArrayList<>();
        for (GroupRoleEntity groupRoleEntity1 : entities) {
            roleIds.add(groupRoleEntity1.getRoleId());
        }
        GroupDto groupDto = ObjectUtils.convert(new GroupDto(), groupEntity);
        groupDto.setRoleIds(roleIds);
        return groupDto;
    }


    @Override
    public List<GroupEntity> getAppUserValidGroupList(String appId, String userId) {
        // 获取当前用户所在用户组下的所有用户id
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupEntity.setUserId(userId);
        List<UserGroupEntity> userGroupEntities = userGroupMapper.selectList(new QueryWrapper<>(userGroupEntity));
        List<String> groupIds = new ArrayList<>();
        for (UserGroupEntity userGroup : userGroupEntities) {
            groupIds.add(userGroup.getGroupId());
        }
        if (groupIds.size() > 0) {
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setAppId(appId);
            groupEntity.setStatus(EffectStatus.VALID.getStatus());
            QueryWrapper<GroupEntity> queryWrapper = new QueryWrapper<>(groupEntity);
            queryWrapper.lambda().in(GroupEntity::getId, groupIds);
            return list(queryWrapper);
        }
        return new ArrayList<>();
    }

    @Override
    public List<GroupDto> getGroupList(QueryBean queryBean, GroupEntity groupEntity) {
        QueryWrapper<GroupEntity> queryWrapper = ServiceUtils.queryLike(groupEntity, queryBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, queryBean);
        List<GroupEntity> dictEntities = list(queryWrapper);
        return ObjectUtils.convertList(dictEntities, GroupDto::new);
    }

    @Override
    public List<GroupDto> getUserGroupList(String userId, QueryBean queryBean, GroupEntity groupEntity) {
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupEntity.setUserId(userId);
        List<UserGroupEntity> userGroupEntities = userGroupMapper.selectList(new QueryWrapper<>(userGroupEntity));
        if (userGroupEntities.size() > 0) {
            List<String> groupIds = userGroupEntities.stream().map(UserGroupEntity::getGroupId).collect(Collectors.toList());
            QueryWrapper<GroupEntity> queryWrapper = ServiceUtils.queryLike(groupEntity, queryBean.getLikeFields());
            ServiceUtils.querySort(queryWrapper, queryBean);
            queryWrapper.lambda().in(GroupEntity::getId, groupIds);
            List<GroupEntity> dictEntities = list(queryWrapper);
            return ObjectUtils.convertList(dictEntities, GroupDto::new);
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public List<GroupDto> createGroupList(String appId, List<GroupBean> groupBeans) {
        // 删除查询用户组
        GroupEntity entity = new GroupEntity();
        entity.setAppId(appId);
        List<GroupEntity> groupEntities = this.list(new QueryWrapper<>(entity));
        List<String> groupIds = groupEntities.stream().map(GroupEntity::getId).collect(Collectors.toList());
        if (groupIds.size() > 0) {
            // 删除用户和组关联
            QueryWrapper<UserGroupEntity> userGroupWrapper = new QueryWrapper<>();
            userGroupWrapper.lambda().in(UserGroupEntity::getGroupId, groupIds);
            userGroupMapper.delete(userGroupWrapper);
            // 删除组和角色关联
            QueryWrapper<GroupRoleEntity> groupRoleWrapper = new QueryWrapper<>();
            groupRoleWrapper.lambda().in(GroupRoleEntity::getGroupId, groupIds);
            groupRoleMapper.delete(groupRoleWrapper);
        }
        // 删除所有用户组
        remove(new QueryWrapper<>(entity));
        // 批量添加用户组
        List<GroupEntity> entities = new ArrayList<>();
        for (GroupBean groupBean : groupBeans) {
            GroupEntity groupEntity = ObjectUtils.convert(new GroupEntity(), groupBean);
            groupEntity.setAppId(appId);
            entities.add(groupEntity);
        }
        try {
            if (entities.size() > 0) {
                if (!this.saveBatch(entities)) {
                    throw FailCode.GROUP_CREATE_FAIL.getOperateException();
                }
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.GROUP_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convertList(entities, GroupDto::new);
    }

    @Override
    public List<GroupRoleDto> createAppGroupRoleRelationList(String appId, List<GroupRoleBean> groupRoleBeans) {
        // 删除账户组角色
        deleteGroupGroup(appId);
        // 查询用户组
        List<String> groupCodes = groupRoleBeans.stream().map(GroupRoleBean::getGroupCode).collect(Collectors.toList());
        List<GroupEntity> groupEntities = new ArrayList<>();
        if (groupCodes.size() > 0) {
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setAppId(appId);
            QueryWrapper<GroupEntity> groupWrapper = new QueryWrapper<>(groupEntity);
            groupWrapper.lambda().in(GroupEntity::getCode, groupCodes);
            groupEntities = this.list(groupWrapper);
        }
        // 查询角色
        List<String> roleCodes = groupRoleBeans.stream().map(GroupRoleBean::getRoleCode).collect(Collectors.toList());
        List<RoleEntity> roleEntities = new ArrayList<>();
        if (roleCodes.size() > 0) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setAppId(appId);
            QueryWrapper<RoleEntity> roleWrapper = new QueryWrapper<>(roleEntity);
            roleWrapper.lambda().in(RoleEntity::getCode, roleCodes);
            roleEntities = roleService.list(roleWrapper);
        }
        // 处理数据
        List<GroupRoleDto> roleRelationList = new ArrayList<>();
        for (GroupRoleBean groupRoleBean : groupRoleBeans) {
            String roleId = null;
            for (RoleEntity entity : roleEntities) {
                if (entity.getCode().equals(groupRoleBean.getRoleCode())) {
                    roleId = entity.getId();
                }
            }
            String groupId = null;
            for (GroupEntity entity : groupEntities) {
                if (entity.getCode().equals(groupRoleBean.getGroupCode())) {
                    groupId = entity.getId();
                }
            }
            if (roleId != null && groupId != null) {
                GroupRoleEntity groupRole = new GroupRoleEntity();
                groupRole.setGroupId(groupId);
                groupRole.setRoleId(roleId);
                if (!SqlHelper.retBool(groupRoleMapper.insert(groupRole))) {
                    throw FailCode.GROUP_AND_ROLE_RELATION_CREATE_FAIL.getOperateException();
                }
                roleRelationList.add(ObjectUtils.convert(new GroupRoleDto(), groupRoleBean));
            }
        }
        return roleRelationList;
    }

    @Override
    public List<GroupRoleDto> getAppGroupRoleRelationList(String appId) {
        List<GroupRoleEntity> groupRoleEntities = groupRoleMapper.selectList(new QueryWrapper<>());
        // 查询用户组
        List<String> groupIds = groupRoleEntities.stream().map(GroupRoleEntity::getGroupId).collect(Collectors.toList());
        List<GroupEntity> groupEntities = new ArrayList<>();
        if (groupIds.size() > 0) {
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setAppId(appId);
            QueryWrapper<GroupEntity> groupWrapper = new QueryWrapper<>(groupEntity);
            groupWrapper.lambda().in(GroupEntity::getId, groupIds);
            groupEntities = this.list(groupWrapper);
        }
        // 查询角色
        List<String> roleIds = groupRoleEntities.stream().map(GroupRoleEntity::getRoleId).collect(Collectors.toList());
        List<RoleEntity> roleEntities = new ArrayList<>();
        if (groupIds.size() > 0) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setAppId(appId);
            QueryWrapper<RoleEntity> roleWrapper = new QueryWrapper<>(roleEntity);
            roleWrapper.lambda().in(RoleEntity::getId, roleIds);
            roleEntities = roleService.list(roleWrapper);
        }
        // 处理数据
        List<GroupRoleDto> roleRelationList = new ArrayList<>();
        for (GroupRoleEntity groupRoleEntity : groupRoleEntities) {
            String roleCode = null;
            for (RoleEntity entity : roleEntities) {
                if (entity.getId().equals(groupRoleEntity.getRoleId())) {
                    roleCode = entity.getCode();
                }
            }
            String groupCode = null;
            for (GroupEntity entity : groupEntities) {
                if (entity.getId().equals(groupRoleEntity.getGroupId())) {
                    groupCode = entity.getCode();
                }
            }
            if (roleCode != null && groupCode != null) {
                GroupRoleDto groupRoleDto = new GroupRoleDto();
                groupRoleDto.setGroupCode(groupCode);
                groupRoleDto.setRoleCode(roleCode);
                roleRelationList.add(groupRoleDto);
            }
        }
        return roleRelationList;
    }

    /**
     * 获取页面栏目Model
     *
     * @param appId 应用主键
     */
    private void deleteGroupGroup(String appId) {
        // 删除查询账户组
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setAppId(appId);
        List<GroupEntity> groupEntities = this.list(new QueryWrapper<>(groupEntity));
        List<String> groupIds = groupEntities.stream().map(GroupEntity::getId).collect(Collectors.toList());
        if (groupIds.size() > 0) {
            QueryWrapper<GroupRoleEntity> groupWrapper = new QueryWrapper<>(new GroupRoleEntity());
            groupWrapper.lambda().in(GroupRoleEntity::getGroupId, groupIds);
            groupRoleMapper.delete(groupWrapper);
        }
        // 删除查询角色
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setAppId(appId);
        List<RoleEntity> roleEntities = roleService.list(new QueryWrapper<>(roleEntity));
        List<String> roleIds = roleEntities.stream().map(RoleEntity::getId).collect(Collectors.toList());
        if (roleIds.size() > 0) {
            QueryWrapper<GroupRoleEntity> roleWrapper = new QueryWrapper<>(new GroupRoleEntity());
            roleWrapper.lambda().in(GroupRoleEntity::getRoleId, roleIds);
            groupRoleMapper.delete(roleWrapper);
        }
    }

    /**
     * 创建用户组权限
     *
     * @return 关联成功的角色id集合
     */
    private List<String> createGroupRole(String id, List<String> roleIds) {
        // 删除所有角色
        GroupRoleEntity groupRoleEntity = new GroupRoleEntity();
        groupRoleEntity.setGroupId(id);
        groupRoleMapper.delete(new QueryWrapper<>(groupRoleEntity));
        if (roleIds != null) {
            for (String roleId : roleIds) {
                GroupRoleEntity groupRole = new GroupRoleEntity();
                groupRole.setGroupId(id);
                groupRole.setRoleId(roleId);
                if (!SqlHelper.retBool(groupRoleMapper.insert(groupRole))) {
                    throw FailCode.GROUP_AND_ROLE_RELATION_CREATE_FAIL.getOperateException();
                }
            }
            return roleIds;
        }
        return new ArrayList<>();
    }

}
