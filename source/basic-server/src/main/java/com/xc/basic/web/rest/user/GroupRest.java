package com.xc.basic.web.rest.user;

import com.xc.api.basic.dto.GroupDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.GroupBean;
import com.xc.basic.bean.GroupRoleBean;
import com.xc.basic.dto.GroupRoleDto;
import com.xc.basic.entity.GroupEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.GroupService;
import com.xc.core.annotation.Authority;
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
 * <p>需要登录权限接口，用户组</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，用户组"})
@RestController
public class GroupRest {

    @Autowired
    private GroupService groupService;
    @Autowired
    private AppService appService;

    @ApiOperation(value = "用户组分页", notes = "获取应用的用户组分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/group_page/{current}")
    @Authority
    public PagingDto<GroupDto> getAppGroupPage(TokenModel tokenModel, @PathVariable Integer current, @PathVariable String appId,
                                               @ModelAttribute PagingBean pagingBean, @ModelAttribute GroupBean groupBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppGroupPage");
        GroupEntity groupEntity = ObjectUtils.convert(new GroupEntity(), groupBean);
        groupEntity.setAppId(appId);
        return groupService.getGroupPage(current, pagingBean, groupEntity);
    }


    @ApiOperation(value = "创建用户组")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/group")
    @Authority
    public GroupDto createAppGroup(TokenModel tokenModel, @PathVariable String appId, @RequestBody GroupBean groupBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppGroup");
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setAppId(appId);
        ObjectUtils.convert(groupEntity, groupBean);
        return groupService.createGroup(groupEntity, groupBean.getRoleIds());
    }

    @ApiOperation(value = "修改用户组")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "组主键", name = "groupId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/group/{groupId}")
    @Authority
    public GroupDto updateAppGroup(TokenModel tokenModel, @PathVariable String appId,
                                   @PathVariable String groupId, @RequestBody GroupBean groupBean) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppGroup");
        GroupEntity groupEntity = verifyUpdateDelete(appId, groupId);
        ObjectUtils.convert(groupEntity, groupBean);
        return groupService.updateGroup(groupEntity, groupBean.getRoleIds());
    }

    /**
     * 编辑删除验证
     *
     * @param appId   应用id
     * @param groupId 组id
     */
    private GroupEntity verifyUpdateDelete(String appId, String groupId) {
        GroupEntity groupEntity = groupService.getById(groupId);
        if (groupEntity == null) {
            throw FailCode.GROUP_ID_ERROR.getOperateException();
        }
        if (!groupEntity.getAppId().equals(appId)) {
            throw FailCode.GROUP_APP_ID_ERROR.getOperateException();
        }
        return groupEntity;
    }

    @ApiOperation(value = "删除用户组")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "组主键", name = "groupId", paramType = "path", required = true)
    })
    @DeleteMapping("/app/{appId}/group/{groupId}")
    @Authority
    public void deleteAppGroup(TokenModel tokenModel, @PathVariable String appId, @PathVariable String groupId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "deleteAppGroup");
        verifyUpdateDelete(appId, groupId);
        groupService.deleteGroup(groupId);
    }


    @ApiOperation(value = "查询用户组")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "组主键", name = "groupId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/group/{groupId}")
    @Authority
    public GroupDto getAppGroup(TokenModel tokenModel, @PathVariable String appId, @PathVariable String groupId) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppGroup");
        return groupService.getGroupById(groupId);
    }

    @ApiOperation(value = "获取用户组集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/group_list")
    @Authority
    public List<GroupDto> getAppGroupList(TokenModel tokenModel, @PathVariable String appId,
                                          @ModelAttribute QueryBean queryBean, @ModelAttribute GroupBean groupBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppGroupList");
        GroupEntity groupEntity = ObjectUtils.convert(new GroupEntity(), groupBean);
        groupEntity.setAppId(appId);
        return groupService.getGroupList(queryBean, groupEntity);
    }

    @ApiOperation(value = "创建用户组集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @PostMapping("/app/{appId}/group_list")
    @Authority
    public List<GroupDto> createAppGroupList(TokenModel tokenModel, @PathVariable String appId,
                                             @RequestBody List<GroupBean> groupBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppGroupList");
        return groupService.createGroupList(appId, groupBeans);
    }


    @ApiOperation(value = "创建用户组角色关联集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @PostMapping("/app/{appId}/group_role_relation_list")
    @Authority
    public List<GroupRoleDto> createAppGroupRoleRelationList(TokenModel tokenModel, @PathVariable String appId,
                                                             @RequestBody List<GroupRoleBean> groupRoleBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppGroupRoleRelationList");
        return groupService.createAppGroupRoleRelationList(appId, groupRoleBeans);
    }

    @ApiOperation(value = "获取的用户组角色关联集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/group_role_relation_list")
    @Authority
    public List<GroupRoleDto> getAppGroupRoleRelationList(TokenModel tokenModel, @PathVariable String appId) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppGroupRoleRelationList");
        return groupService.getAppGroupRoleRelationList(appId);
    }

}
