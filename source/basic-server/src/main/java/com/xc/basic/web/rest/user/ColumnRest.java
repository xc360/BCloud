package com.xc.basic.web.rest.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xc.api.basic.dto.ColumnDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.ColumnBean;
import com.xc.basic.entity.ColumnEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.ColumnService;
import com.xc.core.annotation.Authority;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>需要登录权限接口，栏目</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，栏目"})
@RestController
public class ColumnRest {
    @Autowired
    private ColumnService columnService;
    @Autowired
    private AppService appService;

    @ApiOperation(value = "栏目分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/column_page/{current}")
    @Authority
    public PagingDto<ColumnDto> getAppColumnPage(TokenModel tokenModel, @PathVariable String appId, @PathVariable Integer current, @ModelAttribute PagingBean pagingBean,
                                                 @ModelAttribute ColumnBean columnBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppColumnPage");
        ColumnEntity columnEntity = ObjectUtils.convert(new ColumnEntity(), columnBean);
        columnEntity.setAppId(appId);
        return columnService.getColumnPage(current, pagingBean, columnEntity);
    }

    @ApiOperation(value = "查询栏目")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "栏目主键", name = "columnId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/column/{columnId}")
    @Authority
    public ColumnDto getAppColumn(TokenModel tokenModel, @PathVariable String appId, @PathVariable String columnId) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppColumn");
        ColumnEntity columnEntity = verifyUpdateDelete(appId, columnId);
        return columnService.getColumn(columnEntity);
    }

    @ApiOperation(value = "创建栏目")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/column")
    @Authority
    public ColumnDto createAppColumn(TokenModel tokenModel, @PathVariable String appId, @RequestBody ColumnBean columnBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppColumn");
        ColumnEntity columnEntity = ObjectUtils.convert(new ColumnEntity(), columnBean);
        columnEntity.setAppId(appId);
        // 初始化排序字段
        if (columnBean.getSeq() == null) {
            ColumnEntity entity = new ColumnEntity();
            entity.setAppId(columnEntity.getAppId());
            entity.setParentNode(columnEntity.getParentNode());
            columnEntity.setSeq(columnService.count(new QueryWrapper<>(entity)) + 1);
        }
        return columnService.createColumn(columnEntity);
    }

    @ApiOperation(value = "修改栏目")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "栏目主键", name = "columnId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/column/{columnId}")
    @Authority
    public ColumnDto updateAppColumn(TokenModel tokenModel, @PathVariable String appId, @PathVariable String columnId, @RequestBody ColumnBean columnBean) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppColumn");
        ColumnEntity columnEntity = verifyUpdateDelete(appId, columnId);
        ObjectUtils.convert(columnEntity, columnBean);
        return columnService.updateColumn(columnEntity);
    }

    /**
     * 编辑删除验证
     *
     * @param columnId 栏目id
     */
    private ColumnEntity verifyUpdateDelete(String appId, String columnId) {
        ColumnEntity columnEntity = columnService.getById(columnId);
        if (columnEntity == null) {
            throw FailCode.COLUMN_ID_ERROR.getOperateException();
        }
        if (!columnEntity.getAppId().equals(appId)) {
            throw FailCode.COLUMN_APP_ID_ERROR.getOperateException();
        }
        return columnEntity;
    }

    @ApiOperation(value = "删除栏目")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "栏目主键", name = "columnId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @DeleteMapping("/app/{appId}/column/{columnId}")
    @Authority
    public void deleteAppColumn(TokenModel tokenModel, @PathVariable String appId, @PathVariable String columnId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "deleteAppColumn");
        ColumnEntity columnEntity = verifyUpdateDelete(appId, columnId);
        columnService.deleteColumn(columnEntity);
    }

    @ApiOperation(value = "获取栏目集合", notes = "根据条件获取当前用户的应用下的所有栏目")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/column_list")
    @Authority
    public List<ColumnDto> getAppColumnList(TokenModel tokenModel, @PathVariable String appId, @ModelAttribute QueryBean queryBean, @ModelAttribute ColumnBean columnBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppColumnList");
        ColumnEntity columnEntity = ObjectUtils.convert(new ColumnEntity(), columnBean);
        columnEntity.setAppId(appId);
        return columnService.getColumnList(queryBean, columnEntity);
    }


    @ApiOperation(value = "上移栏目")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "栏目主键", name = "columnId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/column/{columnId}/up")
    @Authority
    public void updateAppColumnUp(TokenModel tokenModel, @PathVariable String appId, @PathVariable String columnId) {
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppColumnUp");
        ColumnEntity columnEntity = verifyUpdateDelete(appId, columnId);
        if (!columnService.move(columnEntity, false)) {
            throw FailCode.ALREADY_THE_FIRST_LINE.getOperateException();
        }
    }

    @ApiOperation(value = "下移栏目")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "栏目主键", name = "columnId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/column/{columnId}/down")
    @Authority
    public void updateAppColumnDown(TokenModel tokenModel, @PathVariable String appId, @PathVariable String columnId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppColumnDown");
        ColumnEntity columnEntity = verifyUpdateDelete(appId, columnId);
        if (!columnService.move(columnEntity, true)) {
            throw FailCode.ALREADY_THE_LAST_LINE.getOperateException();
        }
    }

    @ApiOperation(value = "创建栏目集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @PostMapping("/app/{appId}/column_list")
    @Authority
    public List<ColumnDto> createAppColumnList(TokenModel tokenModel, @PathVariable String appId,
                                               @RequestBody List<ColumnBean> columnBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppColumnList");
        return columnService.createColumnList(appId, columnBeans);
    }
}
