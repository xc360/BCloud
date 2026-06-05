package com.xc.basic.web.rest.open;

import com.xc.api.basic.dto.ApiSupplierDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.SignBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.service.ApiSupplierService;
import com.xc.basic.service.BasicAuthorizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>开放接口，接口供应商</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"开放接口，接口供应商"})
@RestController
public class OpenApiSupplierRest {
    @Autowired
    private ApiSupplierService apiSupplierService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "获取接口供应商", notes = "开放接口，获取接口供应商")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "接口供应商标识", name = "code", paramType = "path", required = true),
    })
    @GetMapping("/open/api_supplier/{code}")
    public ApiSupplierDto getOpenApiSupplier(@ModelAttribute SignBean signBean, @PathVariable String code) {
        signBean.setAuthorityCode(BasicRestCode.getOpenApiSupplier.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        return apiSupplierService.getAppApiSupplierByCode(appEntity.getId(), code);
    }

}
