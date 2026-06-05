package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xc.basic.config.Constants;
import com.xc.basic.entity.*;
import com.xc.basic.mapper.*;
import com.xc.basic.service.BasicService;
import com.xc.basic.service.DeletedUserService;
import com.xc.basic.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 基础ServiceImpl
 * </p>
 *
 * @author xc
 * @since 2026-05-14
 */
@Slf4j
@Service
public class BasicServiceImpl implements BasicService {
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private AuthorizeMapper authorizeMapper;
    @Autowired
    private AppMapper appMapper;
    @Autowired
    private UserAuthorizeMapper userAuthorizeMapper;
    @Autowired
    private GroupRoleMapper groupRoleMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private UserGroupMapper userGroupMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private ColumnMapper columnMapper;
    @Autowired
    private PageColumnMapper pageColumnMapper;
    @Autowired
    private PageMapper pageMapper;
    @Autowired
    private UserAuthorizeAuthorityMapper userAuthorizeAuthorityMapper;
    @Autowired
    private Constants constants;
    @Autowired
    private DeletedUserService deletedUserService;
    @Autowired
    private UserInfoService userInfoService;


    @Override
    public void testData() {
        // 查询未完全注销的用户
        long expireTime = System.currentTimeMillis() - constants.getLogOffGuardTime();
        QueryWrapper<DeletedUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ge(DeletedUserEntity::getDeleteTime, new Date(expireTime));
        long count = deletedUserService.count(queryWrapper);
        log.info("已注销用户数量：{}", count);
        // 角色关联权限-权限主键
        List<AuthorityEntity> authorityEntities = authorityMapper.selectList(new QueryWrapper<>());
        List<RoleAuthorityEntity> roleAuthorityEntities = roleAuthorityMapper.selectList(new QueryWrapper<>());
        for (RoleAuthorityEntity roleAuthorityEntity : roleAuthorityEntities) {
            boolean bool = true;
            for (AuthorityEntity entity : authorityEntities) {
                if (entity.getId().equals(roleAuthorityEntity.getAuthorityId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("角色关联权限-权限主键：{}", roleAuthorityEntity);
            }
        }
        // 角色关联权限-角色主键
        List<RoleEntity> roleEntities = roleMapper.selectList(new QueryWrapper<>());
        for (RoleAuthorityEntity roleAuthorityEntity : roleAuthorityEntities) {
            boolean bool = true;
            for (RoleEntity entity : roleEntities) {
                if (entity.getId().equals(roleAuthorityEntity.getRoleId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("角色关联权限-角色主键：{}", roleAuthorityEntity);
            }

        }
        // 用户关联角色-用户主键
        List<UserEntity> userEntities = userMapper.selectList(new QueryWrapper<>());
        List<UserRoleEntity> userRoleEntities = userRoleMapper.selectList(new QueryWrapper<>());
        for (UserRoleEntity userRoleEntity : userRoleEntities) {
            boolean bool = true;
            for (UserEntity entity : userEntities) {
                if (entity.getId().equals(userRoleEntity.getUserId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("用户关联角色-用户主键：{}", userRoleEntity);
            }
        }
        // 用户关联角色-角色主键
        for (UserRoleEntity userRoleEntity : userRoleEntities) {
            boolean bool = true;
            for (RoleEntity entity : roleEntities) {
                if (entity.getId().equals(userRoleEntity.getRoleId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("用户关联角色-角色主键：{}", userRoleEntity);
            }
        }
        // 权限关联应用-权限主键
        List<AuthorizeEntity> appAuthorityEntities = authorizeMapper.selectList(new QueryWrapper<>());
        for (AuthorizeEntity authorizeEntity : appAuthorityEntities) {
            boolean bool = true;
            for (AuthorityEntity entity : authorityEntities) {
                if (entity.getId().equals(authorizeEntity.getAuthorityId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("权限关联应用-权限主键：{}", authorizeEntity);
            }
        }
        // 权限关联应用-应用主键
        List<AppEntity> appEntities = appMapper.selectList(new QueryWrapper<>());
        for (AuthorizeEntity authorizeEntity : appAuthorityEntities) {
            boolean bool = true;
            for (AppEntity entity : appEntities) {
                if (entity.getId().equals(authorizeEntity.getAppId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("权限关联应用-应用主键：{}", authorizeEntity);
            }
        }
        // 应用关联用户-用户主键
        List<UserAuthorizeEntity> appUserEntities = userAuthorizeMapper.selectList(new QueryWrapper<>());
        for (UserAuthorizeEntity userAuthorizeEntity : appUserEntities) {
            boolean bool = true;
            for (UserEntity entity : userEntities) {
                if (entity.getId().equals(userAuthorizeEntity.getUserId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("应用关联用户-用户主键：{}", userAuthorizeEntity);
            }
        }
        // 应用关联用户-应用主键
        for (UserAuthorizeEntity userAuthorizeEntity : appUserEntities) {
            boolean bool = true;
            for (AppEntity entity : appEntities) {
                if (entity.getId().equals(userAuthorizeEntity.getAppId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("应用关联用户-应用主键：{}", userAuthorizeEntity);
            }
        }
        // 用户授权关联权限-权限主键
        List<UserAuthorizeAuthorityEntity> userAuthorizeAuthorityEntities = userAuthorizeAuthorityMapper.selectList(new QueryWrapper<>());
        for (UserAuthorizeAuthorityEntity userAuthorizeAuthorityEntity : userAuthorizeAuthorityEntities) {
            boolean bool = true;
            for (AuthorityEntity entity : authorityEntities) {
                if (entity.getId().equals(userAuthorizeAuthorityEntity.getAuthorityId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("用户授权关联权限-权限主键：{}", userAuthorizeAuthorityEntity);
            }
        }
        // 用户授权关联权限-用户授权主键
        for (UserAuthorizeAuthorityEntity userAuthorizeAuthorityEntity : userAuthorizeAuthorityEntities) {
            boolean bool = true;
            for (UserAuthorizeEntity entity : appUserEntities) {
                if (entity.getId().equals(userAuthorizeAuthorityEntity.getUserAuthorizeId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("用户授权关联权限-用户授权主键：{}", userAuthorizeAuthorityEntity);
            }
        }
        // 组关联角色-角色主键
        List<GroupRoleEntity> groupRoleEntities = groupRoleMapper.selectList(new QueryWrapper<>());
        for (GroupRoleEntity groupRoleEntity : groupRoleEntities) {
            boolean bool = true;
            for (RoleEntity entity : roleEntities) {
                if (entity.getId().equals(groupRoleEntity.getRoleId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("组关联角色-角色主键：{}", groupRoleEntity);
            }
        }
        // 组关联角色-组主键
        List<GroupEntity> groupEntities = groupMapper.selectList(new QueryWrapper<>());
        for (GroupRoleEntity groupRoleEntity : groupRoleEntities) {
            boolean bool = true;
            for (GroupEntity entity : groupEntities) {
                if (entity.getId().equals(groupRoleEntity.getGroupId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("组关联角色-组主键：{}", groupRoleEntity);
            }
        }
        // 组关联用户-用户主键
        List<UserGroupEntity> userGroupEntities = userGroupMapper.selectList(new QueryWrapper<>());
        for (UserGroupEntity userGroupEntity : userGroupEntities) {
            boolean bool = true;
            for (UserEntity entity : userEntities) {
                if (entity.getId().equals(userGroupEntity.getUserId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("组关联用户-用户主键：{}", userGroupEntity);
            }
        }
        // 组关联用户-组主键
        for (UserGroupEntity userGroupEntity : userGroupEntities) {
            boolean bool = true;
            for (GroupEntity entity : groupEntities) {
                if (entity.getId().equals(userGroupEntity.getGroupId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("组关联用户-组主键：{}", userGroupEntity);
            }
        }
        List<PageEntity> pageEntities = pageMapper.selectList(new QueryWrapper<>());
        List<ColumnEntity> columnEntities = columnMapper.selectList(new QueryWrapper<>());
        // 页面关联栏目-页面主键
        List<PageColumnEntity> pageColumnEntities = pageColumnMapper.selectList(new QueryWrapper<>());
        for (PageColumnEntity pageColumnEntity : pageColumnEntities) {
            boolean bool = true;
            for (PageEntity entity : pageEntities) {
                if (entity.getId().equals(pageColumnEntity.getPageId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("页面关联栏目-页面主键：{}", pageColumnEntity);
            }
        }
        // 页面关联栏目-栏目主键
        for (PageColumnEntity pageColumnEntity : pageColumnEntities) {
            boolean bool = true;
            for (ColumnEntity entity : columnEntities) {
                if (entity.getId().equals(pageColumnEntity.getColumnId())) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                log.info("页面关联栏目-栏目主键：{}", pageColumnEntity);
            }
        }
        log.info("检测完成！");
    }

    @Override
    public void logoutHandle() {
        long expireTime = System.currentTimeMillis() - constants.getLogOffGuardTime();
        QueryWrapper<DeletedUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().lt(DeletedUserEntity::getDeleteTime, new Date(expireTime));
        List<DeletedUserEntity> expireList = deletedUserService.list(queryWrapper);
        if (expireList.size() > 0) {
            for (DeletedUserEntity deletedUserEntity : expireList) {
                userInfoService.deleteUserInfoByUserId(deletedUserEntity.getUserId());
            }
        }
        log.info("自动清理注销用户完成，共删除 {} 个账号", expireList.size());
    }
}
