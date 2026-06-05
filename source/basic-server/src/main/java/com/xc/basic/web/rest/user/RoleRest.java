package com.xc.basic.web.rest.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xc.basic.bean.RoleAuthorityBean;
import com.xc.basic.bean.RoleBean;
import com.xc.basic.dto.RoleAuthorityDto;
import com.xc.basic.dto.RoleDto;
import com.xc.basic.entity.RoleEntity;
import com.xc.basic.entity.RoleEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.RoleService;
import com.xc.core.annotation.Authority;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>需要登录权限接口，角色</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，角色"})
@RestController
public class RoleRest {
    @Autowired
    private RoleService roleService;
    @Autowired
    private AppService appService;

    @ApiOperation(value = "角色分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/role_page/{current}")
    @Authority
    public PagingDto<RoleDto> getAppRolePage(TokenModel tokenModel, @PathVariable Integer current,
                                             @PathVariable String appId, @ModelAttribute PagingBean pagingBean,
                                             @ModelAttribute RoleBean roleBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppRolePage");
        RoleEntity roleEntity = ObjectUtils.convert(new RoleEntity(), roleBean);
        roleEntity.setAppId(appId);
        return roleService.getRolePage(current, pagingBean, roleEntity);
    }

    @ApiOperation(value = "创建角色")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/role")
    @Authority
    public RoleDto createAppRole(TokenModel tokenModel, @PathVariable String appId, @RequestBody RoleBean roleBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppRole");
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setAppId(appId);
        ObjectUtils.convert(roleEntity, roleBean);
        // 初始化排序字段
        if (roleBean.getSeq() == null) {
            RoleEntity entity = new RoleEntity();
            entity.setAppId(roleEntity.getAppId());
            roleEntity.setSeq(roleService.count(new QueryWrapper<>(entity)) + 1);
        }
        return roleService.createRole(roleEntity, roleBean.getAuthorityIds());
    }

    @ApiOperation(value = "修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "角色主键", name = "roleId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/role/{roleId}")
    @Authority
    public RoleDto updateAppRole(TokenModel tokenModel, @PathVariable String appId,
                                 @PathVariable String roleId, @RequestBody RoleBean roleBean) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppRole");
        RoleEntity roleEntity = verifyUpdateDelete(appId, roleId);
        ObjectUtils.convert(roleEntity, roleBean);
        return roleService.updateRole(roleEntity, roleBean.getAuthorityIds());
    }

    /**
     * 编辑删除验证
     *
     * @param appId  应用id
     * @param roleId 角色id
     */
    private RoleEntity verifyUpdateDelete(String appId, String roleId) {
        RoleEntity roleEntity = roleService.getById(roleId);
        if (roleEntity == null) {
            throw FailCode.ROLE_ID_ERROR.getOperateException();
        }
        if (!roleEntity.getAppId().equals(appId)) {
            throw FailCode.ROLE_APP_ID_ERROR.getOperateException();
        }
        return roleEntity;
    }


    @ApiOperation(value = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "角色主键", name = "roleId", paramType = "path", required = true)
    })
    @DeleteMapping("/app/{appId}/role/{roleId}")
    @Authority
    public void deleteAppRole(TokenModel tokenModel, @PathVariable String appId, @PathVariable String roleId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "deleteAppRole");
        verifyUpdateDelete(appId, roleId);
        roleService.deleteRole(roleId);
    }

    @ApiOperation(value = "查询角色")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "角色主键", name = "roleId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/role/{roleId}")
    @Authority
    public RoleDto getAppRole(TokenModel tokenModel, @PathVariable String appId, @PathVariable String roleId) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppRole");
        return roleService.getRole(roleId);
    }

    @ApiOperation(value = "获取角色集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/role_list")
    @Authority
    public List<RoleDto> getAppRoleList(TokenModel tokenModel, @PathVariable String appId,
                                        @ModelAttribute QueryBean queryBean, @ModelAttribute RoleBean roleBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppRoleList");
        RoleEntity roleEntity = ObjectUtils.convert(new RoleEntity(), roleBean);
        roleEntity.setAppId(appId);
        return roleService.getRoleList(queryBean, roleEntity);
    }

    @ApiOperation(value = "上移角色")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "栏目主键", name = "roleId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/role/{roleId}/up")
    @Authority
    public void updateAppRoleUp(TokenModel tokenModel, @PathVariable String appId, @PathVariable String roleId) {
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppRoleUp");
        RoleEntity roleEntity = verifyUpdateDelete(appId, roleId);
        if (!roleService.move(roleEntity, false)) {
            throw FailCode.ALREADY_THE_FIRST_LINE.getOperateException();
        }
    }

    @ApiOperation(value = "下移角色")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "栏目主键", name = "roleId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/role/{roleId}/down")
    @Authority
    public void updateAppRoleDown(TokenModel tokenModel, @PathVariable String appId, @PathVariable String roleId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppRoleDown");
        RoleEntity roleEntity = verifyUpdateDelete(appId, roleId);
        if (!roleService.move(roleEntity, true)) {
            throw FailCode.ALREADY_THE_LAST_LINE.getOperateException();
        }
    }

    @ApiOperation(value = "创建角色集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @PostMapping("/app/{appId}/role_list")
    @Authority
    public List<RoleDto> createAppRoleList(TokenModel tokenModel, @PathVariable String appId,
                                           @RequestBody List<RoleBean> roleBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppRoleList");
        return roleService.createRoleList(appId, roleBeans);
    }

    @ApiOperation(value = "创建角色权限关联集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @PostMapping("/app/{appId}/role_authority_relation_list")
    @Authority
    public List<RoleAuthorityDto> createAppRoleAuthorityRelationList(TokenModel tokenModel, @PathVariable String appId,
                                                                     @RequestBody List<RoleAuthorityBean> roleAuthorityBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppRoleAuthorityRelationList");
        return roleService.createAppRoleAuthorityRelationList(appId, roleAuthorityBeans);
    }

    @ApiOperation(value = "获取的角色权限关联集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/role_authority_relation_list")
    @Authority
    public List<RoleAuthorityDto> getAppRoleAuthorityRelationList(TokenModel tokenModel, @PathVariable String appId) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppRoleAuthorityRelationList");
        return roleService.getAppRoleAuthorityRelationList(appId);
    }
}
