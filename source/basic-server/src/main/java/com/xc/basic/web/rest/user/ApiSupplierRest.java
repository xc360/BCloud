package com.xc.basic.web.rest.user;

import com.xc.api.basic.dto.ApiSupplierDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.ApiSupplierBean;
import com.xc.basic.entity.ApiSupplierEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.ApiSupplierService;
import com.xc.core.annotation.Authority;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>需要登录权限接口，接口供应商</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，接口供应商"})
@RestController
public class ApiSupplierRest {

    @Autowired
    private ApiSupplierService apiSupplierService;
    @Autowired
    private AppService appService;


    @ApiOperation(value = "接口供应商分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/api_supplier_page/{current}")
    @Authority
    public PagingDto<ApiSupplierDto> getAppApiSupplierPage(TokenModel tokenModel, @PathVariable Integer current, @PathVariable String appId,
                                                                   @ModelAttribute PagingBean pagingBean, @ModelAttribute ApiSupplierBean apiSupplierBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppApiSupplierPage");
        ApiSupplierEntity apiSupplierEntity = ObjectUtils.convert(new ApiSupplierEntity(), apiSupplierBean);
        apiSupplierEntity.setAppId(appId);
        return apiSupplierService.getApiSupplierPage(current, pagingBean, apiSupplierEntity);
    }

    @ApiOperation(value = "创建接口供应商")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true)
    })
    @PostMapping("/app/{appId}/api_supplier")
    @Authority
    public ApiSupplierDto createAppApiSupplier(TokenModel tokenModel, @PathVariable String appId, @RequestBody ApiSupplierBean apiSupplierBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppApiSupplier");
        ApiSupplierEntity apiSupplierEntity = ObjectUtils.convert(new ApiSupplierEntity(), apiSupplierBean);
        apiSupplierEntity.setAppId(appId);
        // 保存
        try {
            if (!apiSupplierService.save(apiSupplierEntity)) {
                throw FailCode.API_SUPPLIER_CREATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.API_SUPPLIER_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new ApiSupplierDto(), apiSupplierEntity);
    }

    @ApiOperation(value = "修改接口供应商")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "接口供应商主键", name = "apiSupplierId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/api_supplier/{apiSupplierId}")
    @Authority
    public ApiSupplierDto updateAppApiSupplier(TokenModel tokenModel, @PathVariable String appId,
                                                       @PathVariable String apiSupplierId,
                                                       @RequestBody ApiSupplierBean apiSupplierBean) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppApiSupplier");
        ApiSupplierEntity apiSupplierEntity = verifyUpdateDelete(appId, apiSupplierId);
        ObjectUtils.convert(apiSupplierEntity, apiSupplierBean);
        try {
            if (!apiSupplierService.updateById(apiSupplierEntity)) {
                throw FailCode.API_SUPPLIER_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.API_SUPPLIER_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new ApiSupplierDto(), apiSupplierEntity);
    }

    /**
     * 编辑删除验证
     *
     * @param appId             应用id
     * @param apiSupplierId 接口供应商id
     */
    private ApiSupplierEntity verifyUpdateDelete(String appId, String apiSupplierId) {
        ApiSupplierEntity apiSupplierEntity = apiSupplierService.getById(apiSupplierId);
        if (apiSupplierEntity == null) {
            throw FailCode.API_SUPPLIER_ID_ERROR.getOperateException();
        }
        if (!apiSupplierEntity.getAppId().equals(appId)) {
            throw FailCode.API_SUPPLIER_APP_ID_ERROR.getOperateException();
        }
        return apiSupplierEntity;
    }

    @ApiOperation(value = "删除接口供应商")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "接口供应商主键", name = "apiSupplierId", paramType = "path", required = true),
    })
    @DeleteMapping("/app/{appId}/api_supplier/{apiSupplierId}")
    @Authority
    public void deleteAppApiSupplier(TokenModel tokenModel, @PathVariable String appId, @PathVariable String apiSupplierId) {
        appService.verifyUserHaveApp(appId, tokenModel, "deleteAppApiSupplier");
        verifyUpdateDelete(appId, apiSupplierId);
        if (!apiSupplierService.removeById(apiSupplierId)) {
            throw FailCode.API_SUPPLIER_DELETE_FAIL.getOperateException();
        }
    }

    @ApiOperation(value = "批量创建接口供应商", notes = "创建应用接口供应商集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "接口供应商主键", name = "apiSupplierId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/api_supplier_list")
    @Authority
    public List<ApiSupplierDto> createAppApiSupplierList(TokenModel tokenModel, @PathVariable String appId, @RequestBody List<ApiSupplierBean> apiSupplierBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppApiSupplierList");
        return apiSupplierService.createAppApiSupplierList(appId, apiSupplierBeans);
    }

    @ApiOperation(value = "获取接口供应商集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/api_supplier_list")
    @Authority
    public List<ApiSupplierDto> getAppApiSupplierList(TokenModel tokenModel, @PathVariable String appId,
                                                              @ModelAttribute QueryBean queryBean, @ModelAttribute ApiSupplierBean apiSupplierBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppApiSupplierList");
        ApiSupplierEntity apiSupplierEntity = ObjectUtils.convert(new ApiSupplierEntity(), apiSupplierBean);
        apiSupplierEntity.setAppId(appId);
        return apiSupplierService.getApiSupplierList(queryBean, apiSupplierEntity);
    }
}
