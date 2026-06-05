package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.dto.AppDto;
import com.xc.api.basic.dto.AuthorityDto;
import com.xc.api.basic.dto.UserAppDto;
import com.xc.api.basic.enums.AuthorityType;
import com.xc.api.file.FileApi;
import com.xc.api.file.bean.DiskSignBean;
import com.xc.api.file.config.FileConstants;
import com.xc.api.file.enums.FileRestCode;
import com.xc.api.file.utils.FileUrlUtils;
import com.xc.basic.config.Constants;
import com.xc.basic.dto.AuditAppDto;
import com.xc.basic.entity.*;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.*;
import com.xc.basic.service.AppService;
import com.xc.basic.service.TaskService;
import com.xc.basic.service.VersionService;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.aspect.AuthorityHandle;
import com.xc.core.dto.PagingDto;
import com.xc.core.enums.*;
import com.xc.core.model.GroupModel;
import com.xc.core.model.TokenModel;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>应用程序服务实现类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, AppEntity> implements AppService {
    @Autowired
    private UserAuthorizeMapper userAuthorizeMapper;
    @Autowired
    private UserAuthorizeAuthorityMapper userAuthorizeAuthorityMapper;
    @Autowired
    private AppMapper appMapper;
    @Autowired
    private AuthorizeMapper authorizeMapper;
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private InfoMapper infoMapper;
    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private GroupRoleMapper groupRoleMapper;
    @Autowired
    private UserGroupMapper userGroupMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;
    @Autowired
    private FileConstants fileConstants;
    @Autowired
    private Constants constants;
    @Autowired
    private FileApi fileApi;
    @Autowired
    private VersionService versionService;
    @Autowired
    private VersionMapper versionMapper;
    @Autowired
    private TreeDictMapper treeDictMapper;
    @Autowired
    private DataTypeMapper dataTypeMapper;
    @Autowired
    private ColumnMapper columnMapper;
    @Autowired
    private PageMapper pageMapper;
    @Autowired
    private PageColumnMapper pageColumnMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private ApiSupplierMapper apiSupplierMapper;
    @Autowired
    private MessageTemplateMapper messageTemplateMapper;
    @Autowired
    private MessageLogMapper messageLogMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskLogMapper taskLogMapper;
    @Lazy
    @Autowired
    private TaskService taskService;


    @Override
    public PagingDto<AppDto> getUserAppPage(Integer current, TokenModel tokenModel, PagingBean pagingBean, AppEntity appEntity) {
        QueryWrapper<AppEntity> queryWrapper = ServiceUtils.queryLike(appEntity, pagingBean.getLikeFields());
        List<String> groupIds = tokenModel.getGroups().stream().map(GroupModel::getGroupId).collect(Collectors.toList());
        if (groupIds.size() > 0) {
            queryWrapper.lambda().nested(i -> i.eq(AppEntity::getUserId, tokenModel.getUserId()).or().in(AppEntity::getGroupId, groupIds));
        } else {
            queryWrapper.lambda().eq(AppEntity::getUserId, tokenModel.getUserId());
        }
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<AppEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), AppDto::new));
    }

    @Override
    public PagingDto<AppDto> getAuditAppPage(Integer current, PagingBean pagingBean, AppEntity appEntity) {
        QueryWrapper<AppEntity> queryWrapper = ServiceUtils.queryLike(appEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<AppEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        List<String> userIds = iPage.getRecords().stream().map(AppEntity::getUserId).collect(Collectors.toList());
        List<AppDto> appDtoList = new ArrayList<>();
        if (userIds.size() > 0) {
            List<UserInfoEntity> userInfoEntities = userInfoMapper.selectBatchIds(userIds);
            for (AppEntity entity : iPage.getRecords()) {
                AppDto appDto = ObjectUtils.convert(new AppDto(), entity);
                for (UserInfoEntity userInfoEntity : userInfoEntities) {
                    if (userInfoEntity.getUserId().equals(appDto.getUserId())) {
                        appDto.setNickName(userInfoEntity.getNickName());
                    }
                }
                appDtoList.add(appDto);
            }
        }
        return new PagingDto<>(iPage.getTotal(), appDtoList);
    }

    @Override
    public PagingDto<AppDto> getOvertAppPage(Integer current, PagingBean pagingBean, AppEntity appEntity) {
        appEntity.setIsOpen(Whether.YES.getValue());
        QueryWrapper<AppEntity> queryWrapper = ServiceUtils.queryLike(appEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<AppEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        List<AppDto> appList = ObjectUtils.convertList(iPage.getRecords(), AppDto::new);
        for (AppDto appDto : appList) {
            appDto.setLogoUrl(FileUrlUtils.templateTurnUrl(appDto.getLogoUrl()));
        }
        return new PagingDto<>(iPage.getTotal(), appList);
    }


    @Override
    public AppEntity getValidAppByAppId(String appId) {
        AppEntity appEntity = new AppEntity();
        appEntity.setAppId(appId);
        AppEntity app = this.getOne(new QueryWrapper<>(appEntity));
        if (app == null) {
            throw FailCode.APP_APP_ID_ERROR.getOperateException();
        }
        if (app.getStatus().equals(EffectStatus.INVALID.getStatus())) {
            throw FailCode.APP_STATUS_INVALID.getOperateException();
        }
        return app;
    }

    @Override
    public AppEntity getReviewedAppByAppId(String appId) {
        AppEntity appEntity = new AppEntity();
        appEntity.setAppId(appId);
        AppEntity app = this.getOne(new QueryWrapper<>(appEntity));
        if (app == null) {
            throw FailCode.APP_APP_ID_ERROR.getOperateException();
        }
        if (app.getStatus().equals(EffectStatus.INVALID.getStatus())) {
            throw FailCode.APP_STATUS_INVALID.getOperateException();
        }
        if (app.getAuditStatus().equals(AuditStatus.REVIEWED.getStatus())) {
            return app;
        }
        throw FailCode.APP_NOT_AUDIT.getOperateException();
    }


    @Override
    @Transactional
    public void deleteApp(AppEntity appEntity) {
        String id = appEntity.getId();
        // 删除应用的信息
        InfoEntity infoEntity = new InfoEntity();
        infoEntity.setAppId(id);
        infoMapper.delete(new QueryWrapper<>(infoEntity));
        // 删除应用的字典
        DictEntity dictEntity = new DictEntity();
        dictEntity.setAppId(id);
        dictMapper.delete(new QueryWrapper<>(dictEntity));
        // 删除树形字典字典
        TreeDictEntity treeDictEntity = new TreeDictEntity();
        treeDictEntity.setAppId(id);
        treeDictMapper.delete(new QueryWrapper<>(treeDictEntity));
        // 删除版本
        VersionEntity versionEntity = new VersionEntity();
        versionEntity.setAppId(id);
        QueryWrapper<VersionEntity> versionQueryWrapper = new QueryWrapper<>(versionEntity);
        List<VersionEntity> versionEntities = versionMapper.selectList(versionQueryWrapper);
        versionMapper.delete(versionQueryWrapper);
        List<String> deleteFileUrls = new ArrayList<>();
        for (VersionEntity entity : versionEntities) {
            deleteFileUrls.addAll(FileUrlUtils.getMidValue(entity.getDocContent(), FileUrlUtils.templateKey, constants.getArticleFileSuffixArray()));
            deleteFileUrls.add(entity.getPackageUrl());
        }
        // 删除数据类型
        DataTypeEntity dataTypeEntity = new DataTypeEntity();
        dataTypeEntity.setAppId(id);
        dataTypeMapper.delete(new QueryWrapper<>(dataTypeEntity));
        // 删除接口供应商
        ApiSupplierEntity apiSupplierEntity = new ApiSupplierEntity();
        apiSupplierEntity.setAppId(id);
        apiSupplierMapper.delete(new QueryWrapper<>(apiSupplierEntity));
        // 删除消息模板
        MessageTemplateEntity messageTemplateEntity = new MessageTemplateEntity();
        messageTemplateEntity.setAppId(id);
        QueryWrapper<MessageTemplateEntity> messageTemplateWrapper = new QueryWrapper<>(messageTemplateEntity);
        List<MessageTemplateEntity> messageTemplateEntities = messageTemplateMapper.selectList(messageTemplateWrapper);
        messageTemplateMapper.delete(messageTemplateWrapper);
        // 删除消息日志
        List<String> messageTemplateCodeList = messageTemplateEntities.stream().map(MessageTemplateEntity::getCode).collect(Collectors.toList());
        MessageLogEntity messageLogEntity = new MessageLogEntity();
        messageLogEntity.setAppId(id);
        QueryWrapper<MessageLogEntity> messageLogWrapper = new QueryWrapper<>(messageLogEntity);
        if (messageTemplateCodeList.size() > 0) {
            messageLogWrapper.lambda().in(MessageLogEntity::getMessageTemplateCode, messageTemplateCodeList);
        }
        messageLogMapper.delete(messageLogWrapper);
        // 删除任务
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setAppId(id);
        QueryWrapper<TaskEntity> taskWrapper = new QueryWrapper<>(taskEntity);
        List<TaskEntity> taskEntities = taskMapper.selectList(taskWrapper);
        taskService.stopTask(id);
        taskMapper.delete(taskWrapper);
        // 删除任务记录
        List<String> taskCodeList = taskEntities.stream().map(TaskEntity::getCode).collect(Collectors.toList());
        TaskLogEntity taskLogEntity = new TaskLogEntity();
        taskLogEntity.setAppId(id);
        QueryWrapper<TaskLogEntity> taskLogWrapper = new QueryWrapper<>(taskLogEntity);
        if (taskCodeList.size() > 0) {
            taskLogWrapper.lambda().in(TaskLogEntity::getTaskCode, taskCodeList);
        }
        taskLogMapper.delete(taskLogWrapper);
        // 删除栏目
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setAppId(id);
        QueryWrapper<ColumnEntity> columnQueryWrapper = new QueryWrapper<>(columnEntity);
        List<ColumnEntity> columnEntities = columnMapper.selectList(columnQueryWrapper);
        columnMapper.delete(columnQueryWrapper);
        // 删除页面
        PageEntity pageEntity = new PageEntity();
        pageEntity.setAppId(id);
        QueryWrapper<PageEntity> pageQueryWrapper = new QueryWrapper<>(pageEntity);
        List<PageEntity> pageEntities = pageMapper.selectList(pageQueryWrapper);
        pageMapper.delete(pageQueryWrapper);
        List<String> pageIds = pageEntities.stream().map(PageEntity::getId).collect(Collectors.toList());
        List<String> columnIds = columnEntities.stream().map(ColumnEntity::getId).collect(Collectors.toList());
        if (pageIds.size() > 0 || columnIds.size() > 0) {
            QueryWrapper<PageColumnEntity> pageColumnDeleteWrapper = new QueryWrapper<>();
            if (pageIds.size() > 0) {
                pageColumnDeleteWrapper.lambda().in(PageColumnEntity::getPageId, pageIds);
            }
            if (columnIds.size() > 0) {
                pageColumnDeleteWrapper.lambda().or();
                pageColumnDeleteWrapper.lambda().in(PageColumnEntity::getColumnId, columnIds);
            }
            pageColumnMapper.delete(pageColumnDeleteWrapper);
        }
        // 删除用户和应用关联
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        userAuthorizeEntity.setAppId(id);
        QueryWrapper<UserAuthorizeEntity> queryWrapper = new QueryWrapper<>(userAuthorizeEntity);
        List<UserAuthorizeEntity> userAuthorizeEntities = userAuthorizeMapper.selectList(queryWrapper);
        userAuthorizeMapper.delete(new QueryWrapper<>(userAuthorizeEntity));
        // 删除用户授权的权限
        for (UserAuthorizeEntity userAuthorize : userAuthorizeEntities) {
            UserAuthorizeAuthorityEntity userAuthorizeAuthorityEntity = new UserAuthorizeAuthorityEntity();
            userAuthorizeAuthorityEntity.setUserAuthorizeId(userAuthorize.getId());
            userAuthorizeAuthorityMapper.delete(new QueryWrapper<>(userAuthorizeAuthorityEntity));
        }
        // 删除应用的用户组
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setAppId(id);
        QueryWrapper<GroupEntity> groupQueryWrapper = new QueryWrapper<>(groupEntity);
        List<GroupEntity> groupEntities = groupMapper.selectList(groupQueryWrapper);
        groupMapper.delete(groupQueryWrapper);

        // 删除应用的角色
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setAppId(id);
        QueryWrapper<RoleEntity> roleQueryWrapper = new QueryWrapper<>(roleEntity);
        List<RoleEntity> roleEntities = roleMapper.selectList(roleQueryWrapper);
        roleMapper.delete(roleQueryWrapper);
        // 删除应用的权限
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAppId(id);
        QueryWrapper<AuthorityEntity> authorityQueryWrapper = new QueryWrapper<>(authorityEntity);
        List<AuthorityEntity> authorityEntities = authorityMapper.selectList(authorityQueryWrapper);
        authorityMapper.delete(authorityQueryWrapper);

        List<String> groupIds = groupEntities.stream().map(GroupEntity::getId).collect(Collectors.toList());
        List<String> roleIds = roleEntities.stream().map(RoleEntity::getId).collect(Collectors.toList());
        List<String> authorityIds = authorityEntities.stream().map(AuthorityEntity::getId).collect(Collectors.toList());

        // 删除用户和组关联
        QueryWrapper<UserGroupEntity> userGroupDeleteWrapper = new QueryWrapper<>();
        if (groupIds.size() > 0) {
            userGroupDeleteWrapper.lambda().in(UserGroupEntity::getGroupId, groupIds);
            userGroupMapper.delete(userGroupDeleteWrapper);
        }
        // 删除组合角色关联
        QueryWrapper<GroupRoleEntity> groupRoleDeleteWrapper = new QueryWrapper<>();
        if (groupIds.size() > 0 || roleIds.size() > 0) {
            if (groupIds.size() > 0) {
                groupRoleDeleteWrapper.lambda().in(GroupRoleEntity::getGroupId, groupIds);
            }
            if (roleIds.size() > 0) {
                groupRoleDeleteWrapper.lambda().or();
                groupRoleDeleteWrapper.lambda().in(GroupRoleEntity::getRoleId, roleIds);
            }
            groupRoleMapper.delete(groupRoleDeleteWrapper);
        }
        // 删除用户和角色关联
        if (roleIds.size() > 0) {
            QueryWrapper<UserRoleEntity> userRoleDeleteWrapper = new QueryWrapper<>();
            userRoleDeleteWrapper.lambda().in(UserRoleEntity::getRoleId, roleIds);
            userRoleMapper.delete(userRoleDeleteWrapper);
        }
        // 删除角色和权限
        if (roleIds.size() > 0 || authorityIds.size() > 0) {
            QueryWrapper<RoleAuthorityEntity> roleAuthorityDeleteWrapper = new QueryWrapper<>();
            if (roleIds.size() > 0) {
                roleAuthorityDeleteWrapper.lambda().in(RoleAuthorityEntity::getRoleId, roleIds);
            }
            if (authorityIds.size() > 0) {
                roleAuthorityDeleteWrapper.lambda().or();
                roleAuthorityDeleteWrapper.lambda().in(RoleAuthorityEntity::getAuthorityId, authorityIds);
            }
            roleAuthorityMapper.delete(roleAuthorityDeleteWrapper);
        }
        // 删除应用权限关联
        AuthorizeEntity authorizeEntity = new AuthorizeEntity();
        authorizeEntity.setAppId(id);
        authorizeMapper.delete(new QueryWrapper<>(authorizeEntity));
        // 删除文件
        DiskSignBean diskSignBean = new DiskSignBean(fileConstants.getDiskNo(), fileConstants.getDiskSecret(), FileRestCode.updateOpenDiskFileStatus.getCode());
        deleteFileUrls.add(appEntity.getLogoUrl());
        FileUrlUtils.confirmFile(fileApi, diskSignBean, deleteFileUrls, new ArrayList<>());
        // 删除应用
        if (!this.removeById(id)) {
            throw FailCode.APP_DELETE_FAIL.getOperateException();
        }
    }

    @Override
    @Transactional
    public void deleteAppByUserId(String userId) {
        AppEntity entity = new AppEntity();
        entity.setUserId(userId);
        List<AppEntity> appEntities = this.list(new QueryWrapper<>(entity));
        for (AppEntity appEntity : appEntities) {
            deleteApp(appEntity);
        }
    }

    @Override
    public List<AppEntity> getAppList(QueryBean queryBean, AppEntity appEntity) {
        QueryWrapper<AppEntity> queryWrapper = ServiceUtils.queryLike(appEntity, queryBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, queryBean);
        return appMapper.selectList(queryWrapper);
    }

    @Override
    public List<AppEntity> getUserAppList(QueryBean queryBean, TokenModel tokenModel, AppEntity appEntity) {
        QueryWrapper<AppEntity> queryWrapper = ServiceUtils.queryLike(appEntity, queryBean.getLikeFields());
        List<String> groupIds = tokenModel.getGroups().stream().map(GroupModel::getGroupId).collect(Collectors.toList());
        if (groupIds.size() > 0) {
            queryWrapper.lambda().nested(i -> i.eq(AppEntity::getUserId, tokenModel.getUserId()).or().in(AppEntity::getGroupId, groupIds));
        } else {
            queryWrapper.lambda().eq(AppEntity::getUserId, tokenModel.getUserId());
        }
        ServiceUtils.querySort(queryWrapper, queryBean);
        return appMapper.selectList(queryWrapper);
    }

    @Override
    public AppEntity verifyUserHaveApp(String appId, TokenModel tokenModel, String authorityCode) {
        AppEntity appEntity = getById(appId);
        if (!appEntity.getUserId().equals(tokenModel.getUserId())) {
            if (!AuthorityHandle.verifyGroupAuthority(tokenModel, appEntity.getGroupId(), authorityCode)) {
                throw FailCode.NOT_INTERFACE_DATA_AUTHORITY.getOperateException();
            }
        }
        return appEntity;
    }

    @Override
    public AppEntity verifyAppIsOpen(String appId, String userId) {
        AppEntity appEntity = getById(appId);
        if (appEntity == null) {
            throw FailCode.APP_APP_ID_ERROR.getOperateException();
        }
        // 是不是公开应用
        if (Whether.YES.getValue().equals(appEntity.getIsOpen())) {
            return appEntity;
        }
        // 是不是当前用户的应用
        if (appEntity.getUserId().equals(userId)) {
            return appEntity;
        }
        //判断应用和用户是否在同一个组
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupEntity.setUserId(userId);
        List<UserGroupEntity> userGroupEntities = userGroupMapper.selectList(new QueryWrapper<>(userGroupEntity));
        List<String> groupIds = userGroupEntities.stream().map(UserGroupEntity::getGroupId).collect(Collectors.toList());
        if (groupIds.contains(appEntity.getGroupId())) {
            return appEntity;
        }
        throw FailCode.NOT_OVERT_APP.getOperateException();
    }

    @Override
    public AppEntity verifyAppSecret(String appId, String appSecret) {
        AppEntity app = new AppEntity();
        app.setAppId(appId);
        AppEntity appEntity = this.getOne(new QueryWrapper<>(app));
        if (appEntity == null) {
            throw FailCode.APP_APP_ID_ERROR.getOperateException();
        }
        if (!appEntity.getAppSecret().equals(appSecret)) {
            throw FailCode.APP_SECRET_ERROR.getOperateException();
        }
        return appEntity;
    }

    @Override
    public List<String> getAuthorizeAppUserIds(String appId, List<String> userIds) {
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        userAuthorizeEntity.setAppId(appId);
        QueryWrapper<UserAuthorizeEntity> appUserQueryWrapper = new QueryWrapper<>(userAuthorizeEntity);
        if (userIds != null) {
            appUserQueryWrapper.lambda().in(UserAuthorizeEntity::getUserId, userIds);
        }
        List<UserAuthorizeEntity> appUserEntities = userAuthorizeMapper.selectList(appUserQueryWrapper);
        return appUserEntities.stream().map(UserAuthorizeEntity::getUserId).collect(Collectors.toList());
    }

    @Override
    public UserAuthorizeEntity getAppUser(String appId, String userId) {
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        userAuthorizeEntity.setAppId(appId);
        userAuthorizeEntity.setUserId(userId);
        return userAuthorizeMapper.selectOne(new QueryWrapper<>(userAuthorizeEntity));
    }

    @Override
    public AuditAppDto getAuditApp(String appId) {
        // 查询应用
        AuditAppDto auditAppDto = new AuditAppDto();
        AppEntity appEntity = this.getById(appId);
        if (appEntity == null) {
            throw FailCode.APP_ID_ERROR.getOperateException();
        }
        if (AuditStatus.APPLIED.getStatus().equals(appEntity.getAuditStatus()) ||
                AuditStatus.REVIEWED.getStatus().equals(appEntity.getAuditStatus())) {
            AppDto appDto = ObjectUtils.convert(new AppDto(), appEntity);
            // 查询用户
            UserInfoEntity userInfoEntity = userInfoMapper.selectById(appDto.getUserId());
            if (userInfoEntity != null) {
                appDto.setNickName(userInfoEntity.getNickName());
            }
            // 处理logo
            appDto.setLogoUrl(FileUrlUtils.templateTurnUrl(appDto.getLogoUrl()));
            auditAppDto.setAppDto(appDto);
            // 查询权限
            AuthorityEntity entity = new AuthorityEntity();
            entity.setAppId(appEntity.getId());
            QueryWrapper<AuthorityEntity> queryWrapper = new QueryWrapper<>(entity);
            queryWrapper.lambda().in(AuthorityEntity::getType, AuthorityType.APP.getType(), AuthorityType.USER_INFO.getType());
            List<AuthorityEntity> entities = authorityMapper.selectList(queryWrapper);
            auditAppDto.setAuthorityList(ObjectUtils.convertList(entities, AuthorityDto::new));
            // 最新文档
            VersionEntity versionEntity = new VersionEntity();
            versionEntity.setAppId(appEntity.getId());
            auditAppDto.setVersionList(versionService.getVersionList(new QueryBean(), versionEntity));
            return auditAppDto;
        } else {
            throw FailCode.AUDIT_STATUS_ERROR.getOperateException();
        }
    }

    @Override
    public List<UserAppDto> getAppListByUserId(String userId, AppEntity appEntity, String versionType) {
        QueryWrapper<AppEntity> queryWrapper = new QueryWrapper<>(appEntity);
        // 获取当前用户所在用户组下的所有用户id
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupEntity.setUserId(userId);
        List<UserGroupEntity> userGroupEntities = userGroupMapper.selectList(new QueryWrapper<>(userGroupEntity));
        List<String> groupIds = userGroupEntities.stream().map(UserGroupEntity::getGroupId).collect(Collectors.toList());
        queryWrapper.lambda().nested(wrapper -> {
            wrapper.eq(AppEntity::getUserId, userId);
            if (groupIds.size() > 0) {
                wrapper.or();
                wrapper.in(AppEntity::getGroupId, groupIds);
            }
            wrapper.or().eq(AppEntity::getIsOpen, Whether.YES.getValue());
        });
        List<AppEntity> appEntities = this.list(queryWrapper);
        List<UserAppDto> overtAppList = ObjectUtils.convertList(appEntities, UserAppDto::new);
        if (overtAppList.size() > 0) {
            List<String> ids = overtAppList.stream().map(UserAppDto::getId).collect(Collectors.toList());
            VersionEntity version = new VersionEntity();
            version.setStatus(EffectStatus.VALID.getStatus());
            version.setType(versionType);
            QueryWrapper<VersionEntity> wrapper = new QueryWrapper<>(version);
            wrapper.lambda().in(VersionEntity::getAppId, ids);
            wrapper.lambda().select(VersionEntity::getId, VersionEntity::getAppId, VersionEntity::getAppVersion, VersionEntity::getPackageUrl, VersionEntity::getUpdateTime);
            List<VersionEntity> versionEntities = versionService.list(wrapper);
            for (UserAppDto userAppDto : overtAppList) {
                List<VersionEntity> entities = new ArrayList<>();
                for (VersionEntity entity : versionEntities) {
                    if (userAppDto.getId().equals(entity.getAppId())) {
                        entities.add(entity);
                    }
                }
                entities.sort((o1, o2) -> o2.getAppVersion().compareTo(o1.getAppVersion()));
                if (entities.size() != 0) {
                    VersionEntity versionEntity = entities.get(0);
                    userAppDto.setNewAppVersion(versionEntity.getAppVersion());
                    String packageUrl = FileUrlUtils.templateTurnUrl(versionEntity.getPackageUrl());
                    userAppDto.setNewPackageUrl(packageUrl);
                }
            }
        }
        return overtAppList;
    }

    @Override
    public void verifyAppIp(String ip, String appIp) {
        if (appIp != null) {
            String[] appIpArray = appIp.split(",");
            boolean bool = true;
            for (String arr : appIpArray) {
                if (ip.contains(arr)) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                throw FailCode.SIGN_CLIENT_IP_ERROR.getOperateException();
            }
        }
    }

    @Override
    public void verifyAppDomain(String domain, String appDomain) {
        if (appDomain != null) {
            String[] domainArray = appDomain.split(",");
            boolean bool = true;
            for (String arr : domainArray) {
                if (domain.contains(arr)) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                throw FailCode.APP_CALLBACK_DOMAIN_ERROR.getOperateException();
            }
        }
    }
}
