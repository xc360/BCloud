package com.xc.basic.web.rest.open;

import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.QueryBean;
import com.xc.core.bean.SignBean;
import com.xc.api.basic.dto.DataTypeDto;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.DataTypeEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.DataTypeService;
import com.xc.core.enums.EffectStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>开放接口，数据类型</p>
 *
 * @author xc
 * @version v1.0.0
 */
@RestController
@Api(tags = {"开放接口，数据类型"})
public class OpenDataTypeRest {
    @Autowired
    private DataTypeService dataTypeService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "获取数据类型集合", notes = "开放接口，获取全部，获取的是当前应用的数据")
    @GetMapping("/open/data_type_list")
    public List<DataTypeDto> getOpenDataTypeList(@ModelAttribute SignBean signBean) {
        signBean.setAuthorityCode(BasicRestCode.getOpenDataTypeList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        DataTypeEntity dataTypeEntity = new DataTypeEntity();
        dataTypeEntity.setAppId(appEntity.getId());
        dataTypeEntity.setStatus(EffectStatus.VALID.getStatus());
        return dataTypeService.getDataTypeList(new QueryBean(), dataTypeEntity);
    }
}
