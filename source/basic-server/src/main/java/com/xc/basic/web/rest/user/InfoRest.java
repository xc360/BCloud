package com.xc.basic.web.rest.user;

import com.xc.api.basic.bean.InfoBean;
import com.xc.api.basic.dto.InfoDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.entity.InfoEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.InfoService;
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
 * <p>需要登录权限接口，应用信息</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，应用信息"})
@RestController
public class InfoRest {

    @Autowired
    private InfoService infoService;
    @Autowired
    private AppService appService;

    @ApiOperation(value = "信息分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true),
    })
    @GetMapping("/app/{appId}/info_page/{current}")
    @Authority
    public PagingDto<InfoDto> getAppInfoPage(TokenModel tokenModel, @PathVariable Integer current,
                                             @PathVariable String appId, @ModelAttribute PagingBean pagingBean,
                                             @ModelAttribute InfoBean infoBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppInfoPage");
        InfoEntity infoEntity = ObjectUtils.convert(new InfoEntity(), infoBean);
        infoEntity.setAppId(appId);
        return infoService.getInfoPage(current, pagingBean, infoEntity);
    }

    @ApiOperation(value = "创建信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @PostMapping("/app/{appId}/info")
    @Authority
    public InfoDto createAppInfo(TokenModel tokenModel, @PathVariable String appId, @RequestBody InfoBean infoBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppInfo");
        InfoEntity infoEntity = ObjectUtils.convert(new InfoEntity(), infoBean);
        if (infoEntity.getKey() == null) {
            throw FailCode.INFO_KEY_NOT_NULL.getOperateException();
        }
        infoEntity.setAppId(appId);
        try {
            if (!infoService.save(infoEntity)) {
                throw FailCode.INFO_CREATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.INFO_KEY_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new InfoDto(), infoEntity);
    }

    @ApiOperation(value = "修改信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "信息主键", name = "infoId", paramType = "path", required = true),
    })
    @PutMapping("/app/{appId}/info/{infoId}")
    @Authority
    public InfoDto updateAppInfo(TokenModel tokenModel, @PathVariable String appId,
                                 @PathVariable String infoId, @RequestBody InfoBean infoBean) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppInfo");
        InfoEntity infoEntity = verifyUpdateDelete(appId, infoId);
        ObjectUtils.convert(infoEntity, infoBean);
        if (infoEntity.getKey() == null) {
            throw FailCode.INFO_KEY_NOT_NULL.getOperateException();
        }
        try {
            if (!infoService.updateById(infoEntity)) {
                throw FailCode.INFO_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.INFO_KEY_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new InfoDto(), infoEntity);
    }

    /**
     * 编辑删除验证
     *
     * @param appId  应用id
     * @param infoId 信息id
     */
    private InfoEntity verifyUpdateDelete(String appId, String infoId) {
        InfoEntity infoEntity = infoService.getById(infoId);
        if (infoEntity == null) {
            throw FailCode.INFO_ID_ERROR.getOperateException();
        }
        if (!infoEntity.getAppId().equals(appId)) {
            throw FailCode.INFO_APP_ID_ERROR.getOperateException();
        }
        return infoEntity;
    }

    @ApiOperation(value = "删除信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "信息主键", name = "infoId", paramType = "path", required = true),
    })
    @DeleteMapping("/app/{appId}/info/{infoId}")
    @Authority
    public void deleteAppInfo(TokenModel tokenModel, @PathVariable String appId, @PathVariable String infoId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "deleteAppInfo");
        verifyUpdateDelete(appId, infoId);
        if (!infoService.removeById(infoId)) {
            throw FailCode.INFO_DELETE_FAIL.getOperateException();
        }
    }

    @ApiOperation(value = "创建信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @PostMapping("/app/{appId}/info_list")
    @Authority
    public List<InfoDto> createAppInfoList(TokenModel tokenModel, @PathVariable String appId,
                                           @RequestBody List<InfoBean> infoBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppInfoList");
        return infoService.createAppInfoList(appId, infoBeans);
    }

    @ApiOperation(value = "获取的信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/info_list")
    @Authority
    public List<InfoDto> getAppInfoList(TokenModel tokenModel, @PathVariable String appId,
                                        @ModelAttribute QueryBean queryBean, @ModelAttribute InfoBean infoBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppInfoList");
        InfoEntity infoEntity = ObjectUtils.convert(new InfoEntity(), infoBean);
        infoEntity.setAppId(appId);
        return infoService.getInfoList(queryBean, infoEntity);
    }
}
