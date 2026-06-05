package com.xc.basic.web.rest.open;

import com.xc.api.basic.dto.TreeDictDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.SignBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.enums.TreeDictType;
import com.xc.basic.service.AppService;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.TreeDictService;
import com.xc.core.aspect.BasicConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>开放接口，区域</p>
 *
 * @author xc
 * @version v1.0.0
 */
@RestController
@Api(tags = {"开放接口，区域"})
public class OpenAreaRest {
    @Autowired
    private TreeDictService treeDictService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;
    @Autowired
    private AppService appService;
    @Autowired
    private BasicConstants basicConstants;

    @ApiOperation(value = "获取基础服务的区域集合", notes = "根据父级node获取基础服务的区域集合，不传node获取顶级区域")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "区域标识,为null查询全部", name = "code", paramType = "query"),
    })
    @GetMapping("/open/basic_area_list")
    public List<TreeDictDto> getOpenBasicAreaList(@ModelAttribute SignBean signBean, @RequestParam(required = false) String firstCode) {
        signBean.setAuthorityCode(BasicRestCode.getOpenBasicAreaList.getCode());
        basicAuthorizeService.verifySign(signBean);
        AppEntity appEntity = appService.verifyAppSecret(basicConstants.getAppId(), basicConstants.getAppSecret());
        return treeDictService.getTreeDictList(appEntity.getId(), TreeDictType.AREA.getType(), firstCode);
    }

    @ApiOperation(value = "获取基础平台的区域节点集合", notes = "获取基础平台的区域节点集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "区域标识", name = "code", paramType = "query"),
    })
    @GetMapping("/open/basic_area_node_list")
    public List<TreeDictDto> getOpenBasicAreaNodeList(@ModelAttribute SignBean signBean, @RequestParam String code) {
        signBean.setAuthorityCode(BasicRestCode.getOpenBasicAreaNodeList.getCode());
        basicAuthorizeService.verifySign(signBean);
        AppEntity appEntity = appService.verifyAppSecret(basicConstants.getAppId(), basicConstants.getAppSecret());
        return treeDictService.getTreeNodeList(appEntity.getId(), TreeDictType.AREA.getType(), code);
    }

}
