package com.xc.basic.web.rest.open;

import com.xc.api.basic.bean.InfoBean;
import com.xc.api.basic.dto.InfoDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.QueryBean;
import com.xc.core.bean.SignBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.InfoEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.InfoService;
import com.xc.core.enums.EffectStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>开放接口，应用信息</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"开放接口，应用信息"})
@RestController
public class OpenAppInfoRest {

    @Autowired
    private InfoService infoService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "获取信息集合", notes = "开放接口，根据类型获取字典集合，" +
            "不传类型获取全部，0：客户端信息，1：后台信息，" +
            "获取的是当前应用的数据，获取的是状态为有效数据")
    @GetMapping("/open/info_list")
    public List<InfoDto> getOpenInfoList(@ModelAttribute SignBean signBean, @RequestParam(required = false) String type) {
        signBean.setAuthorityCode(BasicRestCode.getOpenInfoList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        InfoEntity infoEntity = new InfoEntity();
        infoEntity.setAppId(appEntity.getId());
        infoEntity.setType(type);
        infoEntity.setStatus(EffectStatus.VALID.getStatus());
        return infoService.getInfoList(new QueryBean(), infoEntity);
    }


    @ApiOperation(value = "创建信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
    })
    @PostMapping("/open/info_list")
    public List<InfoDto> createOpenInfoList(@ModelAttribute SignBean signBean, @RequestBody List<InfoBean> infoBeans) {
        signBean.setAuthorityCode(BasicRestCode.createOpenInfoList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        return infoService.createAppInfoList(appEntity.getId(), infoBeans);
    }
}
