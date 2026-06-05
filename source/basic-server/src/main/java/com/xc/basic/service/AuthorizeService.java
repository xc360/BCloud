package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.AuthorityDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.AuditAuthorizeBean;
import com.xc.basic.bean.AuthorizeBean;
import com.xc.basic.dto.AuthorizeDto;
import com.xc.basic.entity.AuthorityEntity;
import com.xc.basic.entity.AuthorizeEntity;
import com.xc.core.bean.AuditBean;
import com.xc.core.model.TokenModel;

import java.util.List;

/**
 * <p>
 * 应用授权Service
 * </p>
 *
 * @author xc
 * @since 2023-08-01
 */
public interface AuthorizeService extends IService<AuthorizeEntity> {
    /**
     * 创建应用的权限
     *
     * @param appId         应用主键
     * @param authorizeBean 申请权限参数
     * @return 权限信息
     */
    public List<AuthorityDto> updateAppAuthorize(String appId, AuthorizeBean authorizeBean);

    /**
     * 权限转授权对象
     *
     * @param authorityEntities 权限集合
     * @param appId             应用主键
     * @return 处理后的结果
     */
    public List<AuthorizeDto> authorityToAuthorize(List<AuthorityEntity> authorityEntities, String appId);

    /**
     * 获取用户应用权限分页数据
     *
     * @param pagingBean 基础查询条件
     * @return 分页数据
     */
    public PagingDto<AuthorizeDto> getAuditAuthorizePage(Integer current, TokenModel tokenModel, AuditAuthorizeBean auditAuthorizeBean, PagingBean pagingBean);

    /**
     * 审核应用权限
     *
     * @param tokenModel  token信息
     * @param authorizeId 授权主键
     * @return 授权对象
     */
    public AuthorizeDto getAuditAppAuthorize(TokenModel tokenModel, String authorizeId);

    /**
     * 审核应用权限
     *
     * @param tokenModel  token信息
     * @param authorizeId 授权主键
     * @param auditBean   审核参数
     * @return 授权对象
     */
    public AuthorizeDto updateAuditAppAuthorize(TokenModel tokenModel, String authorizeId, AuditBean auditBean);
}
