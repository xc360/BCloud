package com.xc.basic.web.controller;

import com.xc.api.basic.dto.AppDto;
import com.xc.api.basic.dto.UserVersionDto;
import com.xc.api.basic.dto.VersionDto;
import com.xc.basic.config.Constants;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.VersionEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.IndexService;
import com.xc.basic.service.VersionService;
import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.enums.Whether;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * <p>无需权限接口，应用Controller</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"无需权限接口，应用Controller"})
@Controller
public class AppController {
    @Autowired
    private IndexService indexService;
    @Autowired
    private AppService appService;
    @Autowired
    private VersionService versionService;
    @Autowired
    private Constants constants;

    /**
     * 应用首页
     *
     * @return 应用首页
     */
    @ApiOperation(value = "应用首页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path"),
    })
    @GetMapping(value = {"/nav/app", "/nav/app/{current}"})
    public String navApp(Model model, @PathVariable(required = false) Integer current) {
        if (current == null) {
            current = 1;
        }
        // 应用信息
        Map<String, Object> map = indexService.getAppInfo();
        model.addAttribute("appName", map.get("appName"));
        model.addAttribute("app", map);
        // 分页数据
        PagingBean pagingBean = new PagingBean();
        pagingBean.setSize(10);
        pagingBean.setSortField("updateTime");
        pagingBean.setSortRule("DESC");
        PagingDto<AppDto> pagingDto = appService.getOvertAppPage(current, pagingBean, new AppEntity());
        pagingDto.setCurrent(current);
        model.addAttribute("paging", pagingDto);
        model.addAttribute("basicPath", constants.getBasicPath());
        return "/app/app-list/app-list";
    }

    /**
     * 应用版本信息
     *
     * @return 应用版本信息
     */
    @ApiOperation(value = "应用版本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "应用ID", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path")
    })
    @GetMapping(value = {"/nav/app/{appId}/version", "/nav/app/{appId}/version/{current}"})
    public String showApp(Model model, @PathVariable String appId, @PathVariable(required = false) Integer current) {
        if (current == null) {
            current = 1;
        }
        // 应用信息
        Map<String, Object> map = indexService.getAppInfo();
        if (Whether.NO.getValue().equals(map.get("isOpen"))) {
            throw FailCode.APP_NOT_OPEN.getOperateException();
        }
        model.addAttribute("appName", map.get("appName"));
        model.addAttribute("app", map);
        // 分页数据
        PagingBean pagingBean = new PagingBean();
        pagingBean.setSize(10);
        pagingBean.setSortField("createTime");
        pagingBean.setSortRule("DESC");
        VersionEntity versionEntity = new VersionEntity();
        versionEntity.setAppId(appId);
        PagingDto<UserVersionDto> pagingDto = versionService.getOvertVersionPage(current, pagingBean, versionEntity);
        pagingDto.setCurrent(current);
        model.addAttribute("paging", pagingDto);
        model.addAttribute("basicPath", constants.getBasicPath());
        return "/app/app-version/app-version";
    }


    /**
     * 应用版本文档
     *
     * @return 应用版本文档
     */
    @ApiOperation(value = "应用版本文档")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "应用ID", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "版本主键", name = "versionId", paramType = "path", required = true)
    })
    @GetMapping("/show/app/{appId}/version/{versionId}")
    public String showVersionDoc(Model model, @PathVariable String appId, @PathVariable String versionId) {
        // 应用信息
        Map<String, Object> map = indexService.getAppInfo();
        if (Whether.NO.getValue().equals(map.get("isOpen"))) {
            throw FailCode.APP_NOT_OPEN.getOperateException();
        }
        model.addAttribute("appName", map.get("appName"));
        model.addAttribute("app", map);
        // 详细数据
        VersionEntity versionEntity = versionService.getById(versionId);
        if (!versionEntity.getAppId().equals(appId)) {
            throw FailCode.APP_ID_ERROR.getOperateException();
        }
        VersionDto versionDto = ObjectUtils.convert(new VersionDto(), versionEntity);
        model.addAttribute("versionDto", versionDto);
        model.addAttribute("basicPath", constants.getBasicPath());
        return "/app/version-doc/version-doc";
    }
}
