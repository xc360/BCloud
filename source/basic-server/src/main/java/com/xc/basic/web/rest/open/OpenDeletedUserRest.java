package com.xc.basic.web.rest.open;

import com.xc.api.basic.dto.DeletedUserDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.SignBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.DeletedUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>开放接口，已删除用户</p>
 *
 * @author xc
 * @version v1.0.0
 */
@RestController
@Api(tags = {"开放接口，已删除用户"})
public class OpenDeletedUserRest {
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;
    @Autowired
    private DeletedUserService deletedUserService;

    @ApiOperation(value = "获取已删除用户Id集合", notes = "开放接口，获取已删除用户Id集合，获取的是当前应用的数据")
    @GetMapping("/open/deleted_user_list")
    public List<DeletedUserDto> getOpenDeletedUserList(@ModelAttribute SignBean signBean) {
        signBean.setAuthorityCode(BasicRestCode.getOpenDeletedUserList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        return deletedUserService.getAppDeletedUserList(appEntity.getId());
    }
}
