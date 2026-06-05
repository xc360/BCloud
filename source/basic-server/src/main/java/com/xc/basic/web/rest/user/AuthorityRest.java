package com.xc.basic.web.rest.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xc.api.basic.dto.AuthorityDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.AuthorityBean;
import com.xc.basic.bean.QueryAuthorityBean;
import com.xc.basic.entity.AuthorityEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.AuthorityService;
import com.xc.core.annotation.Authority;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>需要登录权限接口，权限</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，权限"})
@RestController
public class AuthorityRest {

    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private AppService appService;

    @ApiOperation(value = "权限分页", notes = "获取应用的权限分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/authority_page/{current}")
    @Authority
    public PagingDto<AuthorityDto> getAppAuthorityPage(TokenModel tokenModel, @PathVariable Integer current, @PathVariable String appId, @ModelAttribute PagingBean pagingBean, @ModelAttribute QueryAuthorityBean queryAuthorityBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppAuthorityPage");
        return authorityService.getAppAuthorityPage(appId, current, pagingBean, queryAuthorityBean);
    }

    @ApiOperation(value = "创建权限")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/authority")
    @Authority
    public AuthorityDto createAppAuthority(TokenModel tokenModel, @PathVariable String appId, @RequestBody AuthorityBean authorityBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppAuthority");
        AuthorityEntity authorityEntity = ObjectUtils.convert(new AuthorityEntity(), authorityBean);
        authorityEntity.setAppId(appId);
        authorityEntity.setNode(IdWorker.getIdStr());
        // 初始化排序字段
        if (authorityBean.getSeq() == null) {
            AuthorityEntity entity = new AuthorityEntity();
            entity.setAppId(appId);
            entity.setParentNode(authorityEntity.getParentNode());
            authorityEntity.setSeq(authorityService.count(new QueryWrapper<>(entity)) + 1);
        }
        // 保存
        try {
            if (!authorityService.save(authorityEntity)) {
                throw FailCode.AUTHORITY_CREATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.AUTHORITY_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new AuthorityDto(), authorityEntity);
    }

    @ApiOperation(value = "修改权限")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "权限主键", name = "authorityId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/authority/{authorityId}")
    @Authority
    public AuthorityDto updateAppAuthority(TokenModel tokenModel, @PathVariable String appId, @PathVariable String authorityId, @RequestBody AuthorityBean authorityBean) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppAuthority");
        AuthorityEntity authorityEntity = verifyUpdateDelete(appId, authorityId);
        ObjectUtils.convert(authorityEntity, authorityBean);
        List<AuthorityEntity> entities = authorityService.getChildrenAuthority(appId, authorityEntity.getNode());
        entities.add(authorityEntity);
        for (AuthorityEntity entity : entities) {
            if (entity.getNode().equals(authorityEntity.getParentNode())) {
                throw FailCode.NOT_CHILDREN_ADD.getOperateException();
            }
        }
        try {
            if (!authorityService.updateById(authorityEntity)) {
                throw FailCode.AUTHORITY_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.AUTHORITY_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new AuthorityDto(), authorityEntity);
    }

    /**
     * 编辑删除验证
     *
     * @param appId       应用id
     * @param authorityId 权限id
     */
    private AuthorityEntity verifyUpdateDelete(String appId, String authorityId) {
        AuthorityEntity authorityEntity = authorityService.getById(authorityId);
        if (authorityEntity == null) {
            throw FailCode.AUTHORITY_ID_ERROR.getOperateException();
        }
        if (!authorityEntity.getAppId().equals(appId)) {
            throw FailCode.AUTHORITY_APP_ID_ERROR.getOperateException();
        }
        return authorityEntity;
    }

    @ApiOperation(value = "删除权限")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "权限主键", name = "authorityId", paramType = "path", required = true)
    })
    @DeleteMapping("/app/{appId}/authority/{authorityId}")
    @Authority
    public void deleteAppAuthority(TokenModel tokenModel, @PathVariable String appId, @PathVariable String authorityId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "deleteAppAuthority");
        AuthorityEntity authorityEntity = verifyUpdateDelete(appId, authorityId);
        authorityService.deleteAuthority(authorityEntity);
    }

    @ApiOperation(value = "上移权限")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "权限主键", name = "authorityId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/authority/{authorityId}/up")
    @Authority
    public void updateAppAuthorityUp(TokenModel tokenModel, @PathVariable String appId, @PathVariable String authorityId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppAuthorityUp");
        AuthorityEntity authorityEntity = verifyUpdateDelete(appId, authorityId);
        if (!authorityService.move(authorityEntity, false)) {
            throw FailCode.ALREADY_THE_FIRST_LINE.getOperateException();
        }
    }

    @ApiOperation(value = "下移权限")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "权限主键", name = "authorityId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/authority/{authorityId}/down")
    @Authority
    public void updateAppAuthorityDown(TokenModel tokenModel, @PathVariable String appId, @PathVariable String authorityId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppAuthorityDown");
        AuthorityEntity authorityEntity = verifyUpdateDelete(appId, authorityId);
        if (!authorityService.move(authorityEntity, true)) {
            throw FailCode.ALREADY_THE_LAST_LINE.getOperateException();
        }
    }

    @ApiOperation(value = "批量创建权限", notes = "创建应用权限集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "权限主键", name = "authorityId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/authority_list")
    @Authority
    public List<AuthorityDto> createAppAuthorityList(TokenModel tokenModel, @PathVariable String appId, @RequestBody List<AuthorityBean> authorityBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppAuthorityList");
        return authorityService.createAppAuthorityList(appId, authorityBeans);
    }

    @ApiOperation(value = "获取权限集合", notes = "根据条件获取当前用户的应用下的所有权限")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/authority_list")
    @Authority
    public List<AuthorityDto> getAppAuthorityList(TokenModel tokenModel, @PathVariable String appId, @ModelAttribute QueryBean queryBean, @ModelAttribute QueryAuthorityBean queryAuthorityBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppAuthorityList");
        return ObjectUtils.convertList(authorityService.getAppAuthorityList(appId, queryBean, queryAuthorityBean), AuthorityDto::new);
    }
}
