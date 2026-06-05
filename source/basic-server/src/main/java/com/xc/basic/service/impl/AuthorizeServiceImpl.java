package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.dto.AuthorityDto;
import com.xc.basic.bean.AuditAuthorizeBean;
import com.xc.basic.bean.AuthorizeBean;
import com.xc.basic.dto.AuthorizeDto;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.AuthorityEntity;
import com.xc.basic.entity.AuthorizeEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.AuthorityMapper;
import com.xc.basic.mapper.AuthorizeMapper;
import com.xc.basic.service.AppService;
import com.xc.basic.service.AuthorizeService;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.bean.AuditBean;
import com.xc.core.enums.AuditStatus;
import com.xc.core.model.TokenModel;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>应用授权实现类</p>
 *
 * @author xc
 * @since 2023-08-01
 */
@Service
public class AuthorizeServiceImpl extends ServiceImpl<AuthorizeMapper, AuthorizeEntity> implements AuthorizeService {
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private AppService appService;

    @Override
    @Transactional
    public List<AuthorityDto> updateAppAuthorize(String appId, AuthorizeBean authorizeBean) {
        if (authorizeBean.getAuthorityId() != null) {
            String authorityId = authorizeBean.getAuthorityId();
            AuthorityEntity authorityEntity = authorityMapper.selectById(authorityId);
            if (authorityEntity == null) {
                throw FailCode.AUTHORITY_ID_ERROR.getOperateException();
            }
            AuthorityDto authorityDto = updateAppAuthorize(appId, authorizeBean, authorityEntity);
            return Collections.singletonList(authorityDto);
        } else {
            List<AuthorityDto> authorityList = new ArrayList<>();
            for (String authorityCode : authorizeBean.getAuthorityCodes()) {
                AuthorityEntity authority = new AuthorityEntity();
                authority.setCode(authorityCode);
                authority.setAppId(authorizeBean.getAuthorityAppId());
                AuthorityEntity authorityEntity = authorityMapper.selectOne(new QueryWrapper<>(authority));
                if (authorityEntity == null) {
                    throw FailCode.AUTHORITY_CODE_ERROR.getOperateException();
                }
                authorityList.add(updateAppAuthorize(appId, authorizeBean, authorityEntity));
            }
            return authorityList;
        }
    }

    private AuthorityDto updateAppAuthorize(String appId, AuthorizeBean authorizeBean, AuthorityEntity authorityEntity) {
        AuthorizeEntity authorizeEntity = new AuthorizeEntity();
        authorizeEntity.setAppId(appId);
        authorizeEntity.setAuthorityId(authorityEntity.getId());
        AuthorizeEntity authorize = this.getOne(new QueryWrapper<>(authorizeEntity));
        if (authorize == null) {
            if (!AuditStatus.APPLIED.getStatus().equals(authorizeBean.getAuditStatus())) {
                throw FailCode.AUDIT_STATUS_ERROR.getOperateException();
            }
            AuthorizeEntity entity = new AuthorizeEntity();
            entity.setAppId(appId);
            entity.setAuthorityId(authorityEntity.getId());
            entity.setAuditStatus(AuditStatus.APPLIED.getStatus());
            entity.setReason(authorizeBean.getReason());
            entity.setApplyTime(new Date());
            if (!this.save(entity)) {
                throw FailCode.AUTHORIZE_CREATE_FAIL.getOperateException();
            }
            return ObjectUtils.convert(new AuthorityDto(), entity);
        } else {
            if (AuditStatus.APPLIED.getStatus().equals(authorizeBean.getAuditStatus())) {
                // 申请
                authorize.setAuditStatus(AuditStatus.APPLIED.getStatus());
                authorize.setReason(authorizeBean.getReason());
                authorize.setApplyTime(new Date());
            } else if (AuditStatus.NOT_APPLIED.getStatus().equals(authorizeBean.getAuditStatus())) {
                // 取消申请
                authorize.setAuditStatus(AuditStatus.NOT_APPLIED.getStatus());
                authorize.setReason(authorizeBean.getReason());
                authorize.setApplyTime(new Date());
            } else {
                throw FailCode.AUDIT_STATUS_ERROR.getOperateException();
            }
            if (!this.updateById(authorize)) {
                throw FailCode.AUTHORIZE_UPDATE_FAIL.getOperateException();
            }
        }
        return ObjectUtils.convert(new AuthorityDto(), authorizeEntity);
    }

    @Override
    public List<AuthorizeDto> authorityToAuthorize(List<AuthorityEntity> authorityEntities, String appId) {
        // 查询权限与应用的关联信息
        List<String> authorityIds = authorityEntities.stream().map(AuthorityEntity::getId).collect(Collectors.toList());
        if (authorityIds.size() == 0) {
            return new ArrayList<>();
        }
        AuthorizeEntity authorizeEntity = new AuthorizeEntity();
        authorizeEntity.setAppId(appId);
        QueryWrapper<AuthorizeEntity> queryWrapper = new QueryWrapper<>(authorizeEntity);
        queryWrapper.lambda().in(AuthorizeEntity::getAuthorityId, authorityIds);
        List<AuthorizeEntity> appAuthorityEntities = this.list(queryWrapper);
        // 封装数据
        List<AuthorizeDto> authorityList = new ArrayList<>();
        for (AuthorityEntity authority : authorityEntities) {
            AuthorizeDto authorizeDto = new AuthorizeDto();
            // 权限信息
            authorizeDto.setAuthorityId(authority.getId());
            authorizeDto.setAuthorityName(authority.getName());
            authorizeDto.setAuthorityCode(authority.getCode());
            authorizeDto.setAuthorityType(authority.getType());
            authorizeDto.setAuthorityAppId(authority.getAppId());
            //默认状态
            authorizeDto.setAuditStatus(AuditStatus.NOT_APPLIED.getStatus());
            for (AuthorizeEntity entity : appAuthorityEntities) {
                if (entity.getAuthorityId().equals(authority.getId())) {
                    // 关联信息
                    authorizeDto.setId(entity.getId());
                    authorizeDto.setAuditStatus(entity.getAuditStatus());
                    authorizeDto.setReason(entity.getReason());
                    authorizeDto.setApplyTime(entity.getApplyTime());
                }
            }
            authorityList.add(authorizeDto);
        }
        return authorityList;
    }

    @Override
    public PagingDto<AuthorizeDto> getAuditAuthorizePage(Integer current, TokenModel tokenModel, AuditAuthorizeBean auditAuthorizeBean, PagingBean pagingBean) {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setName(auditAuthorizeBean.getName());
        authorityEntity.setCode(auditAuthorizeBean.getCode());
        authorityEntity.setAppId(auditAuthorizeBean.getAuthorityAppId());
        // 查应用
        List<AppEntity> appEntities = appService.getUserAppList(new QueryBean(), tokenModel, new AppEntity());
        // 判断应用不为空
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        if (appEntities.size() > 0) {
            // 查权限
            QueryWrapper<AuthorityEntity> queryWrapper = ServiceUtils.queryLike(authorityEntity, pagingBean.getLikeFields());
            List<String> appIds = appEntities.stream().map(AppEntity::getId).collect(Collectors.toList());
            queryWrapper.lambda().in(AuthorityEntity::getAppId, appIds);
            authorityEntities = authorityMapper.selectList(queryWrapper);
        }
        // 查询权限与应用的关联信息
        List<String> authorityIds = authorityEntities.stream().map(AuthorityEntity::getId).collect(Collectors.toList());
        if (authorityIds.size() == 0) {
            return new PagingDto<>(0L, new ArrayList<>());
        }
        AuthorizeEntity authorizeEntity = new AuthorizeEntity();
        authorizeEntity.setAppId(auditAuthorizeBean.getAppId());
        authorizeEntity.setAuditStatus(auditAuthorizeBean.getAuditStatus());
        QueryWrapper<AuthorizeEntity> queryWrapper1 = new QueryWrapper<>(authorizeEntity);
        queryWrapper1.lambda().in(AuthorizeEntity::getAuthorityId, authorityIds);
        ServiceUtils.querySort(queryWrapper1, pagingBean);
        IPage<AuthorizeEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper1);
        // 处理数据
        List<AuthorizeDto> appAuthorityList = new ArrayList<>();
        for (AuthorityEntity authority : authorityEntities) {
            for (AuthorizeEntity appAuthority : iPage.getRecords()) {
                if (authority.getId().equals(appAuthority.getAuthorityId())) {
                    AuthorizeDto authorizeDto = new AuthorizeDto();
                    // 权限信息
                    authorizeDto.setAuthorityName(authority.getName());
                    authorizeDto.setAuthorityCode(authority.getCode());
                    authorizeDto.setAuthorityType(authority.getType());
                    authorizeDto.setAuthorityAppId(authority.getAppId());
                    // 关联信息
                    authorizeDto.setId(appAuthority.getId());
                    authorizeDto.setAppId(appAuthority.getAppId());
                    authorizeDto.setAuthorityId(appAuthority.getAuthorityId());
                    authorizeDto.setApplyTime(appAuthority.getApplyTime());
                    authorizeDto.setAuditStatus(appAuthority.getAuditStatus());
                    authorizeDto.setReason(appAuthority.getReason());
                    appAuthorityList.add(authorizeDto);
                }
            }
        }
        // 处理数据
        return new PagingDto<>(iPage.getTotal(), appAuthorityList);
    }

    @Override
    public AuthorizeDto getAuditAppAuthorize(TokenModel tokenModel, String authorizeId) {
        AuthorizeEntity authorizeEntity = this.getById(authorizeId);
        if (authorizeEntity == null) {
            throw FailCode.AUTHORIZE_ID_ERROR.getOperateException();
        }
        AuthorityEntity authorityEntity = authorityMapper.selectById(authorizeEntity.getAuthorityId());
        if (authorityEntity == null) {
            throw FailCode.AUTHORITY_ID_ERROR.getOperateException();
        }
        appService.verifyUserHaveApp(authorityEntity.getAppId(), tokenModel, "getAuditAppAuthorize");
        AuthorizeDto authorizeDto = new AuthorizeDto();
        authorizeDto.setId(authorizeEntity.getId());
        authorizeDto.setAppId(authorizeEntity.getAppId());
        authorizeDto.setAuthorityId(authorizeEntity.getAuthorityId());
        authorizeDto.setAuditStatus(authorizeEntity.getAuditStatus());
        authorizeDto.setReason(authorizeEntity.getReason());
        authorizeDto.setApplyTime(authorizeEntity.getApplyTime());
        // 权限信息
        authorizeDto.setAuthorityAppId(authorityEntity.getAppId());
        authorizeDto.setAuthorityName(authorityEntity.getName());
        authorizeDto.setAuthorityCode(authorityEntity.getCode());
        authorizeDto.setAuthorityType(authorityEntity.getType());
        return authorizeDto;
    }

    @Override
    public AuthorizeDto updateAuditAppAuthorize(TokenModel tokenModel, String authorizeId, AuditBean auditBean) {
        AuthorizeEntity authorizeEntity = this.getById(authorizeId);
        if (authorizeEntity == null) {
            throw FailCode.AUTHORIZE_ID_ERROR.getOperateException();
        }
        AuthorityEntity authorityEntity = authorityMapper.selectById(authorizeEntity.getAuthorityId());
        if (authorityEntity == null) {
            throw FailCode.AUTHORITY_ID_ERROR.getOperateException();
        }
        appService.verifyUserHaveApp(authorityEntity.getAppId(), tokenModel, "updateAuditAppAuthorize");
        if (AuditStatus.REVIEWED.getStatus().equals(auditBean.getAuditStatus())) {
            //通过
            authorizeEntity.setAuditStatus(AuditStatus.REVIEWED.getStatus());
            authorizeEntity.setReason(auditBean.getReason());
            authorizeEntity.setApplyTime(new Date());
        } else if (AuditStatus.REJECTED.getStatus().equals(auditBean.getAuditStatus())) {
            // 拒绝
            authorizeEntity.setAuditStatus(AuditStatus.REJECTED.getStatus());
            authorizeEntity.setReason(auditBean.getReason());
            authorizeEntity.setApplyTime(new Date());
        } else {
            throw FailCode.AUDIT_STATUS_ERROR.getOperateException();
        }
        // 修改数据
        if (!this.updateById(authorizeEntity)) {
            throw FailCode.AUTHORIZE_UPDATE_FAIL.getOperateException();
        }
        AuthorizeDto authorizeDto = new AuthorizeDto();
        authorizeDto.setId(authorizeEntity.getId());
        authorizeDto.setAppId(authorizeEntity.getAppId());
        authorizeDto.setAuthorityId(authorizeEntity.getAuthorityId());
        authorizeDto.setAuditStatus(authorizeEntity.getAuditStatus());
        authorizeDto.setReason(authorizeEntity.getReason());
        authorizeDto.setApplyTime(authorizeEntity.getApplyTime());
        // 权限信息
        authorizeDto.setAuthorityAppId(authorityEntity.getAppId());
        authorizeDto.setAuthorityName(authorityEntity.getName());
        authorizeDto.setAuthorityCode(authorityEntity.getCode());
        authorizeDto.setAuthorityType(authorityEntity.getType());
        return authorizeDto;
    }
}
