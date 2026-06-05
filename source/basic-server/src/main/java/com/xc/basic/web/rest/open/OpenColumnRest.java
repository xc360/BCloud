package com.xc.basic.web.rest.open;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xc.api.basic.dto.ColumnDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.basic.config.Constants;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.ColumnEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.ColumnService;
import com.xc.core.bean.QueryBean;
import com.xc.core.bean.SignBean;
import com.xc.core.enums.EffectStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>开放接口，栏目</p>
 *
 * @author xc
 * @version v1.0.0
 */
@RestController
@Api(tags = {"开放接口，栏目"})
public class OpenColumnRest {
    @Autowired
    private ColumnService columnService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;
    @Autowired
    private Constants constants;

    @ApiOperation(value = "获取栏目集合", notes = "开放接口，获取栏目集合，获取的是当前应用的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "数据类型", name = "dataType", paramType = "path")
    })
    @GetMapping("/open/column_list")
    public List<ColumnDto> getOpenColumnList(@ModelAttribute SignBean signBean, @RequestParam(required = false) String firstCode) {
        signBean.setAuthorityCode(BasicRestCode.getOpenColumnList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        String parentNode = null;
        if (firstCode != null) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setAppId(appEntity.getId());
            columnEntity.setCode(firstCode);
            ColumnEntity entity = columnService.getOne(new QueryWrapper<>(columnEntity));
            if (entity != null) {
                parentNode = entity.getNode();
            } else if (constants.getRoot().equals(firstCode)) {
                parentNode = constants.getRoot();
            } else {
                return new ArrayList<>();
            }
        }
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setParentNode(parentNode);
        columnEntity.setAppId(appEntity.getId());
        columnEntity.setStatus(EffectStatus.VALID.getStatus());
        return columnService.getColumnList(new QueryBean(), columnEntity);
    }
}
