package com.xc.basic.web.rest.open;

import com.xc.api.basic.dto.TreeDictDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.SignBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.TreeDictService;
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
 * <p>开放接口，树形字典</p>
 *
 * @author xc
 * @version v1.0.0
 */
@RestController
@Api(tags = {"开放接口，树形字典"})
public class OpenTreeDictRest {

    @Autowired
    private TreeDictService treeDictService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "获取树形字典集合", notes = "根据父级node获取树形字典集合，不传node获取顶级树形字典")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "树形字典类型,为null查询全部", name = "type", paramType = "query"),
            @ApiImplicitParam(value = "树形字典标识,为null查询全部", name = "code", paramType = "query"),
    })
    @GetMapping("/open/tree_dict_list")
    public List<TreeDictDto> getOpenTreeDictList(@ModelAttribute SignBean signBean, @RequestParam(required = false) String type,
                                                 @RequestParam(required = false) String firstCode) {
        signBean.setAuthorityCode(BasicRestCode.getOpenTreeDictList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        return treeDictService.getTreeDictList(appEntity.getId(), type, firstCode);
    }

    @ApiOperation(value = "获取树形节点集合", notes = "获取树形节点集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "树形字典类型", name = "type", paramType = "query"),
            @ApiImplicitParam(value = "树形字典标识", name = "code", paramType = "query"),
    })
    @GetMapping("/open/tree_node_list")
    public List<TreeDictDto> getOpenTreeNodeList(@ModelAttribute SignBean signBean, @RequestParam String type, @RequestParam String code) {
        signBean.setAuthorityCode(BasicRestCode.getOpenTreeNodeList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        return treeDictService.getTreeNodeList(appEntity.getId(), type, code);
    }
}
