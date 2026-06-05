package com.xc.basic.web.rest.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xc.api.basic.dto.DataTypeDto;
import com.xc.basic.bean.DataTypeBean;
import com.xc.basic.entity.DataTypeEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.DataTypeService;
import com.xc.core.annotation.Authority;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
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
 * <p>需要登录权限接口，数据类型</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，数据类型"})
@RestController
public class DataTypeRest {
    @Autowired
    private DataTypeService dataTypeService;
    @Autowired
    private AppService appService;

    @ApiOperation(value = "获取数据类型分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/data_type_page/{current}")
    @Authority
    public PagingDto<DataTypeDto> getAppDataTypePage(TokenModel tokenModel, @PathVariable Integer current, @PathVariable String appId, @ModelAttribute PagingBean pagingBean,
                                                     @ModelAttribute DataTypeBean dataTypeBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppDataTypePage");
        DataTypeEntity dataTypeEntity = ObjectUtils.convert(new DataTypeEntity(), dataTypeBean);
        dataTypeEntity.setAppId(appId);
        return dataTypeService.getDataTypePage(current, pagingBean, dataTypeEntity);
    }

    @ApiOperation(value = "获取数据类型分页集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @GetMapping("/app/{appId}/data_type_list")
    @Authority
    public List<DataTypeDto> getAppDataTypeList(TokenModel tokenModel, @PathVariable String appId,
                                                @ModelAttribute QueryBean queryBean, @ModelAttribute DataTypeBean dataTypeBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppDataTypeList");
        DataTypeEntity dataTypeEntity = ObjectUtils.convert(new DataTypeEntity(), dataTypeBean);
        dataTypeEntity.setAppId(appId);
        return dataTypeService.getDataTypeList(queryBean, dataTypeEntity);
    }

    @ApiOperation(value = "创建数据类型")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/data_type")
    @Authority
    public DataTypeDto createAppDataType(TokenModel tokenModel, @PathVariable String appId, @RequestBody DataTypeBean dataTypeBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppDataType");
        DataTypeEntity dataTypeEntity = ObjectUtils.convert(new DataTypeEntity(), dataTypeBean);
        dataTypeEntity.setAppId(appId);
        // 初始化排序字段
        if (dataTypeBean.getSeq() == null) {
            DataTypeEntity entity = new DataTypeEntity();
            entity.setAppId(appId);
            dataTypeEntity.setSeq(dataTypeService.count(new QueryWrapper<>(entity)) + 1);
        }
        // 保存
        try {
            if (!dataTypeService.save(dataTypeEntity)) {
                throw FailCode.DATA_TYPE_CREATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.DATA_TYPE_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new DataTypeDto(), dataTypeEntity);
    }

    @ApiOperation(value = "修改数据类型")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "数据类型主键", name = "dataTypeId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/data_type/{dataTypeId}")
    @Authority
    public DataTypeDto updateAppDataType(TokenModel tokenModel, @PathVariable String appId, @PathVariable String dataTypeId, @RequestBody DataTypeBean dataTypeBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppDataType");
        DataTypeEntity dataTypeEntity = verifyUpdateDelete(appId, dataTypeId);
        ObjectUtils.convert(dataTypeEntity, dataTypeBean);
        try {
            if (!dataTypeService.updateById(dataTypeEntity)) {
                throw FailCode.DATA_TYPE_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.DATA_TYPE_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new DataTypeDto(), dataTypeEntity);
    }

    /**
     * 编辑删除验证
     *
     * @param appId      应用id
     * @param dataTypeId 数据类型主键
     */
    private DataTypeEntity verifyUpdateDelete(String appId, String dataTypeId) {
        DataTypeEntity dataTypeEntity = dataTypeService.getById(dataTypeId);
        if (dataTypeEntity == null) {
            throw FailCode.DATA_TYPE_ID_ERROR.getOperateException();
        }
        if (!dataTypeEntity.getAppId().equals(appId)) {
            throw FailCode.DATA_TYPE_APP_ID_ERROR.getOperateException();
        }
        return dataTypeEntity;
    }

    @ApiOperation(value = "删除数据类型")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "数据类型主键", name = "dataTypeId", paramType = "path", required = true)
    })
    @DeleteMapping("/app/{appId}/data_type/{dataTypeId}")
    @Authority
    public DataTypeDto deleteAppDataType(TokenModel tokenModel, @PathVariable String appId, @PathVariable String dataTypeId) {
        appService.verifyUserHaveApp(appId, tokenModel, "deleteAppDataType");
        DataTypeEntity dataTypeEntity = verifyUpdateDelete(appId, dataTypeId);
        if (!dataTypeService.removeById(dataTypeId)) {
            throw FailCode.DATA_TYPE_DELETE_FAIL.getOperateException();
        }
        return ObjectUtils.convert(new DataTypeDto(), dataTypeEntity);
    }

    @ApiOperation(value = "创建数据类型集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @PostMapping("/app/{appId}/data_type_list")
    @Authority
    public List<DataTypeDto> createAppDataTypeList(TokenModel tokenModel, @PathVariable String appId,
                                                   @RequestBody List<DataTypeBean> dataTypeBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppDataTypeList");
        return dataTypeService.createDataTypeList(appId, dataTypeBeans);
    }

    @ApiOperation(value = "上移数据类型")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "数据类型主键", name = "dataTypeId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/data_type/{dataTypeId}/up")
    @Authority
    public void updateAppDataTypeUp(TokenModel tokenModel, @PathVariable String appId, @PathVariable String dataTypeId) {
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppDataTypeUp");
        DataTypeEntity dataTypeEntity = verifyUpdateDelete(appId, dataTypeId);
        if (!dataTypeService.move(dataTypeEntity, false)) {
            throw FailCode.ALREADY_THE_FIRST_LINE.getOperateException();
        }
    }

    @ApiOperation(value = "下移数据类型")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "数据类型主键", name = "dataTypeId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/data_type/{dataTypeId}/down")
    @Authority
    public void updateAppDataTypeDown(TokenModel tokenModel, @PathVariable String appId, @PathVariable String dataTypeId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppDataTypeDown");
        DataTypeEntity dataTypeEntity = verifyUpdateDelete(appId, dataTypeId);
        if (!dataTypeService.move(dataTypeEntity, true)) {
            throw FailCode.ALREADY_THE_LAST_LINE.getOperateException();
        }
    }
}
