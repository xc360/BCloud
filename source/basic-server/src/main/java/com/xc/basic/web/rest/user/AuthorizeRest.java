package com.xc.basic.web.rest.user;

import com.xc.api.basic.dto.AuthorityDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.AuditAuthorizeBean;
import com.xc.basic.bean.AuthorizeAuthorityBean;
import com.xc.basic.bean.AuthorizeBean;
import com.xc.basic.dto.AuthorizeDto;
import com.xc.basic.entity.AuthorityEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.AuthorityService;
import com.xc.basic.service.AuthorizeService;
import com.xc.core.annotation.Authority;
import com.xc.core.bean.AuditBean;
import com.xc.core.enums.AuditStatus;
import com.xc.api.basic.enums.AuthorityType;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 应用授权rest
 * </p>
 *
 * @author xc
 * @since 2023-08-01
 */
@Api(tags = {"需要登录权限接口，应用授权"})
@RestController
public class AuthorizeRest {
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private AppService appService;
    @Autowired
    private AuthorizeService authorizeService;

    @ApiOperation(value = "应用授权权限分页", notes = "应用授权权限分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/authorize/authority_page/{current}")
    @Authority
    public PagingDto<AuthorizeDto> getAppAuthorizeAuthorityPage(TokenModel tokenModel, @PathVariable String appId, @PathVariable Integer current, @ModelAttribute PagingBean pagingBean, @ModelAttribute AuthorizeAuthorityBean authorizeAuthorityBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppAuthorizeAuthorityPage");
        AuthorityEntity authorityEntity = ObjectUtils.convert(new AuthorityEntity(), authorizeAuthorityBean);
        authorityEntity.setAppId(authorizeAuthorityBean.getAuthorityAppId());
        List<String> arrayList = new ArrayList<>();
        arrayList.add(AuthorityType.APP.getType());
        arrayList.add(AuthorityType.USER_INFO.getType());
        PagingDto<AuthorityEntity> authorityPagingDto = authorityService.getAppAuthorityPage(current, pagingBean, arrayList, authorityEntity);
        List<AuthorizeDto> authorizeList = authorizeService.authorityToAuthorize(authorityPagingDto.getResData(), appId);
        return new PagingDto<>(authorityPagingDto.getTotal(), authorizeList);
    }

    @ApiOperation(value = "更新应用授权", notes = "更新应用授权")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/authorize")
    @Authority
    public List<AuthorityDto> updateAppAuthorize(TokenModel tokenModel, @PathVariable String appId, @RequestBody AuthorizeBean authorizeBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppAuthorize");
        return authorizeService.updateAppAuthorize(appId, authorizeBean);
    }

    @ApiOperation(value = "审核应用权限分页", notes = "审核应用权限分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true)
    })
    @GetMapping("/audit_app_authorize_page/{current}")
    @Authority
    public PagingDto<AuthorizeDto> getAuditAppAuthorizePage(TokenModel tokenModel, @PathVariable Integer current,
                                                            @ModelAttribute PagingBean pagingBean, @ModelAttribute AuditAuthorizeBean auditAuthorizeBean) {
        return authorizeService.getAuditAuthorizePage(current, tokenModel, auditAuthorizeBean, pagingBean);
    }

    @ApiOperation(value = "获取审核应用权限", notes = "获取审核应用权限")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "授权主键", name = "authorizeId", paramType = "path", required = true)
    })
    @GetMapping("/audit_app_authorize/{authorizeId}")
    @Authority
    public AuthorizeDto getAuditAppAuthorize(TokenModel tokenModel, @PathVariable String authorizeId) {
        return authorizeService.getAuditAppAuthorize(tokenModel, authorizeId);
    }

    @ApiOperation(value = "审核应用权限", notes = "审核权限")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "授权主键", name = "authorizeId", paramType = "path", required = true)
    })
    @PutMapping("/audit_app_authorize/{authorizeId}")
    @Authority
    public AuthorizeDto updateAuditAppAuthorize(TokenModel tokenModel, @PathVariable String authorizeId, @RequestBody AuditBean auditBean) {
        return authorizeService.updateAuditAppAuthorize(tokenModel, authorizeId, auditBean);
    }
}
