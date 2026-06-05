package com.xc.basic.web.rest.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xc.api.basic.dto.MenuDto;
import com.xc.basic.bean.MenuBean;
import com.xc.basic.bean.QueryMenuBean;
import com.xc.basic.entity.MenuEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.MenuService;
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
 * <p>需要登录权限接口，菜单</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，菜单"})
@RestController
public class MenuRest {

    @Autowired
    private MenuService menuService;
    @Autowired
    private AppService appService;

    @ApiOperation(value = "菜单分页", notes = "获取应用的菜单分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/menu_page/{current}")
    @Authority
    public PagingDto<MenuDto> getAppMenuPage(TokenModel tokenModel, @PathVariable Integer current, @PathVariable String appId, @ModelAttribute PagingBean pagingBean, @ModelAttribute QueryMenuBean queryMenuBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppMenuPage");
        return menuService.getAppMenuPage(appId, current, pagingBean, queryMenuBean);
    }

    @ApiOperation(value = "创建菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/menu")
    @Authority
    public MenuDto createAppMenu(TokenModel tokenModel, @PathVariable String appId, @RequestBody MenuBean menuBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppMenu");
        MenuEntity menuEntity = ObjectUtils.convert(new MenuEntity(), menuBean);
        menuEntity.setAppId(appId);
        menuEntity.setNode(IdWorker.getIdStr());
        // 初始化排序字段
        if (menuBean.getSeq() == null) {
            MenuEntity entity = new MenuEntity();
            entity.setAppId(appId);
            entity.setParentNode(menuEntity.getParentNode());
            menuEntity.setSeq(menuService.count(new QueryWrapper<>(entity)) + 1);
        }
        // 保存
        try {
            if (!menuService.save(menuEntity)) {
                throw FailCode.MENU_CREATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.MENU_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new MenuDto(), menuEntity);
    }

    @ApiOperation(value = "修改菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "菜单主键", name = "menuId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/menu/{menuId}")
    @Authority
    public MenuDto updateAppMenu(TokenModel tokenModel, @PathVariable String appId, @PathVariable String menuId, @RequestBody MenuBean menuBean) {
        // 验证用户有没有这个应用的操作菜单
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppMenu");
        MenuEntity menuEntity = verifyUpdateDelete(appId, menuId);
        ObjectUtils.convert(menuEntity, menuBean);
        List<MenuEntity> entities = menuService.getChildrenMenu(appId, menuEntity.getNode());
        entities.add(menuEntity);
        for (MenuEntity entity : entities) {
            if (entity.getNode().equals(menuEntity.getParentNode())) {
                throw FailCode.NOT_CHILDREN_ADD.getOperateException();
            }
        }
        try {
            if (!menuService.updateById(menuEntity)) {
                throw FailCode.MENU_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.MENU_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new MenuDto(), menuEntity);
    }

    /**
     * 编辑删除验证
     *
     * @param appId  应用id
     * @param menuId 菜单id
     */
    private MenuEntity verifyUpdateDelete(String appId, String menuId) {
        MenuEntity menuEntity = menuService.getById(menuId);
        if (menuEntity == null) {
            throw FailCode.MENU_ID_ERROR.getOperateException();
        }
        if (!menuEntity.getAppId().equals(appId)) {
            throw FailCode.MENU_APP_ID_ERROR.getOperateException();
        }
        return menuEntity;
    }

    @ApiOperation(value = "删除菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "菜单主键", name = "menuId", paramType = "path", required = true)
    })
    @DeleteMapping("/app/{appId}/menu/{menuId}")
    @Authority
    public void deleteAppMenu(TokenModel tokenModel, @PathVariable String appId, @PathVariable String menuId) {
        // 验证用户有没有这个应用的操作菜单
        appService.verifyUserHaveApp(appId, tokenModel, "deleteAppMenu");
        MenuEntity menuEntity = verifyUpdateDelete(appId, menuId);
        menuService.deleteMenu(menuEntity);
    }

    @ApiOperation(value = "上移菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "菜单主键", name = "menuId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/menu/{menuId}/up")
    @Authority
    public void updateAppMenuUp(TokenModel tokenModel, @PathVariable String appId, @PathVariable String menuId) {
        // 验证用户有没有这个应用的操作菜单
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppMenuUp");
        MenuEntity menuEntity = verifyUpdateDelete(appId, menuId);
        if (!menuService.move(menuEntity, false)) {
            throw FailCode.ALREADY_THE_FIRST_LINE.getOperateException();
        }
    }

    @ApiOperation(value = "下移菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "菜单主键", name = "menuId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/menu/{menuId}/down")
    @Authority
    public void updateAppMenuDown(TokenModel tokenModel, @PathVariable String appId, @PathVariable String menuId) {
        // 验证用户有没有这个应用的操作菜单
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppMenuDown");
        MenuEntity menuEntity = verifyUpdateDelete(appId, menuId);
        if (!menuService.move(menuEntity, true)) {
            throw FailCode.ALREADY_THE_LAST_LINE.getOperateException();
        }
    }

    @ApiOperation(value = "批量创建菜单", notes = "创建应用菜单集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "菜单主键", name = "menuId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/menu_list")
    @Authority
    public List<MenuDto> createAppMenuList(TokenModel tokenModel, @PathVariable String appId, @RequestBody List<MenuBean> menuBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppMenuList");
        return menuService.createAppMenuList(appId, menuBeans);
    }

    @ApiOperation(value = "获取菜单集合", notes = "根据条件获取当前用户的应用下的所有菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/menu_list")
    @Authority
    public List<MenuDto> getAppMenuList(TokenModel tokenModel, @PathVariable String appId, @ModelAttribute QueryBean queryBean, @ModelAttribute QueryMenuBean queryMenuBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppMenuList");
        return ObjectUtils.convertList(menuService.getAppMenuList(appId, queryBean, queryMenuBean), MenuDto::new);
    }
}
