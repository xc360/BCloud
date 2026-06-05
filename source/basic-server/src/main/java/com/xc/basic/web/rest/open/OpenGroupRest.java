package com.xc.basic.web.rest.open;

import com.xc.api.basic.dto.GroupDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.QueryBean;
import com.xc.core.bean.SignBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.GroupEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.GroupService;
import com.xc.core.enums.EffectStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>开放接口，用户组</p>
 *
 * @author xc
 * @version v1.0.0
 */
@RestController
@Api(tags = {"开放接口，用户组"})
public class OpenGroupRest {

    @Autowired
    private GroupService groupService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "获取用户组集合", notes = "开放接口，根据类型获取用户组集合,不传类型获取全部，获取的是当前应用的数据")
    @GetMapping("/open/user/group_list")
    public List<GroupDto> getOpenUserGroupList(@ModelAttribute SignBean signBean) {
        signBean.setAuthorityCode(BasicRestCode.getOpenUserGroupList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setAppId(appEntity.getId());
        groupEntity.setStatus(EffectStatus.VALID.getStatus());
        return groupService.getUserGroupList(signBean.getUserId(), new QueryBean(), groupEntity);
    }
}
