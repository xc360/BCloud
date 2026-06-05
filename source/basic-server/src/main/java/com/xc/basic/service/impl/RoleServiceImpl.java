package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.xc.basic.bean.RoleAuthorityBean;
import com.xc.basic.bean.RoleBean;
import com.xc.basic.dto.RoleAuthorityDto;
import com.xc.basic.dto.RoleDto;
import com.xc.basic.entity.*;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.GroupRoleMapper;
import com.xc.basic.mapper.RoleAuthorityMapper;
import com.xc.basic.mapper.RoleMapper;
import com.xc.basic.mapper.UserRoleMapper;
import com.xc.basic.service.AuthorityService;
import com.xc.basic.service.RoleService;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.model.RelationModel;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>角色服务实现</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;
    @Autowired
    private GroupRoleMapper groupRoleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private AuthorityService authorityService;


    @Override
    public PagingDto<RoleDto> getRolePage(Integer current, PagingBean pagingBean, RoleEntity roleEntity) {
        QueryWrapper<RoleEntity> queryWrapper = ServiceUtils.queryLike(roleEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<RoleEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), RoleDto::new));
    }

    @Override
    @Transactional
    public RoleDto createRole(RoleEntity roleEntity, List<String> authorityIds) {
        try {
            if (!this.save(roleEntity)) {
                throw FailCode.ROLE_CREATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.ROLE_CODE_REPEAT.getOperateException();
        }
        RoleDto roleDto = ObjectUtils.convert(new RoleDto(), roleEntity);
        roleDto.setAuthorityIds(createRoleAuthority(roleEntity.getId(), authorityIds));
        return roleDto;
    }

    /**
     * 修改角色关联权限
     *
     * @return 关联成功的权限id集合
     */
    private List<String> createRoleAuthority(String id, List<String> newIds) {
        // 查询需要删除的权限和需要创建的权限
        RoleAuthorityEntity roleAuthorityEntity = new RoleAuthorityEntity();
        roleAuthorityEntity.setRoleId(id);
        List<RoleAuthorityEntity> entities = roleAuthorityMapper.selectList(new QueryWrapper<>(roleAuthorityEntity));
        List<String> oldIds = entities.stream().map(RoleAuthorityEntity::getAuthorityId).collect(Collectors.toList());
        RelationModel relationModel = ServiceUtils.relationHandle(newIds, oldIds);
        // 删除关联
        if (relationModel.getDeleteIds().size() > 0) {
            RoleAuthorityEntity deleteRoleAuthority = new RoleAuthorityEntity();
            deleteRoleAuthority.setRoleId(id);
            QueryWrapper<RoleAuthorityEntity> queryWrapper = new QueryWrapper<>(deleteRoleAuthority);
            queryWrapper.lambda().in(RoleAuthorityEntity::getAuthorityId, relationModel.getDeleteIds());
            roleAuthorityMapper.delete(queryWrapper);
        }
        // 添加关联
        for (String authorityId : relationModel.getCreateIds()) {
            RoleAuthorityEntity roleAuthority = new RoleAuthorityEntity();
            roleAuthority.setRoleId(id);
            roleAuthority.setAuthorityId(authorityId);
            if (!SqlHelper.retBool(roleAuthorityMapper.insert(roleAuthority))) {
                throw FailCode.ROLE_AND_AUTHORITY_RELATION_CREATE_FAIL.getOperateException();
            }
        }
        return relationModel.getCreateIds();
    }

    @Override
    @Transactional
    public RoleDto updateRole(RoleEntity roleEntity, List<String> authorityIds) {
        try {
            if (!this.updateById(roleEntity)) {
                throw FailCode.ROLE_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.ROLE_CODE_REPEAT.getOperateException();
        }
        RoleDto roleDto = ObjectUtils.convert(new RoleDto(), roleEntity);
        roleDto.setAuthorityIds(createRoleAuthority(roleEntity.getId(), authorityIds));
        return roleDto;
    }

    @Override
    @Transactional
    public void deleteRole(String roleId) {
        // 删除角色和权限关联
        RoleAuthorityEntity roleAuthorityEntity = new RoleAuthorityEntity();
        roleAuthorityEntity.setRoleId(roleId);
        roleAuthorityMapper.delete(new QueryWrapper<>(roleAuthorityEntity));
        // 删除角色和组的关联
        GroupRoleEntity groupRoleEntity = new GroupRoleEntity();
        groupRoleEntity.setRoleId(roleId);
        groupRoleMapper.delete(new QueryWrapper<>(groupRoleEntity));
        // 删除角色和组的关联
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setRoleId(roleId);
        userRoleMapper.delete(new QueryWrapper<>(userRoleEntity));
        // 删除角色
        if (!this.removeById(roleId)) {
            throw FailCode.ROLE_DELETE_FAIL.getOperateException();
        }
    }

    @Override
    public RoleDto getRole(String roleId) {
        RoleEntity roleEntity = this.getById(roleId);
        RoleAuthorityEntity roleAuthorityEntity = new RoleAuthorityEntity();
        roleAuthorityEntity.setRoleId(roleEntity.getId());
        List<RoleAuthorityEntity> entities = roleAuthorityMapper.selectList(new QueryWrapper<>(roleAuthorityEntity));
        List<String> authorityIds = new ArrayList<>();
        for (RoleAuthorityEntity groupEntity : entities) {
            authorityIds.add(groupEntity.getAuthorityId());
        }
        RoleDto roleDto = ObjectUtils.convert(new RoleDto(), roleEntity);
        roleDto.setAuthorityIds(authorityIds);
        return roleDto;
    }

    @Override
    public List<RoleDto> getRoleList(QueryBean queryBean, RoleEntity roleEntity) {
        QueryWrapper<RoleEntity> queryWrapper = ServiceUtils.queryLike(roleEntity, queryBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, queryBean);
        List<RoleEntity> dictEntities = list(queryWrapper);
        return ObjectUtils.convertList(dictEntities, RoleDto::new);
    }

    @Override
    public List<RoleEntity> getAppUserRoleList(String appId, String userId) {
        // 获取当前用户所在用户组下的所有用户id
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUserId(userId);
        List<UserRoleEntity> userRoleEntities = userRoleMapper.selectList(new QueryWrapper<>(userRoleEntity));
        List<String> roleIds = new ArrayList<>();
        for (UserRoleEntity userRole : userRoleEntities) {
            roleIds.add(userRole.getRoleId());
        }
        if (roleIds.size() > 0) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setAppId(appId);
            QueryWrapper<RoleEntity> queryWrapper = new QueryWrapper<>(roleEntity);
            queryWrapper.lambda().in(RoleEntity::getId, roleIds);
            return list(queryWrapper);
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public List<RoleDto> createRoleList(String appId, List<RoleBean> roleBeans) {
        // 删除查询角色
        RoleEntity entity = new RoleEntity();
        entity.setAppId(appId);
        List<RoleEntity> roleEntities = this.list(new QueryWrapper<>(entity));
        List<String> roleIds = roleEntities.stream().map(RoleEntity::getId).collect(Collectors.toList());
        if (roleIds.size() > 0) {
            // 删除角色和权限关联
            QueryWrapper<RoleAuthorityEntity> roleWrapper = new QueryWrapper<>(new RoleAuthorityEntity());
            roleWrapper.lambda().in(RoleAuthorityEntity::getRoleId, roleIds);
            roleAuthorityMapper.delete(roleWrapper);
            // 删除角色和组的关联
            QueryWrapper<GroupRoleEntity> groupWrapper = new QueryWrapper<>(new GroupRoleEntity());
            groupWrapper.lambda().in(GroupRoleEntity::getRoleId, roleIds);
            groupRoleMapper.delete(groupWrapper);
            // 删除角色和用户的关联
            QueryWrapper<UserRoleEntity> userWrapper = new QueryWrapper<>(new UserRoleEntity());
            userWrapper.lambda().in(UserRoleEntity::getRoleId, roleIds);
            userRoleMapper.delete(userWrapper);
        }
        // 删除所有角色
        remove(new QueryWrapper<>(entity));
        // 批量添加角色
        List<RoleEntity> entities = new ArrayList<>();
        for (RoleBean roleBean : roleBeans) {
            RoleEntity roleEntity = ObjectUtils.convert(new RoleEntity(), roleBean);
            roleEntity.setAppId(appId);
            entities.add(roleEntity);
        }
        try {
            if (entities.size() > 0) {
                if (!this.saveBatch(entities)) {
                    throw FailCode.ROLE_CREATE_FAIL.getOperateException();
                }
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.ROLE_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convertList(entities, RoleDto::new);
    }

    @Override
    @Transactional
    public boolean move(RoleEntity entity, boolean isUp) {
        // 查询需要处理的权限
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setAppId(entity.getAppId());
        QueryWrapper<RoleEntity> queryWrapper = new QueryWrapper<>(roleEntity);
        queryWrapper.lambda().orderByAsc(RoleEntity::getSeq);
        List<RoleEntity> entities = this.list(queryWrapper);
        Set<Long> entitySet = entities.stream().map(RoleEntity::getSeq).collect(Collectors.toSet());
        if (entitySet.size() != entities.size() || entities.get(entities.size() - 1).getSeq() != entities.size()) {
            for (int i = 0; i < entities.size(); i++) {
                RoleEntity role = entities.get(i);
                if (role.getSeq() != i + 1) {
                    role.setSeq(i + 1L);
                    this.updateById(role);
                }
            }
        }
        for (int i = 0; i < entities.size(); i++) {
            RoleEntity role = entities.get(i);
            if (entity.getId().equals(role.getId())) {
                RoleEntity role1;
                if (isUp) {
                    if (entities.size() <= i + 1) {
                        return false;
                    }
                    role1 = entities.get(i + 1);
                } else {
                    if (i - 1 < 0) {
                        return false;
                    }
                    role1 = entities.get(i - 1);
                }
                Long seq = role1.getSeq();
                role1.setSeq(role.getSeq());
                this.updateById(role1);
                // 修改原位置
                role.setSeq(seq);
                this.updateById(role);
            }
        }
        return true;
    }

    @Override
    public List<RoleAuthorityDto> createAppRoleAuthorityRelationList(String appId, List<RoleAuthorityBean> roleAuthorityBeans) {
        // 删除角色权限
        deleteRoleAuthority(appId);
        // 查询角色
        List<String> roleCodes = roleAuthorityBeans.stream().map(RoleAuthorityBean::getRoleCode).collect(Collectors.toList());
        List<RoleEntity> roleEntities = new ArrayList<>();
        if (roleCodes.size() > 0) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setAppId(appId);
            QueryWrapper<RoleEntity> roleWrapper = new QueryWrapper<>(roleEntity);
            roleWrapper.lambda().in(RoleEntity::getCode, roleCodes);
            roleEntities = this.list(roleWrapper);
        }
        // 查询权限
        List<String> authorityCodes = roleAuthorityBeans.stream().map(RoleAuthorityBean::getAuthorityCode).collect(Collectors.toList());
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        if (roleCodes.size() > 0) {
            AuthorityEntity authorityEntity = new AuthorityEntity();
            authorityEntity.setAppId(appId);
            QueryWrapper<AuthorityEntity> authorityWrapper = new QueryWrapper<>(authorityEntity);
            authorityWrapper.lambda().in(AuthorityEntity::getCode, authorityCodes);
            authorityEntities = authorityService.list(authorityWrapper);
        }
        // 处理数据
        List<RoleAuthorityDto> authorityRelationList = new ArrayList<>();
        for (RoleAuthorityBean roleAuthorityBean : roleAuthorityBeans) {
            String authorityId = null;
            for (AuthorityEntity entity : authorityEntities) {
                if (entity.getCode().equals(roleAuthorityBean.getAuthorityCode())) {
                    authorityId = entity.getId();
                }
            }
            String roleId = null;
            for (RoleEntity entity : roleEntities) {
                if (entity.getCode().equals(roleAuthorityBean.getRoleCode())) {
                    roleId = entity.getId();
                }
            }
            if (authorityId != null && roleId != null) {
                RoleAuthorityEntity roleAuthority = new RoleAuthorityEntity();
                roleAuthority.setRoleId(roleId);
                roleAuthority.setAuthorityId(authorityId);
                if (!SqlHelper.retBool(roleAuthorityMapper.insert(roleAuthority))) {
                    throw FailCode.ROLE_AND_AUTHORITY_RELATION_CREATE_FAIL.getOperateException();
                }
                authorityRelationList.add(ObjectUtils.convert(new RoleAuthorityDto(), roleAuthorityBean));
            }
        }
        return authorityRelationList;
    }

    @Override
    public List<RoleAuthorityDto> getAppRoleAuthorityRelationList(String appId) {
        List<RoleAuthorityEntity> roleAuthorityEntities = roleAuthorityMapper.selectList(new QueryWrapper<>());
        // 查询角色
        List<String> roleIds = roleAuthorityEntities.stream().map(RoleAuthorityEntity::getRoleId).collect(Collectors.toList());
        List<RoleEntity> roleEntities = new ArrayList<>();
        if (roleIds.size() > 0) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setAppId(appId);
            QueryWrapper<RoleEntity> roleWrapper = new QueryWrapper<>(roleEntity);
            roleWrapper.lambda().in(RoleEntity::getId, roleIds);
            roleEntities = this.list(roleWrapper);
        }
        // 查询权限
        List<String> authorityIds = roleAuthorityEntities.stream().map(RoleAuthorityEntity::getAuthorityId).collect(Collectors.toList());
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        if (authorityIds.size() > 0) {
            AuthorityEntity authorityEntity = new AuthorityEntity();
            authorityEntity.setAppId(appId);
            QueryWrapper<AuthorityEntity> authorityWrapper = new QueryWrapper<>(authorityEntity);
            authorityWrapper.lambda().in(AuthorityEntity::getId, authorityIds);
            authorityEntities = authorityService.list(authorityWrapper);
        }
        // 处理数据
        List<RoleAuthorityDto> authorityRelationList = new ArrayList<>();
        for (RoleAuthorityEntity roleAuthorityEntity : roleAuthorityEntities) {
            String authorityCode = null;
            for (AuthorityEntity entity : authorityEntities) {
                if (entity.getId().equals(roleAuthorityEntity.getAuthorityId())) {
                    authorityCode = entity.getCode();
                }
            }
            String roleCode = null;
            for (RoleEntity entity : roleEntities) {
                if (entity.getId().equals(roleAuthorityEntity.getRoleId())) {
                    roleCode = entity.getCode();
                }
            }
            if (authorityCode != null && roleCode != null) {
                RoleAuthorityDto roleAuthorityDto = new RoleAuthorityDto();
                roleAuthorityDto.setRoleCode(roleCode);
                roleAuthorityDto.setAuthorityCode(authorityCode);
                authorityRelationList.add(roleAuthorityDto);
            }
        }
        return authorityRelationList;
    }

    /**
     * 获取页面栏目Model
     *
     * @param appId 应用主键
     */
    private void deleteRoleAuthority(String appId) {
        // 删除查询角色
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setAppId(appId);
        List<RoleEntity> roleEntities = this.list(new QueryWrapper<>(roleEntity));
        List<String> roleIds = roleEntities.stream().map(RoleEntity::getId).collect(Collectors.toList());
        if (roleIds.size() > 0) {
            QueryWrapper<RoleAuthorityEntity> roleWrapper = new QueryWrapper<>(new RoleAuthorityEntity());
            roleWrapper.lambda().in(RoleAuthorityEntity::getRoleId, roleIds);
            roleAuthorityMapper.delete(roleWrapper);
        }
        // 删除查询权限
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAppId(appId);
        List<AuthorityEntity> authorityEntities = authorityService.list(new QueryWrapper<>(authorityEntity));
        List<String> authorityIds = authorityEntities.stream().map(AuthorityEntity::getId).collect(Collectors.toList());
        if (authorityIds.size() > 0) {
            QueryWrapper<RoleAuthorityEntity> authorityWrapper = new QueryWrapper<>(new RoleAuthorityEntity());
            authorityWrapper.lambda().in(RoleAuthorityEntity::getAuthorityId, authorityIds);
            roleAuthorityMapper.delete(authorityWrapper);
        }
    }
}
