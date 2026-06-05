package com.xc.basic.web.rest.user;

import com.xc.api.basic.dto.PageDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.PageColumnBean;
import com.xc.basic.bean.PageBean;
import com.xc.basic.dto.PageColumnDto;
import com.xc.basic.entity.PageEntity;
import com.xc.basic.service.AppService;
import com.xc.basic.service.PageService;
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
 * <p>需要登录权限接口，页面</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，页面"})
@RestController
public class PageRest {

    @Autowired
    private PageService pageService;
    @Autowired
    private AppService appService;

    @ApiOperation(value = "页面分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/page_page/{current}")
    @Authority
    public PagingDto<PageDto> getAppPagePage(TokenModel tokenModel, @PathVariable String appId, @PathVariable Integer current,
                                             @ModelAttribute PagingBean pagingBean, @ModelAttribute PageBean pageBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppPagePage");
        PageEntity entity = ObjectUtils.convert(new PageEntity(), pageBean);
        entity.setAppId(appId);
        return pageService.getPagePage(current, pagingBean, entity);
    }

    @ApiOperation(value = "创建页面")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/page")
    @Authority
    public PageDto createAppPage(TokenModel tokenModel, @PathVariable String appId, @RequestBody PageBean pageBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppPage");
        return pageService.createPage(appId, pageBean);
    }

    @ApiOperation(value = "修改页面")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "页面主键", name = "pageId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/page/{pageId}")
    @Authority
    public PageDto updateAppPage(TokenModel tokenModel, @PathVariable String appId, @PathVariable String pageId, @RequestBody PageBean pageBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppPage");
        return pageService.updatePage(appId, pageId, pageBean);
    }

    @ApiOperation(value = "删除页面")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "页面主键", name = "pageId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @DeleteMapping("/app/{appId}/page/{pageId}")
    @Authority
    public void deleteAppPage(TokenModel tokenModel, @PathVariable String appId, @PathVariable String pageId) {
        appService.verifyUserHaveApp(appId, tokenModel, "deleteAppPage");
        pageService.deletePage(appId, pageId);
    }

    @ApiOperation(value = "创建页面集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @PostMapping("/app/{appId}/page_list")
    @Authority
    public List<PageDto> createAppPageList(TokenModel tokenModel, @PathVariable String appId,
                                           @RequestBody List<PageBean> pageBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppPageList");
        return pageService.createPageList(appId, pageBeans);
    }

    @ApiOperation(value = "获取的页面集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/page_list")
    @Authority
    public List<PageDto> getAppPageList(TokenModel tokenModel, @PathVariable String appId,
                                        @ModelAttribute QueryBean queryBean, @ModelAttribute PageBean pageBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppPageList");
        PageEntity pageEntity = ObjectUtils.convert(new PageEntity(), pageBean);
        pageEntity.setAppId(appId);
        return pageService.getPageList(queryBean, pageEntity);
    }

    @ApiOperation(value = "创建页面栏目关联集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @PostMapping("/app/{appId}/page_column_relation_list")
    @Authority
    public List<PageColumnDto> createAppPageColumnRelationList(TokenModel tokenModel, @PathVariable String appId,
                                                               @RequestBody List<PageColumnBean> pageColumnBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppPageColumnRelationList");
        return pageService.createAppPageColumnRelationList(appId, pageColumnBeans);
    }

    @ApiOperation(value = "获取的页面栏目关联集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/page_column_relation_list")
    @Authority
    public List<PageColumnDto> getAppPageColumnRelationList(TokenModel tokenModel, @PathVariable String appId) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppPageColumnRelationList");
        return pageService.getAppPageColumnRelationList(appId);
    }
}
