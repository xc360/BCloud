package com.xc.basic.web.rest.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xc.api.basic.dto.DictDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.DictBean;
import com.xc.basic.entity.DictEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.DictService;
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
 * <p>需要登录权限接口，字典</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，字典"})
@RestController
public class DictRest {
    @Autowired
    private DictService dictService;
    @Autowired
    private AppService appService;


    @ApiOperation(value = "字典分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/dict_page/{current}")
    @Authority
    public PagingDto<DictDto> getAppDictPage(TokenModel tokenModel, @PathVariable Integer current, @PathVariable String appId,
                                             @ModelAttribute PagingBean pagingBean, @ModelAttribute DictBean dictBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppDictPage");
        DictEntity dictEntity = ObjectUtils.convert(new DictEntity(), dictBean);
        dictEntity.setAppId(appId);
        return dictService.getDictPage(current, pagingBean, dictEntity);
    }

    @ApiOperation(value = "创建字典")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true)
    })
    @PostMapping("/app/{appId}/dict")
    @Authority
    public DictDto createAppDict(TokenModel tokenModel, @PathVariable String appId, @RequestBody DictBean dictBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppDict");
        DictEntity dictEntity = ObjectUtils.convert(new DictEntity(), dictBean);
        dictEntity.setAppId(appId);
        // 初始化排序字段
        if (dictBean.getSeq() == null) {
            DictEntity entity = new DictEntity();
            entity.setAppId(appId);
            entity.setType(dictEntity.getType());
            dictEntity.setSeq(dictService.count(new QueryWrapper<>(entity)) + 1);
        }
        // 保存
        try {
            if (!dictService.save(dictEntity)) {
                throw FailCode.DICT_CREATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.DICT_TYPE_VALUE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new DictDto(), dictEntity);
    }

    @ApiOperation(value = "修改字典")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "字典主键", name = "dictId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/dict/{dictId}")
    @Authority
    public DictDto updateAppDict(TokenModel tokenModel, @PathVariable String appId,
                                 @PathVariable String dictId,
                                 @RequestBody DictBean dictBean) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppDict");
        DictEntity dictEntity = verifyUpdateDelete(appId, dictId);
        ObjectUtils.convert(dictEntity, dictBean);
        try {
            if (!dictService.updateById(dictEntity)) {
                throw FailCode.DICT_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.DICT_TYPE_VALUE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new DictDto(), dictEntity);
    }

    /**
     * 编辑删除验证
     *
     * @param appId  应用id
     * @param dictId 字典id
     */
    private DictEntity verifyUpdateDelete(String appId, String dictId) {
        DictEntity dictEntity = dictService.getById(dictId);
        if (dictEntity == null) {
            throw FailCode.DICT_ID_ERROR.getOperateException();
        }
        if (!dictEntity.getAppId().equals(appId)) {
            throw FailCode.DICT_APP_ID_ERROR.getOperateException();
        }
        return dictEntity;
    }

    @ApiOperation(value = "删除字典")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "字典主键", name = "dictId", paramType = "path", required = true),
    })
    @DeleteMapping("/app/{appId}/dict/{dictId}")
    @Authority
    public void deleteAppDict(TokenModel tokenModel, @PathVariable String appId, @PathVariable String dictId) {
        appService.verifyUserHaveApp(appId, tokenModel, "deleteAppDict");
        verifyUpdateDelete(appId, dictId);
        if (!dictService.removeById(dictId)) {
            throw FailCode.DICT_DELETE_FAIL.getOperateException();
        }
    }

    @ApiOperation(value = "批量创建字典", notes = "创建应用字典集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "字典主键", name = "dictId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/dict_list")
    @Authority
    public List<DictDto> createAppDictList(TokenModel tokenModel, @PathVariable String appId, @RequestBody List<DictBean> dictBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppDictList");
        return dictService.createAppDictList(appId, dictBeans);
    }

    @ApiOperation(value = "获取字典集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/dict_list")
    @Authority
    public List<DictDto> getAppDictList(TokenModel tokenModel, @PathVariable String appId,
                                        @ModelAttribute QueryBean queryBean, @ModelAttribute DictBean dictBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppDictList");
        DictEntity dictEntity = ObjectUtils.convert(new DictEntity(), dictBean);
        dictEntity.setAppId(appId);
        return dictService.getDictList(queryBean, dictEntity);
    }


    @ApiOperation(value = "上移字典")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "字典主键", name = "dictId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/dict/{dictId}/up")
    @Authority
    public void updateAppDictUp(TokenModel tokenModel, @PathVariable String appId, @PathVariable String dictId) {
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppDictUp");
        DictEntity dictEntity = verifyUpdateDelete(appId, dictId);
        if (!dictService.move(dictEntity, false)) {
            throw FailCode.ALREADY_THE_FIRST_LINE.getOperateException();
        }
    }

    @ApiOperation(value = "下移字典")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "字典主键", name = "dictId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/dict/{dictId}/down")
    @Authority
    public void updateAppDictDown(TokenModel tokenModel, @PathVariable String appId, @PathVariable String dictId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppDictDown");
        DictEntity dictEntity = verifyUpdateDelete(appId, dictId);
        if (!dictService.move(dictEntity, true)) {
            throw FailCode.ALREADY_THE_LAST_LINE.getOperateException();
        }
    }

}
