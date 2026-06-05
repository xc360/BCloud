package com.xc.basic.web.rest.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xc.api.basic.dto.TreeDictDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.TreeDictBean;
import com.xc.basic.entity.TreeDictEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.TreeDictService;
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
 * <p>需要登录权限接口，树形字典</p>
 *
 * @author xc
 * @version v1.0.0
 */
@RestController
@Api(tags = {"需要登录权限接口，树形字典"})
public class TreeDictRest {

    @Autowired
    private TreeDictService treeDictService;
    @Autowired
    private AppService appService;

    @ApiOperation(value = "获取树形字典分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/tree_dict_page/{current}")
    @Authority
    public PagingDto<TreeDictDto> getAppTreeDictPage(TokenModel tokenModel, @PathVariable Integer current, @PathVariable String appId, @ModelAttribute PagingBean pagingBean,
                                                     @ModelAttribute TreeDictBean treeDictBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppTreeDictPage");
        TreeDictEntity treeDictEntity = ObjectUtils.convert(new TreeDictEntity(), treeDictBean);
        treeDictEntity.setAppId(appId);
        return treeDictService.getTreeDictPage(current, pagingBean, treeDictEntity);
    }

    @ApiOperation(value = "创建树形字典")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/tree_dict")
    @Authority
    public TreeDictDto createAppTreeDict(TokenModel tokenModel, @PathVariable String appId, @RequestBody TreeDictBean treeDictBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppTreeDict");
        TreeDictEntity treeDictEntity = ObjectUtils.convert(new TreeDictEntity(), treeDictBean);
        treeDictEntity.setNode(IdWorker.getIdStr());
        treeDictEntity.setAppId(appId);
        // 初始化排序字段
        if (treeDictBean.getSeq() == null) {
            TreeDictEntity entity = new TreeDictEntity();
            entity.setAppId(appId);
            entity.setParentNode(treeDictEntity.getParentNode());
            treeDictEntity.setSeq(treeDictService.count(new QueryWrapper<>(entity)) + 1);
        }
        // 保存
        try {
            if (!treeDictService.save(treeDictEntity)) {
                throw FailCode.TREE_DICT_CREATE_FAIL.getOperateException();
            }
        } catch (
                DuplicateKeyException e) {
            throw FailCode.TREE_DICT_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new TreeDictDto(), treeDictEntity);
    }

    @ApiOperation(value = "修改树形字典")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "树形字典主键", name = "treeDictId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/tree_dict/{treeDictId}")
    @Authority
    public TreeDictDto updateAppTreeDict(TokenModel tokenModel, @PathVariable String appId, @PathVariable String treeDictId, @RequestBody TreeDictBean treeDictBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppTreeDict");
        TreeDictEntity treeDictEntity = verifyUpdateDelete(appId, treeDictId);
        ObjectUtils.convert(treeDictEntity, treeDictBean);
        try {
            if (!treeDictService.updateById(treeDictEntity)) {
                throw FailCode.TREE_DICT_UPDATE_FAIL.getOperateException();
            }
        } catch (
                DuplicateKeyException e) {
            throw FailCode.TREE_DICT_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new TreeDictDto(), treeDictEntity);
    }

    /**
     * 编辑删除验证
     *
     * @param appId      应用id
     * @param treeDictId 树形字典主键
     */
    private TreeDictEntity verifyUpdateDelete(String appId, String treeDictId) {
        TreeDictEntity treeDictEntity = treeDictService.getById(treeDictId);
        if (treeDictEntity == null) {
            throw FailCode.TREE_DICT_ID_ERROR.getOperateException();
        }
        if (!treeDictEntity.getAppId().equals(appId)) {
            throw FailCode.TREE_DICT_APP_ID_ERROR.getOperateException();
        }
        return treeDictEntity;
    }

    @ApiOperation(value = "删除树形字典")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "树形字典主键", name = "treeDictId", paramType = "path", required = true)
    })
    @DeleteMapping("/app/{appId}/tree_dict/{treeDictId}")
    @Authority
    public TreeDictDto deleteAppTreeDict(TokenModel tokenModel, @PathVariable String appId, @PathVariable String treeDictId) {
        appService.verifyUserHaveApp(appId, tokenModel, "deleteAppTreeDict");
        TreeDictEntity treeDictEntity = verifyUpdateDelete(appId, treeDictId);
        treeDictService.deleteTreeDict(treeDictEntity);
        return ObjectUtils.convert(new TreeDictDto(), treeDictEntity);
    }

    @ApiOperation(value = "上移树形字典")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "树形字典主键", name = "treeDictId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/tree_dict/{treeDictId}/up")
    @Authority
    public void updateAppTreeDictUp(TokenModel tokenModel, @PathVariable String appId, @PathVariable String treeDictId) {
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppTreeDictUp");
        TreeDictEntity treeDictEntity = verifyUpdateDelete(appId, treeDictId);
        if (!treeDictService.move(treeDictEntity, false)) {
            throw FailCode.ALREADY_THE_FIRST_LINE.getOperateException();
        }
    }

    @ApiOperation(value = "下移树形字典")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "树形字典主键", name = "treeDictId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/tree_dict/{treeDictId}/down")
    @Authority
    public void updateAppTreeDictDown(TokenModel tokenModel, @PathVariable String appId, @PathVariable String treeDictId) {
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppTreeDictDown");
        TreeDictEntity treeDictEntity = verifyUpdateDelete(appId, treeDictId);
        if (!treeDictService.move(treeDictEntity, true)) {
            throw FailCode.ALREADY_THE_LAST_LINE.getOperateException();
        }
    }

    @ApiOperation(value = "批量创建树形字典", notes = "创建应用树形字典集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @PostMapping("/app/{appId}/tree_dict_list")
    @Authority
    public List<TreeDictDto> createAppTreeDictList(TokenModel tokenModel, @PathVariable String appId, @RequestBody List<TreeDictBean> treeDictBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppTreeDictList");
        return treeDictService.createTreeDictList(appId, treeDictBeans);
    }

    @ApiOperation(value = "获取树形字典集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })

    @GetMapping("/app/{appId}/tree_dict_list")
    @Authority
    public List<TreeDictDto> getAppTreeDictList(TokenModel tokenModel, @PathVariable String appId, QueryBean queryBean, TreeDictBean treeDictBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppTreeDictList");
        TreeDictEntity treeDictEntity = ObjectUtils.convert(new TreeDictEntity(), treeDictBean);
        treeDictEntity.setAppId(appId);
        return treeDictService.getTreeDictList(queryBean, treeDictEntity);
    }
}
