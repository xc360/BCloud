package com.xc.basic.web.rest.open;

import com.xc.api.basic.dto.PageDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.PageEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.PageService;
import com.xc.core.bean.QueryBean;
import com.xc.core.bean.SignBean;
import com.xc.core.enums.EffectStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>开放接口，页面</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"开放接口，页面"})
@RestController
public class OpenPageRest {

    @Autowired
    private PageService pageService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "获取页面集合", notes = "获取页面集合")
    @GetMapping("/open/page_list")
    public List<PageDto> getOpenPageList(@ModelAttribute SignBean signBean) {
        signBean.setAuthorityCode(BasicRestCode.getOpenPageList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        PageEntity pageEntity = new PageEntity();
        pageEntity.setAppId(appEntity.getId());
        pageEntity.setStatus(EffectStatus.VALID.getStatus());
        return pageService.getPageList(new QueryBean(), pageEntity);
    }
}
