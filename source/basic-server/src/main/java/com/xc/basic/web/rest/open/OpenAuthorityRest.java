package com.xc.basic.web.rest.open;

import com.xc.api.basic.dto.AuthorityDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.QueryBean;
import com.xc.core.bean.SignBean;
import com.xc.basic.bean.QueryAuthorityBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.AuthorityEntity;
import com.xc.basic.service.AuthorityService;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.api.basic.enums.AuthorityType;
import com.xc.core.enums.EffectStatus;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>开放接口，权限</p>
 *
 * @author xc
 * @version v1.0.0
 */
@RestController
@Api(tags = {"开放接口，权限"})
public class OpenAuthorityRest {
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "获取权限集合", notes = "开放接口，获取权限集合，获取的是当前应用的数据")
    @GetMapping("/open/authority_list")
    public List<AuthorityDto> getOpenAuthorityList(@ModelAttribute SignBean signBean, @RequestParam String type) {
        signBean.setAuthorityCode(BasicRestCode.getOpenAuthorityList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        QueryAuthorityBean queryAuthorityBean = new QueryAuthorityBean();
        queryAuthorityBean.setStatus(EffectStatus.VALID.getStatus());
        queryAuthorityBean.setType(type);
        QueryBean queryBean = new QueryBean();
        queryBean.setSortField("seq");
        queryBean.setSortRule("ASC");
        List<AuthorityEntity> authorityEntities = authorityService.getAppAuthorityList(appEntity.getId(), queryBean, queryAuthorityBean);
        return ObjectUtils.convertList(authorityEntities, AuthorityDto::new);
    }

    @ApiOperation(value = "获取用户授权权限集合", notes = "开放接口，获取用户授权权限集合，获取的是当前应用的数据")
    @GetMapping("/open/user_authorize_authority_list")
    public List<AuthorityDto> getOpenUserAuthorizeAuthorityList(@ModelAttribute SignBean signBean) {
        signBean.setAuthorityCode(BasicRestCode.getOpenUserAuthorizeAuthorityList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        List<AuthorityEntity> authorityList = authorityService.getAppValidAuthorityList(appEntity.getId(), null, AuthorityType.USER_INFO.getType());
        return ObjectUtils.convertList(authorityList, AuthorityDto::new);
    }
}
