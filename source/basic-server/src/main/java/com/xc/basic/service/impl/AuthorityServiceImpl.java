package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.dto.AuthorityDto;
import com.xc.basic.bean.AuthorityBean;
import com.xc.basic.bean.QueryAuthorityBean;
import com.xc.basic.config.Constants;
import com.xc.basic.entity.*;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.*;
import com.xc.basic.service.AppService;
import com.xc.basic.service.AuthorityService;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.enums.AuditStatus;
import com.xc.core.enums.EffectStatus;
import com.xc.core.enums.Whether;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>权限服务实现类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, AuthorityEntity> implements AuthorityService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private GroupRoleMapper groupRoleMapper;
    @Autowired
    private AuthorizeMapper authorizeMapper;
    @Autowired
    private AppService appService;
    @Autowired
    private Constants constants;
    @Autowired
    private UserAuthorizeAuthorityMapper userAuthorizeAuthorityMapper;

    @Override
    public PagingDto<AuthorityDto> getAppAuthorityPage(String appId, Integer current, PagingBean pagingBean, QueryAuthorityBean queryAuthorityBean) {
        AuthorityEntity authorityEntity = ObjectUtils.convert(new AuthorityEntity(), queryAuthorityBean);
        authorityEntity.setAppId(appId);
        QueryWrapper<AuthorityEntity> queryWrapper = ServiceUtils.queryLike(authorityEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<AuthorityEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), AuthorityDto::new));
    }

    @Override
    public List<AuthorityEntity> getAppUserValidAuthorityList(String appId, UserEntity userEntity) {
        // 判断是否是超级管理员
        if (constants.getOpenInitialAdmin() && Whether.YES.getValue().equals(userEntity.getInitialAdmin())) {
            AuthorityEntity authorityEntity = new AuthorityEntity();
            authorityEntity.setAppId(appId);
            return authorityMapper.selectList(new QueryWrapper<>(authorityEntity));
        }
        return getAppUserGroupValidAuthorityList(appId, userEntity, null);
    }

    @Override
    public List<AuthorityEntity> getAppUserGroupValidAuthorityList(String appId, UserEntity userEntity, List<String> groupIds) {
        // 查询应用信息
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAppId(appId); //应用主键id
        List<String> roleIds = new ArrayList<>();
        // 查询用户组角色
        List<GroupRoleEntity> groupRoles = new ArrayList<>();
        if (groupIds != null && groupIds.size() > 0) {
            groupRoles = groupRoleMapper.selectList(new QueryWrapper<>(new GroupRoleEntity()).lambda().in(GroupRoleEntity::getGroupId, groupIds));
        }
        for (GroupRoleEntity groupRole : groupRoles) {
            roleIds.add(groupRole.getRoleId());
        }
        // 查询用户角色
        if (userEntity != null) {
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUserId(userEntity.getId());
            List<UserRoleEntity> userRoles = userRoleMapper.selectList(new QueryWrapper<>(userRoleEntity));
            for (UserRoleEntity userRole : userRoles) {
                roleIds.add(userRole.getRoleId());
            }
        }
        if (roleIds.size() > 0) {
            // 查询角色权限关联
            List<RoleAuthorityEntity> roleAuthorityList = roleAuthorityMapper.selectList(
                    new QueryWrapper<>(new RoleAuthorityEntity()).lambda().in(RoleAuthorityEntity::getRoleId, roleIds));
            //查询权限
            List<String> authorityIds = new ArrayList<>();
            for (RoleAuthorityEntity roleAuthority : roleAuthorityList) {
                authorityIds.add(roleAuthority.getAuthorityId());
            }
            // 查询权限信息
            return getValidAuthorityListByIds(authorityIds, authorityEntity);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<AuthorityEntity> getAppValidAuthorityList(String id, String appId, String type) {
        AuthorizeEntity authorizeEntity = new AuthorizeEntity();
        authorizeEntity.setAppId(id);
        authorizeEntity.setAuditStatus(AuditStatus.REVIEWED.getStatus());
        List<AuthorizeEntity> appAuthorityList = authorizeMapper.selectList(new QueryWrapper<>(authorizeEntity));
        List<String> authorityIds = appAuthorityList.stream().map(AuthorizeEntity::getAuthorityId).collect(Collectors.toList());
        // 查权限
        AuthorityEntity authorityEntity = new AuthorityEntity();
        if (appId != null) {
            authorityEntity.setAppId(appId);
        }
        if (type != null) {
            authorityEntity.setType(type);
        }
        return getValidAuthorityListByIds(authorityIds, authorityEntity);
    }

    @Override
    @Transactional
    public void deleteAuthority(AuthorityEntity authorityEntity) {
        // 删除下级权限
        List<AuthorityEntity> entities = getChildrenAuthority(authorityEntity.getAppId(), authorityEntity.getNode());
        List<String> authorityIds = entities.stream().map(AuthorityEntity::getId).collect(Collectors.toList());
        authorityIds.add(authorityEntity.getId());
        // 删除权限关联
        deleteAuthorityRelation(authorityIds);
        // 删除权限及下级权限
        if (!this.removeByIds(authorityIds)) {
            throw FailCode.AUTHORITY_DELETE_FAIL.getOperateException();
        }
    }

    /**
     * 删除权限关联
     *
     * @param authorityIds 权限id集合
     */
    private void deleteAuthorityRelation(List<String> authorityIds) {
        // 删除权限和角色的关联
        QueryWrapper<RoleAuthorityEntity> roleAuthorityWrapper = new QueryWrapper<>(new RoleAuthorityEntity());
        roleAuthorityWrapper.lambda().in(RoleAuthorityEntity::getAuthorityId, authorityIds);
        roleAuthorityMapper.delete(roleAuthorityWrapper);
        // 删除权限应用关联
        QueryWrapper<AuthorizeEntity> AppAuthorityWrapper = new QueryWrapper<>(new AuthorizeEntity());
        AppAuthorityWrapper.lambda().in(AuthorizeEntity::getAuthorityId, authorityIds);
        authorizeMapper.delete(AppAuthorityWrapper);
        // 删除用户授权的权限
        QueryWrapper<UserAuthorizeAuthorityEntity> userAuthorizeAuthorityWrapper = new QueryWrapper<>(new UserAuthorizeAuthorityEntity());
        userAuthorizeAuthorityWrapper.lambda().in(UserAuthorizeAuthorityEntity::getAuthorityId, authorityIds);
        userAuthorizeAuthorityMapper.delete(userAuthorizeAuthorityWrapper);
    }

    /**
     * 获取所有子集权限
     *
     * @param node 节点
     * @return 子集权限集合
     */
    @Override
    public List<AuthorityEntity> getChildrenAuthority(String appId, String node) {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setParentNode(node);
        authorityEntity.setAppId(appId);
        List<AuthorityEntity> entities = authorityMapper.selectList(new QueryWrapper<>(authorityEntity));
        int index = 0;
        while (index < entities.size()) {
            AuthorityEntity entity = entities.get(index);
            AuthorityEntity authority = new AuthorityEntity();
            authority.setParentNode(entity.getNode());
            authority.setAppId(appId);
            List<AuthorityEntity> authorityList = authorityMapper.selectList(new QueryWrapper<>(authority));
            entities.addAll(authorityList);
            index++;
        }
        return entities;
    }

    @Override
    @Transactional
    public List<AuthorityDto> createAppAuthorityList(String appId, List<AuthorityBean> authorityBeans) {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setAppId(appId);
        QueryWrapper<AuthorityEntity> queryWrapper = new QueryWrapper<>(authority);
        List<AuthorityEntity> authorityEntities = authorityMapper.selectList(queryWrapper);
        for (AuthorityEntity entity : authorityEntities) {
            // 删除权限关联
            deleteAuthorityRelation(Collections.singletonList(entity.getId()));
        }
        // 删除应用的所有权限
        authorityMapper.delete(queryWrapper);
        // 批量添加权限
        List<AuthorityEntity> entities = new ArrayList<>();
        for (AuthorityBean authorityBean : authorityBeans) {
            AuthorityEntity authorityEntity = ObjectUtils.convert(new AuthorityEntity(), authorityBean);
            authorityEntity.setAppId(appId);
            entities.add(authorityEntity);
        }
        try {
            if (entities.size() > 0) {
                if (!this.saveBatch(entities)) {
                    throw FailCode.AUTHORITY_CREATE_FAIL.getOperateException();
                }
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.AUTHORITY_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convertList(entities, AuthorityDto::new);
    }

    @Override
    public List<AuthorityEntity> getAppAuthorityList(String appId, QueryBean queryBean, QueryAuthorityBean queryAuthorityBean) {
        AuthorityEntity authorityEntity = ObjectUtils.convert(new AuthorityEntity(), queryAuthorityBean);
        authorityEntity.setAppId(appId);
        QueryWrapper<AuthorityEntity> queryWrapper = ServiceUtils.queryLike(authorityEntity, queryBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, queryBean);
        if (queryAuthorityBean.getTypes() != null && queryAuthorityBean.getTypes().size() > 0) {
            queryWrapper.lambda().in(AuthorityEntity::getType, queryAuthorityBean.getTypes());
        }
        return list(queryWrapper);
    }

    @Override
    public PagingDto<AuthorityEntity> getAppAuthorityPage(Integer current, PagingBean pagingBean, List<String> types, AuthorityEntity authorityEntity) {
        // 判断类型不为空
        if (types.size() == 0) {
            return new PagingDto<>(0L, new ArrayList<>());
        }
        // 查应用
        AppEntity app = new AppEntity();
        app.setAuditStatus(AuditStatus.REVIEWED.getStatus());
        app.setStatus(EffectStatus.VALID.getStatus());
        List<AppEntity> appEntities = appService.list(new QueryWrapper<>(app));
        List<String> appIds = appEntities.stream().map(AppEntity::getId).collect(Collectors.toList());
        if (appIds.size() > 0) {
            // 查权限
            QueryWrapper<AuthorityEntity> queryWrapper = ServiceUtils.queryLike(authorityEntity, pagingBean.getLikeFields());
            queryWrapper.lambda().in(AuthorityEntity::getType, types);
            queryWrapper.lambda().in(AuthorityEntity::getAppId, appIds);
            ServiceUtils.querySort(queryWrapper, pagingBean);
            IPage<AuthorityEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
            return new PagingDto<>(iPage.getTotal(), iPage.getRecords());
        } else {
            return new PagingDto<>(0L, new ArrayList<>());
        }
    }

    /**
     * 根据角色权限关联集合获取权限集合
     *
     * @param authorityIds    权限id集合
     * @param authorityEntity 权限条件
     * @return 权限集合
     */
    private List<AuthorityEntity> getValidAuthorityListByIds(List<String> authorityIds, AuthorityEntity authorityEntity) {
        if (authorityIds.size() == 0) {
            return new ArrayList<>();
        }
        authorityEntity.setStatus(EffectStatus.VALID.getStatus());
        QueryWrapper<AuthorityEntity> wrapper = new QueryWrapper<>(authorityEntity);
        wrapper.lambda().in(AuthorityEntity::getId, authorityIds);
        return authorityMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public boolean move(AuthorityEntity entity, boolean isUp) {
        // 查询需要处理的权限
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAppId(entity.getAppId());
        authorityEntity.setParentNode(entity.getParentNode());
        QueryWrapper<AuthorityEntity> queryWrapper = new QueryWrapper<>(authorityEntity);
        queryWrapper.lambda().orderByAsc(AuthorityEntity::getSeq);
        List<AuthorityEntity> entities = this.list(queryWrapper);
        Set<Long> entitySet = entities.stream().map(AuthorityEntity::getSeq).collect(Collectors.toSet());
        if (entitySet.size() != entities.size() || entities.get(entities.size() - 1).getSeq() != entities.size()) {
            for (int i = 0; i < entities.size(); i++) {
                AuthorityEntity authority = entities.get(i);
                if (authority.getSeq() != i + 1) {
                    authority.setSeq(i + 1L);
                    this.updateById(authority);
                }
            }
        }
        for (int i = 0; i < entities.size(); i++) {
            AuthorityEntity authority = entities.get(i);
            if (entity.getId().equals(authority.getId())) {
                AuthorityEntity authority1;
                if (isUp) {
                    if (entities.size() <= i + 1) {
                        return false;
                    }
                    authority1 = entities.get(i + 1);
                } else {
                    if (i - 1 < 0) {
                        return false;
                    }
                    authority1 = entities.get(i - 1);
                }
                Long seq = authority1.getSeq();
                authority1.setSeq(authority.getSeq());
                this.updateById(authority1);
                // 修改原位置
                authority.setSeq(seq);
                this.updateById(authority);
            }
        }
        return true;
    }
}
